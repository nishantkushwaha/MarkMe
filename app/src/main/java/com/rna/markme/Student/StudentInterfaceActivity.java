package com.rna.markme.Student;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
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
import com.rna.markme.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentInterfaceActivity extends AppCompatActivity implements View.OnClickListener {
    WifiManager wifi;
    ListView lv;
    TextView textStatus;
    Button buttonScan;
    List<ScanResult> results;
    WifiInfo info;
    String ITEM_KEY = "key";
    ArrayList<String> arraylist = new ArrayList<>();
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_interface);

        buttonScan = (Button) findViewById(R.id.scan);
        buttonScan.setOnClickListener(this);
        lv = (ListView)findViewById(R.id.wifilist);
        textStatus=(TextView)findViewById(R.id.textStatus);
        lv.setVisibility(View.VISIBLE);


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

    public String check(String bssid) {
        for(String string:arraylist){
            if(string.equals(bssid)){
                return "Found in Class \uD83D\uDC4D\uD83C\uDFFB";
            }
        }
        return "Not Found in Class";
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
