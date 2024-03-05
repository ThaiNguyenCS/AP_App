package viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import data.Driver;

public class DriverDetailViewModel extends ViewModel {
    private static final String TAG = "DriverDetailViewModel";
    private MutableLiveData<Driver> driverLiveData;
    private Driver driver;
    private int driverID;
    private FirebaseFirestore firestore;

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

    public MutableLiveData<Driver> getDriverLiveData() {
        return driverLiveData;
    }

}
