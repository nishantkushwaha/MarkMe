package com.rna.markme.Teacher;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rna.markme.MainActivity;
import com.rna.markme.R;
import com.rna.markme.Student.StudentLoginActivity;
import com.rna.markme.Student.StudentMainActivity;

public class TeacherMainActivity extends AppCompatActivity {

    TextView txt;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        String uid = user.getUid();
        txt=(TextView)findViewById(R.id.textView);
        txt.setText(email.substring(0, email.length() - 10));
//        ref= FirebaseDatabase.getInstance().getReference().child(uid);
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String Type=dataSnapshot.getValue().toString();
//                txt.setText(Type);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void signOut(View view){
        mAuth= FirebaseAuth.getInstance();
        mAuth.signOut();
        startActivity(new Intent(TeacherMainActivity.this,MainActivity.class));
        finishAffinity();

    }

}
