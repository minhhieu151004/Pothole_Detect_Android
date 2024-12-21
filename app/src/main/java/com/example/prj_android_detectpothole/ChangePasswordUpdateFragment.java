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

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChangePasswordUpdateFragment extends Fragment {
    private Button updateBtn;
    private EditText newPasswordEdt, confirmNewPasswordEdt;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password_update, container, false);

        newPasswordEdt = view.findViewById(R.id.new_password_input);
        confirmNewPasswordEdt = view.findViewById(R.id.confirm_new_password_input);
        updateBtn = view.findViewById(R.id.update_btn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewPassword();
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

    private Boolean checkConfirmPassword() {
        String newPassword = newPasswordEdt.getText().toString();
        String confirmNewPassword = confirmNewPasswordEdt.getText().toString();
        if (newPassword.equals(confirmNewPassword)) {
            return true;
        } else {
            return false;
        }
    }

    private void CreateNewPassword() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String id = sharedPreferences.getString("id", "");
        String accessToken = sharedPreferences.getString("accessToken", "");
        String newPassword = newPasswordEdt.getText().toString();

        ApiClient apiClient = new ApiClient();
        if (checkConfirmPassword()) {
            apiClient.changePassword(id, newPassword, accessToken, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "Change Password Fail", Toast.LENGTH_SHORT).show()
                    );
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        Log.e("code:", String.valueOf(response.code()));
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(requireContext(), "Change Password Success", Toast.LENGTH_SHORT).show()
                        );
                        backSetting();
                    } else {
                        Log.e("Error code:", accessToken);
                        Log.e("Error code:", String.valueOf(response.code()));
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(requireContext(), "Change Password Fail", Toast.LENGTH_SHORT).show()
                        );
                    }
                }
            });
        }
        else {
            requireActivity().runOnUiThread(() ->
                    Toast.makeText(requireContext(), "Confirm Password Fail", Toast.LENGTH_SHORT).show()
            );
        }
    }
}