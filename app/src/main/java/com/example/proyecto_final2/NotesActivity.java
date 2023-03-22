package com.example.proyecto_final2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.proyecto_final2.Notes.InsertNotesActivity;
import com.example.proyecto_final2.Notes.NotesAdapter;
import com.example.proyecto_final2.Notes.NotesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotesActivity extends AppCompatActivity {

    //creamos las variables
    FloatingActionButton newNotesBtn;
    NotesViewModel notesViewModel;
    RecyclerView notesRecycler;
    NotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_rec_layout);

        //inicializamos
        newNotesBtn=findViewById(R.id.newNotesBtn);
        notesRecycler=findViewById(R.id.notesRecycler);

        //nos lleva a la clase NotesViewModel que nos muestra las notas creadas
        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
        //nos lleva a la clase InsertNotesActivity que nos permite crear nuevas notas
        newNotesBtn.setOnClickListener(view -> {
            startActivity(new Intent(NotesActivity.this, InsertNotesActivity.class));
        });
        //muestra todas las notas
        notesViewModel.getAllNotes.observe(this, notes -> {
            notesRecycler.setLayoutManager(new GridLayoutManager(this,2));
            adapter = new NotesAdapter(NotesActivity.this,notes);
            notesRecycler.setAdapter(adapter);

        });
    }
}