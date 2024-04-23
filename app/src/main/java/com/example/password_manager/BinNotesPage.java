package com.example.password_manager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BinNotesPage extends AppCompatActivity {

    RecyclerView rvBinNotes;
    TextView tvDeleteAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bin_notes_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        tvDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAll();
                finish();
            }
        });

    }

    private void init(){
        tvDeleteAll = findViewById(R.id.tvDeleteAll);

        rvBinNotes = findViewById(R.id.rvBinNotes);
        rvBinNotes.setHasFixedSize(true);

        rvBinNotes.setLayoutManager(new LinearLayoutManager(this));

        SQLDatabase database = new SQLDatabase(this);
        database.open();
        ArrayList<NoteClass> notes = database.readAllDeletedNotes();
        database.close();

        rvBinNotes.setAdapter(new DeletedNoteAdapter(this, notes));

    }


    private void deleteAll(){
        SQLDatabase database = new SQLDatabase(this);
        database.open();
        database.deleteNotes();
        database.close();
    }
}