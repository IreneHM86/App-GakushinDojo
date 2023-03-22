package com.example.proyecto_final2.Notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto_final2.R;
import com.example.proyecto_final2.databinding.ActivityInsertNotesBinding;
import com.example.proyecto_final2.databinding.ActivityUpdateNotesBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Date;

public class UpdateNotesActivity extends AppCompatActivity {

    //declaración de variables
    ActivityUpdateNotesBinding binding;
    String priority = "1";
    NotesViewModel notesViewModel;
    String stitle, ssubtitle, snotes, spriority;
    int iid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //accedemos a las variables
        binding = ActivityUpdateNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        iid = getIntent().getIntExtra("id", 0);
        stitle = getIntent().getStringExtra("title");
        ssubtitle = getIntent().getStringExtra("subtitle");
        spriority = getIntent().getStringExtra("priority");
        snotes = getIntent().getStringExtra("note");

        binding.upTitle.setText(stitle);
        binding.upSubtitle.setText(ssubtitle);
        binding.upNotes.setText(snotes);

        if (spriority.equals("1")) {
            binding.greenPriority.setImageResource(R.drawable.ic_baseline_done_24);
        } else if (spriority.equals("2")) {
            binding.yellowPriority.setImageResource(R.drawable.ic_baseline_done_24);
        } else if (spriority.equals("3")) {
            binding.redPriority.setImageResource(R.drawable.ic_baseline_done_24);
        }

        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);


        binding.greenPriority.setOnClickListener(view -> {

            binding.greenPriority.setImageResource(R.drawable.ic_baseline_done_24);
            binding.yellowPriority.setImageResource(0);
            binding.redPriority.setImageResource(0);
            priority = "1";
        });
        binding.yellowPriority.setOnClickListener(view -> {

            binding.greenPriority.setImageResource(0);
            binding.yellowPriority.setImageResource(R.drawable.ic_baseline_done_24);
            binding.redPriority.setImageResource(0);
            priority = "2";
        });
        binding.redPriority.setOnClickListener(view -> {

            binding.greenPriority.setImageResource(0);
            binding.yellowPriority.setImageResource(0);
            binding.redPriority.setImageResource(R.drawable.ic_baseline_done_24);
            priority = "3";
        });

        binding.updateNotesBtn.setOnClickListener(view -> {

            String title = binding.upTitle.getText().toString();
            String subtitle = binding.upSubtitle.getText().toString();
            String notes = binding.upNotes.getText().toString();

            UpdateNotes(title, subtitle, notes);

        });

    }
    //modificamos las notas
    private void UpdateNotes(String title, String subtitle, String notes) {

        //actualiza la fecha actual
        Date date = new Date();
        CharSequence sequence = DateFormat.format("d MMMM, yyyy", date.getTime());

        Notes updateNotes = new Notes();

        //actualiza los diferentes campos
        updateNotes.id = iid;
        updateNotes.notesTitle = title;
        updateNotes.notesSubtitle = subtitle;
        updateNotes.notes = notes;
        updateNotes.notesDate = sequence.toString();
        updateNotes.notesPriority = priority;

        notesViewModel.updateNote(updateNotes);

        Toast.makeText(this, "Nota modificada con éxito", Toast.LENGTH_SHORT).show();

        finish();
    }

}