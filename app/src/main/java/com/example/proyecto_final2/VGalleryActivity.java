package com.example.proyecto_final2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.proyecto_final2.Gallery.Images;
import com.example.proyecto_final2.Gallery.RecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class VGalleryActivity extends AppCompatActivity {

    //creación de variables

    private static final String TAG = "MainActivity9";

    private DatabaseReference reference;
    private StorageReference mStorageRef;
    private Context mContext = VGalleryActivity.this;
    private RecyclerView recyclerView;
    private ArrayList<Images> imagesList;
    private RecyclerAdapter recyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rec_view_gallery_layout);

        //inicializamos
        Log.d(TAG, "OnCreate: started");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewGallery);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        reference = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        imagesList = new ArrayList<>();
        init();
    }

    //muestra las imágenes guardadas en firebase en un RecyclerView
    private void init() {
        clearAll();

        Query query = reference.child("Images");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Images images = new Images ();

                    images.setUrl(snapshot.child("url").getValue().toString());
                    images.setDescription(snapshot.child("description").getValue().toString());

                    imagesList.add(images);

                }
                recyclerAdapter = new RecyclerAdapter(mContext, imagesList);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void clearAll(){

        if(imagesList != null) {

            imagesList.clear();

            if(recyclerAdapter != null) {
                recyclerAdapter.notifyDataSetChanged();
            }
        }

        imagesList = new ArrayList<>();

    }
}