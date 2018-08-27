package ru.develop_for_android.taifun;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import ru.develop_for_android.taifun.data.AddressEntry;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.ViewHolder> {

    private List<AddressEntry> addresses;
    private int defaultAddressId;
    private AddressChangesListener listener;
    private Context context;

    public AddressesAdapter(Context context, AddressChangesListener listener) {
        this.listener = listener;
        this.context = context;
    }

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

    public void initialize(Integer defaultId) {
        int oldPosition = -1;
        if (addresses != null) {
            for (int i = 0; i < addresses.size(); i++) {
                if (addresses.get(i).getId() == this.defaultAddressId) {
                    oldPosition = i;
                    break;
                }
            }
            this.defaultAddressId = defaultId;
            if (oldPosition != -1) {
                notifyItemChanged(oldPosition);
            }
            for (int i = 0; i < addresses.size(); i++) {
                if (addresses.get(i).getId() == defaultId) {
                    notifyItemChanged(i);
                    break;
                }
            }
        } else {
            this.defaultAddressId = defaultId;
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private AddressEntry addressValue;

        final TextView addressTitle;
        final TextView addressText;
        final ImageButton deleteButton;
        final ImageButton editButton;
        final ImageButton starButton;

        ViewHolder(View itemView) {
            super(itemView);
            addressText = itemView.findViewById(R.id.address_line);
            addressTitle = itemView.findViewById(R.id.address_title);

            deleteButton = itemView.findViewById(R.id.address_delete);
            deleteButton.setOnClickListener(v -> listener.deleteAddress(addressValue));

            editButton = itemView.findViewById(R.id.address_edit);
            editButton.setOnClickListener(v -> {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                dialogBuilder.setTitle(addressValue.getTitle());
                dialogBuilder.setMessage(R.string.address_s);
                EditText input = new EditText(context);
                dialogBuilder.setView(input);
                dialogBuilder.setPositiveButton(R.string.update, (dialog, which) -> {
                    addressValue.setAddressLine1(input.getText().toString());
                    listener.editAddress(addressValue);
                });
                dialogBuilder.setCancelable(true);
                dialogBuilder.create().show();
            });

            starButton = itemView.findViewById(R.id.address_star);
            starButton.setOnClickListener(v -> listener.makeAddressDefault(addressValue.getId()));
        }

        void onBind(AddressEntry addressValue) {
            this.addressValue = addressValue;
            addressTitle.setText(addressValue.getTitle());
            addressText.setText(addressValue.getAddressLine1());
            if (defaultAddressId == addressValue.getId()) {
                starButton.setEnabled(false);
                starButton.setImageResource(R.drawable.ic_super_star);
            } else {
                starButton.setEnabled(true);
                starButton.setImageResource(R.drawable.ic_star);
            }
        }
    }
}
