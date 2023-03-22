package com.example.proyecto_final2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {
    //Creamos las variables
    private String userID;
    private EditText user;
    private EditText mail2;
    private EditText pass2;
    private Button registerButton;


    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_acc_layout);
        //inicializamos
        user = findViewById(R.id.userRgstr);
        mail2 = findViewById(R.id.mailRgstr);
        pass2 = findViewById(R.id.passRgstr);
        registerButton = findViewById(R.id.RgstrBtn);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        registerButton.setOnClickListener(view -> {
            createuser();
        });


    }
    public void openMainActivity2() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    //Método que permite la creación de un usuario
    public void createuser(){
        String email = mail2.getText().toString();
        String contraseña = pass2.getText().toString();
        String usuario = user.getText().toString();

        //comprueba si los campos están vácios
        if (TextUtils.isEmpty(email)) {
            mail2.setError("Introduzca un correo");
            mail2.requestFocus();
        }else if (TextUtils.isEmpty(contraseña)){
            pass2.setError("Introduzca una contraseña");
            pass2.requestFocus();
        }else if (TextUtils.isEmpty(usuario)) {
            user.setError("Introduzca un usuario");
            user.requestFocus();
        }else{
            //Registra en Firebase nuestro usuario con contraseña
            mAuth.createUserWithEmailAndPassword(email, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        //si es correcto crea un mapa con los diferentes apartados de la colección "users"
                        userID = mAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = db.collection("users").document(userID);
                        Map<String,Object> user=new HashMap<>();
                        user.put("usuario", usuario);
                        user.put("Correo", email);
                        user.put("Contraseña", contraseña);

                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("TAG", "onSuccess: Datos registrados"+userID);
                            }

                        });
                        Toast.makeText(RegisterActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }else{
                        Toast.makeText(RegisterActivity.this, "Usuario no registrado"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}