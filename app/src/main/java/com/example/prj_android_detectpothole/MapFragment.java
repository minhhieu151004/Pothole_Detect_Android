package com.example.prj_android_detectpothole;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.prj_android_detectpothole.OBJECT.MyMarker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnCameraIdleListener{

    //Khai báo
    View view;
    LinearLayout linear_select_location;
    Button btn_Show_Pothole, btn_Add_Pothole, btn_goback, btn_ok;
    ImageButton btn_zoom_in,btn_zoom_out;
    EditText edt_Serch;
    private GoogleMap map;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;
    private boolean isAddPotholeMode = false;
    List<Marker> listMyMark;
    Marker mark;

    void findID(View view){
        btn_Show_Pothole = view.findViewById(R.id.btn_show_pothole);
        btn_Add_Pothole = view.findViewById(R.id.btn_add_pothole);
        btn_goback = view.findViewById(R.id.btn_goBack);
        btn_ok = view.findViewById(R.id.btn_ok);
        btn_zoom_in = view.findViewById(R.id.btn_zoom_in);
        btn_zoom_out = view.findViewById(R.id.btn_zoom_out);
        edt_Serch = view.findViewById(R.id.edt_Serch);
        linear_select_location = view.findViewById(R.id.notify_select_location);
        linear_select_location.setVisibility(View.GONE);
        listMyMark = new ArrayList<>();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);
        findID(view);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
                edt_Serch.setVisibility(View.GONE);
                btn_Add_Pothole.setVisibility(View.GONE);
                btn_Show_Pothole.setVisibility(View.GONE);
                btn_zoom_in.setVisibility(View.GONE);
                btn_zoom_out.setVisibility(View.GONE);
                linear_select_location.setVisibility(View.VISIBLE);
                isAddPotholeMode = true;
                LatLng center = map.getCameraPosition().target;
                mark = map.addMarker(new MarkerOptions().position(center));
            }
        });
        btn_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_Serch.setVisibility(View.VISIBLE);
                btn_Add_Pothole.setVisibility(View.VISIBLE);
                btn_Show_Pothole.setVisibility(View.VISIBLE);
                btn_zoom_in.setVisibility(View.VISIBLE);
                btn_zoom_out.setVisibility(View.VISIBLE);
                linear_select_location.setVisibility(View.GONE);
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
        return view;
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setOnMyLocationButtonClickListener(this);
        map.setOnMyLocationClickListener(this);
        enableMyLocation();
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
                if (isAddPotholeMode) {
                    return true;
                }
                return false;
            }
        });
        map.setOnMapClickListener(this::onMapClick);
        map.setOnMapLongClickListener(this::onMapLongClick);
        map.setOnCameraIdleListener(this::onCameraIdle);
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(false);

        MyMarker myMarker = new MyMarker(1,10.869633,106.803269,"8 Street, Linh Xuan, Thu Duc, HCM City","HIGH");

        MarkerOptions markerOpt = new MarkerOptions();
        LatLng test = new LatLng(10.869633, 106.803269);
        markerOpt.position(test)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.error));
        map.addMarker(markerOpt).setTag(myMarker);

        myMarker = new MyMarker(1,10.873079,106.807883,"8 Street, Linh Xuan, Thu Duc, HCM City","MEDIUM");
        map.addMarker(new MarkerOptions()
                .position(new LatLng(10.873079,106.807883))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.warning))).setTag(myMarker);

        myMarker = new MyMarker(1,10.864635,106.799511,"8 Street, Linh Xuan, Thu Duc, HCM City","LOW");
        map.addMarker(new MarkerOptions()
                .position(new LatLng(10.864635,106.799511))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.info))).setTag(myMarker);

        MyIn4WindowAdapter adapter = new MyIn4WindowAdapter(getActivity());
        map.setInfoWindowAdapter(adapter);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(test,15.0f));

    }

    @Override
    public void onCameraIdle() {
        if(isAddPotholeMode){
            LatLng center = map.getCameraPosition().target;
            mark = map.addMarker(new MarkerOptions().position(center));
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        //String addr = getAddressFromLatLng(latLng.latitude,latLng.longitude);
        //Toast.makeText(getActivity(), "Addr: " + addr, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                enableMyLocation();
            }
            else {
                Toast.makeText(getActivity(), "Quyền vị trí bị từ chối", Toast.LENGTH_SHORT).show();
            }

            return;
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
                edt_Serch.setVisibility(View.VISIBLE);
                btn_Add_Pothole.setVisibility(View.VISIBLE);
                btn_Show_Pothole.setVisibility(View.VISIBLE);
                btn_zoom_out.setVisibility(View.VISIBLE);
                btn_zoom_in.setVisibility(View.VISIBLE);
                linear_select_location.setVisibility(View.GONE);
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

    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            return;
        }
        ActivityCompat.requestPermissions(getActivity(),new String[] {
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
    }
}