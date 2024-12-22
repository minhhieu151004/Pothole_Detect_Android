package com.example.prj_android_detectpothole;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.example.prj_android_detectpothole.language.data;
import com.example.prj_android_detectpothole.language.language;

import java.util.Locale;

public class SettingFragment extends Fragment {
    private Spinner spinner_language;
    private LanguageSpinnerAdapter languageAdapter;
    private Button editProfileBtn, changePasswordBtn, logOutBtn;

    private final AdapterView.OnItemSelectedListener languageChangeListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selectedLanguage = data.getLanguageList().get(position).getLanguageCode();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
            String currentLanguage = sharedPreferences.getString("selected_language", "en");

            if (!selectedLanguage.equals(currentLanguage)) {
                setLocale(selectedLanguage); // Chỉ thay đổi nếu ngôn ngữ mới khác ngôn ngữ hiện tại
                // Lưu ngôn ngữ đã chọn
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selected_language", selectedLanguage);
                editor.apply();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

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

        loadSavedLanguage();

        spinner_language.setOnItemSelectedListener(languageChangeListener);

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

        // Log out
        logOutBtn = view.findViewById(R.id.log_out_btn);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOut();
            }
        });

        return view;
    }

    public void LogOut() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove("id");
        editor.remove("username");
        editor.remove("email");
        editor.remove("accessToken");

        editor.apply();

        Intent intent = new Intent(requireContext(), WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void setLocale(String langCode) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String currentLanguage = sharedPreferences.getString("selected_language", "en");

        // Chỉ thay đổi nếu ngôn ngữ mới khác ngôn ngữ hiện tại
        if (!currentLanguage.equals(langCode)) {
            Locale locale = new Locale(langCode);
            Locale.setDefault(locale);

            Configuration config = new Configuration();
            config.setLocale(locale);

            Resources resources = requireContext().getResources();
            resources.updateConfiguration(config, resources.getDisplayMetrics());

            // Lưu ngôn ngữ đã chọn vào SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("selected_language", langCode);
            editor.apply();

            // Làm mới Activity để áp dụng ngôn ngữ mới
            requireActivity().recreate();

        }
    }

    private void loadSavedLanguage() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String savedLanguage = sharedPreferences.getString("selected_language", "en"); // "en" là mặc định nếu không có ngôn ngữ nào được lưu

        // Kiểm tra nếu ngôn ngữ lưu trong SharedPreferences khác với ngôn ngữ hiện tại
        Locale currentLocale = requireContext().getResources().getConfiguration().locale;
        if (!currentLocale.getLanguage().equals(savedLanguage)) {
            setLocale(savedLanguage);
        }

        // Cập nhật vị trí `Spinner` theo ngôn ngữ đã lưu
        spinner_language.setOnItemSelectedListener(null); // Tạm thời tắt listener
        for (int i = 0; i < data.getLanguageList().size(); i++) {
            if (data.getLanguageList().get(i).getLanguageCode().equals(savedLanguage)) {
                spinner_language.setSelection(i);
                break;
            }
        }
        spinner_language.setOnItemSelectedListener(languageChangeListener); // Kích hoạt lại listener
    }
}
