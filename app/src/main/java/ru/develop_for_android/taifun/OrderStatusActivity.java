package ru.develop_for_android.taifun;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ru.develop_for_android.taifun.data.AppDatabase;
import ru.develop_for_android.taifun.data.OrderEntry;

public class OrderStatusActivity extends AppCompatActivity {

    private int orderId;
    public static final String ORDER_ID_KEY = "oder_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent startingData = getIntent();
        if (startingData == null) {
            orderId = savedInstanceState.getInt(ORDER_ID_KEY, OrderEntry.UNFINISHED_ORDER_ID);
        } else {
            orderId = startingData.getIntExtra(ORDER_ID_KEY, OrderEntry.UNFINISHED_ORDER_ID);
        }
        setupViewModel();

        setContentView(R.layout.activity_order_info);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupViewModel() {

        OrderStatusViewModelFactory factory = new OrderStatusViewModelFactory(
                AppDatabase.getInstance(getBaseContext()), orderId);
        ViewModelProviders.of(this, factory).get(OrderStatusViewModel.class);
    }

}
