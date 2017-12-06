package com.vnshine.learnjapanese.Translator;

/**
 * Created by phoenix on 12/6/17.
 */

public class ApiUtils {
    public static final String BASE_URL = "https://translate.yandex.net/api/v1.5/tr.json/";
    public static TranslateApi getTranslateApi() {
        return RetrofitClient.getClient(BASE_URL).create(TranslateApi.class);
    }
}
