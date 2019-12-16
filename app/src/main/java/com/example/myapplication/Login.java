package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class Login extends AppCompatActivity {

    List<AuthUI.IdpConfig> providers;
    private  static final int MY_REQUEST_CODE = 700;
    private static final String TAG = "FACELOG";
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    Button facebookbtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        mAuth = FirebaseAuth.getInstance();

        mCallbackManager = CallbackManager.Factory.create();

        // Initialize Facebook Login button

        facebookbtn = (Button) findViewById(R.id.facebook);

        facebookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(Login.this,Arrays.asList("email", "public_profile"));
                 LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                     @Override
                     public void onSuccess(LoginResult loginResult) {
                         Log.d(TAG, "facebook:onSuccess:" + loginResult);
                         handleFacebookAccessToken(loginResult.getAccessToken());
                     }

                     @Override
                     public void onCancel() {
                         Log.d(TAG, "facebook:onCancel");
                         // ...
                     }

                     @Override
                     public void onError(FacebookException error) {
                         Log.d(TAG, "facebook:onError", error);
                         // ...
                     }
                 });
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {
            Log.d("currentuser","currentuser");
            updateUI();
        }
    }

    private void updateUI(){
        Toast.makeText(Login.this, "Successful Login",Toast.LENGTH_SHORT).show();

        Intent accountIntent = new Intent(Login.this,MainActivity.class);
        startActivity(accountIntent);
        finish();

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        }

                        // ...
                    }
                });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }




//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        IdpResponse response = IdpResponse.fromResultIntent(data);
//        if(requestCode == MY_REQUEST_CODE)
//        {
//            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//            Toast.makeText(this, ""+user.getEmail(),Toast.LENGTH_SHORT).show();
//
//        }else{
//            Toast.makeText(this, ""+ response.getError().getMessage() ,Toast.LENGTH_SHORT).show();
//        }
//    }

}
