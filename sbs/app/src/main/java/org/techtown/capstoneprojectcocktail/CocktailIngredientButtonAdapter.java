package org.techtown.capstoneprojectcocktail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CocktailIngredientButtonAdapter extends RecyclerView.Adapter<CocktailIngredientButtonAdapter.ViewHolder> implements OnCocktailIngredientButtonItemClickListener{
    ArrayList<CocktailIngredientButton> items = new ArrayList<CocktailIngredientButton>();
    OnCocktailIngredientButtonItemClickListener listener;
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView ingredientButtonTextView;
        public ViewHolder(View itemView,final OnCocktailIngredientButtonItemClickListener listener){
            super(itemView);

            ingredientButtonTextView = itemView.findViewById(R.id.text_ingredient_home);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick (View view){
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this,view,position);
                    }
                }
            });
        }

        public void setItem(CocktailIngredientButton item){
            ingredientButtonTextView.setText(item.getIngredientCategorizedName());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.cocktail_ingredient_button_home, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        CocktailIngredientButton item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(CocktailIngredientButton item){
        items.add(item);
    }

    public void setItems (ArrayList<CocktailIngredientButton> items){
        this.items = items;
    }

    public CocktailIngredientButton getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, CocktailIngredientButton item){
        items.set(position, item);
    }

    public void setOnItemClickListener(OnCocktailIngredientButtonItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position){
        if(listener!=null){
            listener.onItemClick(holder,view,position);
        }
    }
}
