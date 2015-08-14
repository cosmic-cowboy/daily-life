package com.slgerkamp.daily.life.infra;

import com.google.common.collect.ImmutableList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

public class JSONData {

    private final JSONObject object;

    public JSONData(JSONObject object) {
        this.object = object;
    }

    public static Observable<JSONData> parse(String encoded) {
        try {
            return Observable.just(new JSONData(new JSONObject(encoded)));

        } catch (Throwable th) {
            return Observable.error(th);
        }

    }

    public Observable<List<JSONData>> getList(String path, String... subpath) {
        try {
            Map.Entry<JSONObject, String> parent = component(path, subpath);
            JSONArray array = parent.getKey().getJSONArray(parent.getValue());
            ImmutableList.Builder<JSONData> res = new ImmutableList.Builder<>();

            for (int i = 0; i < array.length(); i++) {
                res.add(new JSONData(array.getJSONObject(i)));
            }
            return Observable.<List<JSONData>>just(res.build());

        } catch (Throwable th) {
            return Observable.error(th);
        }
    }

    public Observable<String> getString(String path, String... subpath) {
        try {
            Map.Entry<JSONObject, String> parent = component(path, subpath);
            return Observable.just(parent.getKey().getString(parent.getValue()));
        } catch (Throwable th) {
            return Observable.error(th);
        }
    }

    public Observable<Long> getLong(String path, String... subpath) {
        try {
            Map.Entry<JSONObject, String> parent = component(path, subpath);
            return Observable.just(parent.getKey().getLong(parent.getValue()));
        } catch (Throwable th) {
            return Observable.error(th);
        }
    }
    private Map.Entry<JSONObject, String> component(String first, String... next) throws JSONException {
        if (next.length == 0) return new AbstractMap.SimpleImmutableEntry<>(object, first);

        String last = next[next.length - 1];
        JSONObject o = object.getJSONObject(first);
        for (int i = 0; i < next.length - 1; ++i) {
            o = o.getJSONObject(next[i]);
        }
        return new AbstractMap.SimpleImmutableEntry<>(o, last);
    }

    @Override
    public String toString() {
        return object.toString();
    }
}
