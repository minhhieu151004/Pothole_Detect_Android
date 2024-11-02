package com.example.prj_android_detectpothole;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ChangePasswordUpdateFragment extends Fragment {
    private Button updateBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password_update, container, false);

        updateBtn = view.findViewById(R.id.update_btn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSuccessDialog("Changed Password Successfully");
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