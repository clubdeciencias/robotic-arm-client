package org.clubdeciencia.roboticarmclient;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import static org.clubdeciencia.roboticarmclient.DeviceList.EXTRA_ADDRESS;

public class MainActivity extends AppCompatActivity {

    String address = "20:14:12:22:01:47";
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

        final Button btButton = findViewById(R.id.bluetoothButton);
        final Button armButton = findViewById(R.id.armButton);
        final Button disconnectButton = findViewById(R.id.disconnectButton);

        btButton.setEnabled(true);
        armButton.setEnabled(false);
        disconnectButton.setEnabled(false);


        disconnectButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Disconnect();
                btButton.setEnabled(true);
                armButton.setEnabled(false);
                disconnectButton.setEnabled(false);
            }
        });


        armButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent( MainActivity.this, armControl.class);
                startActivity(i);
                */
                led();
                }
        });


        btButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new ConnectBT().execute(); //Call the class to connect

                btButton.setEnabled(false);
                armButton.setEnabled(true);
                disconnectButton.setEnabled(true);
            }
        });



    }

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

    }

    protected void led()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("O".toString().getBytes());
                msg("1");
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(MainActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
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
