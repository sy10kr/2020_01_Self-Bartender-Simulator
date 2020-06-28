package org.techtown.capstoneprojectcocktail;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CocktailCommentAdapter extends RecyclerView.Adapter<CocktailCommentAdapter.ViewHolder> implements OnCocktailCommentItemClickListener {
    ArrayList<Comment> items = new ArrayList<Comment>();
    ArrayList<Comment> items_buffer = new ArrayList<Comment>();
    OnCocktailCommentItemClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textForCommentWriter;
        TextView textForComment;
        TextView textForCommentDate;
        ImageView imageForWriter;

        public ViewHolder (View itemView, final  OnCocktailCommentItemClickListener listener){
            super(itemView);

            textForCommentWriter=itemView.findViewById(R.id.textView_writer_recipe_cardView);
            textForComment=itemView.findViewById(R.id.textView_commentText_recipe_cardView);
            textForCommentDate=itemView.findViewById(R.id.textView_uploadDate_recipe_cardView);
            imageForWriter=itemView.findViewById(R.id.profileImageView_comment_recipe_cardView);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(CocktailCommentAdapter.ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(Comment item) {
            /*
            FirebaseStorage storage = FirebaseStorage.getInstance();

            StorageReference gsReference = storage.getReferenceFromUrl(item.getImageUrl());
            gsReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        // Glide 이용하여 이미지뷰에 로딩
                        Glide.with(itemView)
                                .load(task.getResult())
                                .into(imageForCocktail);
                    } else {
                        // URL을 가져오지 못하면 토스트 메세지
                    }
                }
            });
            textForCocktailName.setText(item.getName());
            textForCocktailDescription.setText(item.getDescription());
            textForCocktailABV.setText(item.getAbvNum());
            //Glide.with(itemView).load(imageUrl).into(imageForCocktail);
             */
            final String imageUrl = item.getImageUrl();
            //임시용 수정필
            //임시용 수정필
            //임시용 수정필
            if (imageUrl!="hi") {
                final Bitmap[] bitmap = new Bitmap[1];
                Thread mThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(imageUrl);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true);
                            conn.connect();
                            InputStream is = conn.getInputStream();
                            bitmap[0] = BitmapFactory.decodeStream(is);
                        } catch (MalformedURLException ee) {
                            ee.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                mThread.start();
                try {
                    mThread.join();
                    imageForWriter.setImageBitmap(bitmap[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else{
                imageForWriter.setImageResource(R.mipmap.ic_launcher_round);
            }
            //임시용 수정필
            //임시용 수정필
            //임시용 수정필
            textForCommentWriter.setText(item.getName());
            textForComment.setText(item.getContents());
            textForCommentDate.setText(item.getDate());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.cocktail_comment_listview, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CocktailCommentAdapter.ViewHolder viewHolder, int position) {
        Comment item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Comment item){
        items.add(item);
    }

    public void removeItem(int position){
        items.remove(items.get(position));
    }

    public void setItems(ArrayList<Comment> items){
        this.items = items;
    }

    public Comment getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, Comment item){
        items.set(position, item);
    }

    public void setOnItemClickListener(OnCocktailCommentItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(CocktailCommentAdapter.ViewHolder holder, View view, int position){
        if(listener!=null){
            listener.onItemClick(holder,view,position);
        }
    }

    /*
    public void filterForIngredient(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        items.clear();
        if (charText.length() == 0) {
            items.addAll(items_buffer);
        } else {
            for (Comment potion : items_buffer) {
                if (potion.getName().contains(charText)||potion.getIngredient().contains(charText)) {
                    items.add(potion);
                }
            }
        }
        notifyDataSetChanged();
    }
     */

    public void clearAllForAdapter(){
        items.clear();
        notifyDataSetChanged();
    }
}
