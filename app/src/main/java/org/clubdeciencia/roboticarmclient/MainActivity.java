package org.clubdeciencia.roboticarmclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aronbordin.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button + findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setup();
            }
        });
    }



    BluetoothArduino mBlue = BluetoothArduino.getInstance("ExampleRobot");

    void setup(){
        mBlue.Connect();
        textSize(30);
    }
}
