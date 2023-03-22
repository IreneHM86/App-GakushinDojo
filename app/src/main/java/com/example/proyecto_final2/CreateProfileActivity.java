package com.example.proyecto_final2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.proyecto_final2.Profile.All_UserMmber;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class CreateProfileActivity extends AppCompatActivity {

    //declaramos las variables

    EditText etname, etEmail, etDob, etMobile;
    Button button;
    ImageView imageView;
    ProgressBar progressBar;
    Uri imageUri;
    UploadTask uploadTask;
    StorageReference storageReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    private static final int PICK_IMAGE = 1;
    All_UserMmber member;
    String currentUserId;

    //inicializamos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_prof_layout);

        member = new All_UserMmber();
        imageView = findViewById(R.id.iv_cp);
        etname = findViewById(R.id.et_name_cp);
        etEmail = findViewById(R.id.et_email_cp);
        etDob = findViewById(R.id.et_dot_cp);
        etMobile = findViewById(R.id.et_mobile_cp);
        button = findViewById(R.id.btn_cp);
        progressBar = findViewById(R.id.progressbar_cp);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = user.getUid();

        documentReference = db.collection("user").document(currentUserId);
        storageReference = FirebaseStorage.getInstance().getReference("Profile images");
        databaseReference = database.getReference("All users");

        button.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadData();

            }
        }));

        //abre la galería para establecer una imágen en el imageView
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

    }

    //implementa las imágenes cargadas
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE || resultCode == RESULT_OK ||
                    data != null || data.getData() != null) {
                imageUri = data.getData();

                Picasso.get().load(imageUri).into(imageView);
            }
            //lanzamos una exception en caso de error para evitar que la app crashee
        } catch (Exception e) {

            Toast.makeText(this, "Error" + e, Toast.LENGTH_SHORT).show();
        }
    }

    //obtenemos la file extension
    private String getFileExt(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));

    }

    //Subimos los campos introducidos
    private void uploadData() {
        String name = etname.getText().toString();
        String email = etEmail.getText().toString();
        String birthday = etDob.getText().toString();
        String mobile = etMobile.getText().toString();

        //comprueba que los textos están vacíos o no
        if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(birthday) || !TextUtils.isEmpty(mobile) || imageUri != null) {
            //creamos la progress bar
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getFileExt(imageUri));
            uploadTask = reference.putFile(imageUri);
            //obtenemos la uri de la imágen subida
            Task<Uri> urltask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException()
                                ;
                    }
                    return reference.getDownloadUrl();
                }

            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        //Creamos un hash map para ayudarnos a guardar los campos
                        Map<String, String> profile = new HashMap<>();
                        profile.put("Nombre", name);
                        profile.put("Email", email);
                        profile.put("url", downloadUri.toString());
                        profile.put("Cumpleaños", birthday);
                        profile.put("Teléfono", mobile);
                        profile.put("uid", currentUserId);
                        profile.put("privacy", "public");

                        //usamos la clase member  para guardar los datos en la base de datos
                        member.setName(name);
                        member.setMobile(mobile);
                        member.setUid(currentUserId);
                        member.setUrl(downloadUri.toString());

                        databaseReference.child(currentUserId).setValue(member);

                        documentReference.set(profile)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(CreateProfileActivity.this, "Perfil creado", Toast.LENGTH_SHORT).show();

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(CreateProfileActivity.this, ProfileActivity.class);
                                                startActivity(intent);

                                            }
                                        }, 2000);

                                    }
                                });
                    }
                }
            });

        } else {
            Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
}




