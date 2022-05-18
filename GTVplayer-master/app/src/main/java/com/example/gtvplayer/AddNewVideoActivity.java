package com.example.gtvplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.gtvplayer.db.AppDatabase;
import com.example.gtvplayer.db.Geovid;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class AddNewVideoActivity extends AppCompatActivity {

    GeovidListAdapter adapter;

    Button recordbtn, vidfilepickbtn, gpsfilepickbtn, uploadbtn;
    TextView vidpath, gpspath;

    String vidpathval, gpspathval;
    Intent myFileIntent;
    ProgressBar pb;
    EditText uploadname;
    ProgressDialog dialog;

    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference vidref, gpsref;

    Uri videouri, gpsuri;
    String vdl, gdl, docref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_video);

        recordbtn = findViewById(R.id.record);

        uploadname = findViewById(R.id.uploadName);

        vidpath = findViewById(R.id.vidPath);
        vidfilepickbtn = findViewById(R.id.vidFilePick);

        gpspath = findViewById(R.id.gpsPath);
        gpsfilepickbtn = findViewById(R.id.gpsFilePick);
        pb = findViewById(R.id.vidprogressBar);

        uploadbtn = findViewById(R.id.uploadButton);




        vidfilepickbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                myFileIntent.setType("*/*");
                startActivityForResult(myFileIntent, 10);
            }
        });

        gpsfilepickbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                myFileIntent.setType("*/*");
                startActivityForResult(myFileIntent, 20);
            }
        });

        recordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                //intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                startActivityForResult(intent, 1);
            }
        });

        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(View.VISIBLE);
//                upload();
//                dialog = ProgressDialog.show(AddNewVideoActivity.this, "Uploading","Please wait...", true);
                new FileUploadTask().execute();

            }
        });

    }



    class FileUploadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Geovid geovid = new Geovid();

                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                String Uid = currentFirebaseUser.getUid();

                String uref = uploadname.getText().toString();
                vidref = storageRef.child("/"+Uid+"/upload-"+uref+"/vid.mp4");
                geovid.videoLink = vidref.toString(); //vdl;
                gpsref = storageRef.child("/"+Uid+"/upload-"+uref+"/geojs.geojson");
                geovid.gpsLink =  gpsref.toString(); //gdl;

                videouri = Uri.parse(vidpathval);
                UploadTask ut  = vidref.putFile(videouri);

                ut.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNewVideoActivity.this,
                                "Upload failed: "+e.getLocalizedMessage(),
                                Toast.LENGTH_LONG).show();

                    }
                }).addOnSuccessListener((OnSuccessListener) (takeSnapshot)->{
                    storageRef.child("/"+Uid+"/upload-"+ uref +"/vid.mp4").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("===vidurl===", String.valueOf(uri));
                            vdl = uri.toString();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.d("failure-daya", "exception");
                        }
                    });;
                    Toast.makeText(AddNewVideoActivity.this,
                            "Upload complete",
                            Toast.LENGTH_LONG).show();
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        System.out.println("Upload is " + progress + "% done");
                        int currentprogress = (int) progress;
                        pb.setProgress(currentprogress);
                    }
                });


                gpsuri = Uri.parse(gpspathval);
                UploadTask ut2  = gpsref.putFile(gpsuri);
                ut2.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNewVideoActivity.this,
                                "Upload failed: "+e.getLocalizedMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener((OnSuccessListener) (takeSnapshot)->{
                    storageRef.child("/"+Uid+"/upload-"+ uref +"/geojs.geojson").getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d("===gpsurl===", String.valueOf(uri));
                                    gdl = uri.toString();
                                }
                            });

                    Toast.makeText(AddNewVideoActivity.this,"Upload complete",Toast.LENGTH_LONG).show();
                });


                Map<String, Object> data = new HashMap<>();
                data.put("UID", "GT001");
                data.put("videopath", vidpathval);
                data.put("gpspath", gpspathval);
                data.put("viddl", vidref.toString());
                data.put("gpsdl", gpsref.toString());

                db.collection("users").document(Uid)
                        .collection("uploads").add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("==daya==", "DocumentSnapshot added with ID: " + documentReference.getId());
                                pb.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "DocumentSnapshot added with ID: " + documentReference.getId(), Toast.LENGTH_SHORT).show();
                                docref = documentReference.getId();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("==daya==", "Error adding document", e);
                                Toast.makeText(getApplicationContext(), "Error adding paths", Toast.LENGTH_SHORT).show();
                            }
                        });

                //local db insertion
                AppDatabase ldb = AppDatabase.getInstance(getApplicationContext());

                geovid.docid = uploadname.getText().toString(); //docref
                geovid.lvideoppath = vidpathval;
                geovid.lgpspath = gpspathval;

                ldb.geovidDAO().insertGeovid(geovid);
                finish();

                return null;

            }catch (Exception e){
                if (dialog.isShowing())
                    dialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        "Exception!!!",
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Void... unused) {

        }
        protected void onPostExecute(Long result) {
//            Toast.makeText(getApplicationContext(), "uploaded", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            VideoView videoView = new VideoView(this);
            videoView.setVideoURI(data.getData());
            videoView.start();
            builder.setView(videoView).show();
        }
        else if(requestCode==10 && resultCode==RESULT_OK){
            Uri path = data.getData();
            String type = data.getType();
            if(path!=null){
                vidpathval = path.toString();
                if(vidpathval.toLowerCase().startsWith("file://")){
                    vidpathval = (new File(URI.create(vidpathval))).getAbsolutePath();
                }
            }
            vidpath.setText(vidpathval);
        }
        else if(requestCode==20 && resultCode==RESULT_OK){
            Uri path = data.getData();
            String type = data.getType();
            if(path!=null){
                gpspathval = path.toString();
                if(gpspathval.toLowerCase().startsWith("file://")){
                    gpspathval = (new File(URI.create(gpspathval))).getAbsolutePath();
                }
            }
            gpspath.setText(gpspathval);
        }
    }
}