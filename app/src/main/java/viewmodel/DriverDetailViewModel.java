package viewmodel;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.*;
import myinterface.FinishCallback;

public class DriverDetailViewModel extends ViewModel {
    private static final String TAG = "DriverDetailViewModel";
    private MutableLiveData<Driver> driverLiveData;
    private MutableLiveData<Vehicle> vehicleLiveData;
    private MutableLiveData<Route> routeLiveData;
    private Driver driver;
    private Route currentRoute;
    private Vehicle currentVehicle;
    private int driverID;
    private FirebaseFirestore firestore;
    private DocumentReference reference;
    private List<String> statusListName;
    private List<Route> drivingRoutes;
    private Map<Integer, Vehicle> vehicleMap;
    private FinishCallback mCallback;

    public void setCallback(FinishCallback mCallback) {
        this.mCallback = mCallback;
    }

    public List<Route> getDrivingRoutes() {
        return drivingRoutes;
    }

    public Map<Integer, Vehicle> getVehicleMap() {
        return vehicleMap;
    }

    public Route getCurrentRoute() {
        return currentRoute;
    }

    public Vehicle getCurrentVehicle() {
        return currentVehicle;
    }

    public DriverDetailViewModel() {
        firestore = FirebaseFirestore.getInstance();
        driverLiveData = new MutableLiveData<>();
        statusListName = new ArrayList<>();
        statusListName.add("Available");
        statusListName.add("Driving");
        driverID = -1;
    }
    public String getStatus(int index)
    {
        return statusListName.get(index);
    }
    public void getDriverData(int id)
    {
        driverID = id;
        firestore.collection("Driver")
                .whereEqualTo("ID", driverID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot snapshot : task.getResult())
                            {
                                driver = snapshot.toObject(Driver.class);
                                reference = snapshot.getReference();
                            }
                            driverLiveData.setValue(driver);
                            loadDriverHistory(driver.getListOfDrivingRoutes());
                        }
                        else
                        {
                            Log.e(TAG, "get driver failed");
                            task.getException().printStackTrace();
                        }
                    }
                });
    }

    public void loadDriverHistory(List<Integer> routeLists)
    {
        if(routeLists != null)
        {
            firestore.collection("Route")
                    .whereIn(Route.ROUTE_ID, routeLists)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                QuerySnapshot snapshots = task.getResult();
                                drivingRoutes = new ArrayList<>();
                                List<Integer> vehicleIDs = new ArrayList<>();
                                for(QueryDocumentSnapshot snapshot : snapshots)
                                {
                                    Route route = snapshot.toObject(Route.class);
                                    drivingRoutes.add(route);
                                    vehicleIDs.add(route.getCurrentVehicleID());
                                }
                                for(int i = 0 ; i < vehicleIDs.size(); i++)
                                {
                                    Log.e(TAG, vehicleIDs.get(i) + " ");
                                }
                                firestore.collection("Vehicle")
                                        .whereIn(Vehicle.VEHICLE_ID, vehicleIDs)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful())
                                                {
                                                    QuerySnapshot snapshots = task.getResult();
                                                    vehicleMap = new HashMap<>();
                                                    for(QueryDocumentSnapshot snapshot : snapshots)
                                                    {
                                                        Vehicle vehicle = snapshot.toObject(Vehicle.class);
                                                        vehicleMap.put(vehicle.getID(), vehicle);
                                                    }
                                                    mCallback.finish(true);
                                                }
                                                else
                                                {
                                                    task.getException().printStackTrace();
                                                }
                                            }
                                        });
                            }
                            else
                            {
                                task.getException().printStackTrace();
                            }
                        }
                    });
        }
    }
    public void getCurrentRouteAndVehicle()
    {
        if(driver != null)
        {
            vehicleLiveData = new MutableLiveData<>();
            routeLiveData = new MutableLiveData<>();
            Task<QuerySnapshot> getRoute = firestore.collection("Route")
                    .whereEqualTo(Route.ROUTE_ID, driver.getCurrentRouteID())
                    .limit(1)
                    .get();
            Task<QuerySnapshot> getVehicle = firestore.collection("Vehicle")
                    .whereEqualTo(Vehicle.VEHICLE_ID, driver.getCurrentVehicleID())
                    .limit(1)
                    .get();
            Tasks.whenAllComplete(getRoute, getVehicle).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                @Override
                public void onComplete(@NonNull Task<List<Task<?>>> task) {
                    Log.e(TAG, "onComplete: " + Thread.currentThread());
                    if(task.isSuccessful())
                    {
                        Task<QuerySnapshot> routeResult = (Task<QuerySnapshot>) task.getResult().get(0);
                        Task<QuerySnapshot> vehicleResult = (Task<QuerySnapshot>) task.getResult().get(1);
                        currentRoute = routeResult.getResult().getDocuments().get(0).toObject(Route.class);
                        currentVehicle = vehicleResult.getResult().getDocuments().get(0).toObject(Vehicle.class);
                        if(currentRoute == null || currentVehicle == null)
                        {
                            Log.e(TAG, "Empty current route or vehicle");
                        }

                    }
                    else
                    {
                        task.getException().printStackTrace();
                    }

                }
            });

        }
        else
        {
            Log.e(TAG, "Empty driver");
        }

    }
    public void updateDriver(Driver modifiedDriver)
    {
        if(reference != null)
        {
            Map<String, Object> objectMap= new HashMap<>();
            objectMap.put(Driver.DRIVER_NAME, modifiedDriver.getName());
            objectMap.put(Driver.DRIVER_ADDRESS, modifiedDriver.getAddress());
            objectMap.put(Driver.DRIVER_CITIZENID, modifiedDriver.getCitizenID());
            objectMap.put(Driver.DRIVER_PHONE, modifiedDriver.getPhoneNumber());
            objectMap.put(Driver.DRIVER_YOE, modifiedDriver.getYearOfExperience());
            objectMap.put(Driver.DRIVER_STATUS, modifiedDriver.getStatus());
            objectMap.put(Driver.DRIVER_LICENSE, modifiedDriver.getLicense());
            reference.update(objectMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        //TODO toast here
                        Log.e(TAG, "onComplete: successfully" );
                    }
                    else
                    {
                        Log.e(TAG, "onComplete: fail");
                    }
                }
            });
        }
    }
    public MutableLiveData<Driver> getDriverLiveData() {
        return driverLiveData;
    }

}
