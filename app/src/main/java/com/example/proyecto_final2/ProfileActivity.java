package com.example.proyecto_final2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private static final int GALLERY_INTENT_CODE = 1023;
    //declaramos las variables
    ImageView imageView;
    TextView nameEt, emailEt, dobEt, mobileEt;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    FirebaseUser user;
    StorageReference storageReference;


    //incializamos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        imageView = findViewById(R.id.iv_f1);
        nameEt = findViewById(R.id.tv_name_f1);
        emailEt = findViewById(R.id.tv_mail_f1);
        dobEt = findViewById(R.id.tv_dob_f1);
        mobileEt = findViewById(R.id.tv_mobile_f1);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //obtiene la uri de la imágen subida a la bbdd
        StorageReference profileRef = storageReference.child("profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }

        });
        //obtiene los datos introducidos anteriormente de la base de datos
        userId = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("user").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                nameEt.setText(documentSnapshot.getString("Nombre"));
                emailEt.setText(documentSnapshot.getString("Email"));
                dobEt.setText(documentSnapshot.getString("Cumpleaños"));
                mobileEt.setText(documentSnapshot.getString("Teléfono"));
                Picasso.get().load(documentSnapshot.getString("url")).into(imageView);

            }
        });


    }
}