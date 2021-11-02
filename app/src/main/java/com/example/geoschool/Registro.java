package com.example.geoschool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.provider.MediaStore;

import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Registro extends AppCompatActivity {

    //Declaración botones para Captura de imagen y cámara
    ImageView mImageView;
    ImageButton boton;
    ImageButton botonCamara;

    public static final int IMAGE_PICKER_REQUEST= 1;  // definir numero para identificar la llamada a este permiso
    //  public static final int REQUEST_IMAGE_CAPTURE= 8;
    public static final int REQUEST_PERMISSION_CAMERA =9;//Define el numero para identificar la llamada al permiso de la cámara

    //*********

    private FirebaseAuth mAuth;
    EditText email ,password,nombre;
    public static String TAG = "Geoschool" ;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemClicked = item.getItemId();
        if(itemClicked == R.id.menuLogOut)
        {
            mAuth.signOut();
            Intent intent = new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_registro);
        //Inflarte
        email = findViewById(R.id.txtEmail);
        password = findViewById(R.id.txtContraseña);
        nombre = findViewById(R.id.txtNombre);

        //inflate de los botones cámara y galería
        mImageView = findViewById(R.id.publicaImagen);
        botonCamara = findViewById(R.id.botonCamara);//llamado del botón de la captura por cámara
        botonCamara.setOnClickListener(new View.OnClickListener() {     // accion del botón de la captura por cámara
            //*****
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(Registro.this, Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED) {
                    abrirCamara();
                }else{
                    ActivityCompat.requestPermissions(Registro.this,new String[]{Manifest.permission.CAMERA},REQUEST_PERMISSION_CAMERA );
                }
            }
        });

        boton = findViewById(R.id.galeria);        //llamado del botón Seleccionar Imagen
        boton.setOnClickListener(new View.OnClickListener() {         //accion del botón seleccionar imagen
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(Registro.this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    imagenGaleria();
                }else{
                    ActivityCompat.requestPermissions(Registro.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},IMAGE_PICKER_REQUEST );
                }
            }
        });

    }
    //Métodos para el manejo de la cámara y galería de imagenes
    //método que inicia la cámara fotográfica
    private void abrirCamara() {

        Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            startActivityForResult(camaraIntent, REQUEST_PERMISSION_CAMERA);
        } catch (ActivityNotFoundException e) {
            Log.e("PERMISSION_APP", e.getMessage());
        }
    }

    private void imagenGaleria() {

        Intent pickImage = new Intent(Intent.ACTION_PICK);
        pickImage.setType("image/");
        startActivityForResult(pickImage, IMAGE_PICKER_REQUEST);

    }

    //métodos de respuesta y permisos de los botones de cámara y galería

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMAGE_PICKER_REQUEST && resultCode== Activity.RESULT_OK)
        {
            try {
                final Uri imageUri= data.getData();
                final InputStream imageStream= getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage= BitmapFactory.decodeStream(imageStream);
                mImageView.setImageBitmap(selectedImage);
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


        if(requestCode==REQUEST_PERMISSION_CAMERA && resultCode== Activity.RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap=(Bitmap)extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSION_CAMERA:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    abrirCamara();
                }else{
                    Toast.makeText(this, "Se requiere permisos para acceder a la Cámara", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case IMAGE_PICKER_REQUEST:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imagenGaleria();
                }else{
                    Toast.makeText(this, "Se requiere permisos para acceder a la Galería de Fotos", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }


    }

    //métodos para la conexión a Firebase

    public void sigInPressesd(View V) {
        System.out.println("GRaba registro");
        String emailS = email.getText().toString();
        String passwordS = password.getText().toString();
        String nomnbreS = nombre.getText().toString();

        if (password.getText().toString().isEmpty() || email.getText().toString().isEmpty()) {
            Toast.makeText(this, "Ha dejado campos vacios",
                    Toast.LENGTH_LONG).show();
        } else {
            if (validaForm(emailS, passwordS)) {
                mAuth.createUserWithEmailAndPassword(emailS, passwordS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            updateUI(mAuth.getCurrentUser());
                        } else {
                            Log.e(TAG, "CREATION USER FAILED" + task.getException().toString());
                            Toast.makeText(Registro.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        }
    }

    private void updateUI(FirebaseUser user)
    {
        if(user!= null)
        {
            startActivity(new Intent(this,Principal.class));
        }else
        {
            this.email.setText("");
            this.password.setText("");
        }
    }
    private boolean validaForm(String emailS , String passwordS)
    {
        return true;
    }


}