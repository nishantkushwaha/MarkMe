package com.rna.markme.Student;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import net.cryptobrewery.macaddress.MacAddress;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rna.markme.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentInterfaceActivity extends AppCompatActivity implements View.OnClickListener {
    WifiManager wifi;
    ListView lv;
    TextView textStatus;
    Button buttonScan;
    List<ScanResult> results;
    String email;
    WifiInfo info;
    ArrayList<String> arraylist = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayAdapter adapter;
    int c=0;
    boolean b=false;
    boolean connected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_interface);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        email=email.substring(0, email.length() - 10);
        buttonScan = (Button) findViewById(R.id.scan);
        buttonScan.setOnClickListener(this);
        lv = (ListView)findViewById(R.id.wifilist);
        textStatus=(TextView)findViewById(R.id.textStatus);
        lv.setVisibility(View.GONE);


        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false)
        {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }
        info = wifi.getConnectionInfo ();
        adapter =  new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arraylist);
        lv.setAdapter(adapter);
        scanWifiNetworks();


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,StudentMainActivity.class));
        finishAffinity();
    }

    public String check(String bssid) {
        for(String string:arraylist){
            if(string.equals(bssid)){
                b=true;
                return "Found in Class \uD83D\uDC4D\uD83C\uDFFB";
            }
        }
        return "Not Found in Class";
    }
    public void mark(View view){
        if(b==true){
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                //we are connected to a network
                connected = true;
            }
            else
                connected = false;
            if(connected==true){
                Toast.makeText(this,"Wait your Attendance is being marked",Toast.LENGTH_SHORT).show();
            Map<String, Object> user = new HashMap<>();
            c++;
            user.put("Roll", email);
            db.collection("Teacher1").document(email).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(StudentInterfaceActivity.this,"Attendance Marked",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(StudentInterfaceActivity.this,"Faliure: Try Again",Toast.LENGTH_SHORT).show();
                }
            });}
            else {
                Toast.makeText(this,"Please Turn on your Internet",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this,"You are not inside class",Toast.LENGTH_SHORT).show();
        }


    }

    public void onClick(View view)
    {
        textStatus.setText("wait...");
        scanWifiNetworks();
    }
    private void scanWifiNetworks(){

        arraylist.clear();
        registerReceiver(wifi_receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        wifi.startScan();

        Log.d("WifScanner", "scanWifiNetworks");

        Toast.makeText(this, "Scanning....", Toast.LENGTH_SHORT).show();

    }
    BroadcastReceiver wifi_receiver= new BroadcastReceiver()
    {

        @Override
        public void onReceive(Context c, Intent intent)
        {
            Log.d("WifScanner", "onReceive");
            results = wifi.getScanResults();
            unregisterReceiver(this);

            for(ScanResult scanResult: results){
                arraylist.add(scanResult.BSSID);
                adapter.notifyDataSetChanged();
            }

            textStatus.setText(check(info.getBSSID()));


        }
    };


}
