package adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

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
    private List<Vehicle> filterVehicleList;
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
    public VehicleAdapter( OnRVItemClickListener listener2, int viewType) {
        this.mListener2 = listener2;
        this.mViewType = viewType;
        statusStringMapping = new ArrayList<>();
        statusStringMapping.add("Available");
        statusStringMapping.add("In use");
        statusStringMapping.add("Maintenance");
    }

    public List<Vehicle> getFilterVehicleList() {
        return filterVehicleList;
    }

    public void setCallBack(ViewBindCallback callback)
    {
        this.mCallback = callback;
    }
    // for the first view type
    public void setAdapterData(List<Vehicle> list)
    {
        filterVehicleList = list;
        vehicleList = list;
        notifyDataSetChanged();
    }
    // for the second view type
    public void setAdapterData(List<Vehicle> list, List<Boolean> list2)
    {
        filterVehicleList = list;
        checkedList = list2;
        notifyDataSetChanged();
    }
    public Filter getFilter(int type)
    {
        if(type == 0)
        {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    String constraintStr = constraint.toString();
                    if(constraintStr.isEmpty())
                    {
                        Log.e(TAG, "performFiltering: EMPTY" );
                        filterVehicleList = vehicleList;
                    }
                    else
                    {
                        List<Vehicle> list = new ArrayList<>();
                        for(Vehicle vehicle : vehicleList)
                        {
                            if(vehicle.getNumberOfPlate().contains(constraintStr))
                            {
                                list.add(vehicle);
                            }
                        }
                        filterVehicleList = list;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filterVehicleList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    notifyDataSetChanged();
                }
            };
        }
        return new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String constraintStr = constraint.toString();
            if(constraintStr.isEmpty() || constraintStr.equals("000"))
            {
                Log.e(TAG, "performFiltering: EMPTY" );
                filterVehicleList = vehicleList;
            }
            else
            {
                List<Vehicle> list = new ArrayList<>();
                if(constraintStr.charAt(0) == '1')
                {
                    for(Vehicle vehicle : vehicleList)
                    {
                        if(vehicle.getStatus() == 0)
                        {
                            list.add(vehicle);
                        }
                    }
                }
                if(constraintStr.charAt(1) == '1')
                {
                    for(Vehicle vehicle : vehicleList)
                    {
                        if(vehicle.getStatus() == 1)
                        {
                            list.add(vehicle);
                        }
                    }
                }
                if(constraintStr.charAt(2) == '1')
                {
                    for(Vehicle vehicle : vehicleList)
                    {
                        if(vehicle.getStatus() == 2)
                        {
                            list.add(vehicle);
                        }
                    }
                }
                filterVehicleList = list;
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filterVehicleList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    };
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
        Vehicle currentVehicle = filterVehicleList.get(realPosition);
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
        if(filterVehicleList != null)
        {
            return filterVehicleList.size();
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
