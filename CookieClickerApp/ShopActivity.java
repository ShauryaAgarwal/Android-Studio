package com.example.cookieclicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ShopActivity extends AppCompatActivity {

    ConstraintLayout shopConstraintLayout;
    ImageView cookiePageImage, shopPageImage, itemsPageImage;
    TextView cookiePageName, shopPageName, itemsPageName, shopCookieCounter;
    static int totalCookiesPerSecond = 0;
    ListView shopListView;
    ItemAdapter itemTypes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoplayout);

        shopConstraintLayout = findViewById(R.id.id_shopConstraintLayout);
        shopListView = findViewById(R.id.id_shopListView);

        cookiePageName = findViewById(R.id.id_cookiePageIconTextView2);
        shopPageName = findViewById(R.id.id_shopPageIconTextView2);
        itemsPageName = findViewById(R.id.id_itemsPageIconTextView2);
        shopCookieCounter = findViewById(R.id.id_shopDisplayCookieCountTextView);

        cookiePageImage = findViewById(R.id.id_cookiePageIconImageView2);
        cookiePageImage.setImageResource(R.drawable.smallcookieicon);
        shopPageImage = findViewById(R.id.id_shopPageIconImageView2);
        shopPageImage.setImageResource(R.drawable.smallshopicon);
        itemsPageImage = findViewById(R.id.id_itemsPageIconImageView2);
        itemsPageImage.setImageResource(R.drawable.smallitemsicon);

        itemTypes = new ItemAdapter(this, R.layout.shopitem, CookieActivity.itemList);
        shopListView.setAdapter(itemTypes);

        shopCookieCounter.setText("Cookies: " + CookieActivity.cookieCountTotal.intValue());

        cookiePageImage.setOnClickListener(v -> {
            Intent intentShop1 = new Intent(ShopActivity.this, CookieActivity.class);
            //intentShop1.putExtra("ITEM_LIST_FROM_SHOP", itemList);
            //startActivity(intentShop1);
            finish();
        });

        itemsPageImage.setOnClickListener(v -> {
            Intent intentShop2 = new Intent(ShopActivity.this, ItemsActivity.class);
            //intentShop2.putExtra("ITEM_LIST", itemList);
            finish();
            startActivity(intentShop2);
        });

        Thread updateShopCookieCounter = new Thread(new Runnable() {
            Handler shopCookieCounterHandler = new Handler();
            @Override
            public void run() {
                while(true)
                {
                    try {
                        Thread.sleep(1000);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    shopCookieCounterHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            for(int j = 0; j < CookieActivity.itemList.size(); j++)
                            {
                                if(CookieActivity.itemList.get(j).itemCost < CookieActivity.cookieCountTotal.intValue())
                                {
                                    itemTypes.notifyDataSetChanged();
                                }
                            }
                            shopCookieCounter.setText("Cookies: " + CookieActivity.cookieCountTotal.intValue());
                        }
                    });
                }
            }
        });

        updateShopCookieCounter.start();
    }

    Thread passiveIncome = new Thread(new Runnable() {
        @Override
        public void run() {
            Log.d("TAGA", "TEST");
            while(CookieActivity.totalNumShopItems > 0)
            {
                totalCookiesPerSecond = 0;
                for(int i = 0; i < CookieActivity.itemList.size(); i++)
                {
                    totalCookiesPerSecond += (CookieActivity.itemList.get(i).numItemOwned)*((int)CookieActivity.itemList.get(i).cookiesPerSecond);
                    //Log.d("TAGA", "" + CookieActivity.itemList.get(i).itemName);
                }
                //Log.d("TAGA", "Total Cookies per Second: " + totalCookiesPerSecond);
                CookieActivity.cookieCountTotal.getAndAdd(totalCookiesPerSecond);
                try {
                    Thread.sleep(1000);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    });

    public class ItemAdapter extends ArrayAdapter<ShopItem>
    {
        Context context;
        List<ShopItem> list;
        int xml;
        View itemLayout;

        public ItemAdapter(@NonNull Context context, int resource, @NonNull List<ShopItem> objects)
        {
            super(context, resource, objects);
            this.context = context;
            this.list = objects;
            this.xml = resource;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if ((int) (CookieActivity.itemList.get(position).itemCost) > CookieActivity.cookieCountTotal.intValue())
            {
                itemLayout = new Space(context);
            }
            else
            {
                itemLayout = layoutInflater.inflate(xml, null);

                ImageView itemAdapterImage = itemLayout.findViewById(R.id.id_shopItemImageView);
                TextView itemAdapterName = itemLayout.findViewById(R.id.id_shopItemNameTextView);
                TextView itemAdapterCost = itemLayout.findViewById(R.id.id_shopItemCostTextView);
                TextView numAdapterItemOwned = itemLayout.findViewById(R.id.id_shopItemNumOwnedTextView);
                TextView itemCookiesPerSecond = itemLayout.findViewById(R.id.id_shopItemCookiesPerSecondTextView);
                Button buyItem = itemLayout.findViewById(R.id.id_shopBuyButton);

                Log.d("TAGA", "item list printed");

                itemAdapterImage.setImageResource((CookieActivity.itemList.get(position)).itemImage);
                itemAdapterName.setText((CookieActivity.itemList.get(position)).itemName);
                itemAdapterCost.setText("Cost: " + (int)(CookieActivity.itemList.get(position).itemCost) + " Cookies");
                numAdapterItemOwned.setText((CookieActivity.itemList.get(position)).numItemOwned + " Owned");
                itemCookiesPerSecond.setText((int)(CookieActivity.itemList.get(position).cookiesPerSecond) + " CpS");

                buyItem.setOnClickListener(v -> {
                    CookieActivity.cookieCountTotal.getAndAdd((-1)*(int)(CookieActivity.itemList.get(position).itemCost));
                    CookieActivity.itemList.get(position).numItemOwned++;
                    CookieActivity.itemList.get(position).cookiesPerSecond*=1.05;
                    CookieActivity.itemList.get(position).itemCost*=1.2;
                    CookieActivity.totalNumShopItems++;
                    shopCookieCounter.setText("Cookies: " + CookieActivity.cookieCountTotal.intValue());
                    itemTypes.notifyDataSetChanged();
                    if(CookieActivity.totalNumShopItems == 1) {
                        passiveIncome.start();
                        Log.d("TAGA", "TOTALNUMITEMSCHECK");
                    }
                    CookieActivity.itemDisplayList.get(position).numItems++;
                });
            }

            return itemLayout;
        }
    }

    public static class ShopItem implements Serializable
    {
        int itemImage, numItemOwned;
        double itemCost, cookiesPerSecond;
        String itemName;

        public ShopItem(int image, int numOwn, String name, double cost, double cps)
        {
            itemImage = image;
            numItemOwned = numOwn;
            itemName = name;
            itemCost = cost;
            cookiesPerSecond = cps;
        }
    }
}
