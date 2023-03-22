package com.example.proyecto_final2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    //creamos las variables
    private EditText mail;
    private EditText password;
    private Button loginButton;
    private Button registerButton;
    private FirebaseAuth mAuth;

    //Inicializamos las variables
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        mail = findViewById(R.id.mailLogin);
        password = findViewById(R.id.passLogin);
        loginButton = findViewById(R.id.loginBtn);
        registerButton = findViewById(R.id.registerBtn);

        mAuth = FirebaseAuth.getInstance();
        loginButton.setOnClickListener(view -> {
                userLogin();
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainActivity3();
            }
        });
    }

    public void openMainActivity3(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    //Comprobamos que los campos no están vacios
     public void userLogin(){
        String email = mail.getText().toString();
        String contraseña = password.getText().toString();
        if (TextUtils.isEmpty(email)){
            mail.setError("Introduzca un correo");
            mail.requestFocus();
        }else if (TextUtils.isEmpty(contraseña)){
            password.setError("Introduzca una contraseña");
            password.requestFocus();
        }else{
            //comprueba que el email y la contraseña existen y nos loguea
            mAuth.signInWithEmailAndPassword(email, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Bienvenido/a", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent( LoginActivity.this, MenuActivity.class));
                    }else{
                        Toast.makeText(LoginActivity.this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
                        Log.w("TAG", "Error:", task.getException());
                    }
                }
            });                                                                      }

        }
    }
