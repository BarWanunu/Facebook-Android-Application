package com.example.foobarapplication.activities;

public interface ApiResponseCallback<T> {
    void onResponse(T response);
    void onFailure(Throwable throwable);
}
