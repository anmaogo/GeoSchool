package com.example.geoschool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    //Views
    EditText email ,password;
    Button  boton;

    public static String TAG = "Geoschool" ;
    //firebbase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        //Inflarte
        email = findViewById(R.id.email);
        password = findViewById(R.id.passwd);
        boton = findViewById(R.id.registroS);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);

    }
    private void updateUI(FirebaseUser user)
    {

        if(user!= null)
        {
            startActivity(new Intent(this, Principal.class));
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
    public void logInPressesd(View V) {
        String emailS = email.getText().toString();

        String passwordS  = password.getText().toString();

        if (password.getText().toString().isEmpty() || email.getText().toString().isEmpty() )

        {
            Toast.makeText(this, "Ha dejado campos vacios",
                    Toast.LENGTH_LONG).show();
        }
        else {

            if (validaForm(emailS, passwordS)) {
                mAuth.signInWithEmailAndPassword(emailS, passwordS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            updateUI(mAuth.getCurrentUser());
                        } else {
                            Log.e(TAG, "aUTHENTIFICATION FAILED" + task.getException().toString());
                            Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        }
    }

    public void registroS(View V)
    {

        Intent intent= new Intent(getApplicationContext(), Registro.class);

        startActivity(intent);
    }

    public void irprincipal(View v) {

        Intent intent= new Intent(getApplicationContext(), Principal.class);
        startActivity(intent);

    }

}