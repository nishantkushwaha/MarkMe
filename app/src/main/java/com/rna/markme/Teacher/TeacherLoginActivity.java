package com.rna.markme.Teacher;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rna.markme.R;
import com.rna.markme.Student.StudentLoginActivity;
import com.rna.markme.Student.StudentMainActivity;

public class TeacherLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authstateListener;

    private EditText emailText;
    private EditText passText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);
        emailText= (EditText) findViewById(R.id.userEmail);
        passText= (EditText) findViewById(R.id.userPass);

        mAuth=FirebaseAuth.getInstance();

        authstateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser()!=null){
                    Intent intent = new Intent(TeacherLoginActivity.this,TeacherMainActivity.class);
                    startActivity(intent);

                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authstateListener);
    }

    public void loginButtonClicked(View view) {
        String email= emailText.getText().toString();
        String pass =passText.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Enter both Values",Toast.LENGTH_LONG).show();
        }

        else{
            if(email.length()!=25){
                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(!task.isSuccessful()){
                            Toast.makeText(TeacherLoginActivity.this,"Incorrect Username or password",Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
            else {
                Toast.makeText(this,"Incorrect Email",Toast.LENGTH_LONG).show();
            }

        }
    }
}
