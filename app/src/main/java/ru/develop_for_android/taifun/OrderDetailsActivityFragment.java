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
import android.widget.TextView;

public class OrderDetailsActivityFragment extends Fragment implements AddRemoveListener{

    OrderDetailsViewModel viewModel;
    OrderDetailsFoodAdapter adapter;
    TextView price, discount, total;

    public OrderDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_order_details, container, false);
        RecyclerView foodInOrderList = fragmentView.findViewById(R.id.order_details_recyclerlist);
        foodInOrderList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderDetailsFoodAdapter(requireContext(), this);
        foodInOrderList.setAdapter(adapter);

        price = fragmentView.findViewById(R.id.order_subtotal);
        discount = fragmentView.findViewById(R.id.order_discount);
        total = fragmentView.findViewById(R.id.order_total);

        setupViewModel();
        return fragmentView;
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(requireActivity()).get(OrderDetailsViewModel.class);
        viewModel.getOrder().observe(this, order -> {
            if (order == null) {
                adapter.initializeData(null);
                price.setText(getString(R.string.money, "0"));
                discount.setText(getString(R.string.money, "-"));
                total.setText(getString(R.string.money, "0"));
            } else {
                adapter.initializeData(order.getFoodInOrder());
                price.setText(order.getFoodPrice(requireContext()));
                discount.setText(order.getTotalDiscount(requireContext()));
                total.setText(order.getTotalPrice(requireContext()));
            }
        });
    }

    @Override
    public void onCountChange(String foodId, int count) {
        viewModel.setFoodCount(foodId, count);
    }
}
