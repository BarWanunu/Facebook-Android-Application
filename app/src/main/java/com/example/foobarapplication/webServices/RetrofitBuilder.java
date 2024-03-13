package com.example.foobarapplication.webServices;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class RetrofitBuilder {

    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            synchronized (RetrofitBuilder.class) {
//                if (retrofit == null) {
//                    OkHttpClient okHttpClient = new OkHttpClient.Builder().
//                            connectTimeout(30, TimeUnit.SECONDS).
//                            readTimeout(30, TimeUnit.SECONDS).
//                            writeTimeout(30, TimeUnit.SECONDS).build();

                    retrofit = new Retrofit.Builder()
                            .baseUrl("http://10.0.2.2:80/")
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
//            }
        }
        return retrofit;
    }
}
