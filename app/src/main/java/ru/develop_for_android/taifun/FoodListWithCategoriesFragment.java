package ru.develop_for_android.taifun;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.develop_for_android.taifun.data.CategoryEntry;


/**
 * A simple {@link Fragment} subclass.
 */
public class FoodListWithCategoriesFragment extends Fragment {

    CategoriesAdapter adapter;

    public FoodListWithCategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_food_list_with_categories, container, false);
        ViewPager viewPager = fragmentView.findViewById(R.id.food_categories_content);
        adapter = new CategoriesAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabs = fragmentView.findViewById(R.id.food_categories_tabs);
        tabs.setupWithViewPager(viewPager);
        setupViewModel();
        return fragmentView;
    }

    private void setupViewModel() {
        CategoriesListViewModel viewModel = ViewModelProviders.of(this).get(CategoriesListViewModel.class);
        LiveData<List<CategoryEntry>> categories = viewModel.categories;
        categories.observe(this, categoryEntries -> adapter.initialize(categoryEntries));
    }

}
