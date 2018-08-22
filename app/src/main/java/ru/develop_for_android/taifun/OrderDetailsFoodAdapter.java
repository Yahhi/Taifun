package ru.develop_for_android.taifun;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.develop_for_android.taifun.data.FoodWithCount;

public class OrderDetailsFoodAdapter extends RecyclerView.Adapter<OrderDetailsFoodAdapter.ViewHolder> {

    private List<FoodWithCount> food;
    private Context context;
    AddRemoveListener listener;

    public OrderDetailsFoodAdapter(Context context, AddRemoveListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void initializeData(List<FoodWithCount> foodEntries) {
        this.food = foodEntries;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_food_with_count,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(food.get(position));
        Log.i("FOOD", "binding food " + food.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        if (food == null) {
            return 0;
        } else {
            return food.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private String foodId;
        int count;

        private final ImageView mImage;
        private final TextView mTitle;
        private final TextView mPrice;
        private final TextView mCount;
        private final TextView mTotalPrice;
        private final ImageButton mIncrement;
        private final ImageButton mDecrement;

        public ViewHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.food_info_image);
            mTitle = itemView.findViewById(R.id.food_info_title);
            mPrice = itemView.findViewById(R.id.food_info_price);
            mCount = itemView.findViewById(R.id.food_info_count);
            mTotalPrice = itemView.findViewById(R.id.food_info_total);
            mIncrement = itemView.findViewById(R.id.increment);
            mIncrement.setOnClickListener(v -> listener.onCountChange(foodId, count + 1));
            mDecrement = itemView.findViewById(R.id.decrement);
            mDecrement.setOnClickListener(dView -> listener.onCountChange(foodId, count - 1));
        }

        void onBind(FoodWithCount foodEntry) {
            foodId = foodEntry.getId();
            count = foodEntry.getCount();

            mTitle.setText(foodEntry.getTitle());
            mPrice.setText(foodEntry.getReadablePrice(context));
            int count = foodEntry.getCount();
            mCount.setText(context.getResources().getQuantityString(R.plurals.pcs, count, count));
            mTotalPrice.setText(foodEntry.getFinalPrice(context));
            Glide.with(mImage.getContext()).load(foodEntry.getImageAddressNetwork()).into(mImage);
            Log.i("FOOD", "loading " + foodEntry.getImageAddressNetwork());
        }
    }
}
