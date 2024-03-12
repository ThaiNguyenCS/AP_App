package myfragment;

import android.graphics.drawable.ColorDrawable;
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
import com.example.transportmanagement.databinding.FragmentVehicleBinding;

import java.util.ArrayList;
import java.util.List;

import adapter.VehicleAdapter;
import data.Vehicle;
import myinterface.OnRVItemClickListener;
import myinterface.ViewBindCallback;
import viewmodel.AssignJobViewModel;
import viewmodel.MainViewModel;

public class VehicleFragment extends Fragment implements OnRVItemClickListener, ViewBindCallback {
    private static final String TAG = "VehicleFragment";
    FragmentVehicleBinding mBinding;
    private int mViewType;
    AssignJobViewModel assignJobViewModel;
    MainViewModel mainViewModel;
    private VehicleAdapter adapter;
    private int lastPosition; // used for viewtype of 2
    private List<Vehicle> vehicleList;
    private List<Boolean> checkedStatusList;
    private Handler mHandler;
    public VehicleFragment() {
        // Required empty public constructor
    }

    public VehicleFragment(int viewType) {
        this.mViewType = viewType;
        lastPosition = -1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        if(mViewType == 0)
        {
            mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
            return;
        }
        assignJobViewModel = new ViewModelProvider(requireActivity()).get(AssignJobViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentVehicleBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated: " );
        mBinding.progressIndicator.setVisibility(View.VISIBLE);
        adapter = new VehicleAdapter(this, mViewType);
        adapter.setCallBack(this);
        mBinding.recyclerView.setAdapter(adapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        if(mViewType == 0)
        {
            mainViewModel.getVehicleLiveList().observe(requireActivity(), new Observer<List<Vehicle>>() {
                @Override
                public void onChanged(List<Vehicle> vehicles) {
                    vehicleList = vehicles;
                    adapter.setAdapterData(vehicleList);
                    mBinding.progressIndicator.setVisibility(View.GONE);
                }
            });
        }
        else
        {
                assignJobViewModel.getVehicleListLiveData().observe(requireActivity(), new Observer<List<Vehicle>>() {
                @Override
                public void onChanged(List<Vehicle> vehicles) {
                    vehicleList = vehicles;
                    checkedStatusList = new ArrayList<>();
                    for(int i = 0; i < vehicleList.size(); i++)
                    {
                        checkedStatusList.add(false);
                    }
                    adapter.setAdapterData(vehicleList, checkedStatusList);
                    mBinding.progressIndicator.setVisibility(View.GONE);

                }
            });
        }
    }

    @Override
    public void onRVItemClick(int position) {
        if(lastPosition == position)
        {
            lastPosition = -1;
            assignJobViewModel.setChosenVehiclePos(lastPosition);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    checkedStatusList.set(position, false);
                    adapter.notifyItemChanged(position, checkedStatusList.get(position));
                }
            });
            return;
        }
        int tempLastPosition = lastPosition;
        lastPosition = position;
        assignJobViewModel.setChosenVehiclePos(lastPosition);
        if(tempLastPosition != -1)
        {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    checkedStatusList.set(tempLastPosition, false);
                    adapter.notifyItemChanged(tempLastPosition);
                }
            });
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                checkedStatusList.set(lastPosition, true);
                adapter.notifyItemChanged(lastPosition);
            }
        });

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