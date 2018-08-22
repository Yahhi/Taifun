package ru.develop_for_android.taifun;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class OrderDetailsActivityFragment extends Fragment {

    public OrderDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_order_details, container, false);
        RecyclerView foodInOrderList = fragmentView.findViewById(R.id.order_details_recyclerlist);
        foodInOrderList.setLayoutManager(new LinearLayoutManager(getContext()));

        return fragmentView;
    }
}
