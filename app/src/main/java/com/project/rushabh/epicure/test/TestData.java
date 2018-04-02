package com.project.rushabh.epicure.test;

/**
 * Created by rushabh.modi on 02/04/18.
 */

public class TestData {

    /** Refers to ItemActivity
     *  For category, subcategory and items static data
     */

    /*categoryList.add(new Category(1, "All", R.drawable.all));

        categoryList.add(new Category(2, "Food", R.drawable.food));
        subCategoryList.add(new SubCategory(1, 2, "Hamburger"));
        subCategoryList.add(new SubCategory(2, 2, "Pizza"));
        subCategoryList.add(new SubCategory(3, 2, "Kebab"));
        subCategoryList.add(new SubCategory(4, 2, "Dessert"));

        categoryList.add(new Category(3, "Drinks", R.drawable.drink));
        subCategoryList.add(new SubCategory(5, 3, "Water"));
        subCategoryList.add(new SubCategory(6, 3, "Soda"));
        subCategoryList.add(new SubCategory(7, 3, "Tea"));
        subCategoryList.add(new SubCategory(8, 3, "Coffee"));
        subCategoryList.add(new SubCategory(9, 3, "Fruit Juice"));

        //Hamburger
        itemList.add(new Item(1, 2, 1, "Ultimate Hamburger", 5.0, "https://assets.epicurious.com/photos/57c5c6d9cf9e9ad43de2d96e/master/pass/the-ultimate-hamburger.jpg"));
        itemList.add(new Item(2, 2, 1, "Double Cheese Burger", 7.0, "https://1.bp.blogspot.com/-i2e3XPfVwYw/V9GgRgn2Y3I/AAAAAAAAxeM/Ih2LoXrSQr0NBgFKLeupxYNzwGZXBv1VwCLcB/s1600/Hardees-Classic-Double-Cheeseburger.jpg"));

        //Pizza
        itemList.add(new Item(3, 2, 2, "Pepperoni", 10.99, "https://www.cicis.com/media/1138/pizza_trad_pepperoni.png"));
        itemList.add(new Item(4, 2, 2, "Mixed Pizza", 11.20, "http://icube.milliyet.com.tr/YeniAnaResim/2016/10/24/tavada-pizza-tarifi-7857137.Jpeg"));
        itemList.add(new Item(5, 2, 2, "Neapolitan Pizza", 12.10, "https://img.grouponcdn.com/deal/2SVzinBnAH17zHrq3HNpurto2gpK/2S-700x420/v1/c700x420.jpg"));

        //Kebab
        itemList.add(new Item(6, 2, 3, "Garlic-Tomato Kebab", 16.60, "http://www.seriouseats.com/recipes/assets_c/2016/08/20160703-Grilled-Lemon-Garlic-Chicken-Tomato-Kebabs-Basil-Chimichurri-emily-matt-clifton-7-thumb-1500xauto-433447.jpg"));
        itemList.add(new Item(7, 2, 3, "Adana Kebab", 15.0, "http://www.muslumkebap.com/Contents/Upload/MuslumAdana_qyz0b34a.p3w.jpg"));
        itemList.add(new Item(8, 2, 3, "Eggplant Kebab", 18.0, "http://www.muslumkebap.com/Contents/Upload/MuslumPatlicanKebap_y41olk55.kpc.jpg"));
        itemList.add(new Item(9, 2, 3, "Shish Kebab", 10.0, "http://2.bp.blogspot.com/-4_8T6g3qOCU/VVz3oASO5lI/AAAAAAAAUHo/rEw1XlZoxqQ/s1600/Shish.jpg"));

        //Dessert
        itemList.add(new Item(10, 2, 4, "Pistachio Baklava", 10.0, "http://www.karakoygulluoglu.com/fistikli-kare-baklava-32-15-B.jpg"));
        itemList.add(new Item(11, 2, 4, "Palace Pistachio Baklava", 12.50, "http://www.karakoygulluoglu.com/fistikli-havuc-dilim-baklava-1kg-31-14-B.jpg"));
        itemList.add(new Item(12, 2, 4, "Mixed Baklava in Tray", 60.0, "http://www.karakoygulluoglu.com/karisik-baklava-1-tepsi-66-31-B.jpg"));
        itemList.add(new Item(13, 2, 4, "Tel Kadayıf with Pistachio", 9.0, "http://www.karakoygulluoglu.com/fistikli-tel-kadayif-1-39-44-B.jpg"));

        //Water
        itemList.add(new Item(14, 3, 5, "0.5 liter", 300000, "http://cdn.avansas.com/assets/59479/erikli-su-0-5-lt-12-li-1-zoom.jpg"));

        //Soda
        itemList.add(new Item(15, 3, 6, "Coca Cola 0.3l", 0.70, "https://images-na.ssl-images-amazon.com/images/I/818i%2BQm07UL._SL1500_.jpg"));
        itemList.add(new Item(16, 3, 6, "Coca Cola 0.2l", 1.00, "https://images-na.ssl-images-amazon.com/images/I/511rL-aYd9L._SL1000_.jpg"));
        itemList.add(new Item(17, 3, 6, "Fanta 0.3l", 0.70, "http://www.germandeli.com/product-images/Fanta-Orange-Orange-Soda-33l_main-1.jpg"));
        itemList.add(new Item(18, 3, 6, "Fanta 0.5l", 1.10, "http://www.germandeli.com/product-images/Fanta-Orange-0-5-liter-bottle_main-1.jpg"));
        itemList.add(new Item(19, 3, 6, "Sprite 0.5l", 1.10, "https://cdn.grofers.com/app/images/products/full_screen/pro_312.jpg"));

        //Tea
        itemList.add(new Item(20, 3, 7, "Turkish Tea", 20.0, "https://images.hepsiburada.net/assets/Taris/500/Taris_3092120.jpg"));

        //Coffee
        itemList.add(new Item(21, 3, 8, "Turkish Coffee", 20.0, "https://cdn3.volusion.com/p3y5v.vg2ps/v/vspfiles/photos/TCW-XME02-2.jpg?1500568557"));

        //Fruit Juice
        itemList.add(new Item(22, 3, 9, "Mixed Fruit Juice", 1.0, "https://images.hepsiburada.net/assets/Taris/500/Taris_4791763.jpg"));
        itemList.add(new Item(23, 3, 9, "Apricot Fruit Juice", 1.0, "http://cdn.avansas.com/assets/53313/cappy-meyve-suyu-kayisi-1-lt-0-zoom.jpg"));
        itemList.add(new Item(24, 3, 9, "Orange Fruit Juice", 1.0, "https://images.hepsiburada.net/assets/Taris/500/Taris_4791980.jpg"));*/

