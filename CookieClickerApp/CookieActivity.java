package com.example.cookieclicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

//TODO: add cookie image that pops up in a parabolic path beside the +1
//TODO: save data when app shuts down
//TODO: create splash screen activity and call it on the onCreate of CookieActivity; display "Shaurya Agarwal's Cookie Clicker" with an icon of a cookie being clicked or something with dark blue nice background

public class CookieActivity extends AppCompatActivity {

    ConstraintLayout mainConstraintLayout;
    ImageView cookieImageView, cookiePageImage, shopPageImage, itemsPageImage, cookieBackgroundImage1, cookieBackgroundImage2;
    static AtomicInteger cookieCountTotal;
    TextView cookieCountTextView, cookiesPerSecondTextView, cookiePageName, shopPageName, itemsPageName;
    static ArrayList<ShopActivity.ShopItem> itemList = new ArrayList<>();
    boolean itemListCheck = false;
    static int totalNumShopItems = 0;
    static ArrayList<ItemsActivity.ItemDisplay> itemDisplayList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cookielayout);
        Log.d("TAGA", "Cookie Activity Created");

        mainConstraintLayout = findViewById(R.id.id_cookieConstraintLayout);

        cookieCountTextView = findViewById(R.id.id_cookieCountTextView);
        cookiesPerSecondTextView = findViewById(R.id.id_cookiesPerSecondTextView);
        cookiePageName = findViewById(R.id.id_cookiePageIconTextView);
        shopPageName = findViewById(R.id.id_shopPageIconTextView);
        itemsPageName = findViewById(R.id.id_itemsPageIconTextView);

        cookieImageView = findViewById(R.id.id_cookieImageView);
        cookieImageView.setImageResource(R.drawable.cookieclicker);
        cookiePageImage = findViewById(R.id.id_cookiePageIconImageView);
        cookiePageImage.setImageResource(R.drawable.smallcookieicon);
        shopPageImage = findViewById(R.id.id_shopPageIconImageView);
        shopPageImage.setImageResource(R.drawable.smallshopicon);
        itemsPageImage = findViewById(R.id.id_itemsPageIconImageView);
        itemsPageImage.setImageResource(R.drawable.smallitemsicon);
        cookieBackgroundImage1 = findViewById(R.id.id_cookiesFallingBackgroundImageView1);
        cookieBackgroundImage1.setImageResource(R.drawable.cookiesfallingbackground);
        cookieBackgroundImage2 = findViewById(R.id.id_cookiesFallingBackgroundImageView2);
        cookieBackgroundImage2.setImageResource(R.drawable.cookiesfallingbackground);

        cookieImageView.bringToFront();

        cookieCountTotal = new AtomicInteger(0);
        cookieCountTotal.set(0);
        cookieCountTextView.setText(cookieCountTotal.intValue() + " Cookies");

        itemList.add(new ShopActivity.ShopItem(R.drawable.itemgrandmaicon, 0, "Grandma", 5.0, 1.0));
        itemList.add(new ShopActivity.ShopItem(R.drawable.itemfactoryicon, 0, "Factory", 25.0, 10.0));
        itemList.add(new ShopActivity.ShopItem(R.drawable.itemtempleicon, 0, "Temple", 100.0, 25.0));
        itemList.add(new ShopActivity.ShopItem(R.drawable.itemshipmenticon, 0, "Shipment", 5000.0, 100.0));

        itemDisplayList.add(new ItemsActivity.ItemDisplay("Grandmas:", 0));
        itemDisplayList.add(new ItemsActivity.ItemDisplay("Factories:", 0));
        itemDisplayList.add(new ItemsActivity.ItemDisplay("Temples:", 0));
        itemDisplayList.add(new ItemsActivity.ItemDisplay("Shipments:", 0));

        ValueAnimator cookieFallingValueAnimator = ValueAnimator.ofFloat(0f, 1f);
        cookieFallingValueAnimator.setInterpolator(new LinearInterpolator());
        cookieFallingValueAnimator.setRepeatCount(Animation.INFINITE);
        cookieFallingValueAnimator.setDuration(5000);
        cookieFallingValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                cookieBackgroundImage1.setTranslationY((float)cookieFallingValueAnimator.getAnimatedValue()*cookieBackgroundImage1.getHeight());
                cookieBackgroundImage2.setTranslationY((float)cookieFallingValueAnimator.getAnimatedValue()*cookieBackgroundImage2.getHeight() - cookieBackgroundImage2.getHeight());
            }
        });
        cookieFallingValueAnimator.start();

        Thread updateCookieCounter = new Thread(new Runnable() {
            Handler cookieCounterHandler = new Handler();
            @Override
            public void run() {
                while(true)
                {
                    try {
                        Thread.sleep(1000);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    cookieCounterHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            cookieCountTextView.setText(cookieCountTotal.intValue() + " Cookies");
                            cookiesPerSecondTextView.setText( ShopActivity.totalCookiesPerSecond + " Cookies Per Second");
                        }
                    });
                }
            }
        });

        updateCookieCounter.start();

        shopPageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCookie1 = new Intent(CookieActivity.this, ShopActivity.class);
                intentCookie1.putExtra("INITIAL_CHECK", true);
                startActivity(intentCookie1);
            }
        });

        itemsPageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCookie2 = new Intent(CookieActivity.this, ItemsActivity.class);
                intentCookie2.putExtra("INITIAL_CHECK", true);
                startActivity(intentCookie2);
            }
        });

        cookieImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Cookie Animation
                ScaleAnimation cookieAnimation = new ScaleAnimation(0.9f, 1.05f, 0.9f, 1.05f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                cookieAnimation.setDuration(150);
                cookieImageView.startAnimation(cookieAnimation);

                //Cookie Counter
                cookieCountTotal.getAndAdd(1);
                cookieCountTextView.setText(cookieCountTotal.intValue() + " Cookies");

                //Create OnCookieClick +1
                final TextView plusOneTextView = new TextView(CookieActivity.this);
                plusOneTextView.setId(View.generateViewId());
                plusOneTextView.setText("+1");
                plusOneTextView.setTextSize(25);
                plusOneTextView.setTypeface(Typeface.DEFAULT, 1);
                plusOneTextView.setTextColor(Color.BLACK);
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                plusOneTextView.setLayoutParams(params);
                mainConstraintLayout.addView(plusOneTextView);
                float xClickCoord = event.getX();
                float yClickCoord = event.getY();
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(mainConstraintLayout);
                constraintSet.connect(plusOneTextView.getId(), ConstraintSet.TOP, cookieImageView.getId(), ConstraintSet.TOP);
                constraintSet.connect(plusOneTextView.getId(), ConstraintSet.LEFT, cookieImageView.getId(), ConstraintSet.LEFT);
                constraintSet.connect(plusOneTextView.getId(), ConstraintSet.RIGHT, cookieImageView.getId(), ConstraintSet.RIGHT);
                constraintSet.connect(plusOneTextView.getId(), ConstraintSet.BOTTOM, cookieImageView.getId(), ConstraintSet.BOTTOM);
                float cookieHorizontalBias = xClickCoord/cookieImageView.getWidth();
                float cookieVerticalBias = yClickCoord/cookieImageView.getHeight();
                constraintSet.setHorizontalBias(plusOneTextView.getId(), cookieHorizontalBias);
                constraintSet.setVerticalBias(plusOneTextView.getId(), cookieVerticalBias);
                constraintSet.applyTo(mainConstraintLayout);

                //+1 Animations
                TranslateAnimation plusOneTranslateAnimation = new TranslateAnimation(0f, 0f, 0f, -500f);
                plusOneTranslateAnimation.setDuration(1000);
                AlphaAnimation plusOneFadeOutAnimation = new AlphaAnimation(1f, 0f);
                plusOneFadeOutAnimation.setDuration(1000);
                AnimationSet animationSet = new AnimationSet(false);
                animationSet.addAnimation(plusOneTranslateAnimation);
                animationSet.addAnimation(plusOneFadeOutAnimation);
                animationSet.setDuration(1000);
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        plusOneTextView.setVisibility(View.INVISIBLE);
                        cookieImageView.post(new Runnable() {
                            @Override
                            public void run() {
                                mainConstraintLayout.removeView(plusOneTextView);
                            }
                        });
                        Log.d("TAGA", "Widget Count: " + mainConstraintLayout.getChildCount());
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
                plusOneTextView.startAnimation(animationSet);
                return false;
            }
        });
    }
}