package bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.transportmanagement.databinding.BottomsheetMaintenanceBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import adapter.MaintenanceAdapter;
import data.MaintenanceReport;
import viewmodel.VehicleDetailViewModel;

public class MaintenanceBottomSheet extends BottomSheetDialogFragment {
    VehicleDetailViewModel mViewModel;
    BottomsheetMaintenanceBinding mBinding;
    private MaintenanceAdapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(VehicleDetailViewModel.class);
        adapter = new MaintenanceAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = BottomsheetMaintenanceBinding.inflate(inflater, container, false);
        mBinding.maintenanceRecycleView.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,
                false));
        mBinding.maintenanceRecycleView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        mBinding.maintenanceRecycleView.setAdapter(adapter);
        mViewModel.getMaintenanceReportLiveData().observe(requireActivity(), new Observer<List<MaintenanceReport>>() {
            @Override
            public void onChanged(List<MaintenanceReport> maintenanceReports) {
                adapter.setData(maintenanceReports);
                if(adapter.getItemCount() != 0)
                    mBinding.noHistoryMsg.setVisibility(View.GONE);
            }
        });
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaintenanceBottomSheet.this.dismiss();
            }
        });

    }
}
