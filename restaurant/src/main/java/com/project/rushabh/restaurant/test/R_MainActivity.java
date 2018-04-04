package com.project.rushabh.restaurant.test;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.rushabh.restaurant.R;

import java.util.HashMap;
import java.util.Map;

public class R_MainActivity extends AppCompatActivity {

    private TextInputEditText restaurantName, itemId, categoryId, subCategoryId, itemName, itemPrice, itemImage;
    private Map<String, Object> item;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r_activity_main);

        restaurantName = findViewById(R.id.textInputEditText_restaurant_name);
        itemId = findViewById(R.id.textInputEditText_item_id);
        categoryId = findViewById(R.id.textInputEditText_category_id);
        subCategoryId = findViewById(R.id.textInputEditText_subcategory_id);
        itemName = findViewById(R.id.textInputEditText_item_name);
        itemPrice = findViewById(R.id.textInputEditText_item_price);
        itemImage = findViewById(R.id.textInputEditText_item_image);

        item = new HashMap<>();
        db = FirebaseFirestore.getInstance();
    }

    public void onAddItemClick(View view) {
        item.put("id", Integer.parseInt(itemId.getText().toString()));
        item.put("categoryId", Integer.parseInt(categoryId.getText().toString()));
        item.put("subCategoryId", Integer.parseInt(subCategoryId.getText().toString()));
        item.put("name", itemName.getText().toString());
        item.put("price", Double.parseDouble(itemPrice.getText().toString()));
        item.put("image", itemImage.getText().toString());

        db.collection("restaurants").document("BJSdynFnNrQbGXmX7iMp").collection("items")
                .add(item)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(R_MainActivity.this, documentReference.getId(), Toast.LENGTH_SHORT).show();
                    }
                });

        /*db.collection("restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Toast.makeText(MainActivity.this, document.getString("name"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });*/
    }
}
