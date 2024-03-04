package adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import myfragment.DriverFragment;
import myfragment.RouteFragment;
import myfragment.VehicleFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private static final String TAG = "ViewPagerAdapter";
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.e(TAG, "createFragment" );
        switch (position)
        {
            case 1:
                return new DriverFragment();
            case 2:
                return new RouteFragment();
            default:
                return new VehicleFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
