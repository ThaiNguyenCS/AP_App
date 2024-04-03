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

import data.Route;
import myinterface.RefreshCallback;

public class RouteViewModel extends ViewModel {
    private static final String TAG = "RouteViewModel";
    private List<Route> routeList;
    private FirebaseFirestore firestore;
    private RefreshCallback mRouteRefreshCallback;
    private MutableLiveData<List<Route>> routeLiveList;
    private List<Boolean> filterList;
    private List<String> whichSearch;
    private MutableLiveData<List<Boolean>> filterLiveList;
    private MutableLiveData<List<String>> whichSearchLive;

    public RouteViewModel() {
        firestore = FirebaseFirestore.getInstance();
        routeLiveList = new MutableLiveData<>();
        filterLiveList = new MutableLiveData<>();
        whichSearchLive = new MutableLiveData<>();
        filterList = new ArrayList<>();
        for (int i = 0; i <= 2; i++)
        {
            filterList.add(false);
        }
        filterLiveList.setValue(filterList);
        whichSearch = new ArrayList<>();
        whichSearch.add("");
        whichSearch.add("");
        whichSearchLive.setValue(whichSearch);
    }

    public MutableLiveData<List<Boolean>> getFilterLiveList() {
        return filterLiveList;
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
    public MutableLiveData<List<String>> getWhichSearchLive() {
        return whichSearchLive;
    }

    public void setRefreshCallbackForRoute(RefreshCallback mRouteRefreshCallback) {
        this.mRouteRefreshCallback = mRouteRefreshCallback;
    }

    public MutableLiveData<List<Route>> getRouteLiveList() {
        return routeLiveList;
    }
    public List<Route> getRouteList()
    {
        return routeList;
    }

    public void fetchRouteData(boolean isForce)
    {
        if(routeList == null || isForce)
        {
            routeList = new ArrayList<>();
            firestore.collection("Route").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                        QuerySnapshot snapshots = task.getResult();
                        for(QueryDocumentSnapshot snapshot : snapshots)
                        {
                            routeList.add(snapshot.toObject(Route.class));
                        }
                        routeLiveList.setValue(routeList);
                        Log.e(TAG, "onComplete: " );
                        if(isForce)
                        {
                            mRouteRefreshCallback.refreshDone();
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
}
