package com.rna.markme.teacher;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rna.markme.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetAttendance extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> presentStudents = new ArrayList<>();
    ArrayAdapter adapter;
    ListView lv;
    WifiManager wifiManager;
    String lectureTag,email,teacherID,attString="";
    boolean connected = false;
    Button saveAtt;
    private ProgressBar mLoadingIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_attendance);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        saveAtt=findViewById(R.id.saveAtt);
        email = user.getEmail();
        teacherID=email.substring(0, email.length() - 10);
        Intent intent=getIntent();
        lectureTag=intent.getStringExtra("subTag");
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        adapter =  new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,presentStudents);
        lv = (ListView)findViewById(R.id.data);
        lv.setAdapter(adapter);
        getData(teacherID,lectureTag);
        saveAtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!attString.equals("")){
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(GetAttendance.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1000);
                    }else{
                        saveAttendance(lectureTag,attString);
                    }

                }else {
                    Toast.makeText(GetAttendance.this,"No Data",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void refresh(View view){
        mLoadingIndicator.setVisibility(View.VISIBLE);
        presentStudents.clear();
        adapter.clear();
        getData(teacherID,lectureTag);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, TeacherAccount.class));
        finishAffinity();
    }

    public void getData(String teacherID, String lectureTag){

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else{

            mLoadingIndicator.setVisibility(View.INVISIBLE);
            connected = false;}
        if(connected==true){
            db.collection(teacherID).document(lectureTag).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {
                        mLoadingIndicator.setVisibility(View.INVISIBLE);
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                        Map<String, Object> user = new HashMap<>();
                        user=document.getData();
                            attString="";
                        for (String s : user.keySet()) {
                            presentStudents.add(s);
                            attString+= s + "\n";

                        }
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(GetAttendance.this, "No such document", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(GetAttendance.this, "Failed : Try Again", Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }
        else {
            Toast.makeText(this,"Please Turn on your Internet",Toast.LENGTH_SHORT).show();
        }
    }


    public void saveAttendance(String filename, String content) {
        String fileName= filename + ".txt";

        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "MarkMe");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            File file=new File(folder,fileName);

            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(content.getBytes());
                fos.close();
                Toast.makeText(this,"Saved!",Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this,"FileNotFound",Toast.LENGTH_SHORT).show();
            }
            catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this,"Error saving!",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this,"Error saving!",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 1000) {
            if(grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveAttendance(lectureTag,attString);// <-- Start Beemray here
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
// Permission was denied or request was cancelled
            }
        }
    }
}
