package com.vnshine.learnjapanese.Translator;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by phoenix on 12/6/17.
 */

public interface TranslateApi {
    @GET("translate")
    Call<String> translateText(@Query("key")String key, @Query("text")String textToTranslate, @Query("lang")String lang);
}
