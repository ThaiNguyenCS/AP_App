package myfragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.transportmanagement.AddRouteActivity;
import com.example.transportmanagement.databinding.MaintenanceDialogBinding;

import java.util.ArrayList;
import java.util.List;

import myinterface.FinishCallback;
import viewmodel.VehicleDetailViewModel;


public class MaintenanceDialog extends DialogFragment implements FinishCallback {
    MaintenanceDialogBinding mBinding;
    VehicleDetailViewModel mViewModel;
    private static List<String> errorMsg;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(VehicleDetailViewModel.class);
        mViewModel.setCallbackForMaintenance(this);
        errorMsg = new ArrayList<>();
        errorMsg.add("No vehicle data");
        errorMsg.add("Vehicle is currently used");
        errorMsg.add("Vehicle is already in maintenance");

    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mBinding = MaintenanceDialogBinding.inflate(getLayoutInflater());
        mBinding.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = mBinding.maintenanceEdittext.getText().toString().trim();
                if(!description.isEmpty())
                {
                    int result = mViewModel.isAvailableForMaintenance();
                    if(result != 0)
                    {
                        Toast.makeText(requireActivity(), errorMsg.get(result-1), Toast.LENGTH_SHORT).show();
                        Dialog mainDialog = getDialog();
                        if(mainDialog != null)
                        {
                            mainDialog.dismiss();
                        }
                        return;
                    }
                    mViewModel.sendVehicleToMaintenance(description);
                }
                else
                {
                    Toast.makeText(requireActivity(), "Please add description", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setMessage("Do you really want to cancel the progress?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Dialog mainDialog = getDialog();
                        if(mainDialog != null)
                        {
                            mainDialog.dismiss();
                        }
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });
        AlertDialog.Builder dialog = new AlertDialog.Builder(requireContext());
        dialog.setView(mBinding.getRoot());
        return dialog.create();
    }




    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void finish(boolean isSuccessful) {
        if(isSuccessful)
            Toast.makeText(requireActivity(), "Successfully", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_SHORT).show();
        Dialog mainDialog = getDialog();
        if(mainDialog != null)
            mainDialog.dismiss();
    }
}
