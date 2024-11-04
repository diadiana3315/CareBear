package com.example.carebear.network;

import com.example.carebear.network.models.ApiResponse;
import com.example.carebear.network.models.LoginRequest;
import com.example.carebear.network.models.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("register")
    Call<ApiResponse> register(@Body RegisterRequest request);

    @POST("login")
    Call<ApiResponse> login(@Body LoginRequest request);
}
