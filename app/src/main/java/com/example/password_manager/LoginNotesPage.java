package com.example.password_manager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LoginNotesPage extends AppCompatActivity {

    RecyclerView rvLoginNotes;
    NoteAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_notes_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
    }

    private void init(){

        rvLoginNotes = findViewById(R.id.rvLoginNotes);
        rvLoginNotes.setHasFixedSize(true);

        rvLoginNotes.setLayoutManager(new LinearLayoutManager(this));

        LoginDB database = new LoginDB(this);
        database.open();
        ArrayList<LoginNoteClass> logins = database.readAllContacts();
        database.close();

        rvLoginNotes.setAdapter(new NoteAdapter(this, logins));
    }
}