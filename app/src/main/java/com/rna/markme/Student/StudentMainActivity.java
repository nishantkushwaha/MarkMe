package com.rna.markme.Student;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rna.markme.MainActivity;
import com.rna.markme.R;

public class StudentMainActivity extends AppCompatActivity {

    TextView txt;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        txt=(TextView)findViewById(R.id.textView);
        txt.setText(email.substring(0, email.length() - 10));
    }

    public void signOut(View view){
        mAuth= FirebaseAuth.getInstance();
        mAuth.signOut();

        startActivity(new Intent(StudentMainActivity.this,MainActivity.class));
        this.finish();
    }
}
