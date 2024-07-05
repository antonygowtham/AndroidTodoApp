package com.example.todolist.api;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitTaskClient {
    private static final String BASE_URL = "http://localhost:3000/";
    private static RetrofitTaskClient instance;
    private final TaskApi apiService;

    private RetrofitTaskClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(TaskApi.class);
    }

    public static synchronized RetrofitTaskClient getInstance() {
        if (instance == null) {
            instance = new RetrofitTaskClient();
        }
        return instance;
    }

    public TaskApi getApi() {
        return apiService;
    }
}
