package com.example.cookieclicker;

import android.app.Activity;
import android.content.ClipData;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

public class ItemsActivity extends AppCompatActivity {

    static ConstraintLayout itemsConstraintLayout;
    ImageView cookiePageImage, shopPageImage, itemsPageImage;
    TextView cookiePageName, shopPageName, itemsPageName, itemsCookieCounter;
    ListView itemsListView;
    ItemDisplayAdapter itemDisplayTypes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemslayout);

        itemsConstraintLayout = findViewById(R.id.id_itemsConstraintLayout);
        itemsListView = findViewById(R.id.id_itemDisplayListView);

        cookiePageName = findViewById(R.id.id_cookiePageIconTextView3);
        shopPageName = findViewById(R.id.id_shopPageIconTextView3);
        itemsPageName = findViewById(R.id.id_itemsPageIconTextView3);
        itemsCookieCounter = findViewById(R.id.id_itemsDisplayCookieCountTotalTextView);

        cookiePageImage = findViewById(R.id.id_cookiePageIconImageView3);
        cookiePageImage.setImageResource(R.drawable.smallcookieicon);
        shopPageImage = findViewById(R.id.id_shopPageIconImageView3);
        shopPageImage.setImageResource(R.drawable.smallshopicon);
        itemsPageImage = findViewById(R.id.id_itemsPageIconImageView3);
        itemsPageImage.setImageResource(R.drawable.smallitemsicon);

        itemsCookieCounter.setText("Cookies: " + CookieActivity.cookieCountTotal.intValue());

        itemDisplayTypes = new ItemDisplayAdapter(this, R.layout.itemdisplay, CookieActivity.itemDisplayList);
        itemsListView.setAdapter(itemDisplayTypes);

        cookiePageImage.setOnClickListener(v -> {
            Intent intentItems1 = new Intent(ItemsActivity.this, CookieActivity.class);
            //startActivity(intentItems1);
            finish();
        });

        shopPageImage.setOnClickListener(v -> {
            Intent intentItems2 = new Intent(ItemsActivity.this, ShopActivity.class);
            finish();
            startActivity(intentItems2);
        });

        Thread updateCookieCounter = new Thread(new Runnable() {
            Handler itemsCookieCounterHandler = new Handler();
            @Override
            public void run() {
                while(true)
                {
                    try {
                        Thread.sleep(1000);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    itemsCookieCounterHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            itemsCookieCounter.setText("Cookies: " + CookieActivity.cookieCountTotal.intValue());
                        }
                    });
                }
            }
        });

        updateCookieCounter.start();
    }

    public class ItemDisplayAdapter extends ArrayAdapter<ItemDisplay>
    {
        Context context;
        List<ItemDisplay> list;
        int xml;
        View itemDisplayLayout;

        public ItemDisplayAdapter(@NonNull Context context, int resource, @NonNull List<ItemDisplay> objects)
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

            itemDisplayLayout = layoutInflater.inflate(xml, null);

            TextView itemNameTextView = itemDisplayLayout.findViewById(R.id.id_itemsDisplayItemName);
            itemNameTextView.setText(CookieActivity.itemDisplayList.get(position).itemName);
            ConstraintLayout individualItemConstraintLayout = itemDisplayLayout.findViewById(R.id.id_singleItemDisplayLayout);

            for(int i = 0; i < CookieActivity.itemDisplayList.get(position).numItems; i++)
            {
                ImageView itemIconImageView = new ImageView(ItemsActivity.this);
                itemIconImageView.setId(View.generateViewId());
                itemIconImageView.setImageResource((CookieActivity.itemList.get(position)).itemImage);
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(75, 75);
                itemIconImageView.setLayoutParams(params);
                individualItemConstraintLayout.addView(itemIconImageView);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(individualItemConstraintLayout);
                constraintSet.connect(itemIconImageView.getId(), ConstraintSet.TOP, itemNameTextView.getId(), ConstraintSet.BOTTOM);
                constraintSet.connect(itemIconImageView.getId(), ConstraintSet.LEFT, individualItemConstraintLayout.getId(), ConstraintSet.LEFT);
                constraintSet.connect(itemIconImageView.getId(), ConstraintSet.BOTTOM, individualItemConstraintLayout.getId(), ConstraintSet.BOTTOM);
                constraintSet.connect(itemIconImageView.getId(), ConstraintSet.RIGHT, individualItemConstraintLayout.getId(), ConstraintSet.RIGHT);
                float randIconOffsetHorizontal = (float) ((Math.random()*10)/10.0);
                float randIconOffsetVertical = (float) ((Math.random()*10)/10.0);
                constraintSet.setHorizontalBias(itemIconImageView.getId(), randIconOffsetHorizontal);
                constraintSet.setVerticalBias(itemIconImageView.getId(), randIconOffsetVertical);
                constraintSet.applyTo(individualItemConstraintLayout);
                AlphaAnimation itemIconFadeIn = new AlphaAnimation(0f, 1f);
                itemIconFadeIn.setDuration(1000);
                itemIconImageView.startAnimation(itemIconFadeIn);
            }

            return itemDisplayLayout;
        }
    }

    public static class ItemDisplay implements Serializable
    {
        String itemName;
        int numItems;
        public ItemDisplay(String in, int ni)
        {
            itemName = in;
            numItems = ni;
        }
    }
}
