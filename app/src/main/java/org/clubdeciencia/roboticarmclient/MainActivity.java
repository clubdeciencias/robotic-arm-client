package org.clubdeciencia.roboticarmclient;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {



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
                cBaseApplication app = (cBaseApplication) MainActivity.this.getApplication();
                app.bluetooth.execute();//Call the class to connect

                btButton.setEnabled(false);
                armButton.setEnabled(true);
                disconnectButton.setEnabled(true);
            }
        });



    }

    protected void Disconnect()
    {
        if (((cBaseApplication)this.getApplicationContext()).btSocket!=null) //If the btSocket is busy
        {
            try
            {
                ((cBaseApplication)this.getApplicationContext()).btSocket.close(); //close connection

            }
            catch (IOException e)
            { msg("Error");}
        }

    }

    protected void led()
    {
        if (((cBaseApplication)this.getApplicationContext()).btSocket!=null)
        {
            try
            {
                ((cBaseApplication)this.getApplicationContext()).btSocket.getOutputStream().write("O".toString().getBytes());
                msg("1");
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    protected void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }


}