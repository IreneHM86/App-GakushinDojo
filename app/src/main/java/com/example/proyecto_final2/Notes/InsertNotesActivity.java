package com.example.proyecto_final2.Notes;


import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyecto_final2.R;
import com.example.proyecto_final2.databinding.ActivityInsertNotesBinding;

import java.util.Date;

public class InsertNotesActivity extends AppCompatActivity {

    //declaramos las variables

  ActivityInsertNotesBinding binding;
    String title, subtitle, notes;
    NotesViewModel notesViewModel;
    String priority ="1";

    @Override
    protected void onCreate(Bundle savedIntanceState) {
       super.onCreate(savedIntanceState);
       binding=ActivityInsertNotesBinding.inflate(getLayoutInflater());
       setContentView(binding.getRoot());

        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
        //crea las distintas prioridades en base a donde pulsemos.
        binding.greenPriority.setOnClickListener(view -> {

            binding.greenPriority.setImageResource(R.drawable.ic_baseline_done_24);
            binding.yellowPriority.setImageResource(0);
            binding.redPriority.setImageResource(0);
            priority = "1";
        });
        binding.yellowPriority.setOnClickListener(view -> {
            priority = "2";
            binding.greenPriority.setImageResource(0);
            binding.yellowPriority.setImageResource(R.drawable.ic_baseline_done_24);
            binding.redPriority.setImageResource(0);
        });
        binding.redPriority.setOnClickListener(view -> {
            priority = "3";
            binding.greenPriority.setImageResource(0);
            binding.yellowPriority.setImageResource(0);
            binding.redPriority.setImageResource(R.drawable.ic_baseline_done_24);
        });

        //crea una nota al pulsar el botón de aceptar
        binding.doneNotesBtn.setOnClickListener(view ->{

            title = binding.notesTitle.getText().toString();
            subtitle = binding.notesSubtitle.getText().toString();
            notes = binding.notesData.getText().toString();

            CreateNotes(title, subtitle, notes);

        } );
    }
    //método de creación de notas
    private void CreateNotes(String title, String subtitle, String notes) {

        Date date = new Date();
        CharSequence sequence = DateFormat.format("d MMMM, yyyy", date.getTime());
        Notes notes1 = new Notes();
        notes1.notesTitle=title;
        notes1.notesSubtitle=subtitle;
        notes1.notes=notes;
        notes1.notesDate =sequence.toString();
        notes1.notesPriority = priority;

        notesViewModel.insertNote(notes1);

        Toast.makeText(this,"Nota creada con éxito", Toast.LENGTH_SHORT).show();
        finish();
    }

}







