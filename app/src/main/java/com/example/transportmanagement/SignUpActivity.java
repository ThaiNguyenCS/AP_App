package com.example.transportmanagement;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.transportmanagement.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    ActivitySignUpBinding mBinding;
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    private String employeeCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        firestore = FirebaseFirestore.getInstance();
        getEmployeeCodeFromDB();
        mAuth= FirebaseAuth.getInstance();
        setUpGeneralView();
    }

    private void setUpGeneralView()
    {
        mBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setMessage("Do you really want to cancel the progress?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SignUpActivity.this.finish();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });
        mBinding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUpInfo();
            }
        });
        mBinding.username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e(TAG, "afterTextChanged: " + s.toString() );
                if (s.toString().isEmpty()) {
                    mBinding.textInput1.setError("Please enter email!");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    mBinding.textInput1.setError("Please enter a valid email!");
                    return;
                }
                mBinding.textInput1.setError(null);
            }
        });

        mBinding.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < 6) {
                    mBinding.textInput2.setError("Password must have at least 6 characters!");
                    return;
                }
                mBinding.textInput2.setError(null);
            }
        });
        mBinding.rePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.isEmpty()) {
                    mBinding.textInput3.setError("Please confirm password!");
                    return;
                }
                mBinding.textInput3.setError(null);
                if(!mBinding.password.getText().toString().equals(str))
                {
                    mBinding.textInput3.setError("Password does not match!");
                    return;
                }
                mBinding.textInput3.setError(null);
            }
        });
        mBinding.code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.isEmpty()) {
                    mBinding.textInput4.setError("Please enter employee code!");
                    return;
                }
                mBinding.textInput4.setError(null);
            }
        });
    }
    private void checkUpInfo()
    {
        String username = mBinding.username.getText().toString();
        if (username.isEmpty()) {
            mBinding.textInput1.setError("Please enter email!");
            return;
        }
        mBinding.textInput1.setError(null);
        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            mBinding.textInput1.setError("Please enter a valid email!");
            return;
        }
        mBinding.textInput1.setError(null);
        String password = mBinding.password.getText().toString();
        if (password.length() < 6) {
            mBinding.textInput2.setError("Password must have at least 6 characters!");
            return;
        }
        mBinding.textInput2.setError(null);
        String repassword = mBinding.rePassword.getText().toString();
        if (repassword.isEmpty()) {
            mBinding.textInput3.setError("Please confirm password!");
            return;
        }
        mBinding.textInput3.setError(null);
        if(!password.equals(repassword))
        {
            mBinding.textInput3.setError("Password does not match!");
            return;
        }
        mBinding.textInput3.setError(null);
        String code = mBinding.code.getText().toString();
        if (code.isEmpty()) {
            mBinding.textInput4.setError("Please enter employee code!");
            return;
        }
        mBinding.textInput4.setError(null);
        if(employeeCode.isEmpty())
        {
            Toast.makeText(SignUpActivity.this, "Please wait!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!code.equals(employeeCode))
        {
            Toast.makeText(SignUpActivity.this, "Invalid employee code", Toast.LENGTH_SHORT).show();
            return;
        }
        signUpWithEmail(username, password);
    }

    private void getEmployeeCodeFromDB()
    {
        firestore.collection("GeneralInfo")
                .document("employee_code")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            employeeCode = (String) task.getResult().get("code");
                        }
                        else
                        {
                            task.getException().printStackTrace();
                        }
                    }
                });
    }

    private void signUpWithEmail(String username, String password)
    {
        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SignUpActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                            SignUpActivity.this.finish();
                        }
                        else
                        {
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}