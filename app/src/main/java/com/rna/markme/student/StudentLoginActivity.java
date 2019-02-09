package com.rna.markme.student;

import android.content.Intent;
import android.provider.Settings;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rna.markme.MainActivity;
import com.rna.markme.R;
import com.rna.markme.teacher.TeacherLoginActivity;

public class StudentLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authstateListener;
    DatabaseReference ref;
    private EditText emailText;
    private EditText passText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        emailText= (EditText) findViewById(R.id.userEmail);
        passText= (EditText) findViewById(R.id.userPass);

        mAuth=FirebaseAuth.getInstance();

        authstateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser()!=null){
                        String uid = firebaseAuth.getCurrentUser().getUid();
                        ref = FirebaseDatabase.getInstance().getReference().child(uid);
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String TYPE = dataSnapshot.child("type").getValue().toString();
                                String ANDROIDID = dataSnapshot.child("android").getValue().toString();
                                if (TYPE.equals("student")) {
                                    if(ANDROIDID.equals(Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID))){
                                        startActivity(new Intent(StudentLoginActivity.this, StudentMainActivity.class));
                                    }
                                    else {

                                        Toast.makeText(StudentLoginActivity.this, "Device doesn't match", Toast.LENGTH_SHORT).show();
                                        mAuth.signOut();
                                    }
                                }
                                else{
                                    mAuth.signOut();
                                    Toast.makeText(StudentLoginActivity.this, "Something goes wrong!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authstateListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void loginButtonClicked(View view) {
        String email= emailText.getText().toString();
        String pass =passText.getText().toString();


        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Enter both Values",Toast.LENGTH_LONG).show();
        }

        else{
            if(email.length()==25){
                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(!task.isSuccessful()){
                            Toast.makeText(StudentLoginActivity.this,"Incorrect Username or password",Toast.LENGTH_LONG).show();

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
