package com.agewnet.huambo.http;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitService {


    @GET
    Call<String> requestPost(@Url() String url);


}
