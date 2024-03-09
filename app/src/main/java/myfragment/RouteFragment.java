package myfragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.transportmanagement.R;
import com.example.transportmanagement.databinding.FragmentRouteBinding;
import com.example.transportmanagement.databinding.RouteItemHolderBinding;
import com.example.transportmanagement.databinding.RouteItemHolderForChoosingBinding;

import java.util.ArrayList;
import java.util.List;

import adapter.RouteAdapter;
import data.Driver;
import data.Route;
import myinterface.OnRVItemClickListener;
import viewmodel.AssignJobViewModel;
import viewmodel.MainViewModel;

public class RouteFragment extends Fragment implements OnRVItemClickListener {
    private static final String TAG = "RouteFragment";
    private int mViewType;
    private FragmentRouteBinding mBinding;
    private MainViewModel mainViewModel;
    private AssignJobViewModel assignJobViewModel;
    private int lastPosition;
    private List<Boolean> statusList;
    private List<Route> routeList;
    private RouteAdapter adapter;
    private Handler mHandler;
    public RouteFragment() {
    }

    public RouteFragment(int viewType) {
        this.mViewType = viewType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        lastPosition = -1;
        if(mViewType == 0)
        {
            mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        }
        else
        {
            assignJobViewModel = new ViewModelProvider(requireActivity()).get(AssignJobViewModel.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mBinding = FragmentRouteBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new RouteAdapter(this, mViewType);
        if(mViewType == 0)
        {
            mainViewModel.getRouteLiveList().observe(requireActivity(), new Observer<List<Route>>() {
                @Override
                public void onChanged(List<Route> routes) {
                    adapter.setAdapterData(routes);
                }
            });
        }
        else
        {
            assignJobViewModel.getRouteListLiveData().observe(requireActivity(), new Observer<List<Route>>() {
                @Override
                public void onChanged(List<Route> routes) {
                    routeList = routes;
                    statusList = new ArrayList<>();
                    for(int i = 0; i < routes.size(); i++)
                    {
                        statusList.add(false);
                    }
                    adapter.setAdapterData(routeList, statusList);
                }
            });
        }
        mBinding.recyclerView.setAdapter(adapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext() , LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onRVItemClick(int position) {

        if(lastPosition == position)
        {
            Log.e(TAG, " == ");
            // you've unchecked already
            statusList.set(position, false);
            lastPosition = -1;
            assignJobViewModel.setChosenRoutePos(lastPosition);
            return;
        }
        int tempLastPosition = lastPosition;
        lastPosition = position;
        assignJobViewModel.setChosenRoutePos(lastPosition);
        if(tempLastPosition != -1) // case the clicked position is different from the last position
        {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    statusList.set(tempLastPosition, false);
                    adapter.notifyItemChanged(tempLastPosition);
                }
            });
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                statusList.set(position, true);
                adapter.notifyItemChanged(position);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: fragment");
    }
}