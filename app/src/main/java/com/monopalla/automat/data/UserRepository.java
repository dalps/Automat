package com.monopalla.automat.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.monopalla.automat.R;
import com.monopalla.automat.data.model.Product;
import com.monopalla.automat.data.model.User;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

public class UserRepository {
    private static UserRepository instance;
    private final HashMap<String, User> users = new HashMap<>();
    private final Context context;
    private User currentUser;
    public static final User DEFAULT_USER = new User("default", "default", "default", null);

    private UserRepository(Context context) {
        this.context = context;
        currentUser = DEFAULT_USER;

        users.put("walter", new User("walter", "walter", "Walter Clements", toBitmap(R.drawable.walter_dog), 100));
        users.put("mario", new User("mario", "mario", "Mario Saia", toBitmap(R.drawable.placeholder)));
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

    public boolean isCurrentUserValid() {
        return currentUser != null && !currentUser.equals(DEFAULT_USER);
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
        currentUser = DEFAULT_USER;
        EventBus.getDefault().post(new LogoutEvent());
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

    public boolean isAdmin(User user) {
        return user.getUsername() == "walter";
    }

    public static class LogoutEvent {

    }
}
