package com.afroplatypus.oliniaExpertos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    Button btnSubmit, btnPicker;
    ImageView ivProfilePic;
    Intent intentLoad;
    ProgressDialog prog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mFirebaseDatabaseReference;

    //Image picker variables
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath, fileManagerString;

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
        btnPicker = (Button) findViewById(R.id.picker);
        ivProfilePic = (ImageView) findViewById(R.id.profilePicChoser);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mAuth.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder()
                            .setDisplayName(txtName.getText().toString())
                            .build());
                    mFirebaseDatabaseReference.child("users/" + user.getUid() + "/name").setValue(txtName.getText().toString().trim());
                    mFirebaseDatabaseReference.child("users/" + user.getUid() + "/phone").setValue(txtTel.getText().toString().trim());
                    mFirebaseDatabaseReference.child("users/" + user.getUid() + "/connected").setValue(true);
                    startActivity(intentLoad);
                    RegisterActivity.this.finish();
                } /*else {
                    mFirebaseDatabaseReference.child("users/" + user.getUid() + "/connected").setValue(false);
                }*/
            }
        };
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidEmail(txtMail.getText().toString().trim())) {
                    if (passwordsMatch()) {
                        if (fieldsNotEmpty()) {
                            prog = ProgressDialog.show(RegisterActivity.this, "Espere por favor", "Iniciando sesión...", true);
                            mAuth.createUserWithEmailAndPassword(txtMail.getText().toString().trim(), txtPass.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            prog.hide();
                                            if (!task.isSuccessful())
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
        btnPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
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

    private boolean isValidEmail(String email) {
        Pattern pat = Pattern.compile("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,4})+$");
        Matcher mat = pat.matcher(email);
        return mat.matches();
    }

    private boolean fieldsNotEmpty() {
        return !(txtMail.getText().toString().equals("")
                || txtPass.getText().toString().equals("")
                || txtConfPass.getText().toString().equals("")
                || txtTel.getText().toString().equals("")
                || txtName.getText().toString().equals(""));
    }

    private boolean passwordsMatch() {
        return txtPass.getText().toString().equals(txtConfPass.getText().toString());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                //OI FILE Manager
                fileManagerString = selectedImageUri.getPath();
                //MEDIA GALLERY
                selectedImagePath = getPath(selectedImageUri);

                //NOW WE HAVE OUR WANTED STRING
                if(selectedImagePath!=null) {
                    Toast.makeText(RegisterActivity.this, selectedImagePath, Toast.LENGTH_SHORT).show();
                    System.out.println("selectedImagePath is the right one for you!");
                }
                else {
                    Toast.makeText(RegisterActivity.this, fileManagerString, Toast.LENGTH_SHORT).show();
                    System.out.println("filemanagerstring is the right one for you!");
                }
                ivProfilePic.setImageURI(selectedImageUri);
            }
        }
    }

    //UPDATED!
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null)
        {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }
}
