package org.clubdeciencia.roboticarmclient;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.bluetooth.*;
import android.widget.TextView;

import com.aronbordin.*;

public class MainActivity extends AppCompatActivity {

    public static int REQUEST_BLUETOOTH = 1;
    boolean BLUETOOTH_STATUS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Button btButton = findViewById(R.id.bluetoothButton);
        final Button servoButton = findViewById(R.id.servoButton);

        boolean connected = false;

        btButton.setEnabled(false);




        // servoButton.setEnabled(false);




        /*
        Comprobar si el dispositivo es compatible con bluetooth
         */
        final TextView infoTextView = findViewById(R.id.infoTextView);
        BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
        // Phone does not support Bluetooth so let the user know and exit.
        if (BTAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
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


        servoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        });

        btButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setup();
            }
        });

        if(BTAdapter.isEnabled()) {
            infoTextView.setText("Bluetooth Ready ;)");
            btButton.setEnabled(true);
        }else{
            infoTextView.setText("Bluetooth Not enabled :/");
        }
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
