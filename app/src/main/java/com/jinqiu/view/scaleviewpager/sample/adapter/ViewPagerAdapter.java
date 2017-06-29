package com.jinqiu.view.scaleviewpager.sample.adapter;


import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jinqiu.view.scaleviewpager.sample.R;

public class ViewPagerAdapter extends PagerAdapter {

    private static final int DEFAULT_ITEM_COUNT = 10;
    private int[] srcs = {R.drawable.t1, R.drawable.t2, R.drawable.t3,
            R.drawable.t4, R.drawable.t5};

    @Override
    public int getCount() {
        return DEFAULT_ITEM_COUNT;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setImageResource(srcs[position % srcs.length]);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        container.addView(view, layoutParams);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
