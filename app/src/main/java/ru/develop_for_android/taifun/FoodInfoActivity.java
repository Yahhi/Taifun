package ru.develop_for_android.taifun;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import ru.develop_for_android.taifun.data.AppDatabase;
import ru.develop_for_android.taifun.data.FoodWithIngredients;
import ru.develop_for_android.taifun.data.OrderContentEntry;

public class FoodInfoActivity extends AppCompatActivity {

    FoodInfoViewModel viewModel;
    String foodId;
    public static final String FOOD_ID_KEY = "food_id";

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState == null) {
            foodId = getIntent().getStringExtra(FOOD_ID_KEY);
        } else {
            foodId = savedInstanceState.getString(FOOD_ID_KEY);
        }
        setupViewModel();

        setContentView(R.layout.activity_food_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> viewModel.addFoodToOrder());
        fab.setEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupViewModel() {
        FoodInfoViewModelFactory factory = new FoodInfoViewModelFactory(
                AppDatabase.getInstance(getBaseContext()), foodId);
        viewModel = ViewModelProviders.of(this, factory).get(FoodInfoViewModel.class);

        LiveData<FoodWithIngredients> foodItem = viewModel.getFood();
        foodItem.observe(this, foodWithIngredients -> {
            if (foodWithIngredients != null) {
                setTitle(foodWithIngredients.getTitle());
                fab.setEnabled(true);
            } else {
                fab.setEnabled(false);
            }
        });

        LiveData<OrderContentEntry> orderContent = viewModel.getExistingFoodInOrder();
        orderContent.observe(this, orderContentEntry -> {
            if (orderContentEntry != null) {
                informUserAboutTotalUpdate(orderContentEntry.getCount());
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(FOOD_ID_KEY, foodId);
        super.onSaveInstanceState(outState);
    }

    private void informUserAboutTotalUpdate(int count) {
        Toast.makeText(getBaseContext(), getResources().getQuantityString(R.plurals.order_count_update, count, count),
                Toast.LENGTH_LONG).show();
    }

}
