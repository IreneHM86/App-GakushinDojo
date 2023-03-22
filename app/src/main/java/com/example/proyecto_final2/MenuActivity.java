package com.example.proyecto_final2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {

    //declaramos las variables
    private Button b9;
    private Button perfil;
    private Button profile;
    private Button boardBtn;
    private Button calendarBtn;
    private Button galleryButton;
    private ImageButton callButton;
    private ImageButton mailButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        //inicializamos
        galleryButton = findViewById(R.id.galleryBtn);
        profile = findViewById(R.id.profileBtn);
        perfil = findViewById(R.id.addprofileBtn);
        b9 = findViewById(R.id.discBtn);
        boardBtn = findViewById(R.id.boardBtn);
        calendarBtn = findViewById(R.id.calendarBtn);
        callButton = findViewById(R.id.callBtn);
        mailButton = findViewById(R.id.mailBtn);
        mAuth = FirebaseAuth.getInstance();

        //botones que nos llevan a los diferentes módulos
        b9.setOnClickListener(view -> {
            mAuth.signOut();
            Toast.makeText(MenuActivity.this, "Cerrando sesión", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
        });
        perfil.setOnClickListener(view -> {
            startActivity(new Intent(this, CreateProfileActivity.class));
        });
        profile.setOnClickListener(view -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });
        boardBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, NotesActivity.class));
        });
        calendarBtn.setOnClickListener(view -> {
        startActivity(new Intent(this, CalendarViewActivity.class));
        });
        galleryButton.setOnClickListener(view -> {
            startActivity(new Intent(this, VGalleryActivity.class));
        });

        //envía un mail al mail proporcionado
        mailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:maildeprueba@gmail.com"));
                startActivity(intent);
            }
        });
        //realiza una llamada al número proporcionado
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: 647282194"));
                startActivity(intent);
        }



    });


    }

}