package com.rna.markme.teacher;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rna.markme.R;
import com.rna.markme.student.StudentMainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeacherInterfaceActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> arraylist = new ArrayList<>();
    ArrayAdapter adapter;
    ListView lv;
    WifiManager wifiManager;
    String sub,email,id;
    boolean connected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_interface);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        id=email.substring(0, email.length() - 10);
        Intent intent=getIntent();
        sub=intent.getStringExtra("subTag");
        adapter =  new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arraylist);
        lv = (ListView)findViewById(R.id.data);
        lv.setAdapter(adapter);
        getData();
    }
    public void refresh(View view){
        arraylist.clear();
        adapter.clear();
        getData();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, TeacherMainActivity.class));
        finishAffinity();
    }

    public void getData(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        if(connected==true){
            db.collection(id).document(sub).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        Map<String, Object> user = new HashMap<>();
                        user=document.getData();
                        for (String s : user.keySet()) {
                            arraylist.add(s);
                            adapter.notifyDataSetChanged();
                        }
                    } else {

                    }

                }
            });
        }
        else {
            Toast.makeText(this,"Please Turn on your Internet",Toast.LENGTH_SHORT).show();
        }
    }
}
