package com.jinqiu.view.scaleviewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;


import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Lifq on 2017/3/14
 */
public class ScaleViewPager extends ViewPager {

    private static final String TAG = "ScaleViewPager";
    private ArrayList<Integer> childCenterXAbs = new ArrayList<>();
    private SparseArray<Integer> childIndex = new SparseArray<>();

    private float mScaleMax = 1.0f;
    private float mScaleMin = 0.914f;
    private float mCoverWidth = 40f;

    public ScaleViewPager(Context context) {
        super(context);
        init(context, null);
    }

    public ScaleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScaleViewPager);
            mScaleMax = typedArray.getFloat(R.styleable.ScaleViewPager_svp_maxScale, 1f);
            mScaleMin = typedArray.getFloat(R.styleable.ScaleViewPager_svp_minScale, 0.914f);
            mCoverWidth = typedArray.getDimension(R.styleable.ScaleViewPager_svp_coverWidth, 40f);
            typedArray.recycle();
        }
        setPageTransformer(true, new SPageTransformer());//默认调用了 setChildrenDrawingOrderEnabledCompat(true);使得getChildDrawingOrder起作用
        setClipToPadding(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    /**
     * 设置左右的缩小系数
     *
     * @param scaleMin
     */
    public void setMinScale(float scaleMin) {
        mScaleMin = scaleMin;
    }

    /**
     * 设置中间的放大系数
     *
     * @param scaleMax
     */
    public void setMaxScale(float scaleMax) {
        mScaleMax = scaleMax;
    }

    /**
     * 设置重叠的大小
     *
     * @param coverWidth
     */
    public void setCoverWidth(float coverWidth) {
        mCoverWidth = coverWidth;
    }

    /**
     * @param childCount
     * @param n
     * @return 第n个位置的child 的绘制索引
     */
    @Override
    protected int getChildDrawingOrder(int childCount, int n) {
        if (n == 0 || childIndex.size() != childCount) {
            childCenterXAbs.clear();
            childIndex.clear();
            int viewCenterX = getViewCenterX(this);
            for (int i = 0; i < childCount; ++i) {
                int indexAbs = Math.abs(viewCenterX - getViewCenterX(getChildAt(i)));
                //两个距离相同，后来的那个做自增，从而保持abs不同
                if (childIndex.get(indexAbs) != null) {
                    ++indexAbs;
                }
                childCenterXAbs.add(indexAbs);
                childIndex.append(indexAbs, i);
            }
            Collections.sort(childCenterXAbs);//1,0,2  0,1,2
        }
        //那个item距离中心点远一些，就先draw它。（最近的就是中间放大的item,最后draw）
        return childIndex.get(childCenterXAbs.get(childCount - 1 - n));
    }

    private int getViewCenterX(View view) {
        int[] array = new int[2];
        view.getLocationOnScreen(array);
        return array[0] + view.getWidth() / 2;
    }

    class SPageTransformer implements ViewPager.PageTransformer {

        private float reduceX = 0.0f;
        private float itemWidth = 0;
        private float offsetPosition = 0f;

        @Override
        public void transformPage(View view, float position) {
            if (offsetPosition == 0f) {
                float paddingLeft = ScaleViewPager.this.getPaddingLeft();
                float paddingRight = ScaleViewPager.this.getPaddingRight();
                float width = ScaleViewPager.this.getMeasuredWidth();
                offsetPosition = paddingLeft / (width - paddingLeft - paddingRight);
            }
            float currentPos = position - offsetPosition;
            if (itemWidth == 0) {
                itemWidth = view.getWidth();
                //由于左右边的缩小而减小的x的大小的一半
                reduceX = (2.0f - mScaleMax - mScaleMin) * itemWidth / 2.0f;
            }
            if (currentPos <= -1.0f) {
                view.setTranslationX(reduceX + mCoverWidth);
                view.setScaleX(mScaleMin);
                view.setScaleY(mScaleMin);
            } else if (currentPos <= 1.0) {
                float scale = (mScaleMax - mScaleMin) * Math.abs(1.0f - Math.abs(currentPos));
                float translationX = currentPos * -reduceX;
                if (currentPos <= -0.5) {//两个view中间的临界，这时两个view在同一层，左侧View需要往X轴正方向移动覆盖的值()
                    view.setTranslationX(translationX + mCoverWidth * Math.abs(Math.abs(currentPos) - 0.5f) / 0.5f);
                } else if (currentPos <= 0.0f) {
                    view.setTranslationX(translationX);
                } else if (currentPos >= 0.5) {//两个view中间的临界，这时两个view在同一层
                    view.setTranslationX(translationX - mCoverWidth * Math.abs(Math.abs(currentPos) - 0.5f) / 0.5f);
                } else {
                    view.setTranslationX(translationX);
                }
                view.setScaleX(scale + mScaleMin);
                view.setScaleY(scale + mScaleMin);
            } else {
                view.setScaleX(mScaleMin);
                view.setScaleY(mScaleMin);
                view.setTranslationX(-reduceX - mCoverWidth);
            }

        }
    }
}
