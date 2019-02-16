package com.rna.markme.student;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rna.markme.User;
import com.rna.markme.R;

public class StudentAccount extends AppCompatActivity {

    TextView studentID;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_account);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        studentID=(TextView)findViewById(R.id.textView);
        studentID.setText(email.substring(0, email.length() - 10));
        //studentID.setText(Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID));
    }

    @Override
    public void onBackPressed() {
        finishAffinity();

    }

    public void signOut(View view){
        mAuth= FirebaseAuth.getInstance();
        mAuth.signOut();
        startActivity(new Intent(StudentAccount.this,User.class));
        finishAffinity();

    }
    public void markYourAttendance(View view){
//        Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//        startActivity(myIntent);
        startActivity(new Intent(StudentAccount.this,MarkAttendance.class));
    }
}
