package com.example.transportmanagement;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.transportmanagement.databinding.ActivityAddRouteBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.Route;

public class AddRouteActivity extends AppCompatActivity {
    private static final String TAG = "AddRouteActivity";
    ActivityAddRouteBinding mBinding;
    private static List<String> errorMsgs;
    final Calendar c = Calendar.getInstance();
    private FirebaseFirestore firestore;
    private Calendar arrivingDate = Calendar.getInstance();
    private Calendar departureDate = Calendar.getInstance();
    private Map<String, Object> mRoute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        mBinding = ActivityAddRouteBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setUpGeneralView();
        createErrorMessages();
    }
    private void createErrorMessages()
    {
        errorMsgs = new ArrayList<>();
        errorMsgs.add("Departure");
        errorMsgs.add("Destination");
        errorMsgs.add("Distance");
        errorMsgs.add("Departure Date");
        errorMsgs.add("Arriving Date");
        errorMsgs.add("Cost");
        errorMsgs.add("Revenue");
    }

    private void setUpGeneralView()
    {
        mBinding.errorMessage.setVisibility(View.GONE);
        mBinding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddRouteActivity.this);
                builder.setMessage("Do you really want to cancel the progress?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddRouteActivity.this.finish();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");

        mBinding.routeScheDepartureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int initYear = c.get(Calendar.YEAR);
                int initMonth = c.get(Calendar.MONTH);
                int initDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddRouteActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    }
                }, initYear, initMonth, initDay);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddRouteActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                departureDate.set(Calendar.MINUTE, minute);
                                departureDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                Date date = departureDate.getTime();
                                mBinding.routeScheDepartureDate.setText(dateFormat.format(date));
                            }
                        }, 12, 0, true);
                datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        departureDate.set(Calendar.DAY_OF_MONTH, datePickerDialog.getDatePicker().getDayOfMonth());
                        departureDate.set(Calendar.MONTH, datePickerDialog.getDatePicker().getMonth());
                        departureDate.set(Calendar.YEAR, datePickerDialog.getDatePicker().getYear());
                        timePickerDialog.show();
                    }
                });
                datePickerDialog.show();
            }
        });
        mBinding.routeScheArrivingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int initYear = c.get(Calendar.YEAR);
                int initMonth = c.get(Calendar.MONTH);
                int initDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddRouteActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    }
                }, initYear, initMonth, initDay);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddRouteActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                arrivingDate.set(Calendar.MINUTE, minute);
                                arrivingDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                Date date = arrivingDate.getTime();
                                mBinding.routeScheArrivingDate.setText(dateFormat.format(date));
                            }
                        }, 12, 0, true);
                datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                arrivingDate.set(Calendar.DAY_OF_MONTH, datePickerDialog.getDatePicker().getDayOfMonth());
                                arrivingDate.set(Calendar.MONTH, datePickerDialog.getDatePicker().getMonth());
                                arrivingDate.set(Calendar.YEAR, datePickerDialog.getDatePicker().getYear());
                                timePickerDialog.show();
                            }
                        });
                datePickerDialog.show();

            }
        });

        mBinding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO store into the database
                Toast.makeText(AddRouteActivity.this, "Add", Toast.LENGTH_SHORT).show();
                int result = addRouteCheckUp();
                if(result != -1)
                {
                    mBinding.errorMessage.setVisibility(View.VISIBLE);
                    mBinding.errorMessage.setText("Missing fields: " + errorMsgs.get(result));
                    return;
                }
                if(mRoute != null)
                    addRouteToFirebase(mRoute);
            }
        });
    }

    private void addRouteToFirebase(Map<String, Object> route)
    {
        mBinding.progressIndicator.setVisibility(View.VISIBLE);
        firestore = FirebaseFirestore.getInstance();
        DocumentReference generalRef = firestore.collection("GeneralInfo").document("route_max_id");
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
                    route.put(Route.ROUTE_ID, maxId);
                    firestore.collection("Route")
                            .add(route)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if(task.isSuccessful()) {
                                        Log.e(TAG, "onComplete: Successfully");
                                        mBinding.progressIndicator.setVisibility(View.GONE);
                                        generalRef.update("maxID", maxId);
                                        AddRouteActivity.this.finish();
                                    }
                                    else {
                                        Log.e(TAG, "onComplete: fail");
                                    }

                                }
                            });
                }
            }
        });
    }

    public int addRouteCheckUp()
    {
        String departure = mBinding.routeDeparture.getText().toString().trim();
        if(departure.isEmpty())
        {
            return 0;
        }
        String destination = mBinding.routeDestination.getText().toString().trim();
        if(destination.isEmpty())
        {
            return 1;
        }
        String distance = mBinding.routeDistance.getText().toString().trim();
        if(distance.isEmpty())
        {
            return 2;
        }
        String departureDateStr = mBinding.routeScheDepartureDate.getText().toString().trim();
        if(departureDateStr.isEmpty())
        {
            return 3;
        }
        String arrivingDateStr = mBinding.routeScheArrivingDate.getText().toString().trim();
        if(arrivingDateStr.isEmpty())
        {
            return 4;
        }
        String cost = mBinding.routeCost.getText().toString().trim();
        if(cost.isEmpty())
        {
            return 5;
        }

        String revenue = mBinding.routeRevenue.getText().toString().trim();
        if(revenue.isEmpty())
        {
            return 6;
        }
        mRoute = new HashMap<>();
        mRoute.put(Route.ROUTE_COST, Double.parseDouble(cost));
        mRoute.put(Route.ROUTE_DESTINATION, destination);
        mRoute.put(Route.ROUTE_DEPARTURE, departure);
        mRoute.put(Route.ROUTE_REVENUE, Double.parseDouble(revenue));
        mRoute.put(Route.ROUTE_DISTANCE, Double.parseDouble(distance));
        mRoute.put(Route.ROUTE_LEFT_DIST, 0);
        mRoute.put(Route.ROUTE_SCHE_ARRIVE, new Timestamp(arrivingDate.getTime()));
        mRoute.put(Route.ROUTE_SCHE_DEPART, new Timestamp(departureDate.getTime()));
        mRoute.put(Route.ROUTE_STATUS, 0);
        mRoute.put(Route.ROUTE_ACTUAL_ARRIVE, null);
        mRoute.put(Route.ROUTE_ACTUAL_DEPART, null);
        mRoute.put(Route.ROUTE_DRIVER_ID, null);
        mRoute.put(Route.ROUTE_VEHICLE_ID, null);
        return -1;
    }
}