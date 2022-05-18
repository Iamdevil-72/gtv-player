package com.example.gtvplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.gtvplayer.db.AppDatabase;
import com.example.gtvplayer.db.Geovid;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GeovidListAdapter.OnGeovidListener{

    Button lgout;
    FirebaseAuth fAuth;
    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private GeovidListAdapter geovidListAdapter;
    private List<Geovid>  geovidList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addnewvideobtn = findViewById(R.id.addNewVideo);
        lgout = findViewById(R.id.lgout);
        fAuth = FirebaseAuth.getInstance();


        addnewvideobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, AddNewVideoActivity.class), 100);
            }
        });
        lgout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();//logout
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });

        initRecyclerView();
        loadVideoList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.help:

                Toast.makeText(getApplicationContext(), "Help is selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Tutorial.class);
                startActivity(intent);
                return true;
            case R.id.refresh:
//                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
//                Toast.makeText(this, "" + currentFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Refresh Clicked", Toast.LENGTH_SHORT).show();
                return true;
            default:return super.onOptionsItemSelected(item);
        }


    }

    private void initRecyclerView() {
        RecyclerView rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration did = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(did);
        geovidListAdapter = new GeovidListAdapter(this);
        rv.setAdapter(geovidListAdapter);
    }

    private void loadVideoList(){
        AppDatabase db = AppDatabase.getInstance(this.getApplicationContext());
        geovidList = db.geovidDAO().getAllGeovids();
        geovidListAdapter.setGeovidList(geovidList, this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==100){
            loadVideoList();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onGeovidClick(int position) {
        Log.d("====", "item clicked");
        geovidList.get(position);
        Intent intent = new Intent(this, videoPlayer.class  );

        intent.putExtra("docName", geovidList.get(position).docid);
        intent.putExtra("videoLink", geovidList.get(position).videoLink);
        intent.putExtra("gpsFile", geovidList.get(position).lgpspath);
        startActivity(intent);
    }
}