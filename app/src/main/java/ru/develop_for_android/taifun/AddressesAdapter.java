package ru.develop_for_android.taifun;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.List;

import ru.develop_for_android.taifun.data.AddressEntry;
import ru.develop_for_android.taifun.data.AppDatabase;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.ViewHolder> {

    private List<AddressEntry> addresses;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_address,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(addresses.get(position));
    }

    @Override
    public int getItemCount() {
        if (addresses == null) {
            return 0;
        } else {
            return addresses.size();
        }
    }

    public void initialize(List<AddressEntry> addressEntries) {
        this.addresses = addressEntries;
        notifyDataSetChanged();
    }

    public void add(AddressEntry newAddress) {
        addresses.add(newAddress);
        notifyItemInserted(addresses.size() - 1);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private AddressEntry addressValue;

        final TextInputEditText addressText;
        final ImageButton deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            addressText = itemView.findViewById(R.id.my_address_in_list);
            addressText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    addressValue.setAddressLine1(s.toString());
                    AppExecutors.getInstance().diskIO().execute(() -> AppDatabase.getInstance(addressText.getContext()).foodDao().updateAddress(addressValue));
                }
            });
            deleteButton = itemView.findViewById(R.id.my_address_in_list_delete);
            deleteButton.setOnClickListener(v -> {
                AppExecutors.getInstance().diskIO().execute(() -> AppDatabase.getInstance(v.getContext()).foodDao().makeAddressInvisible(addressValue));
                addresses.remove(addressValue);
                notifyDataSetChanged();
            });
        }

        void onBind(AddressEntry addressValue) {
            this.addressValue = addressValue;
            addressText.setHint(addressValue.getTitle());
            addressText.setText(addressValue.getAddressLine1());
        }
    }
}
