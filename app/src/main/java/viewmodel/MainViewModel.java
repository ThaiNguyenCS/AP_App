package viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import data.*;
public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";
    private List<Driver>  driverList;
    private List<Route> routeList;
    private List<Vehicle> vehicleList;
    private FirebaseFirestore firestore;

    public MainViewModel() {
        Log.e(TAG, "MainViewModel: ");
        firestore = FirebaseFirestore.getInstance();
        driverList = new ArrayList<>();
        routeList = new ArrayList<>();
        vehicleList = new ArrayList<>();

//        firestore.collection("Route").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//            }
//        });
        firestore.collection("Vehicle").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    QuerySnapshot snapshots = task.getResult();
                    for(QueryDocumentSnapshot snapshot : snapshots)
                    {
                        vehicleList.add(snapshot.toObject(Vehicle.class));
                        Log.e(TAG, "vehicle id: " +  vehicleList.get(vehicleList.size()-1).getID());
                    }
                }
                else
                {
                    task.getException().printStackTrace();
                }
            }
        });
    }
    public void fetchDriverData()
    {
        if(driverList == null)
        {
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
