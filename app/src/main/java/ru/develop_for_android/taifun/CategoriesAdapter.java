package ru.develop_for_android.taifun;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import ru.develop_for_android.taifun.data.CategoryEntry;

public class CategoriesAdapter extends FragmentStatePagerAdapter {
    private List<CategoryEntry> categoryEntries;

    public CategoriesAdapter(FragmentManager fm) {
        super(fm);
    }

    public void initialize(List<CategoryEntry> categoryEntries) {
        this.categoryEntries = categoryEntries;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return FoodListFragment
                .newInstance(categoryEntries.get(position).getId());
    }

    @Override
    public int getCount() {
        if (categoryEntries == null)
            return 0;
        else
            return categoryEntries.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categoryEntries.get(position).getTitle();
    }
}
