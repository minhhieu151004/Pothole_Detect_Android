package com.example.prj_android_detectpothole;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.prj_android_detectpothole.OBJECT.MyUserToken;
import com.example.prj_android_detectpothole.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private Fragment dashboardFragment;
    private Fragment settingFragment;
    private Fragment mapFragment;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo Fragment
        dashboardFragment = new DashboardFragment();
        settingFragment = new SettingFragment();
        mapFragment = new MapFragment();

        // Đặt item mặc định của BottomNavigationView
        binding.bottomNavigationView.setSelectedItemId(R.id.dashboard);

        // Nếu Activity lần đầu tiên được tạo, thay thế bằng DashboardFragment
        if (savedInstanceState == null) {
            replaceFragment(dashboardFragment);
        }

        // Xử lý sự kiện khi người dùng chọn mục trong BottomNavigationView
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.dashboard) {
                replaceFragment(dashboardFragment);
            } else if (itemId == R.id.setting) {
                replaceFragment(settingFragment);
            } else if (itemId == R.id.map) {
                replaceFragment(mapFragment);
            }
            return true;
        });

        // Lấy token người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        MyUserToken.token = sharedPreferences.getString("accessToken", null);
        Log.d(TAG, "MainActivity: " + MyUserToken.token);
    }

    // Hàm thay thế fragment
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Kiểm tra nếu fragment chưa được thêm vào thì thêm
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.main_fragment, fragment);
            fragmentTransaction.addToBackStack(null);
        }

        // Xóa các fragment khác không phải là fragment đang hiển thị
        for (Fragment f : fragmentManager.getFragments()) {
            if (f != fragment) {
                fragmentTransaction.remove(f);
            }
        }

        // Hiển thị fragment hiện tại
        fragmentTransaction.show(fragment).commit();
    }
}
