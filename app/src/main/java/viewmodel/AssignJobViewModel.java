package viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import data.Route;
import data.Vehicle;

public class AssignJobViewModel extends ViewModel {
    private List<Route> routeList;
    private List<Vehicle> vehicleList;
    private MutableLiveData<List<Route>> routeListLiveData;
    private MutableLiveData<List<Vehicle>> vehicleListLiveData;
    private FirebaseFirestore firestore;

    public AssignJobViewModel() {
        firestore = FirebaseFirestore.getInstance();
        routeListLiveData = new MutableLiveData<>();
        vehicleListLiveData = new MutableLiveData<>();
        routeList = new ArrayList<>();
        vehicleList = new ArrayList<>();
        getAvailableRouteList();
        getAvailableVehicleList();
    }
    public void getAvailableVehicleList()
    {
        firestore.collection("Vehicle")
                .whereEqualTo("status", 0) // only get the free vehicle
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
                .whereEqualTo("status", 0) // only get the free vehicle
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

}
