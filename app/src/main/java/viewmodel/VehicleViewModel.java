package viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import data.Driver;
import data.Vehicle;
import myinterface.RefreshCallback;

public class VehicleViewModel extends ViewModel {
    private List<Vehicle> vehicleList;
    private MutableLiveData<List<Vehicle>> vehicleLiveList;
    private MutableLiveData<List<Boolean>> filterLiveList;
    private RefreshCallback mVehicleRefreshCallback;
    private List<Boolean> filterList;
    private FirebaseFirestore firestore;

    public VehicleViewModel() {
        firestore = FirebaseFirestore.getInstance();
        vehicleLiveList = new MutableLiveData<>();
        filterLiveList = new MutableLiveData<>();
        filterList = new ArrayList<>();
        for(int i = 0; i < 3; i++)
        {
            filterList.add(false);
        }
        filterLiveList.setValue(filterList);
    }

    public MutableLiveData<List<Boolean>> getFilterLiveList() {
        return filterLiveList;
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

    public MutableLiveData<List<Vehicle>> getVehicleLiveList() {
        return vehicleLiveList;
    }

    public void setRefreshCallbackForVehicle(RefreshCallback vehicleRefreshCallback) {
        this.mVehicleRefreshCallback = vehicleRefreshCallback;
    }

    public CharSequence getFilterConstraints()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < 3; i++)
        {
            if(filterList.get(i))
            {
                stringBuilder.append("1");
            }
            else
            {
                stringBuilder.append("0");
            }
        }
        return new StringBuffer(stringBuilder);
    }
}
