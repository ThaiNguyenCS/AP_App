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

import java.util.ArrayList;
import java.util.List;

import data.Driver;
import myinterface.RefreshCallback;

public class DriverViewModel extends ViewModel {
    private static final String TAG = "DriverViewModel";
    private List<Driver> driverList;
    private MutableLiveData<List<Driver>> driverLiveList;
    private FirebaseFirestore firestore;
    private String searchedDriverName;
    private List<Boolean> filterList;
    private MutableLiveData<List<Boolean>> filterLiveList;
    private MutableLiveData<String> searchedNameLive;
    private RefreshCallback mDriverRefreshCallback;
    public DriverViewModel() {
        Log.e(TAG, "DriverViewModel: ");
        firestore = FirebaseFirestore.getInstance();
        driverLiveList = new MutableLiveData<>();
        searchedDriverName = "";
        filterLiveList = new MutableLiveData<>();
        filterList = new ArrayList<>();
        for (int i = 0; i < 2; i++)
        {
            filterList.add(false);
        }
        filterLiveList.setValue(filterList);
        searchedNameLive = new MutableLiveData<>();
        searchedNameLive.setValue(searchedDriverName);
    }
    public MutableLiveData<String> getSearchedNameLive() {
        return searchedNameLive;
    }

    public MutableLiveData<List<Boolean>> getFilterLiveList() {
        return filterLiveList;
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
    public MutableLiveData<List<Driver>> getDriverLiveList() {
        return driverLiveList;
    }

    public CharSequence getFilterConstraints()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < 2; i++)
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
    public void setRefreshCallbackForDriver(RefreshCallback mDriverRefreshCallback) {
        this.mDriverRefreshCallback = mDriverRefreshCallback;
    }
}
