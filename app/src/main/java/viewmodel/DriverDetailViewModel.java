package viewmodel;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import data.Driver;

public class DriverDetailViewModel extends ViewModel {
    private static final String TAG = "DriverDetailViewModel";
    private MutableLiveData<Driver> driverLiveData;
    private Driver driver;
    private int driverID;
    private FirebaseFirestore firestore;
    private DocumentReference reference;

    public DriverDetailViewModel() {
        firestore = FirebaseFirestore.getInstance();
        driverLiveData = new MutableLiveData<>();
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
                        }
                        else
                        {
                            Log.e(TAG, "get driver failed");
                            task.getException().printStackTrace();
                        }
                    }
                });
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
