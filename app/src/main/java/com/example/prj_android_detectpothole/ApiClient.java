package com.example.prj_android_detectpothole;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiClient {
    private static final String LOGIN_URL = "https://api-detect-pothole-app.onrender.com/v1/auth/login";
    private static final String SIGNUP_URL = "https://api-detect-pothole-app.onrender.com/v1/auth/register";
    private static final String SENDPIN_URL = "https://api-detect-pothole-app.onrender.com/v1/auth/sendPin";
    private static final String RESETPASS_URL = "https://api-detect-pothole-app.onrender.com/v1/auth/resetPassword";
    private static final String GOOGLE_LOGIN_URL = "https://api-detect-pothole-app.onrender.com/v1/auth/googleLogin";
    private static final String UPDATE_PROFILE_URL = "https://api-detect-pothole-app.onrender.com/v1/auth/updateUser/";
    private static final String CHECK_PASSWORD_URL = "https://api-detect-pothole-app.onrender.com/v1/auth/checkPassword/";
    private static final String NEW_PASSWORD_URL = "https://api-detect-pothole-app.onrender.com/v1/auth/newPassword/";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client;

    public ApiClient() {
        client = new OkHttpClient.Builder().build();
    }

    public void login(String username, String password, Callback callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", username);
            jsonBody.put("password", password);
            RequestBody body = RequestBody.create(jsonBody.toString(), JSON_MEDIA_TYPE);

            Request request = new Request.Builder()
                    .url(LOGIN_URL)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void signup(String username, String password, String email, Callback callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", username);
            jsonBody.put("password", password);
            jsonBody.put("email", email);
            RequestBody body = RequestBody.create(jsonBody.toString(), JSON_MEDIA_TYPE);

            Request request = new Request.Builder()
                    .url(SIGNUP_URL)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPinByEmail(String email, Callback callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            RequestBody body = RequestBody.create(jsonBody.toString(), JSON_MEDIA_TYPE);

            Request request = new Request.Builder()
                    .url(SENDPIN_URL)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetPassword(String email, String pin, String newPassword, Callback callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("pin", pin);
            jsonBody.put("newPassword", newPassword);
            RequestBody body = RequestBody.create(jsonBody.toString(), JSON_MEDIA_TYPE);

            Request request = new Request.Builder()
                    .url(RESETPASS_URL)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkPassword(String userId, String password, String accessToken,Callback callback) {
        try {
            String url = CHECK_PASSWORD_URL + userId;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("currentPassword", password);
            RequestBody body = RequestBody.create(jsonBody.toString(), JSON_MEDIA_TYPE);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("token", "Bearer " + accessToken)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .build();

            client.newCall(request).enqueue(callback);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void changePassword(String userId, String newPassword, String accessToken, Callback callback) {
        try {
            String url = NEW_PASSWORD_URL + userId;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("newPassword", newPassword);
            RequestBody body = RequestBody.create(jsonBody.toString(), JSON_MEDIA_TYPE);
            Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .addHeader("token", "Bearer " + accessToken)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .build();

            client.newCall(request).enqueue(callback);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateUser(String userId, String username, String accessToken, Callback callback) {
        try {
            // Tạo đối tượng JSON chứa thông tin cần cập nhật
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", username);
            RequestBody body = RequestBody.create(jsonBody.toString(), JSON_MEDIA_TYPE);

            // Tạo URL với userId
            String url = UPDATE_PROFILE_URL + userId;

            // Tạo yêu cầu PUT với header Authorization
            Request request = new Request.Builder()
                    .url(url)
                    .put(body) // Phương thức PUT
                    .addHeader("token", "Bearer " + accessToken) // Thêm accessToken vào header
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .build();
            Log.e("API access: ", "Bearer " + accessToken);
            // Thực hiện yêu cầu
            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Phương thức đăng nhập qua Google
    public void googleLogin(String idToken, Callback callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("idToken", idToken);
            RequestBody body = RequestBody.create(jsonBody.toString(), JSON_MEDIA_TYPE);

            Request request = new Request.Builder()
                    .url(GOOGLE_LOGIN_URL) // Địa chỉ API đăng nhập Google
                    .post(body)
                    .build();

            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
