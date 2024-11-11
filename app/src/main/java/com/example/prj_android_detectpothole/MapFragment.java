package com.example.prj_android_detectpothole;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.prj_android_detectpothole.API.API_Routing;
import com.example.prj_android_detectpothole.MODEL.MyApplication;
import com.example.prj_android_detectpothole.MODEL.MyRouting;
import com.example.prj_android_detectpothole.OBJECT.MyMarker;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment implements
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnMapClickListener {


    final public String TAG = "FragmentMap";
    //Khai báo
    View view;
    LinearLayout linear_select_location;
    Button btn_Show_Pothole, btn_Add_Pothole, btn_goback, btn_ok;
    ImageButton btn_zoom_in, btn_zoom_out, btn_moveToMyLocation, btn_direction,btn_cancel_routing;
    EditText edt_Serch;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;
    private boolean isAddPotholeMode = false;
    private boolean isDirectionMode = false;

    private GoogleMap map;
    LatLng latLng_userLocation;
    List<Marker> listMyMark;
    Marker mark;
    Marker marker_search, current_marker;
    Polyline polyline;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            if(locationResult == null){
                return;
            }
            if(polyline!=null){
                btn_cancel_routing.setVisibility(View.VISIBLE);
            }
            else btn_cancel_routing.setVisibility(View.GONE);

            if(isDirectionMode){
                for (Location location: locationResult.getLocations()){
                    latLng_userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    //map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    PlayRoute(2);
                }
                CheckPothole();
            }
        }
    };

    void findID(View view) {
        btn_Show_Pothole = view.findViewById(R.id.btn_show_pothole);
        btn_Add_Pothole = view.findViewById(R.id.btn_add_pothole);
        btn_goback = view.findViewById(R.id.btn_goBack);
        btn_ok = view.findViewById(R.id.btn_ok);
        btn_zoom_in = view.findViewById(R.id.btn_zoom_in);
        btn_zoom_out = view.findViewById(R.id.btn_zoom_out);
        btn_moveToMyLocation = view.findViewById(R.id.btn_moveToMyLocation);
        btn_direction = view.findViewById(R.id.btn_direction);
        btn_cancel_routing = view.findViewById(R.id.btn_cancel_routing);
        edt_Serch = view.findViewById(R.id.edt_Serch);
        linear_select_location = view.findViewById(R.id.notify_select_location);
        linear_select_location.setVisibility(View.GONE);
        btn_cancel_routing.setVisibility(View.GONE);
        listMyMark = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);
        findID(view);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //Event Button click
        btn_Show_Pothole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogShowPothole();
            }
        });
        btn_Add_Pothole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGONE();
                isAddPotholeMode = true;
                LatLng center = map.getCameraPosition().target;
                mark = map.addMarker(new MarkerOptions().position(center));
            }
        });
        btn_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewVISIBLE();
                isAddPotholeMode = false;
                if (mark != null) {
                    mark.remove();
                }
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogAddPothole();
            }
        });
        btn_zoom_in.setOnClickListener(v -> map.animateCamera(CameraUpdateFactory.zoomIn()));
        btn_zoom_out.setOnClickListener(v -> map.animateCamera(CameraUpdateFactory.zoomOut()));
        btn_moveToMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomToUserLocation();
            }
        });
        edt_Serch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    String address = edt_Serch.getText().toString().trim();
                    String addrKey = address.replace(' ','+');

                    if (!address.isEmpty()) {
                        LatLng latLng = getLatLngFromAddress(address);
                        if(marker_search != null) marker_search.remove();
                        if(latLng != null){
                            String addr = getAddressFromLatLng(latLng.latitude,latLng.longitude);
                            marker_search = map.addMarker(new MarkerOptions().position(latLng));
                            marker_search.setTag("NotInfoWindow");
                            current_marker = marker_search;
                            zoomToLatLng(latLng);
                        }
                        else {
                            Toast.makeText(getContext(), "Not found the address!", Toast.LENGTH_SHORT).show();
                            edt_Serch.setText("");
                        }
                    } else {
                        Toast.makeText(getContext(), "Please enter an address", Toast.LENGTH_SHORT).show();
                    }
                    hideKeyboard(edt_Serch);
                    return true; // Đã xử lý sự kiện
                }
                return false; // Sự kiện không được xử lý, tiếp tục lan truyền
            }
        });
        btn_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(current_marker!=null){
                    isDirectionMode = true;
                    PlayRoute(1);
                }
                else Toast.makeText(getActivity(), "Current = null", Toast.LENGTH_SHORT).show();
            }
        });
        btn_cancel_routing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_Serch.setText("");
                if(current_marker != null) current_marker = null;
                if(marker_search != null) {
                    marker_search.remove();
                    marker_search = null;
                }
                if(polyline != null) {
                    polyline.remove();
                    polyline = null;
                }
                isDirectionMode = false;
                zoomToUserLocation();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        StopLocationUpdates();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        enableMyLocation();
        enableNotification();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.setInfoWindowAdapter(new MyIn4WindowAdapter(getActivity()));
        map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (mark != null && isAddPotholeMode) {
                    mark.remove();
                    mark = null;
                }
            }
        });
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                current_marker = marker;
                if (isAddPotholeMode || "NotInfoWindow".equals(marker.getTag())) {
                    LatLng latLng = new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);
                    zoomToLatLng(latLng);
                    return true;
                }
                return false;
            }
        });
        map.setOnCameraIdleListener(this::onCameraIdle);

    }

    @Override
    public void onCameraIdle() {
        if (isAddPotholeMode) {
            LatLng center = map.getCameraPosition().target;
            mark = map.addMarker(new MarkerOptions().position(center));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
                zoomToUserLocation();
                CheckSettingAndStartLocationUpdates();
            } else {
                Toast.makeText(getActivity(), "Quyền vị trí bị từ chối", Toast.LENGTH_SHORT).show();
            }

            return;
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
    }

    private void zoomToUserLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            }
        });
        locationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    private void zoomToLatLng(LatLng latLng){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
    }
    private void CheckSettingAndStartLocationUpdates() {
        LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(getActivity());

        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(locationSettingsRequest);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                StartLocationUpdates();
            }
        });
        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    try {
                        apiException.startResolutionForResult(getActivity(), 1001);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
    private void StartLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }
    private void StopLocationUpdates(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            zoomToUserLocation();
            CheckSettingAndStartLocationUpdates();
            return;
        }
        ActivityCompat.requestPermissions(getActivity(),new String[] {
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
    }
    private void enableNotification(){
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),new String[] {
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    private void openDialogShowPothole() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_show_pothole);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams winAttributes = window.getAttributes();
        winAttributes.gravity = Gravity.BOTTOM;
        window.setAttributes(winAttributes);

        dialog.setCancelable(true);

        dialog.show();
    }
    private void openDialogAddPothole() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_add_pothole);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams winAttributes = window.getAttributes();
        winAttributes.gravity = Gravity.CENTER;
        window.setAttributes(winAttributes);

        dialog.setCancelable(false);

        Button btn_Cancel, btn_Submit;
        btn_Cancel = dialog.findViewById(R.id.btn_Cancel);
        btn_Submit = dialog.findViewById(R.id.btn_Submit);
        TextView dialog_head = dialog.findViewById(R.id.dialog_head);
        Spinner spinner = dialog.findViewById(R.id.dialog_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.pothole_level, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String levelSelected = adapterView.getItemAtPosition(i).toString();
                switch (levelSelected) {
                    case "HIGH":
                        dialog_head.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bground_dialogadd_head_high));
                        spinner.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bground_dialogadd_txtlevel_high));
                        btn_Submit.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bground_dialogadd_btnsubmit_high));
                        break;
                    case "MEDIUM":
                        dialog_head.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bground_dialogadd_head_medium));
                        spinner.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bground_dialogadd_txtlevel_medium));
                        btn_Submit.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bground_dialogadd_btnsubmit_medium));
                        break;
                    case "LOW":
                        dialog_head.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bground_dialogadd_head_low));
                        spinner.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bground_dialogadd_txtlevel_low));
                        btn_Submit.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bground_dialogadd_btnsubmit_low));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set data roi post lên sever
                String level = spinner.getSelectedItem().toString();
                if (mark != null) {
                    double lat = mark.getPosition().latitude;
                    double lon = mark.getPosition().longitude;
                    String addr = getAddressFromLatLng(lat,lon);
                    MyMarker mMarker = new MyMarker(1,lat,lon,addr,level);
                    mark.setTag(mMarker);
                    switch (level) {
                        case "HIGH":
                            mark.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.error));
                            break;
                        case "MEDIUM":
                            mark.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.warning));
                            break;
                        case "LOW":
                            mark.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.info));
                            break;
                    }
                    listMyMark.add(mark);
                }
                Toast.makeText(getActivity(), "Submit successfully!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                ViewVISIBLE();
                isAddPotholeMode = false;
            }
        });
        dialog.show();
    }
    public String getAddressFromLatLng(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);

                // Trả về địa chỉ dạng chuỗi
                return address.getAddressLine(0); // Địa chỉ đầy đủ
            } else {
                return "NULL";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "NULL";
        }
    }
    public LatLng getLatLngFromAddress(String addr){
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addressList;
        LatLng latLng = null;

        try {
            // Tìm kiếm danh sách địa chỉ khớp với chuỗi đầu vào
            addressList = geocoder.getFromLocationName(addr, 1);
            if (addressList != null && !addressList.isEmpty()) {
                // Lấy địa chỉ đầu tiên và trích xuất lat, long
                Address address = addressList.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();
                latLng = new LatLng(latitude, longitude);
            }
        } catch (IOException e) {
            Log.d(TAG,e.getMessage() );
            e.printStackTrace();
        }
        return latLng;
    }
    private void ViewVISIBLE(){
        edt_Serch.setVisibility(View.VISIBLE);
        btn_Add_Pothole.setVisibility(View.VISIBLE);
        btn_Show_Pothole.setVisibility(View.VISIBLE);
        btn_zoom_in.setVisibility(View.VISIBLE);
        btn_zoom_out.setVisibility(View.VISIBLE);
        btn_moveToMyLocation.setVisibility(View.VISIBLE);
        btn_direction.setVisibility(View.VISIBLE);
        linear_select_location.setVisibility(View.GONE);
    }
    private void ViewGONE(){
        edt_Serch.setVisibility(View.GONE);
        btn_Add_Pothole.setVisibility(View.GONE);
        btn_Show_Pothole.setVisibility(View.GONE);
        btn_zoom_in.setVisibility(View.GONE);
        btn_zoom_out.setVisibility(View.GONE);
        btn_moveToMyLocation.setVisibility(View.GONE);
        btn_direction.setVisibility(View.GONE);
        linear_select_location.setVisibility(View.VISIBLE);
    }
    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void CallApiRouting(double lat1, double lon1, double lat2, double lon2, int mode){
        String s_waypoints = lat1 + "," + lon1 + "|" + lat2 + "," + lon2;
        API_Routing.api_routing.getRouting(s_waypoints,"motorcycle",getString(R.string.GEOAPIFY_KEY))
                .enqueue(new Callback<MyRouting>() {
                    @Override
                    public void onResponse(Call<MyRouting> call, Response<MyRouting> response) {
                        MyRouting myRouting = response.body();
                        if(myRouting!=null){
                            try{
                                MyRouting.Features ft = myRouting.features.get(0);
                                MyRouting.Properties properties = ft.properties;
                                MyRouting.Geometry geometry = ft.geometry;

                                List<LatLng> latLngList = new ArrayList<>();
                                List<List<Double>> list_coordinates = geometry.coordinates.get(0);
                                for (List<Double> coordinate: list_coordinates) {
                                    LatLng latLng = new LatLng(coordinate.get(1),coordinate.get(0));
                                    latLngList.add(latLng);
                                }

                                if(!latLngList.isEmpty()){
                                    PolylineOptions polylineOptions = new PolylineOptions()
                                            .addAll(latLngList) // Thêm tất cả các điểm vào polyline
                                            .width(10)      // Đặt độ rộng của đường
                                            .color(Color.BLUE); // Đặt màu sắc của đường
                                    if(polyline!=null) polyline.remove();
                                    polyline=map.addPolyline(polylineOptions);
                                    if(mode == 1){
                                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                        for (LatLng point : latLngList) {
                                            builder.include(point);
                                        }
                                        LatLngBounds bounds = builder.build();
                                        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                                    }
                                }
                                Log.d(TAG,"onSuccess");
                            }
                            catch (Exception e){
                                Log.e(TAG,"Call api error: " + e.getMessage());
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<MyRouting> call, Throwable throwable) {
                        Toast.makeText(getContext(), "Get Routing Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"onFailure");
                    }
                });
    }
    private void PlayRoute(int mode){
        if(current_marker!=null && latLng_userLocation!=null && marker_search!=null){
            double lat1 = latLng_userLocation.latitude;
            double lon1 = latLng_userLocation.longitude;

            LatLng destination = current_marker.getPosition();
            double lat2 = destination.latitude;
            double lon2 = destination.longitude;

            CallApiRouting(lat1,lon1,lat2,lon2,mode);
        }
    }
    private void SendNotificationNearPothole(MyMarker myMarker){
        Notification notification = new NotificationCompat.Builder(getActivity(), MyApplication.CHANNEL_ID)
                .setContentTitle("Pothole near you!!!")
                .setContentText("Address: "+myMarker.getAddr())
                .setSmallIcon(R.drawable.pothole)
                .build();

        NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager!=null){
            notificationManager.notify(GetNotificationID(),notification);
            Log.d(TAG,"onSendNotificationNearPothole: ");
        }
        else {
            Log.d(TAG,"onSendNotificationNearPothole: null notificationManager");
        }

    }
    private int GetNotificationID(){
        return (int) new Date().getTime();
    }
    private void CheckPothole(){
        if(listMyMark!=null && !listMyMark.isEmpty() && latLng_userLocation!= null){
            for(Marker mark : listMyMark){
                float[] results = new float[1];
                Location.distanceBetween(latLng_userLocation.latitude, latLng_userLocation.longitude, mark.getPosition().latitude, mark.getPosition().longitude, results);
                float distance = results[0];
                if(distance<30){
                    if(mark.getSnippet()==null || !mark.getSnippet().equals("Notified")){
                        mark.setSnippet("Notified");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 111);
                            }
                            else {
                                MyMarker myMarker = (MyMarker) mark.getTag();
                                if(myMarker!=null){
                                    SendNotificationNearPothole(myMarker);
                                    Log.d(TAG, "send notify: "+myMarker.getAddr());
                                }
                            }
                        }
                    }
                }
                else mark.setSnippet(null);
            }
        }

    }

}