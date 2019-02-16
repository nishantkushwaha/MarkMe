package com.rna.markme;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rna.markme.student.StudentLogin;
import com.rna.markme.teacher.TeacherLogin;

public class User extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener authstateListener;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        RelativeLayout rt = (RelativeLayout) findViewById(R.id.Rtlayout);
        TextView txp = (TextView) findViewById(R.id.txp);
        rt.setVisibility(View.GONE);
        txp.setText("Please wait a moment");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            ref = FirebaseDatabase.getInstance().getReference().child(uid);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String TYPE = dataSnapshot.child("type").getValue().toString();
                    String ANDROIDID = dataSnapshot.child("android").getValue().toString();
                    if (TYPE.equals("student")) {
                        if(ANDROIDID.equals(Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID)))
                        startActivity(new Intent(User.this, StudentLogin.class));
                        else Toast.makeText(User.this, "Device Doesn't Match", Toast.LENGTH_SHORT).show();
                    } else {
                        if(ANDROIDID.equals(Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID)))
                        startActivity(new Intent(User.this, TeacherLogin.class));
                        else Toast.makeText(User.this, "Device Doesn't Match", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            txp.setVisibility(View.GONE);
            rt.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void StudentAct(View view) {
        startActivity(new Intent(User.this, StudentLogin.class));
    }

    public void TeacherAct(View view) {
        startActivity(new Intent(User.this, TeacherLogin.class));
    }

    public void register(View view) {
        Intent numbersIntent = new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto","markmeattendance@gmail.com",null));
        numbersIntent.putExtra(Intent.EXTRA_SUBJECT, "New Registration");
        numbersIntent.putExtra(Intent.EXTRA_TEXT, "Registration No.:\nType: \n\n" +
                "Android ID: "+Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID));
        startActivity(numbersIntent);
    }
}
