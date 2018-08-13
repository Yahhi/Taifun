package ru.develop_for_android.taifun;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.develop_for_android.taifun.data.FoodWithIngredients;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {

    private List<FoodWithIngredients> foodEntries;
    private FoodClickListener listener;
    private Context context;

    public FoodListAdapter(FoodClickListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    public void initializeData(List<FoodWithIngredients> foodEntries) {
        this.foodEntries = foodEntries;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_food,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(foodEntries.get(position));
        Log.i("FOOD", "binding food " + foodEntries.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        if (foodEntries == null) {
            return 0;
        } else {
            return foodEntries.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private String foodId;

        private final ImageView mImage;
        private final TextView mTitle;
        private final TextView mPrice;
        private final TextView mIngredients;

        public ViewHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.food_info_image);
            mTitle = itemView.findViewById(R.id.food_info_title);
            mPrice = itemView.findViewById(R.id.food_info_price);
            mIngredients = itemView.findViewById(R.id.food_info_contents);
            itemView.setOnClickListener(v -> listener.onClick(foodId));
        }

        void onBind(FoodWithIngredients foodEntry) {
            foodId = foodEntry.getId();
            mTitle.setText(foodEntry.getTitle());
            mPrice.setText(foodEntry.getReadablePrice(context));
            mIngredients.setText(foodEntry.getReadableIngredientsList());
            Glide.with(mImage.getContext()).load(foodEntry.getImageAddressNetwork()).into(mImage);
            Log.i("FOOD", "loading " + foodEntry.getImageAddressNetwork());
        }
    }
}
