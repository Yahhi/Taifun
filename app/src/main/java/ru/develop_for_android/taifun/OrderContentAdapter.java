package ru.develop_for_android.taifun;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.develop_for_android.taifun.data.FoodWithCount;
import ru.develop_for_android.taifun.data.OrderWithFood;

public class OrderContentAdapter extends RecyclerView.Adapter<OrderContentAdapter.ViewHolder> {

    private List<FoodWithCount> food;
    private Context context;

    public OrderContentAdapter(Context context){
        this.context = context;
    }

    public void initialize(OrderWithFood orderContent) {
        this.food = orderContent.getFoodInOrder();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_food_in_order,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(food.get(position).getImageAddressNetwork(), food.get(position).getCount());
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
        private final ImageView foodImage;
        private final TextView foodText;

        public ViewHolder(View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.food_in_order_image);
            foodText = itemView.findViewById(R.id.food_in_order_quantity);
        }

        void onBind(String imageAddress, int count) {
            foodText.setText(String.valueOf(count));
            Glide.with(context).load(imageAddress).into(foodImage);
        }
    }
}
