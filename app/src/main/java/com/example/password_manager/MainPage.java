package com.example.password_manager;

import android.content.Intent;
import android.content.SharedPreferences;
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

    LinearLayout layoutLogin, layoutProfile;

    SharedPreferences sharedPreferences;

    private static final String SHARED_PF_NAME = "user_pf";
    private static final String KEY_FIRSTNAME = "firstName";
    private static final String KEY_LASTNAME = "lastName";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

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


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        layoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainPage.this, LoginNotesPage.class));

            }
        });

        layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View dialogView = LayoutInflater.from(MainPage.this).inflate(R.layout.dialog_logout, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                Button btnLogout = dialogView.findViewById(R.id.btnLogout);

                btnLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(KEY_IS_LOGGED_IN,false);
                        editor.apply();

                        startActivity(new Intent(MainPage.this,LoginPage.class));
                        finish();

                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }


    private void init(){
        tvNumberLogin = findViewById(R.id.tvNumberLogin);
        tvNumberBin = findViewById(R.id.tvNumberBin);

        btnAdd = findViewById(R.id.btnAdd);

        layoutLogin = findViewById(R.id.layoutLogin);
        layoutProfile= findViewById(R.id.layoutProfile);

        sharedPreferences = getSharedPreferences(SHARED_PF_NAME,MODE_PRIVATE);
    }
}