package com.example.prj_android_detectpothole;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    //Test Case:
    private void CheckPassword(EditText input, TextView error) {
        String password = String.valueOf(input.getText());
        if (password.equals("admin123"))
        {
            NextToUpdate();
        }
        else {
            input.setText("");
            error.setVisibility(View.VISIBLE);
        }
    }
    //
}