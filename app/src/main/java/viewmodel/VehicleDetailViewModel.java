package viewmodel;

import android.os.SystemClock;
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

import data.Driver;
import data.MaintenanceReport;
import data.Route;
import data.Vehicle;
import myinterface.FinishCallback;

public class VehicleDetailViewModel extends ViewModel{
    private static final String TAG = "VehicleDetailViewModel";
    private MutableLiveData<Driver> driverLiveData;
    private MutableLiveData<Vehicle> vehicleLiveData;
    private MutableLiveData<Route> routeLiveData;
    private Driver currentDriver;
    private Route currentRoute;
    private Vehicle vehicle;
    private int vehicleID;
    private FirebaseFirestore firestore;
    private DocumentReference vehicleRef;
    private List<String> statusListName;
    private List<MaintenanceReport> maintenanceReports;
    private MutableLiveData<List<MaintenanceReport>> maintenanceReportLiveData;
    private FinishCallback callbackForMaintenance;

    public void setCallbackForMaintenance(FinishCallback callbackForMaintenance) {
        this.callbackForMaintenance = callbackForMaintenance;
    }

    public VehicleDetailViewModel() {
        firestore = FirebaseFirestore.getInstance();
        vehicleLiveData = new MutableLiveData<>();
        maintenanceReportLiveData = new MutableLiveData<>();
        statusListName = new ArrayList<>();
        statusListName.add("Available");
        statusListName.add("Used");
        statusListName.add("Maintenance");
        vehicleID = -1;
    }

    public String getStatus(int index)
    {
        return statusListName.get(index);
    }
    public int isAvailableForMaintenance() {
        if (vehicle == null) {
            return 1;
        }
        int status = vehicle.getStatus();
        switch (status)
        {
            case 1:
                return 2;
            case 2:
                return 3;
            default:
                return 0;
        }
    }

    public void sendVehicleToMaintenance(String des)
    {
        Map<String, Object> maintenanceMap = new HashMap<>();
        maintenanceMap.put(MaintenanceReport.MAINTENANCE_VEHICLE_ID, vehicleID);
        maintenanceMap.put(MaintenanceReport.MAINTENANCE_BEGIN, Timestamp.now());
        maintenanceMap.put(MaintenanceReport.MAINTENANCE_FINISH, null);
        maintenanceMap.put(MaintenanceReport.MAINTENANCE_DESCRIPTION, des);
        firestore.collection("MaintenanceHistory")
                .add(maintenanceMap)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful())
                        {
                            Map<String, Object> map = new HashMap<>();
                            map.put(Vehicle.VEHICLE_STATUS, 2);
                            vehicleRef.update(map);
                            callbackForMaintenance.finish(true);
                        }
                        else
                        {
                            task.getException().printStackTrace();
                            callbackForMaintenance.finish(false);
                        }
                    }
                });
    }
    public void getVehicleData(int id)
    {
        vehicleID = id;
        firestore.collection("Vehicle")
                .whereEqualTo(Vehicle.VEHICLE_ID, vehicleID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot snapshot : task.getResult())
                            {
                                vehicle = snapshot.toObject(Vehicle.class);
                                vehicleRef = snapshot.getReference();
                            }
                            vehicleLiveData.setValue(vehicle);
                        }
                        else
                        {
                            Log.e(TAG, "get vehicle failed");
                            task.getException().printStackTrace();
                        }
                    }
                });
    }
    public void getVehicleMaintenanceHistory()
    {
        if(vehicleID != -1)
        {
                firestore.collection("MaintenanceHistory")
                        .whereEqualTo(MaintenanceReport.MAINTENANCE_VEHICLE_ID, vehicleID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    QuerySnapshot snapshots = task.getResult();
                                    maintenanceReports = new ArrayList<>();
                                    for(QueryDocumentSnapshot snapshot : snapshots)
                                    {
                                        maintenanceReports.add(snapshot.toObject(MaintenanceReport.class));
                                    }
                                    maintenanceReportLiveData.setValue(maintenanceReports);
                                }
                            }
                        });
        }
    }

    public MutableLiveData<Vehicle> getVehicleLiveData() {
        return vehicleLiveData;
    }

    public MutableLiveData<List<MaintenanceReport>> getMaintenanceReportLiveData() {
        return maintenanceReportLiveData;
    }

    public void getCurrentRouteAndDriver()
    {
        if(vehicle != null)
        {
            driverLiveData = new MutableLiveData<>();
            routeLiveData = new MutableLiveData<>();
            Task<QuerySnapshot> getRoute = firestore.collection("Route")
                    .whereEqualTo(Route.ROUTE_ID, vehicle.getCurrentRouteID())
                    .limit(1)
                    .get();
            Task<QuerySnapshot> getVehicle = firestore.collection("Driver")
                    .whereEqualTo(Driver.DRIVER_ID, vehicle.getCurrentDriverID())
                    .limit(1)
                    .get();
            Tasks.whenAllComplete(getRoute, getVehicle).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                @Override
                public void onComplete(@NonNull Task<List<Task<?>>> task) {
                    Log.e(TAG, "onComplete: " + Thread.currentThread());
                    if(task.isSuccessful())
                    {
                        Task<QuerySnapshot> routeResult = (Task<QuerySnapshot>) task.getResult().get(0);
                        Task<QuerySnapshot> driverResult = (Task<QuerySnapshot>) task.getResult().get(1);
                        currentRoute = routeResult.getResult().getDocuments().get(0).toObject(Route.class);
                        currentDriver = driverResult.getResult().getDocuments().get(0).toObject(Driver.class);
                        if(currentRoute == null || currentDriver == null)
                        {
                            Log.e(TAG, "Empty current route or driver");
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
            Log.e(TAG, "Empty vehicle");
        }

    }
    public void updateVehicle(Vehicle modifiedVehicle)
    {
        if(vehicleRef != null)
        {
            Map<String, Object> objectMap= new HashMap<>();
//            objectMap.put(Driver.DRIVER_NAME, modifiedDriver.getName());
//            objectMap.put(Driver.DRIVER_ADDRESS, modifiedDriver.getAddress());
//            objectMap.put(Driver.DRIVER_CITIZENID, modifiedDriver.getCitizenID());
//            objectMap.put(Driver.DRIVER_PHONE, modifiedDriver.getPhoneNumber());
//            objectMap.put(Driver.DRIVER_YOE, modifiedDriver.getYearOfExperience());
//            objectMap.put(Driver.DRIVER_STATUS, modifiedDriver.getStatus());
//            objectMap.put(Driver.DRIVER_LICENSE, modifiedDriver.getLicense());
            vehicleRef.update(objectMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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

}
