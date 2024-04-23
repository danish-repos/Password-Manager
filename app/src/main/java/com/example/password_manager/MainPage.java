package com.example.password_manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainPage extends AppCompatActivity {


    TextView tvNumberLogin, tvNumberBin;
    FloatingActionButton btnAdd;

    LinearLayout layoutNotes, layoutProfile, layoutBin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        updateNumberOfNotes();



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPage.this, AddNotePage.class));

            }
        });

        layoutNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPage.this, NotesPage.class));

            }
        });

        layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // it will show a dialog where the user name will be displayed so that we know which user is currently
                // logged in, also has a option to logout by a button.

                View dialogView = LayoutInflater.from(MainPage.this).inflate(R.layout.dialog_logout, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                Button btnLogout = dialogView.findViewById(R.id.btnLogout);
                TextView tvName = dialogView.findViewById(R.id.tvName);

                tvName.setText(getLoggedInUserName());

                btnLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        logoutUser();

                        startActivity(new Intent(MainPage.this,LoginPage.class));
                        finish();

                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        layoutBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPage.this, BinNotesPage.class));
            }
        });
    }


    private void init(){
        tvNumberLogin = findViewById(R.id.tvNumberLogin);
        tvNumberBin = findViewById(R.id.tvNumberBin);

        btnAdd = findViewById(R.id.btnAdd);

        layoutNotes = findViewById(R.id.layoutNotes);
        layoutProfile= findViewById(R.id.layoutProfile);
        layoutBin = findViewById(R.id.layoutBin);

    }

    private void logoutUser(){
        SQLDatabase database = new SQLDatabase(this);

        database.open();
        database.logoutUser();
        database.close();
    }

    private String getLoggedInUserName() {
        SQLDatabase database = new SQLDatabase(this);

        database.open();
        String temp = database.getLoggedInUserName();
        database.close();

        return temp;

    }

    private void updateNumberOfNotes(){
        SQLDatabase database = new SQLDatabase(this);

        database.open();
        tvNumberLogin.setText(database.numberOfPresentNotes());
        database.close();
    }
    private void updateNumberOfDeletedNotes(){
        SQLDatabase database = new SQLDatabase(this);

        database.open();
        tvNumberBin.setText(database.numberOfDeletedNotes());
        database.close();
    }
}