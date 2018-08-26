package ru.develop_for_android.taifun;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class OrderDetailsActivity extends AppCompatActivity {

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent openOrderConfirmation = new Intent(getBaseContext(), OrderConfirmationActivity.class);
            startActivity(openOrderConfirmation);
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupViewModel();
    }

    private void setupViewModel() {

        OrderDetailsViewModel viewModel = ViewModelProviders.of(this).get(OrderDetailsViewModel.class);
        viewModel.getOrder().observe(this, order -> {
            if (order == null) {
                fab.setEnabled(false);
            } else {
                if (order.getItemsCount() > 0) {
                    fab.setEnabled(true);
                } else {
                    fab.setEnabled(false);
                }
            }
        });
    }

}
