package org.techtown.capstoneprojectcocktail;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CocktailAdapterForHome extends RecyclerView.Adapter<CocktailAdapterForHome.ViewHolder> implements OnCocktailItemClickListenerForHome {
    ArrayList<Cocktail> items = new ArrayList<Cocktail>();
    OnCocktailItemClickListenerForHome listener;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textForCocktailName;
        //TextView textForCocktailDescription;
        TextView textForCocktailABV;
        ImageView imageForCocktail;

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        public ViewHolder (View itemView, final OnCocktailItemClickListenerForHome listener){
            super(itemView);

            textForCocktailName = itemView.findViewById(R.id.textView_cocktailName_home);
            //textForCocktailDescription = itemView.findViewById(R.id.textView_cocktailDescription_home);
            textForCocktailABV = itemView.findViewById(R.id.textView_cocktailABV_home);
            imageForCocktail = itemView.findViewById(R.id.imageView_cocktail_home);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(Cocktail item) {
            FirebaseStorage storage = FirebaseStorage.getInstance();

            StorageReference gsReference = storage.getReferenceFromUrl(item.getImageUrl());
            gsReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        // Glide 이용하여 이미지뷰에 로딩
                        try{
                            Glide.with(itemView)
                                    .load(task.getResult())
                                    .into(imageForCocktail);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    } else {
                        // URL을 가져오지 못하면 토스트 메세지
                    }
                }
            });
            textForCocktailName.setText(item.getName());
            //textForCocktailDescription.setText(item.getDescription());
            textForCocktailABV.setText(item.getAbvNum());
            //Glide.with(itemView).load(imageUrl).into(imageForCocktail);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.cocktail_cardview_horizontal, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Cocktail item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Cocktail item){
        items.add(item);
    }

    public void setItems(ArrayList<Cocktail> items){
        this.items = items;
    }

    public Cocktail getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, Cocktail item){
        items.set(position, item);
    }

    public void setOnItemClickListener(OnCocktailItemClickListenerForHome listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder,View view,int position){
        if(listener!=null){
            listener.onItemClick(holder,view,position);
        }
    }
}
