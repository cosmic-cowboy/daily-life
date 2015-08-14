package com.slgerkamp.daily.life.generic;

import android.app.Activity;
import android.util.Log;

import com.slgerkamp.daily.life.infra.JSONData;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;

import rx.Observable;
import rx.android.app.AppObservable;
import rx.functions.Func1;
import rx.subjects.AsyncSubject;

/**
 * サーバーとの通信を司るクラスです。
 */
public class Backend {

    private static final OkHttpClient client = new OkHttpClient();

    // TODO ここらへんは固定値ならvalueに移行する
    private static final String domain = "172.20.10.5";
    private static final boolean secure = false;
    private static final int port = 9000;
    private static final String pathPrefix = "/user/api/v1";

    private final Activity activity;

    public Backend(Activity activity) {
        this.activity = activity;
    }

    // ----------------------------------------------------------------
    //     各種リクエスト処理
    // ----------------------------------------------------------------

    public Get get(String path) {
        return new Get(path.split("/"));
    }
    public class Get {
        private final HttpUrl.Builder builder = builder();

        public Get(String[] path) {
            for (String p : path) builder.addPathSegment(p);
        }

        public Get param(String key, String value) {
            builder.addQueryParameter(key, value);
            return this;
        }

        public Observable<JSONData> toObservable() {
            Request req = new Request.Builder().url(builder.build().toString()).build();
            return Observable.just(req).flatMap(new RequestExecutor());
        }
    }




    // ----------------------------------------------------------------
    //     リクエスト共通処理
    // ----------------------------------------------------------------

    /**
     * リクエストを発行するための関数です。
     */
    private class RequestExecutor implements Func1<Request, Observable<JSONData>> {
        @Override
        public Observable<JSONData> call(Request asyncHttpRequest) {
            Log.d(Backend.class.getSimpleName(), asyncHttpRequest.method() + " " + asyncHttpRequest.urlString());

            // 1. リクエストを実行
            final AsyncSubject<Response> responseSubject = AsyncSubject.create();
            client.newCall(asyncHttpRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    responseSubject.onError(e);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    responseSubject.onNext(response);
                    responseSubject.onCompleted();
                }
            });

            // 2. レスポンスをハンドリング
            return AppObservable.bindActivity(activity, responseSubject.flatMap(new Func1<Response, Observable<JSONData>>() {
                @Override
                public Observable<JSONData> call(Response res) {
                    try {
                        Log.d(Backend.class.getSimpleName(), res.toString());
                        return processResponse(res);

                    } catch (IOException ex) {
                        return Observable.error(ex);
                    }
                }
            }));
        }

        private Observable<JSONData> processResponse(Response res) throws IOException {
            if (!res.isSuccessful()) {
                return new ServiceException("Unexpected Status: " + res.code() + " " + res.body().string()).error();
            }

            String json = res.body().string();
            if (json.isEmpty()) return Observable.just(new JSONData(new JSONObject()));

            return JSONData.parse(json);
        }
    }

    private static HttpUrl.Builder builder() {
        return new HttpUrl.Builder()
                .scheme(secure ? "https" : "http")
                .host(domain)
                .port(port)
                .encodedPath(pathPrefix);
    }

    // ----------------------------------------------------------------
    //     その他
    // ----------------------------------------------------------------

    public static class ServiceException extends Exception {
        public ServiceException(String message) {
            super(message);
        }

        public <T> Observable<T> error() {
            return Observable.error(this);
        }
    }
}
