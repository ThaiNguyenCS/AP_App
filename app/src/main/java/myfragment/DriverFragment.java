package myfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import viewmodel.MainViewModel;

public class DriverFragment extends Fragment implements OnRVItemClickListener {
    private static final String TAG = "DriverFragment";
    FragmentDriverBinding mBinding;
    MainViewModel mViewModel;
    private List<Driver> driverList;
    public static String DRIVER_ID = "DRIVER_ID";

    public DriverFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentDriverBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach: " + context );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated: ");
        mBinding = FragmentDriverBinding.bind(view);
        DriverAdapter adapter = new DriverAdapter(this);
        mViewModel.fetchDriverData();
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
        //TODO handle with position or driver
        Intent intent = new Intent(getContext(), DriverDetailActivity.class);
        intent.putExtra(DRIVER_ID, driverList.get(position).getID()); // send the clicked driver's ID to the activity
        startActivity(intent);
    }
}