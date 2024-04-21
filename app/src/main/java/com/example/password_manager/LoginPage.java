package com.example.password_manager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Objects;

public class LoginPage extends AppCompatActivity {

    FragmentManager fragmentManager;
    Fragment loginFragment, registerFragment;
    View loginView, registerView;
    Button btnRegister_r, btnLogin_l, btnRegister_l;
    RadioGroup radioGroup_r;
    EditText etEmail_l, etPassword_l, etFirstName_r , etLastName_r,  etEmail_r, etPassword_r, etConfirmPassword_r;
    TextView tvLogin_r;


    SharedPreferences sharedPreferences;

    int gender;



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
        setContentView(R.layout.activity_login_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        SharedPreferences.Editor editor = sharedPreferences.edit();


        if(sharedPreferences.getBoolean(KEY_IS_LOGGED_IN,false))
        {
            startActivity(new Intent(LoginPage.this, MainPage.class));
            finish();
        }


        fragmentManager.beginTransaction()
                .hide(registerFragment)
                .commit();


        radioGroup_r.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.btnMale_r)
                    gender = 1;
                else if (checkedId == R.id.btnFemale_r)
                    gender = 2;
                else
                    gender = -1;

            }
        });

        btnLogin_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = etEmail_l.getText().toString().trim();
                String password = etPassword_l.getText().toString().trim();

                if ( email.isEmpty() || password.isEmpty() ) {

                    Toast.makeText(LoginPage.this, "Please fill out the form first" , Toast.LENGTH_SHORT).show();
                    return;

                }


                if(sharedPreferences.getString(KEY_EMAIL,null).equals(email) && sharedPreferences.getString(KEY_PASSWORD,null).equals(password)) {

                    editor.putBoolean(KEY_IS_LOGGED_IN, true);
                    editor.apply();

                    startActivity(new Intent(LoginPage.this, MainPage.class));
                    finish();

                }
                else {

                    Toast.makeText(LoginPage.this, "Invalid mail or password" , Toast.LENGTH_SHORT).show();
                    return;

                }

            }
        });

        btnRegister_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // simply show them the register panel
                fragmentManager.beginTransaction()
                        .hide(loginFragment)
                        .show(registerFragment)
                        .commit();

            }
        });

        btnRegister_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String firstName = etFirstName_r.getText().toString().trim();
                String lastName = etLastName_r.getText().toString().trim();
                String email = etEmail_r.getText().toString().trim();
                String password = etPassword_r.getText().toString();
                String rePassword = etConfirmPassword_r.getText().toString();

                if (  firstName.isEmpty() || lastName.isEmpty() ||email.isEmpty() || password.isEmpty() || rePassword.isEmpty() ){
                    Toast.makeText(LoginPage.this, "Please fill out the form first", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(rePassword)){
                    Toast.makeText(LoginPage.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                    return;
                }

                String userGender = "";
                if(gender != -1) {
                    if(gender == 1){
                        userGender += "male";
                    }
                    else if(gender == 2){
                        userGender += "female";
                    }
                }
                else{
                    Toast.makeText(LoginPage.this, "Select your gender", Toast.LENGTH_SHORT).show();
                    return;
                }



                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(KEY_FIRSTNAME,firstName);
                editor.putString(KEY_LASTNAME,lastName);
                editor.putString(KEY_EMAIL,email);
                editor.putString(KEY_PASSWORD,password);
                editor.putString(KEY_GENDER,userGender);
                editor.putBoolean(KEY_IS_LOGGED_IN,false);
                editor.apply();


                Toast.makeText(LoginPage.this, "Registered!", Toast.LENGTH_SHORT).show();
                clear();


                fragmentManager.beginTransaction()
                        .hide(registerFragment)
                        .show(loginFragment)
                        .commit();

            }
        });

        tvLogin_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .hide(registerFragment)
                        .show(loginFragment)
                        .commit();

            }
        });


    }

    private void init(){

        fragmentManager = getSupportFragmentManager();



        loginFragment = fragmentManager.findFragmentById(R.id.loginFragment);
        registerFragment = fragmentManager.findFragmentById(R.id.registerFragment);

        loginView = loginFragment.requireView();
        registerView = registerFragment.requireView();

        btnLogin_l = loginView.findViewById(R.id.btnLogin_l);
        btnRegister_l = loginView.findViewById(R.id.btnRegister_l);

        etEmail_l = loginView.findViewById(R.id.etEmail_l);
        etPassword_l = loginView.findViewById(R.id.etPassword_l);



        btnRegister_r = registerView.findViewById(R.id.btnRegister_r);
        radioGroup_r = registerView.findViewById(R.id.radioGroup_r);


        etFirstName_r = registerView.findViewById(R.id.etFirstName_r);
        etLastName_r = registerView.findViewById(R.id.etLastName_r);
        etEmail_r = registerView.findViewById(R.id.etEmail_r);
        etPassword_r = registerView.findViewById(R.id.etPassword_r);
        etConfirmPassword_r = registerView.findViewById(R.id.etConfirmPassword_r);

        tvLogin_r = registerView.findViewById(R.id.tvLogin_r);

        gender =-1;


       sharedPreferences = getSharedPreferences(SHARED_PF_NAME,MODE_PRIVATE);

    }

    private void clear(){
        etEmail_l.setText("");
        etPassword_l.setText("");

        etFirstName_r.setText("");
        etLastName_r.setText("");
        etEmail_r.setText("");
        etPassword_r.setText("");
        etConfirmPassword_r.setText("");
    }
}