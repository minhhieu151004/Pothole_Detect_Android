package com.example.prj_android_detectpothole;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChangePasswordFragment extends Fragment {
    private Button nextBtn;
    private EditText currentPassword;
    private TextView errorNotification;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        nextBtn = view.findViewById(R.id.next_btn);
        currentPassword = view.findViewById(R.id.current_password_txt);
        errorNotification = view.findViewById(R.id.error_notification_txt);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckPassword(currentPassword, errorNotification);
            }
        });

        return view;
    }

    private void NextToUpdate() {
        ChangePasswordUpdateFragment changePasswordUpdateFragment = new ChangePasswordUpdateFragment();

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment, changePasswordUpdateFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void CheckPassword(EditText input, TextView error) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String id = sharedPreferences.getString("id", "");
        String accessToken = sharedPreferences.getString("accessToken", "");
        String currentPassword = input.getText().toString();

        ApiClient apiClient = new ApiClient();
        apiClient.checkPassword(id, currentPassword, accessToken, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Check Password Fail", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                requireActivity().runOnUiThread(() -> {
                    try {
                        if (response.isSuccessful()) {
                            Toast.makeText(requireContext(), "Password is correct", Toast.LENGTH_SHORT).show();
                            NextToUpdate();
                        } else {
                            Toast.makeText(requireContext(), "Password is incorrect", Toast.LENGTH_SHORT).show();
                            error.setVisibility(View.VISIBLE);
                            Log.e("Error code: ", String.valueOf(response.code()));
                        }
                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "Unexpected error occurred", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
            }
        });
    }
    //
}