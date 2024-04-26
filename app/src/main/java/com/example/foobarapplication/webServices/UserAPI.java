package com.example.foobarapplication.webServices;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.foobarapplication.Globals.GlobalToken;
import com.example.foobarapplication.entities.User;
import com.example.foobarapplication.viewModels.UserViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

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
        Call<JsonObject> call = webServiceAPI.deleteUser(user.getUserName(), "Bearer " + token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        boolean success = jsonObject.getBoolean("success");
                        isUserDeleted.postValue(success);
                        Log.d("deleteUser", "Deleted the user");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    isUserDeleted.postValue(false);
                    Log.d("deleteUser", "Failed to delete user");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                isUserDeleted.postValue(false);
                Log.e("deleteUser", "Failed to delete user: " + t.getMessage());
            }
        });
    }

    public void edit(User user, String token, UserViewModel userViewModel) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("editedUsername", user.getUserName());
        jsonObject.addProperty("imageData", user.getPhoto());
        Call<JsonObject> call = webServiceAPI.editUser(user.getUserName(), "Bearer " + token, jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    createToken(user.getUserName(), userViewModel);
                    Log.d("editUser", "Edited the user");
                } else {
                    Log.d("editUser", "Failed to edit user");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("editUser", "Failed to edit user: " + t.getMessage());
            }
        });
    }

    public void getUser(String username, String token, MutableLiveData<User> user) {
        Call<JsonObject> call = webServiceAPI.getUser(username, "Bearer " + token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    User user2 = gson.fromJson(response.body().get("user"), User.class);
                    user.postValue(user2);
                    Log.d("getUser", "Got the user");
                } else {
                    user.postValue(null);
                    Log.d("getUser", "Failed to get user");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("getUser", "Failed to get user: " + t.getMessage());
            }
        });
    }

    public void removeFriend(String userId, String friendId, String token) {

        Call<JsonObject> call = webServiceAPI.deleteFriend(userId, friendId,"Bearer " + token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call,@NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("deleteFriend", "Succeed to delete friend");
                } else {
                    Log.d("deleteFriend", "Failed to delete friend");
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call,@NonNull Throwable t) {
                Log.e("deleteFriends", "Failed to delete friend" + t.getMessage());
            }
        });
    }

    public List<User> getAllFriends(String username, String token) {
        List<User> friendsList = new LinkedList<>();
        Call<JsonObject> call = webServiceAPI.getAllFriends(username, "Bearer " + token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();
                    if (jsonObject != null && jsonObject.has("success") && jsonObject.get("success").getAsBoolean()) {
                        JsonArray friendsArray = jsonObject.getAsJsonArray("friends");
                        if (friendsArray != null) {
                            for (JsonElement element : friendsArray) {
                                JsonObject friendObject = element.getAsJsonObject();
                                String friendName = friendObject.get("username").getAsString();
                                String photo = friendObject.get("photo").getAsString();
                                friendsList.add(new User(friendName,"", photo));
                            }
                        }
                    } else {
                        String errorMessage = jsonObject != null && jsonObject.has("message") ? jsonObject.get("message").getAsString() : "Unknown error";
                        Log.d("getAllFriends", "Failed to get friends: " + errorMessage);
                    }
                } else {
                    Log.d("getAllFriends", "Failed to get all of the friends");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("getAllFriends", "Failed to fetch friends" + t.getMessage());
            }
        });
        return friendsList;
    }
}
