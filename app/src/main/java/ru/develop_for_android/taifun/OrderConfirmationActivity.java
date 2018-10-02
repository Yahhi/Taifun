package ru.develop_for_android.taifun;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import ru.develop_for_android.taifun_data.OrderEntry;

public class OrderConfirmationActivity extends AppCompatActivity {

    OrderContentAdapter adapter;
    TextView dishesCount, deliveryAddress, deliveryPrice;
    EditText additionalInfo;
    RadioGroup deliveryType;
    ImageButton editAddress;
    Button schedule;

    private int selectedAddressId = -1;
    private Date scheduledDate;

    OrderConfirmationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dishesCount = findViewById(R.id.order_dishes_count);
        RecyclerView foodList = findViewById(R.id.order_content);
        foodList.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new OrderContentAdapter(getBaseContext());
        foodList.setAdapter(adapter);

        FloatingActionButton confirmation = findViewById(R.id.floatingActionButton);
        confirmation.setOnClickListener(v -> viewModel.finishOrder(additionalInfo.getText().toString(), scheduledDate));

        additionalInfo = findViewById(R.id.order_additional_info);

        deliveryType = findViewById(R.id.order_delivery_selection);
        deliveryType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.delivery_pickup) {
                viewModel.selectedAddressId.postValue(OrderEntry.NO_DELIVERY);
            } else {
                viewModel.selectedAddressId.postValue(selectedAddressId);
            }
        });

        deliveryAddress = findViewById(R.id.order_delivery_address);
        deliveryPrice = findViewById(R.id.order_delivery_price);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupViewModel();
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(OrderConfirmationViewModel.class);
        viewModel.unfinishedOrder.observe(this, orderWithFood -> {
            if (orderWithFood != null) {
                adapter.initialize(orderWithFood);
                int dishes = orderWithFood.getItemsCount();
                dishesCount.setText(getResources().getQuantityString(R.plurals.dishes_count, dishes, dishes));
                /*if (selectedAddressId == -1) {
                    selectedAddressId = orderWithFood.getOrderEntry().getAddressId();
                }*/
            }
        });
        viewModel.selectedAddress.observe(this, addressEntry -> {
            if (addressEntry == null) {
                deliveryAddress.setVisibility(View.GONE);
            } else {
                deliveryAddress.setVisibility(View.VISIBLE);
                deliveryAddress.setText(addressEntry.getAddressLine1());
                selectedAddressId = addressEntry.getId();
            }
        });

        viewModel.finishedId.observe(this, integer -> {
            if (integer != null) {
                Toast.makeText(getBaseContext(), "Order is saved", Toast.LENGTH_SHORT).show();
                Intent openOrderStatus = new Intent(getBaseContext(), OrderStatusActivity.class);
                openOrderStatus.putExtra(OrderStatusActivity.ORDER_ID_KEY, integer);
                startActivity(openOrderStatus);
            }
        });

        viewModel.networkResult.observe(this, s -> {
            if (s != null) {
                Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
            }
        });
    }

}
