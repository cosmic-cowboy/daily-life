package com.slgerkamp.daily.life.generic;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteStreams;
import com.slgerkamp.daily.life.infra.JSONData;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import okio.BufferedSink;
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

//    private static final String domain = "192.168.1.26";
    private static final String domain = "172.20.10.4";
    private static final boolean secure = false;
    private static final int port = 9000;
    private static final String pathPrefix = "/user/api/v1";

    private final Activity activity;
    private final Picasso picasso;

    public Backend(Activity activity) {
        this.activity = activity;
        this.picasso = new Picasso.Builder(activity).downloader(new OkHttpDownloader(client)).build();
    }

    // ----------------------------------------------------------------
    //     各種リクエスト処理
    // ----------------------------------------------------------------

    /**
     * <p>このメソッドを使ってGetクラスのインスタンス化を行う</>
     * @param path
     * @return
     */
    public Get get(String path) {
        return new Get(path.split("/"));
    }

    /**
     * <p>GETメソッド用ビルダークラス</>
     */
    public class Get {
        private final HttpUrl.Builder builder = builder();

        // コンストラクタ呼び出し時にPathを設定する
        public Get(String[] path) {
            for (String p : path) builder.addPathSegment(p);
        }

        // queryパラメータを設定する
        public Get param(String key, String value) {
            builder.addQueryParameter(key, value);
            return this;
        }

        // リクエストを実施し、結果をObservableで返却する
        public Observable<JSONData> toObservable() {
            Request req = new Request.Builder().url(builder.build().toString()).build();
            return Observable.just(req).flatMap(new RequestExecutor());
        }
    }

    /**
     * <p>このメソッドを使ってPostクラスのインスタンス化を行う</>
     * @param path
     * @return
     */
    public Post post(String path) {
        return new Post(path.split("/"));
    }
    /**
     * <p>POSTメソッド用ビルダークラス</>
     */
    public class Post {
        private final Request.Builder builder;
        private final ImmutableMap.Builder<String, Object> params = new ImmutableMap.Builder<>();

        // コンストラクタ呼び出し時にPathを設定する
        public Post(String[] path) {
            HttpUrl.Builder builder = builder();
            for (String p : path) builder.addPathSegment(p);
            this.builder = new Request.Builder().url(builder.build());
        }

        // RequestBody に 含む要素をkey, valueで設定する
        public Post param(String key, String value) {
            params.put(key, value);
            return this;
        }

        // リクエストを実施し、結果をObservableで返却する
        public Observable<JSONData> toObservable() {
            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"),
                    new JSONObject(params.build()).toString()
            );
            Request req = builder
                    .post(body)
                    .build();
            return Observable.just(req).flatMap(new RequestExecutor());
        }
    }


    /**
     * <p>このメソッドを使ってDeleteクラスのインスタンス化を行う</>
     * @param path
     * @return
     */
    public Delete delete(String path) {
        return new Delete(path.split("/"));
    }
    /**
     * <p>DELETEメソッド用ビルダークラス</>
     */
    public class Delete {
        private final HttpUrl.Builder builder = builder();

        // コンストラクタ呼び出し時にPathを設定する
        public Delete(String[] path) {
            for (String p : path) builder.addPathSegment(p);
        }

        // queryパラメータを設定する
        public Delete param(String key, String value) {
            builder.addQueryParameter(key, value);
            return this;
        }

        // リクエストを実施し、結果をObservableで返却する
        public Observable<JSONData> toObservable() {
            Request req = new Request.Builder().url(builder.build().toString()).delete().build();
            return Observable.just(req).flatMap(new RequestExecutor());
        }
    }


    /**
     * ファイルをアップロードする。
     * URLパスとinputStreamを設定するとリクエストを実施し、結果をObservableで返却する
     */
    public Observable<JSONData> upload(String path, final InputStream input) {
        RequestBody body = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MediaType.parse("application/octet-stream");
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                ByteStreams.copy(input, sink.outputStream());
            }
        };

        HttpUrl.Builder builder = builder();
        for (String s : path.split("/")) {
            builder.addPathSegment(s);
        }
        Request post = new Request.Builder().url(builder.toString()).post(body).build();

        return Observable.just(post).flatMap(new RequestExecutor());
    }

    public ImageLoader imageLoader() {
        return new ImageLoader(activity);
    }

    // ----------------------------------------------------------------
    //     リクエスト共通処理
    // ----------------------------------------------------------------

    /**
     * リクエストを発行するための関数です。
     * Observableなリクエストを受け取って、
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

        /**
         * レスポンスからJSONDataを生成する
         */
        private Observable<JSONData> processResponse(Response res) throws IOException {
            if (!res.isSuccessful()) {
                return new ServiceException("Unexpected Status: " + res.code() + " " + res.body().string()).error();
            }

            String json = res.body().string();
            if (json.isEmpty()) return Observable.just(new JSONData(new JSONObject()));

            return JSONData.parse(json);
        }
    }


    /**
     * 画像をロードするためのリクエストを構築する。
     */
    public class ImageLoader {
        private final Context context;

        public ImageLoader(Context context) {
            this.context = context;
        }

        public RequestCreator load(Long id) {
            HttpUrl url = builder().addPathSegment("file").addPathSegment("image")
                    .addQueryParameter("fileId", Long.toString(id))
                    .build();

            Log.d(Backend.class.getSimpleName(), "image: " + url);
            return picasso.load(url.toString());
        }
    }

    /**
     * アクセス先の基本情報を設定する
     * スキーム、ドメイン、ポート、共通のURLパス
     */
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
