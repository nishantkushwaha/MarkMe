package com.rna.markme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.rna.markme.Student.StudentLoginActivity;
import com.rna.markme.Student.StudentMainActivity;
import com.rna.markme.Teacher.TeacherLoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void StudentAct(View view){
        startActivity(new Intent(MainActivity.this,StudentLoginActivity.class));
    }
    public void TeacherAct(View view){
        startActivity(new Intent(MainActivity.this,TeacherLoginActivity.class));
    }
}
