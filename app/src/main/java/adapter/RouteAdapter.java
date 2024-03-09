package adapter;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportmanagement.databinding.RouteItemHolderBinding;
import com.example.transportmanagement.databinding.RouteItemHolderForChoosingBinding;

import java.util.ArrayList;
import java.util.List;

import data.Driver;
import data.Route;
import myinterface.OnRVItemClickListener;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {
    private static final String TAG = "RouteAdapter";
    private List<Route> routeList;
    private List<Boolean> checkedList;
    private int mViewType;
    OnRVItemClickListener mListener;

    public RouteAdapter(OnRVItemClickListener listener, int viewType) {
        this.mViewType = viewType;
        mListener = listener;
    }
    public void setAdapterData(List<Route> list)
    {
        routeList = list;
        notifyDataSetChanged();
    }
    public void setAdapterData(List<Route> list, List<Boolean> list2)
    {
        routeList = list;
        checkedList = list2;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 1)
        {
            RouteItemHolderForChoosingBinding binding = RouteItemHolderForChoosingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new RouteViewHolder(binding, mListener);
        }
        RouteItemHolderBinding binding = RouteItemHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RouteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        Log.e(TAG, "onBindViewHolder: " );
        if(holder.getItemViewType() == 0)
        {
            //TODO normal binding
        }
        else
        {
            int realPosition = holder.getAdapterPosition();
            holder.mBinding2.checkbox.setChecked(checkedList.get(realPosition));
            holder.mBinding2.costTextview.setText(routeList.get(realPosition).getCost() + " VND");
            holder.mBinding2.revenueTextview.setText(routeList.get(realPosition).getRevenue() + " VND");
            holder.mBinding2.departureTextview.setText(routeList.get(realPosition).getDeparture());
            holder.mBinding2.destinationTextview.setText(routeList.get(realPosition).getDestination());
            //TODO handle timestamp
            Log.e(TAG, "time " + routeList.get(realPosition).getActualArrivingDate().getSeconds());
//            routeList.get(realPosition).getActualArrivingDate().toDate()

        }


    }

    @Override
    public int getItemViewType(int position) {
        return mViewType;
    }

    @Override
    public int getItemCount() {
        if(routeList != null)
        {
            return routeList.size();
        }
        return 0;
    }
    public static class RouteViewHolder extends RecyclerView.ViewHolder
    {
        private RouteItemHolderBinding mBinding1;
        private RouteItemHolderForChoosingBinding mBinding2;
        private OnRVItemClickListener mListener;
        public RouteViewHolder(@NonNull RouteItemHolderBinding binding) {
            super(binding.getRoot());
            this.mBinding1 = binding;
        }

        public RouteViewHolder(@NonNull RouteItemHolderForChoosingBinding binding, OnRVItemClickListener listener) {
            super(binding.getRoot());
            this.mBinding2 = binding;
            this.mListener = listener;
            this.mBinding2.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onRVItemClick(getAdapterPosition());
                }
            });
        }

    }
}
