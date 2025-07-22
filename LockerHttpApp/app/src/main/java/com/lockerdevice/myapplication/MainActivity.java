package com.lockerdevice.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private LockerHttpServer httpServer;
    private Rs485Controller rs485Controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rs485Controller = new Rs485Controller(this);

        try {
            httpServer = new LockerHttpServer(8080, rs485Controller);
        } catch (IOException e) {
            Log.e("MainActivity", "Failed to start HTTP server", e);
        }
    }

    @Override
    protected void onDestroy() {
        if (httpServer != null) {
            httpServer.stop();
        }
        if (rs485Controller != null) {
            rs485Controller.close();
        }
        super.onDestroy();
    }
}
