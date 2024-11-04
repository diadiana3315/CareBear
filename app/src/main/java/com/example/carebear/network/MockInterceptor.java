package com.example.carebear.network;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
public class MockInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String path = chain.request().url().encodedPath();

        if (path.equals("/register")) {
            String mockResponse = "{\"success\":true,\"message\":\"Registration successful\"}";
            return new Response.Builder()
                    .code(200)
                    .message(mockResponse)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .body(ResponseBody.create(MediaType.parse("application/json"), mockResponse))
                    .build();
        } else {
            return chain.proceed(chain.request());
        }
    }
}
