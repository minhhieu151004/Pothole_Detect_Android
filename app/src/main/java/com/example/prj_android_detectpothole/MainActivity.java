package com.example.prj_android_detectpothole;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.prj_android_detectpothole.OBJECT.MyUserToken;
import com.example.prj_android_detectpothole.databinding.ActivityMainBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private Fragment dashboardFragment;
    private Fragment settingFragment;
    private Fragment mapFragment;
    final public String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Khoi tao Fragment
        dashboardFragment = new DashboardFragment();
        settingFragment = new SettingFragment();
        mapFragment = new MapFragment();

        binding.bottomNavigationView.setSelectedItemId(R.id.dashboard);

        //Them Dashboard ban dau
        replaceFragment(dashboardFragment);

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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        MyUserToken.token = sharedPreferences.getString("accessToken",null);
        Log.d(TAG,"MainActivity: " + MyUserToken.token);
    }

    //Function
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(!fragment.isAdded()) {
            fragmentTransaction.add(R.id.main_fragment, fragment);
            fragmentTransaction.addToBackStack(null);
        }

        for (Fragment f : fragmentManager.getFragments()) {
            if (f!=fragment) {
                fragmentTransaction.remove(f);
            }
        }

        fragmentTransaction.show(fragment).commit();
    }
}