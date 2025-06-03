//package com.example.irrigationapp.controller;
//
//import com.example.irrigationapp.model.User;
//
//public class LoginController {
//
//    public boolean checkLogin(User user) {
//        // Remplace ceci par une vraie vérification (via RMI ou réseau plus tard)
//        return user.getUsername().equals("admin") && user.getPassword().equals("admin");
//    }
//}
package com.example.irrigationapp.controller;

import android.util.Log;

import com.example.irrigationapp.model.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginController {

    private static final String LOGIN_URL = "http://10.0.2.2:8080/api/users/login"; // pour l'émulateur

    public interface LoginCallback {
        void onSuccess();
        void onFailure(String message);
    }

    public void login(User user, LoginCallback callback) {
        new Thread(() -> {
            try {
                URL url = new URL(LOGIN_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String postData = "username=" + user.getUsername() + "&password=" + user.getPassword();

                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String response = in.readLine();
                    in.close();

                    boolean success = response != null && response.trim().equalsIgnoreCase("true");

                    if (success) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure("Identifiants incorrects");
                    }

                } else {
                    callback.onFailure("Code HTTP : " + responseCode);
                }

            } catch (Exception e) {
                e.printStackTrace();
                callback.onFailure("Erreur de connexion au serveur");
            }
        }).start();
    }
}
