package ru.develop_for_android.taifun;


import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import ru.develop_for_android.taifun.data.AddressEntry;
import ru.develop_for_android.taifun.data.AppDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyInfoFragment extends Fragment {

    private AddressesAdapter adapter;
    MyInfoViewModel viewModel;

    public MyInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_my_info, container, false);

        RecyclerView addressesList = fragmentView.findViewById(R.id.my_addresses_list);
        adapter = new AddressesAdapter();
        addressesList.setAdapter(adapter);
        addressesList.setLayoutManager(new LinearLayoutManager(fragmentView.getContext()));

        setupViewModel();

        TextInputEditText nameEditor = fragmentView.findViewById(R.id.my_name);
        nameEditor.setText(viewModel.getUserName());
        nameEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setUserName(s.toString());
            }
        });
        TextInputEditText phoneEditor = fragmentView.findViewById(R.id.my_phone);
        phoneEditor.setText(viewModel.getUserPhone());
        phoneEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setUserPhone(s.toString());
            }
        });
        Button addAddressButton = fragmentView.findViewById(R.id.my_addresses_add);
        addAddressButton.setOnClickListener(v -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
            dialogBuilder.setTitle("New address");
            dialogBuilder.setMessage("Title:");
            EditText input = new EditText(requireContext());
            dialogBuilder.setView(input);
            dialogBuilder.setPositiveButton("Add", (dialog, which) -> {
                AddressEntry newAddress = new AddressEntry(input.getText().toString(), "");
                AppExecutors.getInstance().diskIO().execute(() -> {
                    AppDatabase.getInstance(requireContext()).foodDao().addAddress(newAddress);
                    requireActivity().runOnUiThread(this::setupViewModel);
                });
            });
            dialogBuilder.setCancelable(true);
            dialogBuilder.create().show();
        });

        return fragmentView;
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(MyInfoViewModel.class);
        LiveData<List<AddressEntry>> addresses = viewModel.getAddresses();
        addresses.observe(this, new Observer<List<AddressEntry>>() {
            @Override
            public void onChanged(@Nullable List<AddressEntry> addressEntries) {
                adapter.initialize(addressEntries);
                addresses.removeObserver(this);
            }
        });

    }

}
