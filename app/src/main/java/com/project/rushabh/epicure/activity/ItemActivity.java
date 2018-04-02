package com.project.rushabh.epicure.activity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.rushabh.epicure.R;
import com.project.rushabh.epicure.adapter.CategoryPagerAdapter;
import com.project.rushabh.epicure.adapter.ItemAdapter;
import com.project.rushabh.epicure.adapter.OrderAdapter;
import com.project.rushabh.epicure.model.Category;
import com.project.rushabh.epicure.model.Item;
import com.project.rushabh.epicure.model.Order;
import com.project.rushabh.epicure.model.Solution;
import com.project.rushabh.epicure.model.SubCategory;
import com.project.rushabh.epicure.util.CircleAnimationUtil;
import com.steelkiwi.library.IncrementProductView;
import com.steelkiwi.library.listener.OnStateListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemActivity extends AppCompatActivity implements ItemAdapter.IItemAdapterCallback,
        OrderAdapter.IOrderAdapterCallback {

    private DrawerLayout drawer;
    private ConstraintLayout rlCart;
    private TextView txtCount;
    private TextView txtTotal;
    private RecyclerView rvOrder;
    private TextView txtClearAll;
    private Button btnCompleteOrder;
    private ProgressDialog dialog;

    private OrderAdapter orderAdapter;

    private Intent intent;
    private String placeId;
    private FirebaseFirestore db;
    private DocumentReference documentReference;

    /*
    * Holds all categories
    */
    private ArrayList<Category> categoryList;
    /*
    * Holds all sub-categories
    */
    private ArrayList<SubCategory> subCategoryList;
    /*
    * Holds all items
    */
    private ArrayList<Item> itemList;
    /*
    * Holds all solutions
    */
    private ArrayList<Solution> solutionList;
    /*
    * Holds the data that are added to cart
    */
    private ArrayList<Order> orderList;

    /**
     * The callback to increase or decrease the quantity
     * of the related item at cart.
     */
    @Override
    public void onIncreaseDecreaseCallback() {
        updateOrderTotal();
        updateBadge();
    }

    /**
     * The callback to see the detail of the item.
     */
    @Override
    public void onItemCallback(Item item) {
        dialogItemDetail(item);
    }

    /**
     * The callback to add item to cart with animation.
     */
    @Override
    public void onAddItemCallback(ImageView imageView, Item item) {
        addItemToCartAnimation(imageView, item, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        intent = getIntent();
        placeId = intent.getStringExtra("restaurant_id");
        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("restaurants").document(placeId);
        //Toast.makeText(this, intent.getStringExtra("restaurant_id"), Toast.LENGTH_SHORT).show();

        prepareData();
        Log.d("order of the calls", "oncreate after preparedata");

        // Find views
        drawer = findViewById(R.id.drawerLayout_cart);
        txtTotal = findViewById(R.id.text_total);
        rvOrder = findViewById(R.id.recyclerView_cart_order);
        txtClearAll = findViewById(R.id.text_cart_clear);
        btnCompleteOrder = findViewById(R.id.btn_cart_complete_order);

        // set
        rvOrder.setLayoutManager(new LinearLayoutManager(ItemActivity.this));
        orderAdapter = new OrderAdapter(ItemActivity.this, orderList);
        rvOrder.setAdapter(orderAdapter);

        // Get the ViewPager and set it's CategoryPagerAdapter so that it can display items
        ViewPager vpItem = findViewById(R.id.viewPager_item);
        CategoryPagerAdapter categoryPagerAdapter = new CategoryPagerAdapter(getSupportFragmentManager(), ItemActivity.this, solutionList);
        vpItem.setOffscreenPageLimit(categoryPagerAdapter.getCount());
        vpItem.setAdapter(categoryPagerAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabCategory = findViewById(R.id.tab_category);
        tabCategory.setupWithViewPager(vpItem);

        for (int i = 0; i < tabCategory.getTabCount(); i++) {
            TabLayout.Tab tab = tabCategory.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(categoryPagerAdapter.getTabView(i));
            }
        }

        btnCompleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderList.size() > 0) {
                    dialogCompleteOrder();
                }
            }
        });

        txtClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderList.size() > 0) {
                    dialogClearAll();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cart, menu);

        final View actionCart = menu.findItem(R.id.actionCart).getActionView();

        txtCount = actionCart.findViewById(R.id.txtCount);
        rlCart = actionCart.findViewById(R.id.rlCart);

        rlCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleOrderDrawer();
            }
        });
        return true;
    }

    /*
    * Prepares sample data to be used within the application
    */
    @SuppressWarnings("StatementWithEmptyBody") //TODO: revie and find better practice, Refer to TestData.class for original code
    private void prepareData() {
        categoryList = new ArrayList<>();
        subCategoryList = new ArrayList<>();
        itemList = new ArrayList<>();
        solutionList = new ArrayList<>();
        orderList = new ArrayList<>();

        Task<QuerySnapshot> categoryTask = documentReference.collection("category").get();
        Log.d("order of the calls", "before category");
        while (!categoryTask.isSuccessful()) {
            //do nothing
        }
        for (QueryDocumentSnapshot document : categoryTask.getResult()) {
            //itemNameList.add(document.getString("name"));
            Log.d("order of the calls", document.getString("name"));
            categoryList.add(new Category((int) ((long) document.get("id")),
                    document.getString("name"), R.drawable.all));
        }

        Task<QuerySnapshot> subCategoryTask = documentReference.collection("subcategories").get();
        while (!subCategoryTask.isSuccessful()) {
            //do nothing
        }
        for (QueryDocumentSnapshot document : subCategoryTask.getResult()) {
            Log.d("order of the calls", document.getString("name"));
            //subCategoryList.add(new SubCategory(1, 2, "Hamburger"));
            subCategoryList.add(new SubCategory((int) ((long) document.get("id")),
                    (int) ((long) document.get("categoryId")),
                    document.getString("name")));
        }

        Task<QuerySnapshot> itemTask = documentReference.collection("items").get();
        while (!itemTask.isSuccessful()) {
            //do nothing
        }
        for (QueryDocumentSnapshot document : itemTask.getResult()) {
            Log.d("order of the calls", "items");
            itemList.add(new Item((int) ((long) document.get("id")),
                    (int) ((long) document.get("categoryId")),
                    (int) ((long) document.get("subCategoryId")),
                    document.getString("name"),
                    document.getDouble("price"),
                    document.getString("image")));
        }

        Log.d("order of the calls", "for loop entrace");
        for (Category categoryItem : categoryList) {
            // Temporary list of the current sıb-categories
            ArrayList<SubCategory> tempSubCategoryList;
            Log.d("order of the calls", "for loop");

            // Temporary list of the current items
            ArrayList<Item> tempItemList;

            // Temporary map
            Map<SubCategory, ArrayList<Item>> itemMap;

            // categoryId == 1 means category with all items and sub-categories.
            // That's why i add all the sub-categories and items directly.
            if (categoryItem.id == 1) {
                itemMap = getItemMap(subCategoryList);

                solutionList.add(new Solution(categoryItem, subCategoryList, itemList, itemMap));
            } else {
                tempSubCategoryList = getSubCategoryListByCategoryId(categoryItem.id);
                tempItemList = getItemListByCategoryId(categoryItem.id);
                itemMap = getItemMap(tempSubCategoryList);

                solutionList.add(new Solution(categoryItem, tempSubCategoryList, tempItemList, itemMap));
            }
        }
    }

    /**
     * Gets the sub-categories belonging to a category
     *
     * @param categoryId The id of the current category.
     *                   <p>
     *                   Returns the sub-categories belonging to that category as a list using the id of the current category.
     * @return a list of sub-category
     */
    private ArrayList<SubCategory> getSubCategoryListByCategoryId(int categoryId) {
        ArrayList<SubCategory> tempSubCategoryList = new ArrayList<>();

        for (SubCategory subCategory : subCategoryList) {
            if (subCategory.categoryId == categoryId) {
                tempSubCategoryList.add(new SubCategory(subCategory));
            }
        }
        return tempSubCategoryList;
    }

    /**
     * Gets the items belonging to a category
     *
     * @param categoryId The id of the current category.
     *                   <p>
     *                   Returns the items belonging to that category as a list using the id of the current category.
     * @return a list of items
     */
    private ArrayList<Item> getItemListByCategoryId(int categoryId) {
        ArrayList<Item> tempItemList = new ArrayList<>();

        for (Item item : itemList) {
            if (item.categoryId == categoryId) {
                tempItemList.add(item);
            }
        }
        return tempItemList;
    }

    /**
     * Gets the items belonging to a sub-category
     *
     * @param subCategoryId The id of the current sub-category.
     *                      <p>
     *                      Returns the item belonging to that sub-category as a list
     *                      using the id of the current sub-category.
     * @return a list of items
     */
    private ArrayList<Item> getItemListBySubCategoryId(int subCategoryId) {
        ArrayList<Item> tempItemList = new ArrayList<>();

        for (Item item : itemList) {
            if (item.subCategoryId == subCategoryId) {
                tempItemList.add(item);
            }
        }
        return tempItemList;
    }

    /**
     * Gets a Map which has the key is the sub-category
     * and the value is the items of that sub-category.
     * That Map will also be used in the Sectioned RecyclerView.
     *
     * @param subCategoryList sub-categories which is owned by a category
     * @return a Map
     */
    private Map<SubCategory, ArrayList<Item>> getItemMap(ArrayList<SubCategory> subCategoryList) {
        Map<SubCategory, ArrayList<Item>> itemMap = new HashMap<>();

        for (SubCategory subCategory : subCategoryList) {
            itemMap.put(subCategory, getItemListBySubCategoryId(subCategory.id));
        }
        return itemMap;
    }

    /*
    * Shows the detail of item
    */
    @SuppressLint("DefaultLocale")
    private void dialogItemDetail(final Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ItemActivity.this);
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.dialog_item_detail, null);

        final IncrementProductView incrementProductView = view.findViewById(R.id.productView);
        TextView txtItemName = view.findViewById(R.id.txtItemName);
        final TextView txtUnitPrice = view.findViewById(R.id.txtUnitPrice);
        final TextView txtExtendedPrice = view.findViewById(R.id.txtExtendedPrice);
        final TextView txtQuantity = view.findViewById(R.id.txtQuantity);
        final ImageView imgThumbnail = view.findViewById(R.id.imgThumbnail);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnOk = view.findViewById(R.id.btnOk);

        txtItemName.setText(item.name);
        txtUnitPrice.setText(String.format("%.2f", item.unitPrice));
        txtQuantity.setText("1");
        txtExtendedPrice.setText(String.format("%.2f", item.unitPrice * 1));

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null)
            dialog.getWindow().getAttributes().windowAnimations = R.style.DetailDialogAnimation;
        dialog.show();

        Glide.with(ItemActivity.this)
                .load(item.url)
                .into(imgThumbnail);

        incrementProductView.getAddIcon();

        incrementProductView.setOnStateListener(new OnStateListener() {
            @Override
            public void onCountChange(int count) {
                txtQuantity.setText(String.valueOf(count));
                txtExtendedPrice.setText(String.format("%.2f", item.unitPrice * count));
            }

            @Override
            public void onConfirm(int count) {

            }

            @Override
            public void onClose() {

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemToCartAnimation(imgThumbnail, item, Integer.parseInt(txtQuantity.getText().toString()));

                dialog.dismiss();
            }
        });
    }

    /**
     * Animates when item is added to the cart.
     * The animation will start at item's ImageView,
     * continue up to cart MenuItem at ToolBar.
     * This will take 0.3 seconds. When the animation is over,
     * the item will be added to the cart in quantities.
     *
     * @param targetView targetView description to add
     * @param item       element wanted to add to cart
     * @param quantity   the amount of how many item you want to add to cart
     * @see CircleAnimationUtil
     * @see com.project.rushabh.epicure.util.CircleImageView
     */
    private void addItemToCartAnimation(ImageView targetView, final Item item, final int quantity) {
        ConstraintLayout destView = findViewById(R.id.rlCart);

        new CircleAnimationUtil().attachActivity(this).setTargetView(targetView).setMoveDuration(300).setDestView(destView).setAnimationListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                addItemToCart(item, quantity);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        }).startAnimation();
    }

    /*
    * Adds the item to order list.
    */
    private void addItemToCart(Item item, int quantity) {
        boolean isAdded = false;

        for (Order order : orderList) {
            if (order.item.id == item.id) {
                //if item already added to cart, dont add new order
                //just add the quantity
                isAdded = true;
                order.quantity += quantity;
                order.extendedPrice += item.unitPrice;
                break;
            }
        }

        //if item's not added yet
        if (!isAdded) {
            orderList.add(new Order(item, quantity));
        }

        orderAdapter.notifyDataSetChanged();
        rvOrder.smoothScrollToPosition(orderList.size() - 1);
        updateOrderTotal();
        updateBadge();
    }

    /*
    * Updates the value of the badge
    */
    private void updateBadge() {
        if (orderList.size() == 0) {
            txtCount.setVisibility(View.INVISIBLE);
        } else {
            txtCount.setVisibility(View.VISIBLE);
            txtCount.setText(String.valueOf(orderList.size()));
        }
    }

    /*
    * Gets the total price of all products added to the cart
    */
    private double getOrderTotal() {
        double total = 0.0;

        for (Order order : orderList) {
            total += order.extendedPrice;
        }
        return total;
    }

    /*
    * Updates the total price of all products added to the cart
    */
    @SuppressLint("DefaultLocale")
    private void updateOrderTotal() {
        double total = getOrderTotal();
        txtTotal.setText(String.format("%.2f", total));
    }

    /*
    * Closes or opens the drawer
    */
    private void handleOrderDrawer() {
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            } else {
                drawer.openDrawer(GravityCompat.END);
            }
        }
    }

    /*
    * Makes the cart empty
    */
    private void dialogClearAll() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(ItemActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(ItemActivity.this);
        }
        builder.setTitle(R.string.cart_clear_all)
                .setMessage(R.string.delete_all_orders)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        clearAll();
                        showMessage(true, getString(R.string.cart_clean));
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    /*
    * Completes the order
    */
    private void dialogCompleteOrder() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(ItemActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(ItemActivity.this);
        }
        builder.setTitle(getString(R.string.cart_complete_order))
                .setMessage(getString(R.string.complete_order_question))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showProgress(true);
                        new CompleteOrderTask().execute();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    /**
     * Represents an asynchronous task used to complete
     * the order.
     */
    @SuppressLint("StaticFieldLeak")
    public class CompleteOrderTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);
            clearAll();
            showMessage(true, getString(R.string.sent_order));
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
            showMessage(false, getString(R.string.failed_order));
        }
    }

    /**
     * Shows the progress
     */
    private void showProgress(boolean show) {
        if (dialog == null) {
            dialog = new ProgressDialog(ItemActivity.this);
            dialog.setMessage(getString(R.string.sending_order));
        }

        if (show) {
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }

    /*
    * Clears all orders from the cart
    */
    private void clearAll() {
        orderList.clear();
        orderAdapter.notifyDataSetChanged();

        updateBadge();
        updateOrderTotal();
        handleOrderDrawer();
    }

    /*
    * Shows a message by using Snackbar
    */
    private void showMessage(Boolean isSuccessful, String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);

        if (isSuccessful) {
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(ItemActivity.this, R.color.colorAccent));
        } else {
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(ItemActivity.this, R.color.colorPomegranate));
        }

        snackbar.show();
    }
}