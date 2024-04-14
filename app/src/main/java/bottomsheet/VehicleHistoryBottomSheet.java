package bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.transportmanagement.databinding.BottomSheetVehicleHistoryBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import adapter.VehicleHistoryAdapter;
import myinterface.FinishCallback;
import viewmodel.VehicleDetailViewModel;

public class VehicleHistoryBottomSheet extends BottomSheetDialogFragment implements FinishCallback {
    VehicleDetailViewModel mViewModel;
    BottomSheetVehicleHistoryBinding mBinding;
    VehicleHistoryAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(VehicleDetailViewModel.class);
        mViewModel.setCallbackForHistory(this);
        mViewModel.getVehicleHistory(); // fetching vehicle history
        adapter = new VehicleHistoryAdapter();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = BottomSheetVehicleHistoryBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.historyRecycleView.setAdapter(adapter);
        mBinding.historyRecycleView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VehicleHistoryBottomSheet.this.dismiss();
            }
        });
    }

    @Override
    public void finish(boolean isSuccessful) {
        if (isSuccessful)
        {
            adapter.setData(mViewModel.getDrivingRoutes(), mViewModel.getDriverMap());
            if(adapter.getItemCount() != 0)
            {
                mBinding.noHistoryMsg.setVisibility(View.GONE);
            }
        }
        else
        {

        }

    }
}
