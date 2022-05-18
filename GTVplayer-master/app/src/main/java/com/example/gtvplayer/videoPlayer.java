package com.example.gtvplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.gtvplayer.db.GPSData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class videoPlayer extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    GoogleMap mMap;
    LatLng place;
    private ArrayList<LatLng> locationList;
    ArrayList<Integer> interval;

    VideoView videoView;
    TextView tv2;
    String docName, videoLink;
    long duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        interval = new ArrayList<>();



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);

        locationList = new ArrayList<>();
//        TextView tv = findViewById(R.id.textView);
        videoView = findViewById(R.id.videoView);
        tv2 = findViewById(R.id.textView2);

        docName = getIntent().getExtras().getString("docName");
        videoLink = getIntent().getExtras().getString("videoLink");
        getVideodata(docName);

        videoView.setMediaController(new MediaController(this));

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });


        String s="";
        s+=docName;
        s+=videoLink;
        tv2.setText(docName);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGpsdata(docName);
                Log.d("DUrFab+++", String.valueOf(duration));
                mMap.clear();
                mMap.setMinZoomPreference(16.0f);
                float zind = 1.0f;
                for(int i=0;i<locationList.size();i++){
                    Log.d("=position=====", String.valueOf(locationList.get(i)));
                    mMap.addMarker(new MarkerOptions().position(locationList.get(i)).title("Video: "+interval.get(i)+" S"));
                    zind = zind+1.0f;

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(locationList.get(i)));
                }

            }
        });


        }

        private void getGpsdata(String docName){

            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            final long ONE_MEGABYTE = 150 * 1024;
            mStorageRef.child("GT001/upload-"+docName+"/geojs.geojson").getBytes(ONE_MEGABYTE)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            int start = 0;
                            String jsonStr = new String(bytes);
                            try {
                                Gson gson = new Gson();
                                GPSData gpsData = gson.fromJson(jsonStr, GPSData.class);
                                int ival = (int) (duration/gpsData.getFeatures().size());
//                                    Log.d("==check=", String.valueOf(gpsData.getFeatures().size())+" && "+String.valueOf(gpsData.getFeatures().get(0).getGeometry().getCoordinates().get(0))+" && "+String.valueOf(gpsData.getFeatures().get(0).getGeometry().getCoordinates().get(1)));
                                for(int i=0;i<gpsData.getFeatures().size();i++){
                                    place = new LatLng(gpsData.getFeatures().get(i).getGeometry().getCoordinates().get(1), gpsData.getFeatures().get(i).getGeometry().getCoordinates().get(0));

                                    interval.add(start);
                                    start+=ival;

                                    Log.d("=position=====", String.valueOf(place));
                                    locationList.add(place);
                                }

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });


        }

    private void getVideodata(String docName) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        mStorageRef.child("GT001/upload-"+docName+"/vid.mp4").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("===vidurl===", String.valueOf(uri));

                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(String.valueOf(uri), new HashMap<String, String>());
                        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        long timeInMillisec = Long.parseLong(time);
                        duration = timeInMillisec/1000;
                        retriever.release();
                        Log.d("++Duration++", String.valueOf(timeInMillisec/1000));

                        videoView.setVideoPath(String.valueOf(uri));
                        videoView.start();
                    }
                });
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(this);

        }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Integer clickCount = (Integer) marker.getTag();
        if(clickCount!=null){
            Log.d("[[[Marker clicl]]]", marker.getTitle()+" Clicked");
        }
        return false;
    }
}