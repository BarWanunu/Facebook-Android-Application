package com.example.foobarapplication.webServices;

import androidx.lifecycle.MutableLiveData;

import com.example.foobarapplication.entities.User;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserAPI {

    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public UserAPI() {
        retrofit = RetrofitBuilder.getInstance();
        webServiceAPI = retrofit.create(webServiceAPI.getClass());
    }
    public void add(User user, final MutableLiveData<Boolean> isUserAdded) {
        Call<Void> call = webServiceAPI.createUser(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                isUserAdded.postValue(response.isSuccessful());
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                isUserAdded.postValue(false);
            }
        });
    }

    public void check(User user, MutableLiveData<Boolean> isUserChecked) {
        Call<Void> call = webServiceAPI.checkUser(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        boolean success = jsonObject.getBoolean("success");
                        isUserChecked.postValue(success);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    isUserChecked.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                isUserChecked.postValue(false);
            }
        });
    }
}
