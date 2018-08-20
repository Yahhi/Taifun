package ru.develop_for_android.taifun;

import android.arch.lifecycle.MutableLiveData;
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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ru.develop_for_android.taifun.data.OrderEntry;
import ru.develop_for_android.taifun.data.OrderStatusEntry;
import ru.develop_for_android.taifun.data.OrderWithFood;

/**
 * A placeholder fragment containing a simple view.
 */
public class OrderInfoActivityFragment extends Fragment {

    TextView orderNumber, orderStatus, orderTimePlased, orderTimeConfirmed, orderTimeProcessed, orderTimeReady;
    OrderContentAdapter adapter;

    public OrderInfoActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_order_info, container, false);
        orderNumber = fragmentView.findViewById(R.id.order_number);
        orderStatus = fragmentView.findViewById(R.id.order_time);
        orderTimePlased = fragmentView.findViewById(R.id.order_status_placed_time);
        orderTimeConfirmed = fragmentView.findViewById(R.id.order_status_confirmed_time);
        orderTimeProcessed = fragmentView.findViewById(R.id.order_status_prepir_time);
        orderTimeReady = fragmentView.findViewById(R.id.order_status_pickup_time);
        RecyclerView recyclerView = fragmentView.findViewById(R.id.order_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new OrderContentAdapter(requireContext());
        recyclerView.setAdapter(adapter);
        setupViewModel();
        return fragmentView;
    }

    private void setupViewModel() {
        OrderInfoViewModel viewModel = ViewModelProviders.of(requireActivity()).get(OrderInfoViewModel.class);
        MutableLiveData<OrderWithFood> order = viewModel.getOrder();
        order.observe(this, order1 -> {
            if (order1 == null) {
                orderNumber.setText("#");
                orderStatus.setText(R.string.order_number_invalid);
            } else {
                orderNumber.setText(getString(R.string.order_number, order1.getOrderEntry().getGlobalNumber()));
                orderStatus.setText(OrderEntry.getStatusReadable(order1.getOrderEntry().getStatus()));
                adapter.initialize(order1);
            }
        });
        MutableLiveData<List<OrderStatusEntry>> statuses = viewModel.getStatuses();
        statuses.observe(this, orderStatusEntries -> {
            if (orderStatusEntries == null || orderStatusEntries.size() == 0) {
                orderTimePlased.setText("");
                orderTimeConfirmed.setText("");
                orderTimeProcessed.setText("");
                orderTimeReady.setText("");
            } else {
                for (OrderStatusEntry statusEntry : orderStatusEntries) {
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    switch (statusEntry.getStatus()) {
                        case OrderEntry.STATUS_PLACED:
                            orderTimePlased.setText(format.format(statusEntry.getDateTime()));
                            break;
                        case OrderEntry.STATUS_CONFIRMED:
                            orderTimeConfirmed.setText(format.format(statusEntry.getDateTime()));
                            break;
                        case OrderEntry.STATUS_PROCESSED:
                            orderTimeProcessed.setText(format.format(statusEntry.getDateTime()));
                            break;
                        case OrderEntry.STATUS_READY_TO_PICKUP:
                            orderTimeReady.setText(format.format(statusEntry.getDateTime()));
                            break;
                    }
                }
            }
        });
    }
}
