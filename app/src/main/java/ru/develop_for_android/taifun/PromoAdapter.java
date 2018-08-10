package ru.develop_for_android.taifun;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.develop_for_android.taifun.data.PromoEntry;

public class PromoAdapter extends RecyclerView.Adapter<PromoAdapter.ViewHolder> {

    private List<PromoEntry> promoEntries;

    public void initialize(List<PromoEntry> promoEntries) {
        this.promoEntries = promoEntries;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_promo,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(promoEntries.get(position));
    }

    @Override
    public int getItemCount() {
        if (promoEntries == null) {
            return 0;
        } else {
            return promoEntries.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mImage;

        ViewHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.promo_image);
        }

        void onBind(PromoEntry promoEntry) {
            Glide.with(itemView.getContext()).load(promoEntry.getImageAddressNetwork()).into(mImage);
        }
    }
}
