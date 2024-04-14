package com.example.transportmanagement;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.transportmanagement.databinding.ActivityAddDriverBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.Driver;
import data.Route;

public class AddDriverActivity extends AppCompatActivity {
    private static final String TAG = "AddDriverActivity";
    ActivityAddDriverBinding mBinding;
    private static List<String> errorMsgs;
    Map<String, Object> mDriver;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAddDriverBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        createErrorMessages();
        setUpGeneralView();
    }

    private void createErrorMessages()
    {
        errorMsgs = new ArrayList<>();
        errorMsgs.add("Full name");
        errorMsgs.add("Phone number");
        errorMsgs.add("Citizen ID");
        errorMsgs.add("Address");
        errorMsgs.add("License");
        errorMsgs.add("Year of experience");
    }
    private void setUpGeneralView()
    {
        mBinding.errorMessage.setVisibility(View.GONE);
        mBinding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddDriverActivity.this);
                builder.setMessage("Do you really want to cancel the progress?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddDriverActivity.this.finish();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });
        mBinding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = checkUpDriverInfo();
                if(result != 0)
                {
                    mBinding.errorMessage.setVisibility(View.VISIBLE);
                    mBinding.errorMessage.setText("Missing fields: " + errorMsgs.get(result-1));
                    return;
                }
                addRouteToFirebase();
            }
        });
    }

    private void addRouteToFirebase()
    {
        firestore = FirebaseFirestore.getInstance();
        mBinding.progressIndicator.setVisibility(View.VISIBLE);
        DocumentReference generalRef = firestore.collection("GeneralInfo").document("driver_max_id");
        firestore.runTransaction(new Transaction.Function<Long>() {
            @Nullable
            @Override
            public Long apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                Log.e(TAG, "apply:"  );
                long maxID = (long) transaction.get(generalRef).get("maxID");
                return maxID;
            }
        }).addOnCompleteListener(new OnCompleteListener<Long>() {
            @Override
            public void onComplete(@NonNull Task<Long> task) {
                if(task.isSuccessful()) {
                    long maxId = task.getResult() + 1;
                    mDriver.put(Driver.DRIVER_ID, maxId);
                    firestore.collection("Driver")
                            .add(mDriver)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if(task.isSuccessful()) {
                                        Log.e(TAG, "onComplete: Successfully");
                                        mBinding.progressIndicator.setVisibility(View.GONE);
                                        generalRef.update("maxID", maxId);
                                        Toast.makeText(AddDriverActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                                        AddDriverActivity.this.finish();
                                    }
                                    else {
                                        Toast.makeText(AddDriverActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                                        task.getException().printStackTrace();
                                    }
                                }
                            });
                }
            }
        });
    }

    public int checkUpDriverInfo()
    {
        String name = mBinding.driverName.getText().toString().trim();
        if(name.isEmpty())
        {
            return 1;
        }
        String phone = mBinding.driverPhone.getText().toString().trim();
        if(phone.isEmpty())
        {
            return 2;
        }
        String citizenID = mBinding.driverCitizenID.getText().toString().trim();
        if(citizenID.isEmpty())
        {
            return 3;
        }
        String address = mBinding.driverAddress.getText().toString().trim();
        if(address.isEmpty())
        {
            return 4;
        }
        String license = mBinding.driverLicense.getText().toString().trim();
        if(license.isEmpty())
        {
            return 5;
        }
        String yearOfEx = mBinding.driverYearOfExperience.getText().toString().trim();
        if(yearOfEx.isEmpty())
        {
            return 6;
        }
        mDriver = new HashMap<>();
        mDriver.put(Driver.DRIVER_ROUTE_ID, null);
        mDriver.put(Driver.DRIVER_VEHICLE_ID, null);
        mDriver.put(Driver.DRIVER_STATUS, 0);
        mDriver.put(Driver.DRIVER_YOE, Long.parseLong(yearOfEx));
        mDriver.put(Driver.DRIVER_PHONE, phone);
        mDriver.put(Driver.DRIVER_ADDRESS, address);
        mDriver.put(Driver.DRIVER_NAME, name);
        mDriver.put(Driver.DRIVER_DRIVING_ROUTES, null);
        mDriver.put(Driver.DRIVER_LICENSE, license);
        mDriver.put(Driver.DRIVER_CITIZENID, citizenID);
        return 0;
    }
}