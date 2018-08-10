package ru.develop_for_android.taifun;

import android.app.Activity;
import android.os.Bundle;

public class OrderInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
