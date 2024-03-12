package viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import data.*;
import myinterface.RefreshCallback;

public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";
    private List<Driver>  driverList;
    private List<Route> routeList;
    private List<Vehicle> vehicleList;
    private MutableLiveData<List<Driver>> driverLiveList;
    private MutableLiveData<List<Route>> routeLiveList;
    private MutableLiveData<List<Vehicle>> vehicleLiveList;
    private FirebaseFirestore firestore;
    private RefreshCallback mRefreshCallback;

    public MutableLiveData<List<Driver>> getDriverLiveList() {
        return driverLiveList;
    }

    public MutableLiveData<List<Route>> getRouteLiveList() {
        return routeLiveList;
    }

    public MutableLiveData<List<Vehicle>> getVehicleLiveList() {
        return vehicleLiveList;
    }


    public MainViewModel() {
        Log.e(TAG, "MainViewModel: ");
        firestore = FirebaseFirestore.getInstance();
        vehicleList = new ArrayList<>();
        driverLiveList = new MutableLiveData<>();
        vehicleLiveList = new MutableLiveData<>();
        routeLiveList = new MutableLiveData<>();
        firestore.collection("Vehicle").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    QuerySnapshot snapshots = task.getResult();
//                    Log.e(TAG, "add vehicle");
                    for(QueryDocumentSnapshot snapshot : snapshots)
                    {
                        vehicleList.add(snapshot.toObject(Vehicle.class));
//                        Log.e(TAG, "vehicle id: " +  vehicleList.get(vehicleList.size()-1).getID());
                    }
                    vehicleLiveList.setValue(vehicleList);
//                    Log.e(TAG, "finish add vehicle");
                }
                else
                {
                    task.getException().printStackTrace();
                }
            }
        });
    }
    public void setRefreshCallback(RefreshCallback callback)
    {
        this.mRefreshCallback = callback;
    }
    public void fetchDriverData(boolean isForce)
    {
        if(driverList == null || isForce)
        {
            Log.e(TAG, "fetchDriverData: " );
            driverList = new ArrayList<>();
            firestore.collection("Driver").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                        QuerySnapshot snapshots = task.getResult();

                        for(QueryDocumentSnapshot snapshot : snapshots)
                        {
                            driverList.add(snapshot.toObject(Driver.class));
                        }
                        driverLiveList.setValue(driverList);
                        if(isForce)
                        {
                            mRefreshCallback.refreshDone();
                        }
                    }
                    else
                    {
                        task.getException().printStackTrace();
                    }
                }
            });
        }
    }
    public void fetchRouteData()
    {
        if(routeList == null)
        {
            routeList = new ArrayList<>();
            firestore.collection("Route").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                        QuerySnapshot snapshots = task.getResult();
                        for(QueryDocumentSnapshot snapshot : snapshots)
                        {
                            routeList.add(snapshot.toObject(Route.class));
                        }
                        routeLiveList.setValue(routeList);
                    }
                    else
                    {
                        task.getException().printStackTrace();
                    }
                }
            });
        }
    }
}
