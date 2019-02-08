package com.rna.markme.teacher;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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
import com.rna.markme.MainActivity;
import com.rna.markme.R;

import net.cryptobrewery.macaddress.MacAddress;

import java.util.HashMap;
import java.util.Map;

public class TeacherMainActivity extends AppCompatActivity {

    TextView txt;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference ref;
    Hotspot hotspot;
    Boolean b=false;
    String email,sub,c="";
    EditText subject;
    WifiManager wifiManager;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);
        subject=(EditText)findViewById(R.id.subject);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        String uid = user.getUid();
        txt = (TextView) findViewById(R.id.textView);
        txt.setText(email.substring(0, email.length() - 10));
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
        startActivity(new Intent(TeacherMainActivity.this, MainActivity.class));
        finishAffinity();
    }

    public void markSlot(View view) {
        sub=subject.getText().toString();
        if(TextUtils.isEmpty(sub)){
            Toast.makeText(this,"Enter subject TAG",Toast.LENGTH_SHORT).show();
        }
        else {

            final Map<String, Object> Bssid = new HashMap<>();
            Bssid.put("Bssid", getBssid());
            db.collection(email.substring(0, email.length() - 10)).document(sub).set(Bssid, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    c=sub;
                    Toast.makeText(TeacherMainActivity.this, "SLOT CREATED", Toast.LENGTH_SHORT).show();
                    hotspot.start("Hotspot-Android", "12345678");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TeacherMainActivity.this, "Faliure: Try Again", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    public void getAttendance(View view) {
//        if (TextUtils.isEmpty(c)) {
//            Toast.makeText(TeacherMainActivity.this, "Mark a slot first", Toast.LENGTH_SHORT).show();
//        } else {
        sub=subject.getText().toString();
            hotspot.stop();
            wifiManager.setWifiEnabled(true);
            Intent intent = new Intent(TeacherMainActivity.this, TeacherInterfaceActivity.class);
            intent.putExtra("subTag", sub);
            startActivity(intent);
//        }
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
