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
    private RefreshCallback mDriverRefreshCallback;
    private RefreshCallback mRouteRefreshCallback;
    private RefreshCallback mVehicleRefreshCallback;


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
        driverLiveList = new MutableLiveData<>();
        vehicleLiveList = new MutableLiveData<>();
        routeLiveList = new MutableLiveData<>();
    }
    public void setRefreshCallbackForDriver(RefreshCallback callback)
    {
        this.mDriverRefreshCallback = callback;
    }
    public void setRefreshCallbackForRoute(RefreshCallback callback)
    {
        this.mRouteRefreshCallback = callback;
    }
    public void setRefreshCallbackForVehicle(RefreshCallback callback)
    {
        this.mVehicleRefreshCallback = callback;
    }
    public void fetchVehicleData(boolean isForce) {
        if (vehicleList == null || isForce)
        {
            vehicleList = new ArrayList<>();
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
                                }
                                vehicleLiveList.setValue(vehicleList);
                                mVehicleRefreshCallback.refreshDone();
                            }
                            else
                            {
                                task.getException().printStackTrace();
                            }
                        }
                    });
        }
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
                            mDriverRefreshCallback.refreshDone();
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
    public void fetchRouteData(boolean isForce)
    {
        if(routeList == null || isForce)
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
                        Log.e(TAG, "onComplete: " );
                        if(isForce)
                        {
                            mRouteRefreshCallback.refreshDone();
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
