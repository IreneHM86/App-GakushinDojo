package com.example.proyecto_final2.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Handler;

import com.example.proyecto_final2.MainActivity;
import com.example.proyecto_final2.R;

public class SplashScreen extends AppCompatActivity {

    //Muestra un splash antes de iniciar la aplicación
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            }
        }, 2000);
    }

}
