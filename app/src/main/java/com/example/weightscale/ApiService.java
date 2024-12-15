package com.example.weightscale;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    // Define GET request for the full endpoint
    @GET("getvalue")
    Call<MyResponseModel> getValue();
}
