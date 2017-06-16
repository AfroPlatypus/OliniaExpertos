package com.afroplatypus.olinia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    TextView txtMail, txtName, txtPass, txtConfPass, txtTel;
    Button btnSubmit;
    Intent intentLoad;
    ProgressDialog prog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mFirebaseDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        intentLoad = new Intent(this, LoadActivity.class);
        txtMail = (TextView) findViewById(R.id.mail);
        txtName = (TextView) findViewById(R.id.name);
        txtPass = (TextView) findViewById(R.id.pass);
        txtConfPass = (TextView) findViewById(R.id.confirm_pass);
        txtTel = (TextView) findViewById(R.id.tel);
        btnSubmit = (Button) findViewById(R.id.submit);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mAuth.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder()
                            .setDisplayName(txtName.getText().toString())
                            .build());
                    mFirebaseDatabaseReference.child("users/"+user.getUid()+"/name").setValue(txtName.getText().toString().trim());
                    mFirebaseDatabaseReference.child("users/"+user.getUid()+"/phone").setValue(txtTel.getText().toString().trim());
                    mFirebaseDatabaseReference.child("users/"+user.getUid()+"/connected").setValue(true);
                    startActivity(intentLoad);
                    RegisterActivity.this.finish();
                } else {
                    mFirebaseDatabaseReference.child("users/"+user.getUid()+"/connected").setValue(false);
                }
            }
        };
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidEmail(txtMail.getText().toString().trim())) {
                    if(passwordsMatch()) {
                        if(fieldsNotEmpty()) {
                            prog = ProgressDialog.show(RegisterActivity.this, "Espere por favor", "Iniciando sesión...", true);
                            mAuth.createUserWithEmailAndPassword(txtMail.getText().toString().trim(), txtPass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    prog.hide();
                                    if(!task.isSuccessful())
                                        Toast.makeText(RegisterActivity.this, "Algo salió mal, intentalo más tarde", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Falta de llenar algún campo.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Correo no válido.", Toast.LENGTH_SHORT).show();
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

    private boolean fieldsNotEmpty(){
        return !(txtMail.getText().toString().equals("")
                || txtPass.getText().toString().equals("")
                || txtConfPass.getText().toString().equals("")
                || txtTel.getText().toString().equals("")
                || txtName.getText().toString().equals(""));
    }

    private boolean passwordsMatch(){
        return txtPass.getText().toString().equals(txtConfPass.getText().toString());
    }
}
