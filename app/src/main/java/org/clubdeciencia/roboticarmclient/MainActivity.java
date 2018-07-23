package org.clubdeciencia.roboticarmclient;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.bluetooth.*;
import android.widget.TextView;

import com.aronbordin.*;

public class MainActivity extends AppCompatActivity {

    boolean connected = false;
    public static int REQUEST_BLUETOOTH = 1;
    boolean BLUETOOTH_STATUS;
    BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
    Handler btConInfo = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Button btButton = findViewById(R.id.bluetoothButton);
        final Button armButton = findViewById(R.id.armButton);



        btButton.setEnabled(false);




        armButton.setEnabled(true);




        /*
        Comprobar si el dispositivo es compatible con bluetooth
         */
        final TextView infoTextView = findViewById(R.id.infoTextView);
        // Phone does not support Bluetooth so let the user know and exit.
        if (BTAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.bt_notsupported_title)
                    .setMessage(R.string.bt_notsupported_msg)
                    .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        if (!BTAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, REQUEST_BLUETOOTH);
        }



        armButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( MainActivity.this, armControl.class);
                startActivity(i);
                }
        });


        btButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setup();
                if(connected){
                    armButton.setEnabled(true);
                }
            }
        });

        final boolean post = btConInfo.post(new Runnable() {
            @Override
            public void run() {
                if (BTAdapter.isEnabled()) {
                    infoTextView.setText(R.string.bluetooth_ok_msg);
                    btButton.setEnabled(true);
                } else {
                    infoTextView.setText(R.string.bluetooth_disabled_msg);
                }
                btConInfo.postDelayed(this, 500); // set time here to refresh textView
            }
        });


    }

    BluetoothArduino mBlue = BluetoothArduino.getInstance("brazo");

    void setup(){
        mBlue.Connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_BLUETOOTH) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) BLUETOOTH_STATUS = true;
        }
    }


}
