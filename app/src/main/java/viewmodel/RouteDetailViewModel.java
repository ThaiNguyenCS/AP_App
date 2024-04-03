package viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.Driver;
import data.Route;
import data.Vehicle;

public class RouteDetailViewModel extends ViewModel {
    private static final String TAG = "DriverDetailViewModel";
    private MutableLiveData<Driver> driverLiveData;
    private MutableLiveData<Vehicle> vehicleLiveData;
    private MutableLiveData<Route> routeLiveData;
    private Route route;
    private Driver currentDriver;
    private Vehicle currentVehicle;
    private int routeID;
    private FirebaseFirestore firestore;
    private DocumentReference routeReference;
    private List<String> statusListName;


    public RouteDetailViewModel() {
        firestore = FirebaseFirestore.getInstance();
        routeLiveData = new MutableLiveData<>();
        statusListName = new ArrayList<>();
        statusListName.add("Not assigned");
        statusListName.add("Taken");
        statusListName.add("Finish");
    }
    public String getStatus(int index)
    {
        return statusListName.get(index);
    }
    public void getRouteData(int id)
    {
        routeID = id;
        firestore.collection("Route")
                .whereEqualTo("ID", routeID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot snapshot : task.getResult())
                            {
                                route = snapshot.toObject(Route.class);
                                routeReference = snapshot.getReference();
                            }
                            routeLiveData.setValue(route);
                        }
                        else
                        {
                            Log.e(TAG, "get route failed");
                            task.getException().printStackTrace();
                        }
                    }
                });
    }
    public void getCurrentDriverAndVehicle()
    {
        if(route != null)
        {
            vehicleLiveData = new MutableLiveData<>();
            driverLiveData = new MutableLiveData<>();
            Task<QuerySnapshot> getVehicle = firestore.collection("Vehicle")
                    .whereEqualTo(Vehicle.VEHICLE_ID, route.getCurrentVehicleID())
                    .limit(1)
                    .get();
            Task<QuerySnapshot> getDriver = firestore.collection("Driver")
                    .whereEqualTo(Driver.DRIVER_ID, route.getCurrentDriverID())
                    .limit(1)
                    .get();
            Tasks.whenAllComplete(getDriver, getVehicle).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                @Override
                public void onComplete(@NonNull Task<List<Task<?>>> task) {
                    Log.e(TAG, "onComplete: " + Thread.currentThread());
                    if(task.isSuccessful())
                    {
                        Task<QuerySnapshot> vehicleResult = (Task<QuerySnapshot>) task.getResult().get(0);
                        Task<QuerySnapshot> driverResult = (Task<QuerySnapshot>) task.getResult().get(1);
                        currentDriver = driverResult.getResult().getDocuments().get(0).toObject(Driver.class);
                        currentVehicle = vehicleResult.getResult().getDocuments().get(0).toObject(Vehicle.class);
                        if(currentDriver == null || currentVehicle == null)
                        {
                            Log.e(TAG, "Empty current driver or vehicle");
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
            Log.e(TAG, "Empty route");
        }

    }
    public void updateRoute(Route modifiedRoute)
    {
//        if(reference != null)
//        {
//            Map<String, Object> objectMap= new HashMap<>();
//            objectMap.put(Driver.DRIVER_NAME, modifiedDriver.getName());
//            objectMap.put(Driver.DRIVER_ADDRESS, modifiedDriver.getAddress());
//            objectMap.put(Driver.DRIVER_CITIZENID, modifiedDriver.getCitizenID());
//            objectMap.put(Driver.DRIVER_PHONE, modifiedDriver.getPhoneNumber());
//            objectMap.put(Driver.DRIVER_YOE, modifiedDriver.getYearOfExperience());
//            objectMap.put(Driver.DRIVER_STATUS, modifiedDriver.getStatus());
//            objectMap.put(Driver.DRIVER_LICENSE, modifiedDriver.getLicense());
//            reference.update(objectMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if(task.isSuccessful())
//                    {
//                        //TODO toast here
//                        Log.e(TAG, "onComplete: successfully" );
//                    }
//                    else
//                    {
//                        Log.e(TAG, "onComplete: fail");
//                    }
//                }
//            });
//        }
    }

    public MutableLiveData<Route> getRouteLiveData() {
        return routeLiveData;
    }
}
