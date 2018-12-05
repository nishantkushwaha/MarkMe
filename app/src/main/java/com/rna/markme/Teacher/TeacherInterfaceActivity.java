package com.rna.markme.Teacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.rna.markme.R;

import net.cryptobrewery.macaddress.MacAddress;

public class TeacherInterfaceActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_interface);
        TextView bssidtxt=(TextView)findViewById(R.id.bssidtxt);
        String bssid=MacAddress.getMacAddr();
        int c=0,i=0;
        for(i=0;i<bssid.length();i++){
            if(bssid.charAt(i)!=':'){
                c++;
            }
            else {
                if (c == 2)
                    c = 0;
                else if (c == 1) {
                     i--;
                     bssid=bssid.substring(0,i)+"0"+bssid.substring(i,bssid.length());
                }
            }
        }
        if(i==16&&c==1){
            i--;
            bssid=bssid.substring(0,i)+"0"+bssid.substring(i,bssid.length());
        }
        bssidtxt.setText(bssid);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
