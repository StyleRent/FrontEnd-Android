package com.example.stylerent.auth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

//    @FormUrlEncoded
//    @POST("/api/users")
//    Call<User> getUserInformation(@Field("name") String name, @Field("job") String job);

    @POST("/api/v1/auth/register")
    //Email, Name, Password
    //{username: example, email: example@gmail.com, password: example}
    Call<RegisterResponse> getRegisterInformation(@Body RegisterRequest body);


    @POST("/api/v1/auth/login")
    //Email, Password
    //{email: example@gmail.com, password: example}
    Call<LoginResponse> getLoginInformation(@Body LoginRequest body);





}
