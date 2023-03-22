package com.example.proyecto_final2.Gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_final2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static final String TAG = "RecyclerAdapter";
    private Context mContext;
    private ArrayList<Images> imagesList;

    public RecyclerAdapter(Context context, ArrayList<Images> imagesList) {

        this.mContext = context;
        this.imagesList = imagesList;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_gallery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvDescription.setText(imagesList.get(position).getDescription());
        Picasso.get().load(imagesList.get(position).getUrl()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView tvDescription;


        public ViewHolder(View itemView) {

            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageViewGallery);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);


        }
    }
}
