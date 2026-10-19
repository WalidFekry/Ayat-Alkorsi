package com.walid.ayatalkorsi;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.walid.ayatalkorsi.utils.EnchantedViewPager;
import com.walid.ayatalkorsi.adapters.CustomViewPagerAdapter;
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

    private static final long DELAY_MS = 500;
    private static final long PERIOD_MS = 8000;
    private final Handler handler = new Handler();
    private final List<SliderItem> sliderItems = new ArrayList<>();
    private Timer timer;
    private EnchantedViewPager enchantedViewPager;
    private CustomViewPagerAdapter customViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainbar);

        setupToolbar();
        setupNavigationDrawer();
        setupRateDialog();
        setupClickListeners();
        setupSlider();

        FirebaseMessaging.getInstance().subscribeToTopic("all");
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupNavigationDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, findViewById(R.id.toolbar), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ShapedNavigationView shapedNavigationView = findViewById(R.id.nav_view);
        shapedNavigationView.getSettings().setShapeType(ShapedViewSettings.ROUNDED_RECT);
        shapedNavigationView.setNavigationItemSelectedListener(this);
    }

    private void setupRateDialog() {
        handler.postDelayed(() -> SmartRate.Rate(this, "تقييم التطبيق", "تقييمك للتطبيق يساعدنا علي التطوير المستمر وتقديم المزيد", "تقييم الان", "حسنا يمكنك تقيممنا الان علي جوجل بلاي", "اضغط هنا", "ليس الان", "Thanks", Color.parseColor("#D7723F"), 2), 40000);
    }

    private void setupClickListeners() {
        findViewById(R.id.rateee).setOnClickListener(v -> openUrl("https://play.google.com/store/apps/details?id=com.walid.ayatalkorsi"));
        findViewById(R.id.cardView66).setOnClickListener(v -> startActivity(new Intent(this, AyaPic.class)));
        findViewById(R.id.cardView33).setOnClickListener(v -> startActivity(new Intent(this, AyaText.class)));
        findViewById(R.id.cardView55).setOnClickListener(v -> startActivity(new Intent(this, AyaFadl.class)));
        findViewById(R.id.cardView88).setOnClickListener(v -> startActivity(new Intent(this, Listen.class)));
    }

    private void setupSlider() {
        enchantedViewPager = findViewById(R.id.viewPager_alphabet);
        loadSliderItems();

        customViewPagerAdapter = new CustomViewPagerAdapter(this, sliderItems);
        enchantedViewPager.setAdapter(customViewPagerAdapter);
        enchantedViewPager.setCurrentItem(1);

        if (customViewPagerAdapter.getCount() > 1) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(() -> {
                        int nextItem = (enchantedViewPager.getCurrentItem() + 1) % customViewPagerAdapter.getCount();
                        enchantedViewPager.setCurrentItem(nextItem, true);
                    });
                }
            }, DELAY_MS, PERIOD_MS);
        }
    }

    private void loadSliderItems() {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray sliderArray = obj.getJSONArray("books");
            for (int i = 0; i < 3; i++) {
                JSONObject jsonObject = sliderArray.getJSONObject(new Random().nextInt(sliderArray.length()));
                sliderItems.add(new SliderItem(jsonObject.getString("pages")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadJSONFromAsset() {
        try (InputStream is = getAssets().open("sample-books.json")) {
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void openUrl(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.maktbtiplus) {
            openUrl("https://walid-fekry.com/maktbti-plus/");
        } else if (id == R.id.facegroub) {
            openUrl("https://www.facebook.com/groups/440403217380641");
        } else if (id == R.id.teleee) {
            openUrl("https://t.me/App_Maktbti");
        } else if (id == R.id.ratee) {
            openUrl("https://play.google.com/store/apps/details?id=com.walid.ayatalkorsi");
        } else if (id == R.id.shareee) {
            shareApp();
        } else if (id == R.id.mass) {
            sendEmail();
        } else if (id == R.id.sysa) {
            openUrl("https://sites.google.com/view/ayatalkorsi/home");
        }

        closeDrawer();
        return true;
    }

    private void shareApp() {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "آية الكرسي مكررة - بدون انترنت 💙");
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.aboutapp) + "\n\n" + "حمل التطبيق من خلال هذا الرابط أو من متجر جوجل بلاي \uD83D\uDC47  https://play.google.com/store/apps/details?id=com.walid.ayatalkorsi \n");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "مشاركة التطبيق"));
    }

    private void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "prowalidfekry@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "رسالة إلى مبرمج تطبيق آية الكرسي 📲");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(Intent.createChooser(emailIntent, "إرسال بريد إلكتروني..."));
    }

    private void closeDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

    }
}
