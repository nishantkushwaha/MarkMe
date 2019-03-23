package com.rna.markme.teacher;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.knexis.hotspot.Hotspot;
import com.rna.markme.User;
import com.rna.markme.R;

import net.cryptobrewery.macaddress.MacAddress;

import java.util.HashMap;
import java.util.Map;

public class TeacherAccount extends AppCompatActivity {

    TextView txt;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference ref;
    Hotspot hotspot;
    Boolean b=false;
    String teacherID,lectureTag;
    EditText subject;
    WifiManager wifiManager;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_account);
        subject=(EditText)findViewById(R.id.subject);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        teacherID = user.getEmail();
        String uid = user.getUid();
        txt = (TextView) findViewById(R.id.textView);
        txt.setText(teacherID.substring(0, teacherID.length() - 10));
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        hotspot = new Hotspot(this);


    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void signOut(View view) {
        hotspot.stop();
        wifiManager.setWifiEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        startActivity(new Intent(TeacherAccount.this, User.class));
        finishAffinity();
    }

    public void makeSlot(View view) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        lectureTag=subject.getText().toString();
        if(TextUtils.isEmpty(lectureTag)){
            Toast.makeText(this,"Enter Lecture-TAG",Toast.LENGTH_SHORT).show();
        }
        else {

            final Map<String, Object> Bssid = new HashMap<>();
            Bssid.put("Bssid", getBssid());
            db.collection(teacherID.substring(0, teacherID.length() - 10)).document(lectureTag).set(Bssid, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    txt.setText(teacherID.substring(0, teacherID.length() - 10)+"\n Slot Created!");
                    Toast.makeText(TeacherAccount.this, "SLOT CREATED", Toast.LENGTH_SHORT).show();
                    hotspot.start("Hotspot-Android", "12345678");
                    mLoadingIndicator.setVisibility(View.INVISIBLE);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    txt.setText(teacherID.substring(0, teacherID.length() - 10)+"\n Make Slot Again!");
                    Toast.makeText(TeacherAccount.this, "Faliure: Try Again", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    public void getAttendance(View view) {
        if (TextUtils.isEmpty(subject.getText().toString())) {
            Toast.makeText(TeacherAccount.this, "Enter Lecture-TAG", Toast.LENGTH_SHORT).show();
        } else {
        lectureTag=subject.getText().toString();
            hotspot.stop();
            wifiManager.setWifiEnabled(true);
            Intent intent = new Intent(TeacherAccount.this, GetAttendance.class);
            intent.putExtra("subTag", lectureTag);
            startActivity(intent);
        }
    }

    public String getBssid(){
        String bssid=MacAddress.getMacAddr();
        int c=0,i=0;
        for(i=0;i<bssid.length();i++){
            if(bssid.charAt(i)!=':'){
                c++;
            }
            else {
                if (c == 2)
                    c = 0;
                else if (c == 1) {
                    i--;
                    bssid=bssid.substring(0,i)+"0"+bssid.substring(i,bssid.length());
                }
            }
        }
        if(i==16&&c==1){
            i--;
            bssid=bssid.substring(0,i)+"0"+bssid.substring(i,bssid.length());
        }
        return bssid;
    }


}
