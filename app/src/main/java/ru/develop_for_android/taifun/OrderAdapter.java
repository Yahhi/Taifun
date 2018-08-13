package ru.develop_for_android.taifun;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.develop_for_android.taifun.data.OrderWithFood;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<OrderWithFood> orders;
    private Context context;

    public OrderAdapter(Context context) {
        this.context = context;
    }

    public void initialize(List<OrderWithFood> orders) {
        this.orders = orders;
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
        holder.onBind(orders.get(position));
    }

    @Override
    public int getItemCount() {
        if (orders == null) {
            return 0;
        } else {
            return orders.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final RecyclerView foodList;
        private OrderContentAdapter contentAdapter;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.order_title);
            foodList = itemView.findViewById(R.id.order_content);
            foodList.setLayoutManager(new LinearLayoutManager(context));
            contentAdapter = new OrderContentAdapter(context);
            foodList.setAdapter(contentAdapter);
        }

        void onBind(OrderWithFood order) {
            title.setText(order.getOrderEntry().getTitle(context));
            contentAdapter.initialize(order);
        }
    }
}
