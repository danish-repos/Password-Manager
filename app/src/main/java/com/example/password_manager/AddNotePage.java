package com.example.password_manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddNotePage extends AppCompatActivity {

    EditText etName, etPassowrd, etURL;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_note_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
                startActivity(new Intent(AddNotePage.this, MainPage.class));
                finish();

            }
        });

    }
    private void init(){
        etName = findViewById(R.id.etName);
        etPassowrd = findViewById(R.id.etPassword);
        etURL = findViewById(R.id.etURL);

        btnAdd = findViewById(R.id.btnAdd);
    }

    private void add(){
        String name = etName.getText().toString().trim();
        String password = etPassowrd.getText().toString().trim();
        String url = etURL.getText().toString().trim();

        if(name.isEmpty() || password.isEmpty() || url.isEmpty())
        {
            Toast.makeText(AddNotePage.this, "Can't leave anything empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLDatabase database = new SQLDatabase(this);
        database.open();

        database.insertNote(name, password, url);

        database.close();

    }


}