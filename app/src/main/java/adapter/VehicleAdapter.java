package adapter;

import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportmanagement.databinding.VehicleItemHolderBinding;
import com.example.transportmanagement.databinding.VehicleItemHolderForChoosingBinding;

import java.util.ArrayList;
import java.util.List;

import data.Vehicle;
import myinterface.OnRVItemClickListener;
import myinterface.ViewBindCallback;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {
    private static final String TAG = "VehicleAdapter";
    private List<Vehicle> vehicleList;
    private List<Boolean> checkedList;
    private OnRVItemClickListener mListener;
    private static List<String> statusStringMapping;
    private ViewBindCallback mCallback;
    // 0 for normal view, 1 for choosing
    private int mViewType;

    public VehicleAdapter(OnRVItemClickListener mListener, int viewType) {
        this.mListener = mListener;
        this.mViewType = viewType;
        statusStringMapping = new ArrayList<>();
        statusStringMapping.add("Available");
        statusStringMapping.add("In use");
        statusStringMapping.add("Maintenance");
    }
    public void setCallBack(ViewBindCallback callback)
    {
        this.mCallback = callback;
    }
    // for the first view type
    public void setAdapterData(List<Vehicle> list)
    {
        vehicleList = list;
        notifyDataSetChanged();
    }
    // for the second view type
    public void setAdapterData(List<Vehicle> list, List<Boolean> list2)
    {
        vehicleList = list;
        checkedList = list2;
        notifyDataSetChanged();
    }

    // this viewType is the result of getItemViewType()
    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 0)
        {
            VehicleItemHolderBinding binding = VehicleItemHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new VehicleViewHolder(binding, mListener);
        }
        VehicleItemHolderForChoosingBinding binding = VehicleItemHolderForChoosingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VehicleViewHolder(binding, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        int realPosition = holder.getAdapterPosition();
        Vehicle currentVehicle = vehicleList.get(realPosition);
        String size = String.join("x",
                String.valueOf(currentVehicle.getLength()),
                String.valueOf(currentVehicle.getWidth()),
                String.valueOf(currentVehicle.getHeight()));
        if(holder.getItemViewType() == 0)
        {
            //TODO image handle
            holder.mBinding1.fuel.setText(currentVehicle.getTypeOfFuel());
            holder.mBinding1.size.setText(size);
            holder.mBinding1.load.setText(currentVehicle.getMaximumLoad()+ " kg");
            holder.mBinding1.status.setText(statusStringMapping.get(currentVehicle.getStatus()));
            holder.mBinding1.licensePlate.setText(currentVehicle.getNumberOfPlate());
            holder.mBinding1.type.setText(currentVehicle.getType());
            holder.mBinding1.statusIndicator.setImageDrawable(mCallback.callBackStatusDrawable(currentVehicle.getStatus()));
        }
        else
        {
            //TODO image handle
            holder.mBinding2.checkbox.setChecked(checkedList.get(realPosition));
            holder.mBinding2.size.setText(size);
            holder.mBinding2.load.setText(currentVehicle.getMaximumLoad()+ " kg");
            holder.mBinding2.licensePlate.setText(currentVehicle.getNumberOfPlate());
            holder.mBinding2.type.setText(currentVehicle.getType());
            holder.mBinding2.fuel.setText(currentVehicle.getTypeOfFuel());
            holder.mBinding2.status.setText(statusStringMapping.get(currentVehicle.getStatus()));
    }

    }

    @Override
    public int getItemCount() {
        if(vehicleList != null)
        {
            return vehicleList.size();
        }
        return 0;
    }

    // return the view type depending on the mViewType
    @Override
    public int getItemViewType(int position) {
        return mViewType;
    }

    public static class VehicleViewHolder extends RecyclerView.ViewHolder
    {
        VehicleItemHolderBinding mBinding1;
        VehicleItemHolderForChoosingBinding mBinding2;
        OnRVItemClickListener mListener;

        public VehicleViewHolder(@NonNull VehicleItemHolderBinding binding, OnRVItemClickListener listener) {
            super(binding.getRoot());
            mBinding1 = binding;
            mListener = listener;
        }
        public VehicleViewHolder(@NonNull VehicleItemHolderForChoosingBinding binding, OnRVItemClickListener listener) {
            super(binding.getRoot());
            mBinding2 = binding;
            mListener = listener;
            mBinding2.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onRVItemClick(getAdapterPosition());
                }
            });
        }
    }


}
