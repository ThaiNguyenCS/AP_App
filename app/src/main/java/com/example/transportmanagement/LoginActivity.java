package com.example.transportmanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.SharedMemory;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.transportmanagement.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    ActivityLoginBinding mBinding;
    FirebaseAuth mAuth;
//    private GoogleSignInClient mGoogleSignInClient;
//    static int SIGN_IN = 1111;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        float width = mBinding.welcome.getPaint().measureText("Welcome back");
        Shader shader = new LinearGradient(0, 0, width, mBinding.welcome.getTextSize(),
                Color.parseColor("#A535FF"),
                Color.parseColor("#3DD5FD"),
                Shader.TileMode.CLAMP);
        mBinding.welcome.getPaint().setShader(shader);

        mAuth = FirebaseAuth.getInstance();
        mBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO temporary
                String username = mBinding.username.getText().toString();
                if(username.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Please enter email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(username).matches())
                {
                    Toast.makeText(LoginActivity.this, "Please enter a valid email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String password = mBinding.password.getText().toString();
                if(password.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Please enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mBinding.progressIndicator.setVisibility(View.VISIBLE);
                signInWithEmail(username, password);
            }
        });
        mBinding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        mBinding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("Do you really want to reset the password?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String username = mBinding.username.getText().toString();
                        if(!Patterns.EMAIL_ADDRESS.matcher(username).matches())
                        {
                            Toast.makeText(LoginActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mAuth.sendPasswordResetEmail(username)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(LoginActivity.this, "We've sent you an email!", Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();

            }
        });
//        mBinding.loginWithGoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//                startActivityForResult(signInIntent, SIGN_IN);
//            }
//        });

//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signInWithEmail(String username, String pass)
    {
          mAuth.signInWithEmailAndPassword(username, pass)
                  .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                          if(task.isSuccessful())
                          {
                              Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                              Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                              intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                              startActivity(intent);
                          }
                          else
                          {
                              mBinding.progressIndicator.setVisibility(View.GONE);
                              Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                          }
                      }
                  });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == RESULT_OK)
//        {
//            if(requestCode == SIGN_IN)
//            {
//                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//                try {
//                    // Google Sign In was successful, authenticate with Firebase
//                    GoogleSignInAccount account = task.getResult(ApiException.class);
//                    Log.d(TAG, "firebaseAuthWithGoogle: " + account.getEmail());
//                    logInWithGoogle(account.getIdToken()); // log in to firebase using credential
//                } catch (ApiException e) {
//                    Log.w(TAG, "Google sign in failed", e);
//                }
//            }
//        }
//    }

//    private void logInWithGoogle(String idToken)
//    {
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            //TODO add this user to FireBaseFireStore
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
//                            startActivity(intent);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
//                            task.getException().printStackTrace();
//                        }
//                    }
//                });
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//    }
}