    /**
     *  For getting from database on listener
     */

     /*taskCategory.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //itemNameList.add(document.getString("name"));
                        Log.d("order of the calls", "category");
                        categoryList.add(new Category((int) ((long) document.get("id")), document.getString("name"), R.drawable.all));
                        Toast.makeText(ItemActivity.this, Integer.toString((int) ((long) document.get("id"))), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });*/

        /*db.collection("restaurants").document(placeId).collection("subcategories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //itemNameList.add(document.getString("name"));
                                //categoryList.add(new Category((int) ((long) document.get("id")), document.getString("name"), R.drawable.all));
                                Log.d("order of the calls", "subcategories");
                                subCategoryList.add(new SubCategory((int) ((long) document.get("id")), (int) ((long) document.get("categoryId")), document.getString("name")));
                                Toast.makeText(ItemActivity.this, document.getString("name"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });*/

        /*db.collection("restaurants").document(placeId).collection("items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //itemNameList.add(document.getString("name"));
                                //itemNameList.add(document.getString("name"));
                                Log.d("order of the calls", "items");
                                itemList.add(new Item(1, 2, 1, "Ultimate Hamburger", 5.0, "https://assets.epicurious.com/photos/57c5c6d9cf9e9ad43de2d96e/master/pass/the-ultimate-hamburger.jpg"));
                                Toast.makeText(ItemActivity.this, document.getString("name"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });*/

        /*
        * Populates solutionList.
        * For each category, gets sub-categories, items and a map
        * showing the connection between the sub-category and its items.
        */
        /*for (Category categoryItem : categoryList) {
            // Temporary list of the current sıb-categories
            ArrayList<SubCategory> tempSubCategoryList;

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
        }*/
}
