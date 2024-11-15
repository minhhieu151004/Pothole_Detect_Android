package com.example.prj_android_detectpothole;

import org.json.JSONObject;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ApiClient {
    private static final String LOGIN_URL = "https://detectpothole.vercel.app/v1/auth/login";
    private static final String SIGNUP_URL = "https://detectpothole.vercel.app/v1/auth/register";
    private static final String SENDPIN_URL = "https://detectpothole.vercel.app/v1/auth/sendPin";
    private static final String RESETPASS_URL = "https://detectpothole.vercel.app/v1/auth/resetPassword";
    private OkHttpClient client;

    public ApiClient() {
        client= new OkHttpClient.Builder().build();
    }
    public void login(String username, String password, Callback callback)
    {
        try {
            //Create json body request
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", username);
            jsonBody.put("password", password);
            RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.parse("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(LOGIN_URL)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(callback);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void signup(String username, String password, String email, Callback callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", username);
            jsonBody.put("password", password);
            jsonBody.put("email", email);
            RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.parse("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(SIGNUP_URL)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(callback);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendPinByEmail(String email, Callback callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.parse("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(SENDPIN_URL)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(callback);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void resetPassword(String email, String pin, String newPassword, Callback callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("pin", pin);
            jsonBody.put("newPassword", newPassword);
            RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.parse("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(RESETPASS_URL)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(callback);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
