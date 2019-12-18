package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity {

    EditText txtEmail,txtPassword;
    Button signUpBtn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        txtEmail = (EditText) findViewById(R.id.emailsu);
        txtPassword = (EditText) findViewById(R.id.passwordsu);

        signUpBtn = (Button) findViewById(R.id.signup2);

        firebaseAuth = FirebaseAuth.getInstance();


        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Signup.this,"Please Enter Email",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Signup.this,"Please Enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.length()>=6){

                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information

                                        startActivity(new Intent(getApplicationContext(),Login.class));
                                        Toast.makeText(Signup.this,"Registration Done",Toast.LENGTH_SHORT).show();
                                        updateUI();

                                    } else {

                                        Log.d("email n password", email+password);

                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(Signup.this,"Authentication Fail",Toast.LENGTH_SHORT).show();
                                   }
                                    //....
                                }
                            });

                }else{

                    Toast.makeText(Signup.this,"Password please be at least 6 characters",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
    public void updateUI(){
        Intent intent = new Intent(Signup.this,Login.class);
        startActivity(intent);
        finish();
    }
}
