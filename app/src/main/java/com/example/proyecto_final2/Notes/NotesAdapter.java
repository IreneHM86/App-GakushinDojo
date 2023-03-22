package com.example.proyecto_final2.Notes;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_final2.NotesActivity;
import com.example.proyecto_final2.R;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.notesViewholder>{

    //declara las variables

    NotesActivity notesActivity;
    List<Notes> notes;

    //accede a los métodos y atributos de las clases
    public NotesAdapter(NotesActivity notesActivity, List<Notes> notes) {
        this.notesActivity = notesActivity;
        this.notes = notes;
    }

    // crea un nuevo ViewHolder e inicializa algunos campos privados usados por el RecyclerView
    @Override
    public notesViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new notesViewholder(LayoutInflater.from(notesActivity).inflate(R.layout.item_notes, parent, false));
    }

    //presenta las diferentes prioridades en una posición determinada
    @Override
    public void onBindViewHolder(NotesAdapter.notesViewholder holder, int position) {

        Notes note = notes.get(position);

        switch (note.notesPriority) {
            case "1":
                holder.notesPriority.setBackgroundResource(R.drawable.green_circle);
                break;
            case "2":
                holder.notesPriority.setBackgroundResource(R.drawable.yellow_circle);

                break;
            case "3":
                holder.notesPriority.setBackgroundResource(R.drawable.red_circle);
                break;
        }
        //nos permite "sostener" un valor mientras permite modificaciones (de las notas ya creadas)
        holder.title.setText(notes.get(position).notesTitle);
        holder.subtitle.setText(note.notesSubtitle);
        holder.notesDate.setText(note.notesDate);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(notesActivity, UpdateNotesActivity.class);
            intent.putExtra("id",note.id);
            intent.putExtra("title",note.notesTitle);
            intent.putExtra("subtitle",note.notesSubtitle);
            intent.putExtra("priority",note.notesPriority);
            intent.putExtra("note",note.notes);
            notesActivity.startActivity(intent);

        });

    }

    //nos devuelve las notas creadas
    @Override
    public int getItemCount() {
        return notes.size();
    }

   static class notesViewholder extends RecyclerView.ViewHolder {

        View notesPriority;
        TextView title, subtitle, notesDate;

        public notesViewholder(View itemView){
            super(itemView);

            title = itemView.findViewById(R.id.notesTitle);
            subtitle = itemView.findViewById(R.id.notesSubtitle);
            notesDate = itemView.findViewById(R.id.notesDate);
            notesPriority = itemView.findViewById(R.id.notesPriority);
        }
    }

}