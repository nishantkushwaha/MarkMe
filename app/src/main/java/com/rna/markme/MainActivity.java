package com.rna.markme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rna.markme.Student.StudentLoginActivity;
import com.rna.markme.Student.StudentMainActivity;
import com.rna.markme.Teacher.TeacherLoginActivity;
import com.rna.markme.Teacher.TeacherMainActivity;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener authstateListener;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout rt =(RelativeLayout)findViewById(R.id.Rtlayout);
        TextView txp=(TextView)findViewById(R.id.txp);
        rt.setVisibility(View.GONE);
        txp.setText("Please wait a moment");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            ref= FirebaseDatabase.getInstance().getReference().child(uid);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String Type=dataSnapshot.getValue().toString();
                    if(Type.equals("student"))
                    {
                        startActivity(new Intent(MainActivity.this,StudentLoginActivity.class));}
                    else {
                        startActivity(new Intent(MainActivity.this,TeacherLoginActivity.class));
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
        this.finish();
    }

    public void StudentAct(View view){

        startActivity(new Intent(MainActivity.this,StudentLoginActivity.class));
        this.finish();


    }
    public void TeacherAct(View view){

        startActivity(new Intent(MainActivity.this,TeacherLoginActivity.class));
        this.finish();


    }
}
