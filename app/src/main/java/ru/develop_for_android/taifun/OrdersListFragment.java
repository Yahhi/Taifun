package ru.develop_for_android.taifun;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersListFragment extends Fragment implements OrderClickListener{

    OrderAdapter adapter;
    OrderListViewModel viewModel;

    public OrdersListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_orders_list, container, false);
        RecyclerView ordersList = fragmentView.findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new OrderAdapter(requireContext(), this);
        ordersList.setAdapter(adapter);

        setupViewModel();
        return fragmentView;
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(requireActivity()).get(OrderListViewModel.class);
        viewModel.getOrders().observe(this, ordersWithFoods -> adapter.initialize(ordersWithFoods));
        viewModel.update.observe(this, integer -> Timber.i("FOOD: update calculated"));
    }

    @Override
    public void onClick(int orderId) {
        viewModel.openOrderInfo(requireContext(), orderId);
    }
}
