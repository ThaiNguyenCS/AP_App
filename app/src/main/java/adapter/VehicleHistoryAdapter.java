package adapter;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportmanagement.databinding.VehicleHistoryHolderBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import data.Driver;
import data.Route;

public class VehicleHistoryAdapter extends RecyclerView.Adapter<VehicleHistoryAdapter.VehicleHistoryViewHolder> {
    private List<Route> routeList;
    private Map<Integer, Driver> driverMap;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");

    public void setData(List<Route> list, Map<Integer, Driver> map)
    {
        this.routeList = list;
        this.driverMap = map;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public VehicleHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VehicleHistoryHolderBinding binding = VehicleHistoryHolderBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new VehicleHistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleHistoryViewHolder holder, int position) {
        Route route = routeList.get(holder.getAdapterPosition());
        Driver driver = driverMap.get(route.getCurrentDriverID());
        holder.mBinding.driverName.setText(driver.getName());
        holder.mBinding.driverCitizenID.setText(driver.getCitizenID());
        holder.mBinding.departureTextview.setText(route.getDeparture());
        holder.mBinding.destinationTextview.setText(route.getDestination());
        Date scheDepart = route.getScheDepartureDate().toDate();
        Date scheArrive = route.getScheArrivingDate().toDate();
        holder.mBinding.scheDepart.setText(dateFormat.format(scheDepart));
        holder.mBinding.scheArrive.setText(dateFormat.format(scheArrive));
        Date actualDepart = route.getActualDepartureDate().toDate();
        holder.mBinding.actualDepart.setText(dateFormat.format(actualDepart));
        if(route.getActualArrivingDate() != null)
            holder.mBinding.actualArrive.setText(dateFormat.format(route.getActualArrivingDate().toDate()));
    }

    @Override
    public int getItemCount() {
        if(routeList != null)
        {
            return routeList.size();
        }
        return 0;
    }

    public static class VehicleHistoryViewHolder extends RecyclerView.ViewHolder
    {
        private VehicleHistoryHolderBinding mBinding;
        public VehicleHistoryViewHolder(@NonNull VehicleHistoryHolderBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
