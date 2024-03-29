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
    private OnRVItemClickListener mListener2;
    private OnVehicleDetailClickListener mListener1;
    private static List<String> statusStringMapping;
    private ViewBindCallback mCallback;
    // 0 for normal view, 1 for choosing
    private int mViewType;

    public VehicleAdapter(OnVehicleDetailClickListener listener1, int viewType) {
        this.mListener1 = listener1;
        this.mViewType = viewType;
        statusStringMapping = new ArrayList<>();
        statusStringMapping.add("Available");
        statusStringMapping.add("In use");
        statusStringMapping.add("Maintenance");
    }
    public VehicleAdapter(OnVehicleDetailClickListener listener1, OnRVItemClickListener listener2, int viewType) {
        this.mListener2 = listener2;
        this.mListener1 = listener1;
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
            return new VehicleViewHolder(binding, mListener1);
        }
        VehicleItemHolderForChoosingBinding binding = VehicleItemHolderForChoosingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VehicleViewHolder(binding, mListener2);
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

    public interface OnVehicleDetailClickListener
    {
        void onVehicleDetailOpen(int position);
    }
    public static class VehicleViewHolder extends RecyclerView.ViewHolder
    {
        VehicleItemHolderBinding mBinding1;
        VehicleItemHolderForChoosingBinding mBinding2;
        OnRVItemClickListener mListener2;
        OnVehicleDetailClickListener mListener1;

        public VehicleViewHolder(@NonNull VehicleItemHolderBinding binding, OnVehicleDetailClickListener listener) {
            super(binding.getRoot());
            mBinding1 = binding;
            mListener1 = listener;
            mBinding1.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener1.onVehicleDetailOpen(getAdapterPosition());
                }
            });
        }
        public VehicleViewHolder(@NonNull VehicleItemHolderForChoosingBinding binding, OnRVItemClickListener listener) {
            super(binding.getRoot());
            mBinding2 = binding;
            mListener2 = listener;
            mBinding2.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener2.onRVItemClick(getAdapterPosition());
                }
            });
        }
    }


}
