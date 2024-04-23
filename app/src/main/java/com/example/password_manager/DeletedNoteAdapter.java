package com.example.password_manager;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DeletedNoteAdapter extends RecyclerView.Adapter<DeletedNoteAdapter.ViewHolder>{


    ArrayList<NoteClass> notes;
    Context context;

    public DeletedNoteAdapter(Context c, ArrayList<NoteClass> list)
    {
        context = c;
        notes = list;
    }

    @NonNull
    @Override
    public DeletedNoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.single_deleted_note, parent, false);
        return new DeletedNoteAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DeletedNoteAdapter.ViewHolder holder, int position) {
        holder.tvName.setText(notes.get(position).getName());
        holder.tvPassword.setText(notes.get(position).getPassword());
        holder.tvUrl.setText(notes.get(position).getUrl());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);
                deleteDialog.setTitle("Confirmation");
                deleteDialog.setMessage("Do you want to restore it?");
                deleteDialog.setPositiveButton("Restore", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SQLDatabase database = new SQLDatabase(context);
                        database.open();
                        database.restoreNote(notes.get(holder.getAdapterPosition()).getId());
                        database.close();

                        notes.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();
                    }
                });
                deleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                deleteDialog.show();


                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvName, tvPassword, tvUrl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPassword = itemView.findViewById(R.id.tvPassword);
            tvUrl = itemView.findViewById(R.id.tvUrl);
        }
    }
}
