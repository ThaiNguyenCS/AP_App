package adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportmanagement.databinding.DriverItemHolderBinding;

import java.io.FilterReader;
import java.util.ArrayList;
import java.util.List;

import myinterface.OnRVItemClickListener;
import data.Driver;
import myinterface.ViewBindCallback;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.DriverViewHolder>  {
    private static final String TAG = "DriverAdapter";
    private OnRVItemClickListener mListener;
    private List<Driver> driverList;
    private List<Driver> filterDriverList;
    private List<String> statusList;
    private ViewBindCallback mCallback;

    public DriverAdapter(OnRVItemClickListener listener) {
        this.mListener = listener;
        statusList = new ArrayList<>();
        statusList.add("Available");
        statusList.add("Driving");
    }

    public void setViewBindCallback(ViewBindCallback callback)
    {
        mCallback = callback;
    }

    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DriverItemHolderBinding binding = DriverItemHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DriverViewHolder(binding, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder holder, int position) {
        Driver driver = filterDriverList.get(position);
        holder.mBinding.driverId.setText(driver.getCitizenID());
        holder.mBinding.driverLicense.setText(driver.getLicense());
        holder.mBinding.driverName.setText(driver.getName());
        holder.mBinding.driverName.setSelected(true);
        holder.mBinding.driverStatus.setText(statusList.get((int)driver.getStatus()));
        holder.mBinding.statusIndicator.setImageDrawable(mCallback.callBackStatusDrawable((int)driver.getStatus()));
    }

    @Override
    public int getItemCount() {
        if(filterDriverList != null)
        {
            return filterDriverList.size();
        }
        return 0;
    }
    public void setInitialData(List<Driver> list)
    {
        this.driverList = list;
        this.filterDriverList = list;
        notifyDataSetChanged();
    }
    public List<Driver> getFilterDriverList()
    {
        return filterDriverList;
    }

    public Filter getFilter(int type) {
        if(type == 0)
        {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    String keyword = constraint.toString();
                    if(keyword.isEmpty())
                    {
                        filterDriverList = driverList;
                    }
                    else
                    {
                        List<Driver> list = new ArrayList<>();
                        for(Driver driver : driverList)
                        {
                            if(driver.getName().toLowerCase().contains(keyword.toLowerCase()))
                            {
                                list.add(driver);
                            }
                        }
                        filterDriverList = list;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filterDriverList;
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
                FilterResults filterResults = new FilterResults();
                String constraintStr = constraint.toString();
                if(constraintStr.isEmpty())
                {
                    filterDriverList = driverList;
                }
                else if(constraintStr.equals("Driving"))
                {
                    List<Driver> list = new ArrayList<>();
                    for(Driver driver : driverList)
                    {
                        if(driver.getStatus() == 1)
                        {
                            list.add(driver);
                        }
                    }
                    filterDriverList = list;
                }
                else if(constraintStr.equals("Available"))
                {
                    List<Driver> list = new ArrayList<>();
                    for(Driver driver : driverList)
                    {
                        if(driver.getStatus() == 0)
                        {
                            list.add(driver);
                        }
                    }
                    filterDriverList = list;
                }
                filterResults.values = filterDriverList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
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
