package ru.develop_for_android.taifun;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ru.develop_for_android.taifun.data.AppDatabase;
import ru.develop_for_android.taifun.data.OrderEntry;

public class OrderInfoActivity extends AppCompatActivity {

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
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupViewModel() {

        OrderInfoViewModelFactory factory = new OrderInfoViewModelFactory(
                AppDatabase.getInstance(getBaseContext()), orderId);
        ViewModelProviders.of(this, factory).get(OrderInfoViewModel.class);
    }

}
