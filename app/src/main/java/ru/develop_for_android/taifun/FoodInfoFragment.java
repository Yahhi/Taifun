package ru.develop_for_android.taifun;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ru.develop_for_android.taifun.data.FoodWithIngredients;


/**
 * A placeholder fragment containing a simple view.
 */
public class FoodInfoFragment extends Fragment {

    TextView descriptionView, weightView, priceView;
    IngredientsAdapter adapter;
    ImageView foodImage;

    public FoodInfoFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_food_info, container, false);
        descriptionView = fragmentView.findViewById(R.id.food_description);
        weightView = fragmentView.findViewById(R.id.food_weight);
        priceView = fragmentView.findViewById(R.id.food_price);
        foodImage = fragmentView.findViewById(R.id.food_image);

        adapter = new IngredientsAdapter();
        RecyclerView ingredientsList = fragmentView.findViewById(R.id.food_ingredients_list);
        ingredientsList.setAdapter(adapter);
        ingredientsList.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));

        setupViewModel();
        return fragmentView;
    }

    private void setupViewModel() {
        FoodInfoViewModel viewModel = ViewModelProviders.of(requireActivity()).get(FoodInfoViewModel.class);
        LiveData<FoodWithIngredients> food = viewModel.getFood();
        food.observe(this, foodWithIngredients -> {
            if (foodWithIngredients != null) {
                adapter.initialize(foodWithIngredients.getIngredientEntries());
                descriptionView.setText(foodWithIngredients.getDescription());
                weightView.setText(getString(R.string.weight, foodWithIngredients.getWeight()));
                priceView.setText(foodWithIngredients.getReadablePrice());
                Glide.with(FoodInfoFragment.this)
                        .load(foodWithIngredients.getImageAddressNetwork()).into(foodImage);
            }
        });
    }
}
