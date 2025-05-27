package com.example.trivalmotos.model;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_USERS = "users";
    private static UserManager instance;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private List<User> users;

    private UserManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        loadUsers();
    }

    public static synchronized UserManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserManager(context.getApplicationContext());
        }
        return instance;
    }

    private void loadUsers() {
        String json = sharedPreferences.getString(KEY_USERS, null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<User>>(){}.getType();
            users = gson.fromJson(json, type);
        } else {
            users = new ArrayList<>();
        }
    }

    private void saveUsers() {
        String json = gson.toJson(users);
        sharedPreferences.edit().putString(KEY_USERS, json).apply();
    }

    public boolean registerUser(String email, String password) {
        // Check if user already exists
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return false;
            }
        }

        // Add new user
        users.add(new User(email, password));
        saveUsers();
        return true;
    }

    public boolean loginUser(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
} 