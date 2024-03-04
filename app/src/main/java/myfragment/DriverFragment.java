package myfragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.transportmanagement.DriverDetailActivity;
import com.example.transportmanagement.R;
import com.example.transportmanagement.databinding.FragmentDriverBinding;

import adapter.DriverAdapter;
import myinterface.OnRVItemClickListener;

public class DriverFragment extends Fragment implements OnRVItemClickListener {
    FragmentDriverBinding mBinding;

    public DriverFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentDriverBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = FragmentDriverBinding.bind(view);
        DriverAdapter adapter = new DriverAdapter(this);
        mBinding.recyclerView.setAdapter(adapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onRVItemClick(int position) {
        //TODO handle with position or driver
        Intent intent = new Intent(getContext(), DriverDetailActivity.class);
        startActivity(intent);
    }
}