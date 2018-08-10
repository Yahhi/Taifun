package ru.develop_for_android.taifun;

import android.app.Activity;
import android.os.Bundle;

public class OrderConfirmationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
