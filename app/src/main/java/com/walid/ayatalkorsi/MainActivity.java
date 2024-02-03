package com.walid.ayatalkorsi;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.walid.ayatalkorsi.Util.EnchantedViewPager;
import com.walid.ayatalkorsi.adapter.CustomViewPagerAdapter;
import com.walid.ayatalkorsi.items.SliderItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import guy4444.smartrate.SmartRate;
import softpro.naseemali.ShapedNavigationView;
import softpro.naseemali.ShapedViewSettings;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final Handler handler = new Handler();
    private final long DELAY_MS = 500;
    private final long PERIOD_MS = 8000;
    ImageView rateee;
    List<SliderItem> sliderItems;
    CardView cardView;
    private EnchantedViewPager enchantedViewPager;
    private Runnable Update;
    private Boolean isTimerStart = false;
    private CustomViewPagerAdapter customViewPagerAdapter;
    private Timer timer;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainbar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        ShapedNavigationView shapedNavigationView = findViewById(R.id.nav_view);
        shapedNavigationView.getSettings().setShapeType(ShapedViewSettings.ROUNDED_RECT);
        shapedNavigationView.setNavigationItemSelectedListener(this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (this != null && !isFinishing()) {
                    SmartRate.Rate(MainActivity.this
                            , "تقييم التطبيق"
                            , "تقييمك للتطبيق يساعدنا علي التطوير المستمر وتقديم المزيد"
                            , "تقييم الان"
                            , "حسنا يمكنك تقيممنا الان علي جوجل بلاي"
                            , "اضغط هنا"
                            , "ليس الان"
                            , "Thanks "
                            , Color.parseColor("#D7723F"), 2);


                }
            }
        }, 40000);

        rateee = findViewById(R.id.rateee);
        rateee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.co/2xVh8rqVLS")));
            }
        });

        enchantedViewPager = findViewById(R.id.viewPager_alphabet);


        cardView = findViewById(R.id.cardView66);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AyaPic.class));
            }
        });

        cardView = findViewById(R.id.cardView33);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AyaText.class));
            }
        });

        cardView = findViewById(R.id.cardView55);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AyaFadl.class));
            }
        });

        cardView = findViewById(R.id.cardView88);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Listen.class));
            }
        });


        sliderItems = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray SliderItem = obj.getJSONArray("books");
            for (int i = 0; i <= 2; i++) {
                JSONObject jsonObject = SliderItem.getJSONObject(new Random().nextInt(SliderItem.length()));
                String nameEn = jsonObject.getString("pages");
                sliderItems.add(new SliderItem(nameEn));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Update = () -> {
            isTimerStart = true;
            if (enchantedViewPager.getCurrentItem() == (customViewPagerAdapter.getCount() - 1)) {
                enchantedViewPager.setCurrentItem(0, true);
            } else {
                enchantedViewPager.setCurrentItem(enchantedViewPager.getCurrentItem() + 1, true);
            }
        };

        customViewPagerAdapter = new CustomViewPagerAdapter(MainActivity.this, sliderItems);
        enchantedViewPager.setAdapter(customViewPagerAdapter);
        enchantedViewPager.setCurrentItem(1);

        if (customViewPagerAdapter.getCount() > 1) {
            timer = new Timer(); // This will create a new Thread
            timer.schedule(new TimerTask() { // task to be scheduled
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, DELAY_MS, PERIOD_MS);
        }


        FirebaseMessaging.getInstance().subscribeToTopic("all");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("sample-books.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, StandardCharsets.UTF_8);


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.maktbtiplus) {
            Intent o = new Intent(Intent.ACTION_VIEW);
            o.setData(Uri.parse("https://walid-fekry.com/maktbti-plus/"));
            startActivity(o);
        } else if (id == R.id.facegroub) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/groups/440403217380641")));
        }
//        else if (id == R.id.mohafez) {
//            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.co/XerG0QVIvi")));
//        }
        else if (id == R.id.teleee) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/App_Maktbti")));
        } else if (id == R.id.ratee) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.co/2xVh8rqVLS")));
        } else if (id == R.id.shareee) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "آية الكرسي مكررة - بدون انترنت 💙");
            sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.aboutapp) + "\n" +
                    "\n" +
                    "حمل التطبيق من خلال هذا الرابط أو من متجر جوجل بلاي \uD83D\uDC47  https://t.co/2xVh8rqVLS \n");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == R.id.mass) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "prowalidfekry@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "رسالة إلى مبرمج تطبيق آية الكرسي 📲");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } else if (id == R.id.sysa) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/ayatalkorsi/home")));

        }
//        else if (id == R.id.walidmore) {
//            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.co/US34fsUZeW")));
//        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }


}