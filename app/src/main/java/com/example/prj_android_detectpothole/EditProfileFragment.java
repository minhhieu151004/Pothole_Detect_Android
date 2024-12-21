package com.example.prj_android_detectpothole;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class EditProfileFragment extends Fragment {
    private EditText username_edt;
    private Button updateBtn;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        updateBtn = view.findViewById(R.id.update_btn);
        username_edt = view.findViewById(R.id.update_user_input);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUsername();
            }
        });
        return view;
    }
    private void backSetting(){
        SettingFragment settingFragment = new SettingFragment();

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment, settingFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void updateUsername() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String id = sharedPreferences.getString("id", "");
        String accessToken = sharedPreferences.getString("accessToken", "");
        String username = username_edt.getText().toString();

        ApiClient client =new ApiClient();
        client.updateUser(id, username, accessToken, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Update Fail", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String responseBody = response.body().string();
                    Log.e("code:", String.valueOf(response.code()));
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String updatedUsername = jsonResponse.getString("username");
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", updatedUsername);
                        editor.apply();
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(requireContext(), "Update success", Toast.LENGTH_SHORT).show()
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Error", "Error parsing JSON response");
                    }
                    backSetting();
                }
                else {
                    Log.e("Error code:", accessToken);
                    Log.e("Error code:", String.valueOf(response.code()));
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "Update fail", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }

    private void showSuccessDialog(String message)
    {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_success);
        dialog.setCancelable(true);


        TextView messageText = dialog.findViewById(R.id.message_txt);
        Button okBtn = dialog.findViewById(R.id.ok_btn);

        messageText.setText(message);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                backSetting();
            }
        });

        if (dialog.getWindow() != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = (int) (displayMetrics.widthPixels * 0.8); // 80% chiều rộng màn hình
            int height = (int) (displayMetrics.heightPixels * 0.4); // 40% chiều cao màn hình
            dialog.getWindow().setLayout(width, height);

        }
        dialog.show();
    }
}