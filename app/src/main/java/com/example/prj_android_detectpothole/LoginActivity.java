package com.example.prj_android_detectpothole;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prj_android_detectpothole.OBJECT.MyUserToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.io.IOException;
import java.util.prefs.Preferences;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {  // Phải kế thừa từ AppCompatActivity
    EditText username, password;
    Button loginBtn, googleBtn;
    GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        ImageView btnBack = findViewById(R.id.btn_back);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.button_login);
        googleBtn = findViewById(R.id.button_google);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.forgot_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPassword1Activity.class));
            }
        });
        findViewById(R.id.sign_up_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
        TextView forgotPassword = findViewById(R.id.forgot_password);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPassword1Activity.class));
            }
        });

        //Configure Google Sign-in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1012310011566-s6lbpsolaghl3r17a8ks7k9ee65bcaia.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        //Login
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiClient apiClient = new ApiClient();
                apiClient.login(username.getText().toString(), password.getText().toString(), new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String responseBody = response.body().string();
                            runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show());
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa ngăn xếp hoạt động
                            startActivity(intent);

                            try {
                                JSONObject jsonResponse = new JSONObject(responseBody);
                                String id = jsonResponse.getString("_id");
                                String accessToken = jsonResponse.getString("accessToken");
                                String username = jsonResponse.getString("username");
                                String email = jsonResponse.getString("email");
                                saveAccessToken(accessToken, id ,username, email);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show());
                        }
                    }
                });
            }
        });

        //Login with Google
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
    }

    public void saveAccessToken(String accessToken, String id,String username, String email) {
        long expireTime = System.currentTimeMillis() + 86400000;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("accessToken", accessToken);
        editor.putString("id", id);
        editor.putString("username", username);
        editor.putString("email", email);
        editor.putLong("expireTime", expireTime);
        editor.apply();
        MyUserToken.token = accessToken;
    }

    //Handle Login with Google
    private void signInWithGoogle() {
        Intent signInGoogleIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInGoogleIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                handleGoogleSignIn(account);
            } catch (ApiException e) {
                Toast.makeText(this, "Google Sign-In failed.", Toast.LENGTH_SHORT).show();
                Log.w("Google Sign-In", "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }

    private void handleGoogleSignIn(GoogleSignInAccount account) {
        if (account != null) {
            String idToken = account.getIdToken();
            // Send the idToken to your backend server for verification
            ApiClient apiClient = new ApiClient();
            apiClient.googleLogin(idToken, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Google Login Failed", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Google Login Successful", Toast.LENGTH_SHORT).show());
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        try {
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            String id = jsonResponse.getString("_id");
                            String accessToken = jsonResponse.getString("accessToken");
                            String username = jsonResponse.getString("username");
                            String email = jsonResponse.getString("email");
                            saveAccessToken(accessToken, id ,username, email);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Google Login Failed", Toast.LENGTH_SHORT).show());
                    }
                }
            });
        }
    }
}