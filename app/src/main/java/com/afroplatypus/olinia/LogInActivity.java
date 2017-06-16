package com.afroplatypus.olinia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogInActivity extends AppCompatActivity {

    TextView anon, register;
    EditText txtUserName, txtPass, txtConfirm;
    Intent intentLoad;
    Button btnLogIn;
    ProgressDialog prog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mAuth = FirebaseAuth.getInstance();
        anon = (TextView) findViewById(R.id.txtAnon);
        register = (TextView) findViewById(R.id.txtRegister);
        txtUserName = (EditText) findViewById(R.id.user_name);
        txtPass = (EditText) findViewById(R.id.pass);
        txtConfirm = (EditText) findViewById(R.id.confirm_pass);
        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        intentLoad = new Intent(this, LoadActivity.class);
        final Intent regIntent = new Intent(this, RegisterActivity.class);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    startActivity(intentLoad);
                    LogInActivity.this.finish();
                } else {
                    // User is signed out
                }
            }
        };
        anon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInAnonymously()
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()){
                                    Toast.makeText(LogInActivity.this, "Algo salió mal :( Vuelve a intentarlo más tarde.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(regIntent);
            }
        });
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail, pass;
                mail = txtUserName.getText().toString().trim();
                pass = txtPass.getText().toString();
                if (mail.length() > 0 && pass.length() > 0) {
                    if (isValidEmail(mail)) {
                        prog = ProgressDialog.show(LogInActivity.this, "Por favor espere", "Iniciando sesión...", true);
                        mAuth.signInWithEmailAndPassword(mail, pass)
                            .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    prog.hide();
                                    if (!task.isSuccessful()){
                                        Toast.makeText(LogInActivity.this, "Contraseña incorrecta", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    } else {
                        Toast.makeText(LogInActivity.this, "Correo no válido", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(LogInActivity.this, "Falta llenar algún campo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private boolean isValidEmail(String email){
        Pattern pat = Pattern.compile("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,4})+$");
        Matcher mat = pat.matcher(email);
        return mat.matches();
    }
}