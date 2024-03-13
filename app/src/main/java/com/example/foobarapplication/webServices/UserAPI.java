package com.example.foobarapplication.webServices;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.foobarapplication.entities.User;
import com.example.foobarapplication.viewModels.UserViewModel;
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
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                isUserAdded.postValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                isUserAdded.postValue(false);
            }
        });
    }

    public void check(User user, MutableLiveData<Boolean> isUserChecked, UserViewModel userViewModel) {
        JsonObject userCheck = new JsonObject();
        userCheck.addProperty("username", user.getUsername());
        userCheck.addProperty("password", user.getPassword());
        Call<JsonObject> call = webServiceAPI.checkUser(userCheck);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            createToken(user.getUsername(), userViewModel);
                        }
                        isUserChecked.postValue(success);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        isUserChecked.postValue(false);
                    }
                } else {
                    isUserChecked.postValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                isUserChecked.postValue(false);
            }
        });
    }

    private void createToken(String username, UserViewModel userViewModel) {
        if(webServiceAPI != null) {
            Call<JsonObject> call = webServiceAPI.createToken(username);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NonNull Call<JsonObject> call,@NonNull Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(response.body().toString());
                            boolean successful = object.getBoolean("success");
                            if (successful) {
                                String token = object.getString("token");
                                userViewModel.setToken(token);
                            } else {
                                userViewModel.setToken(null);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            userViewModel.setToken(null);
                        }
                    } else {
                        userViewModel.setToken(null);
                    }
                }

                @Override
                public void onFailure (@NonNull Call < JsonObject > call,@NonNull Throwable t){
                    userViewModel.setToken(null);
                }
            });
        } else {
            System.out.println("1");
        }

        }

        public void delete (User user, MutableLiveData < Boolean > isUserDeleted){
            Call<JsonObject> call = webServiceAPI.deleteUser(user.getUsername());
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
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
