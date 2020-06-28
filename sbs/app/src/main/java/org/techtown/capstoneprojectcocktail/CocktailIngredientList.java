package org.techtown.capstoneprojectcocktail;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CocktailIngredientList {
    public CocktailIngredient[] Ingredient_list;
    FirebaseFirestore db;

    public CocktailIngredientList(
            final CocktailIngredient[] Ingredient_list
           ) {
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef;

        for(int i=1; i < 128; i++)
        {
            docRef = db.collection("Ingredient").document(String.valueOf(i+5000));
            final int finalI = i;
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Ingredient_list[finalI] = documentSnapshot.toObject(CocktailIngredient.class);
                }
            });
        }
        this.Ingredient_list = Ingredient_list;
    }
    public CocktailIngredient[] getIngredient_list(int number) {

        return getIngredient_list(number);
    }
}


