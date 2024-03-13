package com.example.foobarapplication.webServices;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.foobarapplication.entities.User;
import com.google.gson.JsonObject;

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
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }
    public void add(User user, final MutableLiveData<Boolean> isUserAdded) {
        JsonObject userCreate = new JsonObject();
        userCreate.addProperty("email", user.getEmail());
        userCreate.addProperty("username", user.getUsername());
        userCreate.addProperty("password", user.getPassword());
        userCreate.addProperty("confirmPassword", user.getConfirmPasswordPassword());
        userCreate.addProperty("photo", user.getImagePath());
        Call<JsonObject> call = webServiceAPI.createUser(userCreate);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call,@NonNull Response<JsonObject> response) {
                isUserAdded.postValue(response.isSuccessful());
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                isUserAdded.postValue(false);
            }
        });
    }

    public void check(User user, MutableLiveData<Boolean> isUserChecked) {
        JsonObject userCheck = new JsonObject();
        userCheck.addProperty("username", user.getUsername());
        userCheck.addProperty("password", user.getPassword());
        Call<JsonObject> call = webServiceAPI.checkUser(userCheck);
        String check;
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call,@NonNull Response<JsonObject> response) {
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
            public void onFailure(@NonNull Call<JsonObject> call,@NonNull Throwable t) {
                isUserChecked.postValue(false);
            }
        });
    }

    public void delete(User user, MutableLiveData<Boolean> isUserDeleted){
        Call<JsonObject> call = webServiceAPI.deleteUser(user.getUsername());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call,@NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        boolean success = jsonObject.getBoolean("success");
                        isUserDeleted.postValue(success);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    isUserDeleted.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}
