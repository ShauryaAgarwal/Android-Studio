package com.example.topicprojectshaurya;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    TextView moreInfo, myOpinion;
    ArrayList<IPhone> phoneList;
    LinearLayout undoLayout;
    int currentElementPosition, removedPosition;
    boolean checkRemoved, checkClicked;
    IPhone recentlyRemoved;
    static final String SAVE_INT = "SAVE_INT";
    static final String SAVE_INT_2 = "SAVE_INT_2";
    static final String SAVE_ARRAY = "SAVE_ARRAY";
    static final String SAVE_BOOLEAN = "SAVE_BOOLEAN";
    static final String SAVE_BOOLEAN_2 = "SAVE_BOOLEAN_2";
    static final String SAVE_REMOVED = "SAVE_REMOVED";

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TAG","RESUME");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TAG","START");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TAG","STOP");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TAG","PAUSE");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TAG","DESTROY");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("TAG","SAVING");
        outState.putSerializable(SAVE_ARRAY, phoneList);
        outState.putInt(SAVE_INT, currentElementPosition);
        outState.putInt(SAVE_INT_2, removedPosition);
        outState.putBoolean(SAVE_BOOLEAN, checkRemoved);
        outState.putBoolean(SAVE_BOOLEAN_2, checkClicked);
        outState.putSerializable(SAVE_REMOVED, recentlyRemoved);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("TAG","CREATE");

        listView = findViewById(R.id.listViewMain);
        moreInfo = findViewById(R.id.moreInformationTextView);
        myOpinion = findViewById(R.id.myOpinionTextView);
        undoLayout = findViewById(R.id.undoLayout);
        removedPosition = -1;

        phoneList = new ArrayList<>();
        if(savedInstanceState != null)
        {
            checkClicked = savedInstanceState.getBoolean(SAVE_BOOLEAN_2);
            checkRemoved = savedInstanceState.getBoolean(SAVE_BOOLEAN);
            currentElementPosition = savedInstanceState.getInt(SAVE_INT);
            phoneList = (ArrayList) savedInstanceState.getSerializable(SAVE_ARRAY);
            if(savedInstanceState.getBoolean(SAVE_BOOLEAN) || !savedInstanceState.getBoolean(SAVE_BOOLEAN_2))
            {
                moreInfo.setText("Select an element from the list to display more information on the model here");
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                    myOpinion.setText("Select an element from the list to display my personal opinions/experiences with the model here");
            }
            else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                moreInfo.setText(phoneList.get(savedInstanceState.getInt(SAVE_INT)).explanation);
                myOpinion.setText(phoneList.get(savedInstanceState.getInt(SAVE_INT)).opinion);
            }
            else
                moreInfo.setText("Release Date(s): " + phoneList.get(savedInstanceState.getInt(SAVE_INT)).releaseDate + "\nAvailable Colors: " + phoneList.get(savedInstanceState.getInt(SAVE_INT)).colors + "\nStorage Options: " + phoneList.get(savedInstanceState.getInt(SAVE_INT)).storage);
        }
        else
        {
            phoneList.add(new IPhone(R.drawable.iphone_1, "iPhone 2G", "Sep 12, 2007", "Aluminum (Gray)", "4/8/16 GB", "Released on June 29, 2007, the original Apple iPhone, had a 3.5\" display screen, was 11.6mm thick and had 16GB of storage. Compared to phone memory today that doesn't seem like much, but it was enormous at the time. It had a 128MB memory and weighed 135 grams.", "The introduction of the iPhone was a revolutionary change in the technological industry. Personally, I have owned an iPod Touch, which was Apple's first touchscreen device. The advancements made upon introducing the first iPhone set up the industry well for great success in the future."));
            phoneList.add(new IPhone(R.drawable.iphone_3g, "iPhone 3G / 3GS", "Jun 29, 2008 / June 19, 2009", "Black/White", "8/16/32 GB", "This iPhone was 12.3mm thick, weighed 133 grams, had a 3.5\" display screen and a 2MP camera, along with a 128MB memory. The differences between the iPhone 3 and the original iPhone were slight, except for the App Store and its 3G connectivity.", "I think that the introduction of 3G in iPhones was a large stepping stone and turning point for Apple. The fact that 8 gigs of storage 10 years ago seemed sufficient 12 years ago shows how far we have come with technology."));
            phoneList.add(new IPhone(R.drawable.iphone_4, "iPhone 4 / 4S", "Jun 21, 2010 / Oct 14, 2011", "Black/White", "8/16/32/64 GB", "The iPhone 4s was the first iPhone to introduce the digital assistant Siri. With 9.3 mm thickness and a 3.5\" display and a 8 MP camera, and 512 MB memory, Apple just increased and improved its product in all dimensions.", "The iPhone 4 is the first iPhone that made its way into my family's possession and the second Apple product I have interacted with since the iPod Touch. The introduction of Siri was mesmerising to my 7 year-old self."));
            phoneList.add(new IPhone(R.drawable.iphone_5, "iPhone 5 / 5C / 5S", "Sep 21, 2012 / Sep 10, 2013", "Black/White/Blue/Green/Pink/White/Yellow/Gold/Silver/Space Gray", "16/32/64 GB", "This phone released and introduced some revolutionary changes: 1 GB memory, LTE Connectivity, and lightning connectors. It also had slow-motion video capture, dual flash, touch ID, 4\" display and 8.0 MP camera.", "The iPhone 5 was my first smartphone, which was given to me by my dad. With 32 gigs of storage, I felt free to download several games, many of which I never used, as my storage initially felt infinite. As I look back, those 32 gigs would not allow me to download as much as it did before."));
            phoneList.add(new IPhone(R.drawable.iphone_6, "iPhone 6 / 6+ / 6S / 6S+", "Sep 20, 2014 / Sep 20, 2015", "Gold/Silver/Space Gray/Rose Gold", "16/64/128 GB", "This iPhone had a retina HD display, 128 GB of storage, a 5.5\" display (for the Plus Model), a 12 MP camera, and 2 GB memory. This iPhone introduced a completely new design and look which most iPhones in the future would adopt.", "I currently have an iPhone 6S as my personal smartphone. Other than problems with battery life and some loading speeds, I do not face many problems with using a 5 year-old model daily."));
            phoneList.add(new IPhone(R.drawable.iphone_se1, "iPhone SE (1st Gen)", "Mar 31, 2016", "Gold/Silver/Space Gray/Rose Gold", "16/32/64/128 GB", "A more affordable option compared to the iPhone 6, the SE had everything but the iPhone 6's 3D touch. It was basically a cheaper version of the iPhone, which people really loved, but later, the success of the iPhone 7 overshadowed the minuscule changes the SE made to the affordable phones industry.", "At first I was very intrigued by the thought and intent behind the iPhone SE, as it essentially went back to the model and style of an iPhone 5s while still performing up to par with newer models such as the iPhone 7."));
            phoneList.add(new IPhone(R.drawable.iphone_7, "iPhone 7 / 7+", "Sep 16, 2016", "Rose Gold/Silver/Gold/Black/Jet Black/PRODUCT (RED)", "32/128/256 GB", "With the introduction of the iPhone 7, the headphone jack was removed to increase the relevance of wireless earbuds. It had a 16GB base option, 32 GB and 256 GB storage, dual camera and new color options.", "Personally, I did not approve or or like Apple's choice to remove the headphone jack from this model and all models going forward. I felt that it was unnecessary and just a marketing tactic to improve sales for their to-be made AirPods and other wireless headphones rather than for practical purposes."));
            phoneList.add(new IPhone(R.drawable.iphone_8, "iPhone 8 / 8+", "Sep 22, 2017", "Gold/Red/Silver/Space Gray", "64/256 GB", "The iPhone 8 introduced several new features and improvements for photo and video capture. The introduction of the glass cover on the back of the phone allowed for wireless charging. Overall, the look and feel of the phone was similar to the iPhone 7, with some improvements in each regard.", "I feel that the iPhone 8's camera improvements were nice and improved the status of cellphones as replacements for professional cameras in casual photography. However, I feel that the introduction of a glass back for wireless charging is kind of pointless as an outlet is still necessary."));
            phoneList.add(new IPhone(R.drawable.iphone_x, "iPhone X / XR / XS / XS Max", "Nov 3, 2017 / Sep 21, 2018 / Oct 26, 2018", "Silver/Space Gray/PRODUCT (RED)/Yellow/White/Coral/Black/Blue", "64/128/256/512 GB", "Apple once again made a drastic change to the look and design of iPhones with the introduction of the iPhone X. With the introduction of Face ID and portrait mode and the removal of the home button, users found themselves in a new age of iPhones.", "The introduction of Apple's Face ID was a game-changer in the iPhone industry and for all smartphones going forward. However, I did not initially approve of Apple's removal of the home button, similar to my thoughts on removing the headphone jack."));
            phoneList.add(new IPhone(R.drawable.iphone_11, "iPhone 11 / 11 Pro / 11 Pro Max", "Sep 20, 2019", "Purple/Yellow/Green/Black/White/PRODUCT (RED)/Midnight Green/Space Gray/Silver/Gold", "64/128/256/512 GB", "The iPhone 11 made many improvements to the iPhone X models, introducing 11 and 12 MB cameras with ultra-wide lenses, along with a third camera in the 11 Pro model. The 11 Pro and 11 Pro Max introduced super retina XDR displays.", "I would say that the iPhone 11 Pro is one of my favorite phones in regards to aesthetic. I also like the 3-camera feature with different available zoom options. Overall, I feel that Apple did a good job with this device."));
            phoneList.add(new IPhone(R.drawable.iphone_se2, "iPhone SE (2nd Gen)", "April 24, 2020", "Black/White/PRODUCT (RED)", "64/128/256 GB", "Returning to an iPhone model with Touch ID and a home button, the 2nd Gen iPhone SE was mostly similar to the iPhone 7, but with improved camera and video quality, and with the use of an A13 bionic chip. Similar to the 1st Gen iPhone SE, this phone was introduced as an affordable option for users.", "I tend to like Apple's choices in creating new devices that harbor an older model's appearance. From my experiences seeing other people with this device, it functions like a proper new model with the look of an iPhone 8, and at a more affordable price."));
            phoneList.add(new IPhone(R.drawable.iphone_12, "iPhone 12 / 12 Mini / 12 Pro / 12 Pro Max", "Oct 23, 2020", "Black/White/PRODUCT (RED)/Blue/Green/Silver/Graphite/Gold/Pacific Blue", "64/128/256/512 GB", "Currently the latest addition to the iPhone industry, the iPhone 12 is built to support 5G connectivity, and uses an A14 bionic chip. There is also night mode for every camera and a ceramic shield with better drop performance compared to its predecessors.", "Similar to my thoughts on 3G and 4G LTE connectivity, I feel that the introduction of 5G is a remarkable change for the technological industry and for cellular data. I am leaning towards getting an iPhone 12 mini to replace my current iPhone 6S."));
            moreInfo.setText("Select an element from the list to display more information on the model here");
        }

        final IPhoneAdapter phoneTypes = new IPhoneAdapter(this, R.layout.topic_shaurya, phoneList);
        listView.setAdapter(phoneTypes);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentElementPosition = position;
                checkClicked = true;
                checkRemoved = false;
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                {
                    moreInfo.setText(phoneList.get(position).explanation);
                    myOpinion.setText(phoneList.get(position).opinion);
                }
                else
                    moreInfo.setText("Release Date(s): " + phoneList.get(position).releaseDate + "\nAvailable Colors: " + phoneList.get(position).colors + "\nStorage Options: " + phoneList.get(position).storage);
            }
        });

        undoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(removedPosition == -1 && savedInstanceState == null)
                {
                    Toast myMessage = Toast.makeText(MainActivity.this, "Nothing to Undo", Toast.LENGTH_SHORT);
                    myMessage.show();
                }
                else if(phoneList.size() < 12)
                {
                    if(savedInstanceState == null)
                        phoneList.add(removedPosition, recentlyRemoved);
                    else
                        phoneList.add(savedInstanceState.getInt(SAVE_INT_2), (IPhone) savedInstanceState.getSerializable(SAVE_REMOVED));
                    Toast myMessage = Toast.makeText(MainActivity.this, "Item Added Back", Toast.LENGTH_SHORT);
                    myMessage.show();
                    phoneTypes.notifyDataSetChanged();
                }
            }
        });
    }

    public class IPhoneAdapter extends ArrayAdapter<IPhone>
    {
        Context context;
        List<IPhone> list;
        int xml;

        public IPhoneAdapter(@NonNull Context context, int resource, @NonNull List<IPhone> objects) {
            super(context, resource, objects);

            this.context = context;
            this.list = objects;
            this.xml = resource;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            final View phoneLayout = layoutInflater.inflate(xml, null);

            ImageView imageView = phoneLayout.findViewById(R.id.topic_imageView);
            TextView textView = phoneLayout.findViewById(R.id.topic_textView);
            final Button removeButton = phoneLayout.findViewById(R.id.topic_removeItemButton);

            imageView.setImageResource((phoneList.get(position)).iPhoneImage);
            textView.setText((phoneList.get(position)).iPhoneModel);
            removeButton.setText("Remove");

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recentlyRemoved = phoneList.get(position);
                    removedPosition = position;
                    phoneList.remove(position);
                    moreInfo.setText("Select an element from the list to display more information on the model here");
                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                        myOpinion.setText("Select an element from the list to display my personal opinions/experiences with the model here");
                    checkRemoved = true;
                    Toast myMessage = Toast.makeText(MainActivity.this, "Item Removed", Toast.LENGTH_SHORT);
                    myMessage.show();
                    notifyDataSetChanged();
                }
            });

            return phoneLayout;
        }
    }

    public class IPhone implements Serializable
    {
        int iPhoneImage;
        String iPhoneModel, releaseDate, colors, storage, explanation, opinion;

        public IPhone(int ipImage, String ipModel, String rlDate, String cl, String stg, String exp, String opi)
        {
            iPhoneImage = ipImage;
            iPhoneModel = ipModel;
            releaseDate = rlDate;
            colors = cl;
            storage = stg;
            explanation = exp;
            opinion = opi;
        }
    }
}