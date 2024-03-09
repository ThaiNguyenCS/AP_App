package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportmanagement.databinding.DriverItemHolderBinding;

import java.util.ArrayList;
import java.util.List;

import myinterface.OnRVItemClickListener;
import data.Driver;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.DriverViewHolder> {
    private OnRVItemClickListener mListener;
    private List<Driver> driverList;
    private List<String> statusList;
    private int viewType;
    public DriverAdapter(OnRVItemClickListener listener, int viewType) {
        this.viewType = viewType;
        this.mListener = listener;
        statusList = new ArrayList<>();
        statusList.add("Free");
        statusList.add("Driving");
    }


    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 0)
        {
            DriverItemHolderBinding binding = DriverItemHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new DriverViewHolder(binding, mListener);
        }

        //TODO handle view 2
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder holder, int position) {
        Driver driver = driverList.get(position);
        holder.mBinding.driverId.setText(driver.getCitizenID());
        holder.mBinding.driverLicense.setText(driver.getLicense());
        holder.mBinding.driverName.setText(driver.getName());
        holder.mBinding.driverName.setSelected(true);
        holder.mBinding.driverStatus.setText(statusList.get((int)driver.getStatus()));
    }

    @Override
    public int getItemCount() {
        if(driverList != null)
        {
            return driverList.size();
        }
        return 0;
    }
    public void setData(List<Driver> list)
    {
        this.driverList = list;
        notifyDataSetChanged();
    }

    public static class DriverViewHolder extends RecyclerView.ViewHolder {

        private DriverItemHolderBinding mBinding;
        private final OnRVItemClickListener mListener;
        public DriverViewHolder(@NonNull DriverItemHolderBinding binding, OnRVItemClickListener listener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.mListener = listener;
            this.mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DriverViewHolder.this.mListener.onRVItemClick(getAdapterPosition());
                }
            });
        }
    }
}
