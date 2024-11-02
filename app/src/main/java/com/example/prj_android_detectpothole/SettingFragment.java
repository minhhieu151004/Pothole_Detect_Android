package com.example.prj_android_detectpothole;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.example.prj_android_detectpothole.language.data;

public class SettingFragment extends Fragment {
    private Spinner spinner_language;
    private LanguageSpinnerAdapter languageAdapter;
    private Button editProfileBtn, changePasswordBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        spinner_language = view.findViewById(R.id.spinner_language);
        languageAdapter = new LanguageSpinnerAdapter(SettingFragment.this, data.getLanguageList());
        spinner_language.setAdapter(languageAdapter);
        editProfileBtn = view.findViewById(R.id.edit_profile_btn);
        changePasswordBtn = view.findViewById(R.id.change_password_btn);

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfileFragment editProfileFragment = new EditProfileFragment();

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragment, editProfileFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragment, changePasswordFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}