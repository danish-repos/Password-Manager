package com.example.password_manager;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    ArrayList<NoteClass> notes;
    Context context;

    public NoteAdapter(Context c, ArrayList<NoteClass> list)
    {
        context = c;
        notes = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.single_note, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(notes.get(position).getName());
        holder.tvPassword.setText(notes.get(position).getPassword());
        holder.tvUrl.setText(notes.get(position).getUrl());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);
                deleteDialog.setTitle("Confirmation");
                deleteDialog.setMessage("Do you really want to delete it?");
                deleteDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SQLDatabase database = new SQLDatabase(context);
                        database.open();
                        database.deleteNote(notes.get(holder.getAdapterPosition()).getId());
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

        holder.imgUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog editDialog = new AlertDialog.Builder(context).create();
                View view = LayoutInflater.from(context).inflate(R.layout.update_note_layout, null, false);
                editDialog.setView(view);

                EditText etNewName = view.findViewById(R.id.etNewName);
                EditText etNewPassword = view.findViewById(R.id.etNewPassword);
                EditText etNewUrl = view.findViewById(R.id.etNewURL);
                Button btnUpdate = view.findViewById(R.id.btnUpdate);


                etNewName.setText(notes.get(holder.getAdapterPosition()).getName());
                etNewPassword.setText(notes.get(holder.getAdapterPosition()).getPassword());
                etNewUrl.setText(notes.get(holder.getAdapterPosition()).getUrl());

                editDialog.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = etNewName.getText().toString().trim();
                        String password = etNewPassword.getText().toString().trim();
                        String url = etNewUrl.getText().toString().trim();

                        SQLDatabase myDatabaseHelper = new SQLDatabase(context);
                        myDatabaseHelper.open();
                        myDatabaseHelper.updateNote(notes.get(holder.getAdapterPosition()).getId(), name, password, url);
                        myDatabaseHelper.close();

                        editDialog.dismiss();

                        notes.get(holder.getAdapterPosition()).setName(name);
                        notes.get(holder.getAdapterPosition()).setPassword(password);
                        notes.get(holder.getAdapterPosition()).setUrl(url);
                        notifyDataSetChanged();

                    }
                });


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
        ImageView imgUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPassword = itemView.findViewById(R.id.tvPassword);
            tvUrl = itemView.findViewById(R.id.tvUrl);
            imgUpdate = itemView.findViewById(R.id.imgUpdate);
        }
    }
}
