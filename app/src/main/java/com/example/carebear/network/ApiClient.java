package com.example.carebear.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiClient {
    private static Retrofit retrofit;

    public static Retrofit getMockClient() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new MockInterceptor())
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://mockapi.com/") // Base URL is a placeholder
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static ApiService getApiService() {
        return getMockClient().create(ApiService.class);
    }
}
