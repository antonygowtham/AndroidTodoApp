package com.example.todolist.api;
import com.example.todolist.model.User;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TaskApi {
    @GET("/isAuthorized")
    Call<User> isAuthorized();
}