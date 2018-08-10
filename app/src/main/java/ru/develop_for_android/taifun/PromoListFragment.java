package ru.develop_for_android.taifun;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.develop_for_android.taifun.data.PromoEntry;


/**
 * A simple {@link Fragment} subclass.
 */
public class PromoListFragment extends Fragment {

    PromoAdapter adapter;

    public PromoListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_promo_list, container, false);
        RecyclerView recyclerView = fragmentView.findViewById(R.id.promo_list);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        adapter = new PromoAdapter();
        recyclerView.setAdapter(adapter);
        setupViewModel();
        return fragmentView;
    }

    private void setupViewModel() {
        PromoListViewModel viewModel = ViewModelProviders.of(this).get(PromoListViewModel.class);
        LiveData<List<PromoEntry>> promoEntries = viewModel.promoEntries;
        promoEntries.observe(this, promoEntries1 -> adapter.initialize(promoEntries1));
    }

}
