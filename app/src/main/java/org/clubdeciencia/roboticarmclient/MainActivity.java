package org.clubdeciencia.roboticarmclient;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ConnectBT().execute(); //Call the class to connect

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

        private void Disconnect()
        {
            if (btSocket!=null) //If the btSocket is busy
            {
                try
                {
                    btSocket.close(); //close connection
                }
                catch (IOException e)
                { msg("Error");}
            }
            finish(); //return to the first layout

        }


    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(ledControl.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

}
