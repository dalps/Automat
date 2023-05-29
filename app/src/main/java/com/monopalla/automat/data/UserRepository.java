package com.monopalla.automat.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.monopalla.automat.R;
import com.monopalla.automat.data.model.User;

import java.util.HashMap;

public class UserRepository {
    private static UserRepository instance;
    private final HashMap<String, User> users = new HashMap<>();
    private final Context context;
    private User currentUser;

    private UserRepository(Context context) {
        this.context = context;

        users.put("walter", new User("walter", "walter", "Walter Clements", toBitmap(R.drawable.walter_dog)));
    }

    public static UserRepository getInstance(Context context) {
        if(instance == null) {
            instance = new UserRepository(context);
        }

        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isLoginCorrect(String username, String password) {
        User user = users.get(username);

        if(user != null) {
            String storedPassword = user.getPassword();

            return password.compareTo(storedPassword) == 0;
        }

        return false;
    }

    public boolean login(String username, String password) {
        boolean result = isLoginCorrect(username, password);

        if(result) {
            setCurrentUser(users.get(username));
        }

        return result;
    }


    public void logout() {
        currentUser = null;
    }

    public void register(User newUser) {
        users.put(newUser.getUsername(), newUser);
    }

    public void updateAvatar(User user, Bitmap newPic) {
        user.setProfilePicture(newPic);
        users.replace(user.getUsername(), user);
    }

    Bitmap toBitmap(int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }
}
