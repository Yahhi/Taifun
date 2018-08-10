package ru.develop_for_android.taifun;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.develop_for_android.taifun.data.FoodWithIngredients;


/**
 * A simple {@link Fragment} subclass.
 */
public class FoodListFragment extends Fragment implements FoodClickListener {

    private static final String CATEGORY_ID_KEY = "category_id";
    private String categoryId;
    FoodListAdapter adapter;

    public FoodListFragment() {
        // Required empty public constructor
    }

    public static FoodListFragment newInstance(String categoryId) {

        Bundle args = new Bundle();
        args.putString(CATEGORY_ID_KEY, categoryId);
        FoodListFragment fragment = new FoodListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getString(CATEGORY_ID_KEY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_food_list, container, false);
        RecyclerView recyclerView = fragmentView.findViewById(R.id.food_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new FoodListAdapter(this);
        recyclerView.setAdapter(adapter);
        setupViewModel();

        return fragmentView;
    }

    private void setupViewModel() {
        if (getParentFragment() != null) {
            CategoriesListViewModel viewModel = ViewModelProviders.of(getParentFragment())
                    .get(CategoriesListViewModel.class);
            LiveData<List<FoodWithIngredients>> foodList = viewModel.getFoodListForCategory(categoryId);
            foodList.observe(this, foodWithIngredients -> adapter.initializeData(foodWithIngredients));
        }
    }

    @Override
    public void onClick(String foodId) {
        Intent intent = new Intent(requireContext(), FoodInfoActivity.class);
        intent.putExtra(FoodInfoActivity.FOOD_ID_KEY, foodId);
        startActivity(intent);
    }
}
