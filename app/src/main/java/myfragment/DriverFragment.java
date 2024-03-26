package myfragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.transportmanagement.DriverDetailActivity;
import com.example.transportmanagement.R;
import com.example.transportmanagement.databinding.FragmentDriverBinding;

import java.util.ArrayList;
import java.util.List;

import adapter.DriverAdapter;
import data.Driver;
import myinterface.OnRVItemClickListener;
import myinterface.RefreshCallback;
import myinterface.ViewBindCallback;
import viewmodel.MainViewModel;

public class DriverFragment extends Fragment implements OnRVItemClickListener, RefreshCallback, ViewBindCallback {
    private static final String TAG = "DriverFragment";
    FragmentDriverBinding mBinding;
    MainViewModel mViewModel;
    private List<Driver> driverList;
    public static String DRIVER_ID_EXTRA = "DRIVER_ID";

    public DriverFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mViewModel.setRefreshCallbackForDriver(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentDriverBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.fetchDriverData(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated: ");
        mBinding = FragmentDriverBinding.bind(view);
        mBinding.getRoot().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(TAG, "onRefresh: ");
                mViewModel.fetchDriverData(true);
            }
        });
        DriverAdapter adapter = new DriverAdapter(this);
        adapter.setViewBindCallback(this);
        mBinding.recyclerView.setAdapter(adapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mViewModel.getDriverLiveList().observe(requireActivity(), new Observer<List<Driver>>() {
            @Override
            public void onChanged(List<Driver> drivers) {
                driverList = drivers;
                adapter.setData(driverList);
                mBinding.progressCircular.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onRVItemClick(int position) {
        Intent intent = new Intent(getContext(), DriverDetailActivity.class);
        intent.putExtra(DRIVER_ID_EXTRA, driverList.get(position).getID()); // send the clicked driver's ID to the activity
        startActivity(intent);
    }


    @Override
    public void refreshDone() {
        mBinding.getRoot().setRefreshing(false);
    }

    @Override
    public ColorDrawable callBackStatusDrawable(int statusId) {
        switch (statusId) {
            case 0: {
                return new ColorDrawable(getResources().getColor(R.color.green));
            }
            case 1:
            {
                return new ColorDrawable(getResources().getColor(R.color.red));
            }
            default:
            {
                return new ColorDrawable(getResources().getColor(R.color.yellow));
            }
        }
    }
}