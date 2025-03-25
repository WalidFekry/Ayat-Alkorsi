package com.walid.ayatalkorsi.adapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.walid.ayatalkorsi.R;
import com.walid.ayatalkorsi.items.SliderItem;
import com.walid.ayatalkorsi.utils.EnchantedViewPager;

import java.util.List;

public class CustomViewPagerAdapter extends PagerAdapter {
    private final Activity activity;
    private final List<SliderItem> sliderItems;
    private final LayoutInflater inflater;

    public CustomViewPagerAdapter(Activity activity, List<SliderItem> sliderItems) {
        this.activity = activity;
        this.sliderItems = sliderItems;
        this.inflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = inflater.inflate(R.layout.slider_adapter, container, false);

        TextView t3 = itemView.findViewById(R.id.t3);
        ImageView shareS = itemView.findViewById(R.id.shareS);
        ImageView copyS = itemView.findViewById(R.id.copyS);

        t3.setText(sliderItems.get(position).getT3());

        shareS.setOnClickListener(v -> shareText(sliderItems.get(position).getT3()));
        copyS.setOnClickListener(v -> copyToClipboard(sliderItems.get(position).getT3()));

        itemView.setTag(EnchantedViewPager.ENCHANTED_VIEWPAGER_POSITION + position);
        container.addView(itemView, 0);
        return itemView;
    }

    @Override
    public int getCount() {
        return sliderItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private void shareText(String text) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "تطبيق آية الكرسي مكررة - بدون انترنت");
        sendIntent.putExtra(Intent.EXTRA_TEXT, text + "\n\n" + "تفضل رابط التطبيق  https://t.co/2xVh8rqVLS");
        activity.startActivity(Intent.createChooser(sendIntent, "مشاركة اقتباسات من تطبيق آية الكرسي"));
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", text + "\n\nتم نسخ هذه النصوص من تطبيق آية الكرسي مكررة - بدون انترنت\nhttps://t.co/2xVh8rqVLS");
        clipboard.setPrimaryClip(clip);
        Toast.makeText(activity, "تم نسخ الاقتباس بنجاح", Toast.LENGTH_SHORT).show();
    }
}