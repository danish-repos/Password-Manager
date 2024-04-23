package com.example.password_manager;

import android.content.Intent;
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

public class LoginPage extends AppCompatActivity {

    FragmentManager fragmentManager;
    Fragment loginFragment, registerFragment;
    View loginView, registerView;
    Button btnRegister_r, btnLogin_l, btnRegister_l;
    RadioGroup radioGroup_r;
    EditText etEmail_l, etPassword_l, etFirstName_r , etLastName_r,  etEmail_r, etPassword_r, etConfirmPassword_r;
    TextView tvLogin_r;


    int gender;


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

        if(isUserLoggedIn()){
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

                int result = loginUser(email,password);
                if (result <= 0)
                {
                    Toast.makeText(LoginPage.this, "Invalid mail or password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                startActivity(new Intent(LoginPage.this,MainPage.class));
                finish();

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

                registerUser(firstName, lastName, email, password, userGender);
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

    private void registerUser(String firstName, String lastName,String email,  String password, String gender){

        SQLDatabase database = new SQLDatabase(this);

        database.open();
        database.insertLogin(firstName,lastName,email,password,gender);
        database.close();
    }
    private int loginUser(String email,String password){
        SQLDatabase database = new SQLDatabase(this);

        database.open();
        int result = database.loginUser(email,password);
        database.close();

        return result;
    }

    private boolean isUserLoggedIn(){
        SQLDatabase database = new SQLDatabase(this);

        database.open();
        boolean temp = database.isUserLoggedIn();
        database.close();

        return temp;

    }
}