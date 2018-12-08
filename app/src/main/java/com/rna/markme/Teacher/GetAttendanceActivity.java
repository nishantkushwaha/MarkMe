package com.rna.markme.Teacher;

import android.content.Context;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.knexis.hotspot.Hotspot;
import com.rna.markme.R;

import java.util.ArrayList;

public class GetAttendanceActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> arraylist = new ArrayList<>();
    ArrayAdapter adapter;
    ListView lv;
    WifiManager wifiManager;
    boolean connected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_attendance);
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
            db.collection("Teacher1").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            arraylist.add(document.getString("Roll"));
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
