package viewmodel;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import data.Driver;
import data.Route;
import data.Vehicle;

public class AssignJobViewModel extends ViewModel {
    private static final String TAG = "AssignJobViewModel";
    private List<Route> routeList;
    private List<Vehicle> vehicleList;
    private MutableLiveData<List<Route>> routeListLiveData;
    private MutableLiveData<List<Vehicle>> vehicleListLiveData;
    private FirebaseFirestore firestore;
    private int chosenVehiclePos;
    private int chosenRoutePos;
    private DocumentReference vehicleRef;
    private DocumentReference routeRef;
    private Handler mHandler;
    private int currentDriverID;
    private DocumentReference driverRef;
    private Driver driver;
    private ExecutorService executorService;
    private UpdateCallback callback;

    public void setCallback(UpdateCallback callback) {
        this.callback = callback;
    }

    public void setChosenVehiclePos(int chosenVehiclePos) {
        this.chosenVehiclePos = chosenVehiclePos;
    }

    public void setCurrentDriverID(int driverID) {
        this.currentDriverID = driverID;
        firestore.collection("Driver")
                .whereEqualTo(Driver.DRIVER_ID, currentDriverID)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot snapshot =task.getResult().getDocuments().get(0);
                            driverRef = snapshot.getReference();
                            driver = snapshot.toObject(Driver.class);
                        }
                        else
                        {
                            task.getException().printStackTrace();
                        }
                    }
                });
    }

    public void setChosenRoutePos(int chosenRoutePos) {
        this.chosenRoutePos = chosenRoutePos;
    }
    public void updateAssignment()
    {
        if(chosenRoutePos == -1 || chosenVehiclePos == -1 || chosenVehiclePos >= vehicleList.size() || chosenRoutePos >= routeList.size())
        {
            Log.e(TAG, "updateAssignment: ERROR");
            callback.updateDone(false);
            return;
        }
        if(driverRef == null)
        {
            Log.e(TAG, "No Driver Ref");
            callback.updateDone(false);
            return;
        }
        Log.e(TAG, chosenRoutePos + " and " + chosenVehiclePos );
        Task<QuerySnapshot> task1 = firestore.collection("Vehicle")
                .whereEqualTo("ID", vehicleList.get(chosenVehiclePos).getID())
                .limit(1)
                .get();
        Task<QuerySnapshot> task2 = firestore.collection("Route")
                        .whereEqualTo("ID", routeList.get(chosenRoutePos).getID())
                        .limit(1)
                        .get();
        executorService = Executors.newSingleThreadExecutor();

        Tasks.whenAllComplete(task1, task2).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
            @Override
            public void onComplete(@NonNull Task<List<Task<?>>> task) {
                if(task.isSuccessful()) {
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            Log.e(TAG, "run executor " + Thread.currentThread());
                            List<Task<?>> taskList = task.getResult();

                            Task<QuerySnapshot> querySnapshot1 = (Task<QuerySnapshot>) taskList.get(0);
                            Task<QuerySnapshot> querySnapshot2 = (Task<QuerySnapshot>) taskList.get(1);
                            vehicleRef = querySnapshot1.getResult().getDocuments().get(0).getReference();
                            routeRef = querySnapshot2.getResult().getDocuments().get(0).getReference();
                            Map<String, Object> vehicleMap = new HashMap<>();
                            vehicleMap.put(Vehicle.VEHICLE_ROUTE_ID, routeList.get(chosenRoutePos).getID());
                            vehicleMap.put(Vehicle.VEHICLE_DRIVER_ID, driver.getID());
                            vehicleMap.put(Vehicle.VEHICLE_STATUS, 1);
                            Map<String, Object> routeMap = new HashMap<>();
                            routeMap.put(Route.ROUTE_VEHICLE_ID, vehicleList.get(chosenVehiclePos).getID());
                            routeMap.put(Route.ROUTE_DRIVER_ID, driver.getID());
                            routeMap.put(Route.ROUTE_STATUS, 1);
                            routeMap.put(Route.ROUTE_ACTUAL_DEPART, Timestamp.now());
                            Map<String, Object> driverMap = new HashMap<>();
                            driverMap.put(Driver.DRIVER_STATUS, 1);
                            driverMap.put(Driver.DRIVER_ROUTE_ID, routeList.get(chosenRoutePos).getID());
                            driverMap.put(Driver.DRIVER_VEHICLE_ID, vehicleList.get(chosenVehiclePos).getID());
                            List<Integer> drivingRoutes = driver.getListOfDrivingRoutes();
                            if(drivingRoutes == null)
                            {
                                Log.e(TAG, "null driving routes");
                                drivingRoutes = new ArrayList<>();
                            }
                            drivingRoutes.add(routeList.get(chosenRoutePos).getID());
                            driverMap.put(Driver.DRIVER_DRIVING_ROUTES, drivingRoutes);
                            // to get the result of the update
                            Tasks.whenAllComplete(vehicleRef.update(vehicleMap),
                                            routeRef.update(routeMap),
                                            driverRef.update(driverMap))
                                    .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                                        @Override
                                        public void onComplete(@NonNull Task<List<Task<?>>> task) {
                                            if (task.isSuccessful()) {
                                                Log.e(TAG, "onComplete: update done ");
                                                mHandler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        callback.updateDone(true);
                                                    }
                                                });
                                            } else {
                                                mHandler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        callback.updateDone(false);
                                                    }
                                                });
                                            }
                                        }
                                    });
                        }
                    });
                }
                else
                {
                    Log.e(TAG, "get error");
                }
            }
        });

    }

    public AssignJobViewModel() {
        firestore = FirebaseFirestore.getInstance();
        routeListLiveData = new MutableLiveData<>();
        vehicleListLiveData = new MutableLiveData<>();
        routeList = new ArrayList<>();
        vehicleList = new ArrayList<>();
        getAvailableRouteList();
        mHandler = new Handler();
        getAvailableVehicleList();
        chosenRoutePos = -1;
        chosenVehiclePos = -1;
    }
    public void getAvailableVehicleList()
    {
        firestore.collection("Vehicle")
                .whereEqualTo(Vehicle.VEHICLE_STATUS, 0) // only get the free vehicle
                .get()
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
                            vehicleListLiveData.setValue(vehicleList);
                        }
                        else
                        {
                            task.getException().printStackTrace();
                        }
                    }
                });
    }

    public void getAvailableRouteList()
    {
        firestore.collection("Route")
                .whereEqualTo(Route.ROUTE_STATUS, 0) // only get the unassigned route
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            QuerySnapshot snapshots = task.getResult();
                            for(QueryDocumentSnapshot snapshot : snapshots)
                            {
                                routeList.add(snapshot.toObject(Route.class));
                            }
                            routeListLiveData.setValue(routeList);
                        }
                        else
                        {
                            task.getException().printStackTrace();
                        }
                    }
                });
    }

    public MutableLiveData<List<Route>> getRouteListLiveData() {
        return routeListLiveData;
    }


    public MutableLiveData<List<Vehicle>> getVehicleListLiveData() {
        return vehicleListLiveData;
    }
    public interface UpdateCallback
    {
        void updateDone(boolean isSuccess);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(executorService != null)
            executorService.shutdown();
    }
}
