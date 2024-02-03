package com.walid.ayatalkorsi.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.walid.ayatalkorsi.Util.EnchantedViewPager;
import com.walid.ayatalkorsi.items.SliderItem;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

public class CustomViewPagerAdapter extends PagerAdapter {
Context context;
    private Method method;
    TextView t1,t2,t3;
    private Activity activity;
    private List<SliderItem> sliderItems;
    private LayoutInflater inflater;
    String randomName;
    private String[] names;
    ImageView shareS,copyS;
    public CustomViewPagerAdapter(Activity activity, List<SliderItem> sliderItems) {
        this.activity = activity;
        this.sliderItems = sliderItems;
        // TODO Auto-generated constructor stub
        inflater = activity.getLayoutInflater();
    }


    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View imageLayout = inflater.inflate(R.layout.slider_adapter, container, false);
        assert imageLayout != null;
        t3=(TextView)imageLayout.findViewById(R.id.t3);
        shareS=(ImageView)imageLayout.findViewById(R.id.shareS);
        copyS=(ImageView)imageLayout.findViewById(R.id.copyS);
        shareS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT,"تطبيق آية الكرسي مكررة - بدون انترنت");
                sendIntent.putExtra(Intent.EXTRA_TEXT,"\n" +sliderItems.get(position).getT3()+"\n"+
                        "\n"+ "تفضل رابط التطبيق  https://t.co/2xVh8rqVLS \n");
                sendIntent.setType("text/plain");
                activity.startActivity(Intent.createChooser(sendIntent,"مشاركه اقتباسات  من تطبيق آية الكرسي مكررة - بدون انترنت:"));

            }
        });
        copyS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager)activity. getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("",sliderItems.get(position).getT3()+"\n"+
                        "\n"+"تم نسخ هذه النصوص من تطبيق آية الكرسي مكررة - بدون انترنت" +"\n"+  "https://t.co/2xVh8rqVLS\n");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(activity, "تم نسخ الاقتباس بنجاح", Toast.LENGTH_SHORT).show();
            }
            
        });
        t3.setText(sliderItems.get(position).getT3());
        imageLayout.setTag(EnchantedViewPager.ENCHANTED_VIEWPAGER_POSITION + position);



//        names = activity.getResources().getStringArray(R.array.nomes);
//        int randomIndex = new Random().nextInt(names.length);
//        randomName = names[randomIndex];


        container.addView(imageLayout, 0);
        return imageLayout;
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
        (container).removeView((View) object);
    }


}

