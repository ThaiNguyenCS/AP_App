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

import com.example.transportmanagement.databinding.ActivityAddVehicleBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import data.Vehicle;

public class AddVehicleActivity extends AppCompatActivity {
    private static final String TAG = "AddVehicleActivity";
    ActivityAddVehicleBinding mBinding;
    FirebaseFirestore firestore;
    private static List<String> errorList;
    private Map<String, Object> mVehicle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAddVehicleBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setUpGeneralView();
        firestore = FirebaseFirestore.getInstance();
        setUpErrorList();
    }

    private void setUpErrorList()
    {
        errorList = new ArrayList<>();
        errorList.add("License Plate");
        errorList.add("Maximum Load");
        errorList.add("Height");
        errorList.add("Width");
        errorList.add("Length");
        errorList.add("Fuel");
    }

    private void setUpGeneralView()
    {
        mBinding.progressIndicator.setVisibility(View.GONE);
        mBinding.errorMessage.setVisibility(View.GONE);
        mBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddVehicleActivity.this);
                builder.setMessage("Do you really want to cancel the progress?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddVehicleActivity.this.finish();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });
        mBinding.doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = checkUpInformation();
                if(result != 0)
                {
                    mBinding.errorMessage.setVisibility(View.VISIBLE);
                    mBinding.errorMessage.setText("Missing field: "+ errorList.get(result-1));
                    return;
                }
                addVehicleToDatabase();
            }
        });
    }
    private int checkUpInformation()
    {
        String license = mBinding.licenseEdittext.getText().toString().trim();
        if(license.isEmpty())
        {
            return 1;
        }
        String type = mBinding.typeSpinner.getSelectedItem().toString();
        Log.e(TAG, "type is " + type);
        if(type.isEmpty())
        {
            return 2;
        }
        String brand = mBinding.brandSpinner.getSelectedItem().toString();
        Log.e(TAG, "brand is " + brand);
        if(brand.isEmpty())
        {
            return 3;
        }
        String loadStr = mBinding.loadEdittext.getText().toString().trim();
        if(loadStr.isEmpty())
        {
            return 4;
        }
        String heightStr = mBinding.heightEdittext.getText().toString().trim();
        if(heightStr.isEmpty())
        {
            return 5;
        }
        String widthStr = mBinding.widthEdittext.getText().toString().trim();
        if(widthStr.isEmpty())
        {
            return 6;
        }
        String lengthStr = mBinding.lengthEdittext.getText().toString().trim();
        if(lengthStr.isEmpty())
        {
            return 7;
        }
        String fuel = mBinding.fuelEdittext.getText().toString().trim();
        if(fuel.isEmpty())
        {
            return 8;
        }
        mVehicle = new HashMap<>();
        mVehicle.put(Vehicle.VEHICLE_FUEL, fuel);
        mVehicle.put(Vehicle.VEHICLE_STATUS, 0);
        mVehicle.put(Vehicle.VEHICLE_TYPE, type);
        mVehicle.put(Vehicle.VEHICLE_LENGTH, Double.parseDouble(lengthStr));
        mVehicle.put(Vehicle.VEHICLE_WIDTH, Double.parseDouble(widthStr));
        mVehicle.put(Vehicle.VEHICLE_HEIGHT, Double.parseDouble(heightStr));
        mVehicle.put(Vehicle.VEHICLE_LOAD, Double.parseDouble(loadStr));
        mVehicle.put(Vehicle.VEHICLE_PLATE, license);
        mVehicle.put(Vehicle.VEHICLE_BRAND, brand);
        mVehicle.put(Vehicle.VEHICLE_DRIVER_ID, null);
        mVehicle.put(Vehicle.VEHICLE_ROUTE_ID, null);
        return 0;
    }
    private void addVehicleToDatabase()
    {

        if(mVehicle != null)
        {
            mBinding.progressIndicator.setVisibility(View.VISIBLE);
            DocumentReference generalRef = firestore.collection("GeneralInfo").document("vehicle_max_id");
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
                        mVehicle.put(Vehicle.VEHICLE_ID, maxId);
                        firestore.collection("Vehicle")
                                .add(mVehicle)
                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if(task.isSuccessful()) {
                                            Log.e(TAG, "onComplete: Successfully");
                                            generalRef.update("maxID", maxId);
                                            Toast.makeText(AddVehicleActivity.this, "Add vehicle Successfully", Toast.LENGTH_SHORT).show();
                                            mBinding.progressIndicator.setVisibility(View.GONE);
                                            AddVehicleActivity.this.finish();
                                        }
                                        else {
                                            Log.e(TAG, "onComplete: fail");
                                            mBinding.progressIndicator.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                }
            });
        }
    }
}