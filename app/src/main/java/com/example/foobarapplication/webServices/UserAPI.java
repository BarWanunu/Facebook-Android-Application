package com.example.foobarapplication.webServices;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.foobarapplication.Globals.GlobalToken;
import com.example.foobarapplication.entities.User;
import com.example.foobarapplication.viewModels.UserViewModel;
import com.google.gson.Gson;
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
        userCreate.addProperty("username", user.getUserName());
        userCreate.addProperty("password", user.getPassword());
        userCreate.addProperty("confirmPassword", user.getConfirmPasswordPassword());
        userCreate.addProperty("photo", user.getPhoto());
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
        userCheck.addProperty("username", user.getUserName());
        userCheck.addProperty("password", user.getPassword());
        Call<JsonObject> call = webServiceAPI.checkUser(userCheck);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        boolean success = jsonObject.getBoolean("success");
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

    public void createToken(String username, UserViewModel userViewModel) {
        JsonObject user = new JsonObject();
        user.addProperty("username", username);
        Call<JsonObject> call = webServiceAPI.createToken(user);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(response.body().toString());
                        boolean successful = object.getBoolean("success");
                        if (successful) {
                            String token = object.getString("token");
                            GlobalToken.token = token;
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
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                t.printStackTrace();
                userViewModel.setToken(null);
            }
        });
    }

    public void delete(User user, MutableLiveData<Boolean> isUserDeleted, String token) {
        Call<JsonObject> call = webServiceAPI.deleteUser(user.getUserName(),"Bearer " + token);
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

    public void edit(User user, String token) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("editedUsername", user.getUserName());
        jsonObject.addProperty("imageData", user.getPhoto());
        Call<JsonObject> call = webServiceAPI.editUser(user.getUserName(),"Bearer " + token, jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {

                } else {

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void getUser(String username, String token, MutableLiveData<User> user) {
        Call<JsonObject> call = webServiceAPI.getUser(username,"Bearer " + token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    User user2 = gson.fromJson(response.body().get("user"), User.class);
                    user.postValue(user2);
                } else {
                    user.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}
