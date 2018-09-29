package ru.develop_for_android.taifun;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.develop_for_android.taifun_data.IngredientEntry;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private List<IngredientEntry> ingredientEntries;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_ingredient,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(ingredientEntries.get(position));
    }

    @Override
    public int getItemCount() {
        if (ingredientEntries == null) {
            return 0;
        } else {
            return ingredientEntries.size();
        }
    }

    public void initialize(List<IngredientEntry> ingredientEntries) {
        this.ingredientEntries = ingredientEntries;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView ingredientTitle;

        ViewHolder(View itemView) {
            super(itemView);
            ingredientTitle = itemView.findViewById(R.id.ingredient_title);
        }

        void onBind(IngredientEntry ingredientEntry) {
            ingredientTitle.setText(ingredientEntry.getTitle());
        }
    }
}
