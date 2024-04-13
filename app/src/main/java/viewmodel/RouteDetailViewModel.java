package viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.Driver;
import data.Route;
import data.Vehicle;
import myinterface.FinishCallback;

public class RouteDetailViewModel extends ViewModel {
    private static final String TAG = "DriverDetailViewModel";
    private int routeID;

    private MutableLiveData<Driver> driverLiveData;
    private MutableLiveData<Vehicle> vehicleLiveData;
    private MutableLiveData<Route> routeLiveData;

    private Route route;
    private Driver currentDriver;
    private Vehicle currentVehicle;
    private List<String> statusListName;

    private FirebaseFirestore firestore;
    private DocumentReference routeRef;
    private DocumentReference driverRef;
    private DocumentReference vehicleRef;

    private FinishCallback mFinishCallback;


    public RouteDetailViewModel() {
        firestore = FirebaseFirestore.getInstance();
        routeLiveData = new MutableLiveData<>();
        statusListName = new ArrayList<>();
        statusListName.add("Not assigned");
        statusListName.add("Taken");
        statusListName.add("Finish");
    }
    public void setFinishCallback(FinishCallback callback)
    {
        this.mFinishCallback = callback;
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
                                routeRef = snapshot.getReference();
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
                        QuerySnapshot vehicleSnapshots = vehicleResult.getResult();
                        QuerySnapshot driverSnapshots = driverResult.getResult();
                        if(vehicleSnapshots.isEmpty() ||  driverSnapshots.isEmpty())
                        {
                            Log.e(TAG, "Fail to get current vehicle or driver");
                            return;
                        }
                        driverRef = driverSnapshots.getDocuments().get(0).getReference();
                        vehicleRef = vehicleSnapshots.getDocuments().get(0).getReference();
                        currentDriver = driverSnapshots.getDocuments().get(0).toObject(Driver.class);
                        currentVehicle = vehicleSnapshots.getDocuments().get(0).toObject(Vehicle.class);
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
    public void markRouteAsDone()
    {
        if(routeRef == null || driverRef == null || vehicleRef == null)
        {
            Log.e(TAG, "lack of document refs");
        }
        else {
            Map<String, Object> routeMap = new HashMap<>();
            routeMap.put(Route.ROUTE_STATUS, 2);
            routeMap.put(Route.ROUTE_LEFT_DIST, 0);
            routeMap.put(Route.ROUTE_ACTUAL_ARRIVE, Timestamp.now());

            Map<String, Object> driverMap = new HashMap<>();
            driverMap.put(Driver.DRIVER_STATUS, 0);
            List<Integer> drivingRoutes = currentDriver.getListOfDrivingRoutes();
            if (drivingRoutes == null)
            {
                drivingRoutes = new ArrayList<>();
            }
            drivingRoutes.add(routeID);
            Log.e(TAG, "driving routes size = " + drivingRoutes.size());
            driverMap.put(Driver.DRIVER_DRIVING_ROUTES, drivingRoutes);
            driverMap.put(Driver.DRIVER_VEHICLE_ID, null);
            driverMap.put(Driver.DRIVER_ROUTE_ID, null);

            Map<String, Object> vehicleMap = new HashMap<>();
            vehicleMap.put(Vehicle.VEHICLE_ROUTE_ID, null);
            vehicleMap.put(Vehicle.VEHICLE_DRIVER_ID, null);
            vehicleMap.put(Vehicle.VEHICLE_STATUS, 0);

            Tasks.whenAllComplete(routeRef.update(routeMap),
                    vehicleRef.update(vehicleMap),
                    driverRef.set(driverMap, SetOptions.merge()))
                    .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                        @Override
                        public void onComplete(@NonNull Task<List<Task<?>>> task) {
                            if(task.isSuccessful())
                            {
                                Log.e(TAG, "update successfully");
                                mFinishCallback.finish(true);
                            }
                            else
                            {
                                mFinishCallback.finish(false);
                                task.getException().printStackTrace();
                            }
                        }
                    });
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
