package com.jinqiu.view.scalerecyclerpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Lifq on 2017/5/11.
 */
public class ScaleRecyclerViewPager extends RecyclerViewPager {

    private static final String TAG = "ScaleRecyclerViewPager";

    private ArrayList<Integer> childCenterX = new ArrayList<>();
    private SparseArray<Integer> childIndex = new SparseArray<>();

    private float mScaleMax = 1.0f;
    private float mScaleMin = 0.914f;
    private float mCoverWidth = 40f;

    public ScaleRecyclerViewPager(Context context) {
        super(context);
        init(context, null);
    }

    public ScaleRecyclerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ScaleRecyclerViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScaleRecyclerViewPager);
            mScaleMax = typedArray.getFloat(R.styleable.ScaleRecyclerViewPager_srp_maxScale, 1f);
            mScaleMin = typedArray.getFloat(R.styleable.ScaleRecyclerViewPager_srp_minScale, 0.914f);
            mCoverWidth = typedArray.getDimension(R.styleable.ScaleRecyclerViewPager_srp_coverWidth, 40f);
            typedArray.recycle();
        }
        setClipToPadding(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
        addOnScrollListener(new ScaleOnScrollListener());
        addOnLayoutChangeListener(new ScaleOnLayoutChangeListener());
        setChildDrawingOrderCallback(new ScaleChildDrawingOrderCallback());
    }

    public void setMaxScale(float scaleMax) {
        this.mScaleMax = scaleMax;
    }

    public void setMinScale(float scaleMin) {
        this.mScaleMin = scaleMin;
    }

    public void setCoverWidth(float coverWidth) {
        this.mCoverWidth = coverWidth;
    }

    private int getViewCenterX(final View view) {
        int[] array = new int[2];
        view.getLocationOnScreen(array);
        return array[0] + view.getWidth() / 2;
    }

    private void setMinScale(View view) {
        view.setScaleY(mScaleMin);
        view.setScaleX(mScaleMin);
    }

    private void setMaxScale(View view) {
        view.setScaleY(mScaleMax);
        view.setScaleX(mScaleMax);
    }

    /**
     * 滑动监听
     */
    class ScaleOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int childCount = recyclerView.getChildCount();
            float width = recyclerView.getChildAt(0).getWidth();
            float padding = (recyclerView.getWidth() - width) / 2;
            float diff = mScaleMax - mScaleMin;
            for (int j = 0; j < childCount; j++) {
                View v = recyclerView.getChildAt(j);
                //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
                float rate = 0f;
                if (v.getLeft() <= padding) {
                    if (v.getLeft() >= padding - v.getWidth()) {
                        rate = (padding - v.getLeft()) * 1f / v.getWidth();
                    } else {
                        rate = 1f;
                    }
                    v.setScaleY(mScaleMax - rate * diff);
                    v.setScaleX(mScaleMax - rate * diff);
                    if (rate >= 0.5) {
                        v.setTranslationX((diff * width / 2) * rate
                                + mCoverWidth * Math.abs(Math.abs(rate) - 0.5f) / 0.5f);
                    } else {
                        v.setTranslationX((diff * width / 2) * rate);
                    }
                } else {
                    //往右 从 padding 到 recyclerView.getWidth()-padding 的过程中，由大到小
                    if (v.getLeft() <= recyclerView.getWidth() - padding) {
                        rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                    }
                    v.setScaleY(mScaleMin + rate * diff);
                    v.setScaleX(mScaleMin + rate * diff);

                    if (rate <= 0.5) {
                        v.setTranslationX((diff * width / 2) * (rate - 1f)
                                - mCoverWidth * Math.abs(Math.abs(rate) - 0.5f) / 0.5f);
                    } else {
                        v.setTranslationX((diff * width / 2) * (rate - 1f));
                    }
                }
            }
//                invalidate();
        }
    }

    /**
     * layout change 监听
     */
    class ScaleOnLayoutChangeListener implements OnLayoutChangeListener {

        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                   int oldLeft, int oldTop, int oldRight, int oldBottom) {
            if (getChildCount() < 3) {

                if (getChildAt(1) != null) {

                    if (getCurrentPosition() == 0) {
                        View v1 = getChildAt(1);
                        float w = v1.getWidth();
                        v1.setTranslationX(-((mScaleMax - mScaleMin) * w / 2 + mCoverWidth));
                        setMinScale(v1);

                        View v2 = getChildAt(0);
                        setMaxScale(v2);

                    } else {

                        View v0 = getChildAt(0);
                        float w = v0.getWidth();
                        v0.setTranslationX((mScaleMax - mScaleMin) * w / 2 + mCoverWidth);
                        setMinScale(v0);

                        View v1 = getChildAt(1);
                        setMaxScale(v1);
                    }
                }
            } else {

                if (getChildAt(0) != null) {
                    View v0 = getChildAt(0);
                    float w = v0.getWidth();
                    v0.setTranslationX((mScaleMax - mScaleMin) * w / 2 + mCoverWidth);
                    setMinScale(v0);
                }

                if (getChildAt(1) != null) {
                    View v1 = getChildAt(0);
                    setMaxScale(v1);
                    v1.setLayerType(LAYER_TYPE_NONE, null);
                }

                if (getChildAt(2) != null) {
                    View v2 = getChildAt(2);
                    float w = v2.getWidth();
                    v2.setTranslationX(-((mScaleMax - mScaleMin) * w / 2 + mCoverWidth));
                    setMinScale(v2);
                }
                //invalidate();
                setLayerType(LAYER_TYPE_NONE, null);
            }
        }
    }

    /**
     * 子View绘制监听
     */
    class ScaleChildDrawingOrderCallback implements ChildDrawingOrderCallback {

        @Override
        public int onGetChildDrawingOrder(int childCount, int i) {
            if (i == 0 || childIndex.size() != childCount) {
                childCenterX.clear();
                childIndex.clear();
                int recyclerViewCenterX = getViewCenterX(ScaleRecyclerViewPager.this);
                for (int j = 0; j < childCount; ++j) {
                    int abs = Math.abs(recyclerViewCenterX - getViewCenterX(getChildAt(j)));
                    if (childIndex.get(abs) != null) {
                        ++abs;
                    }
                    childCenterX.add(abs);
                    childIndex.append(abs, j);
                }
                Collections.sort(childCenterX);//1,0,2  0,1,2
            }
//                invalidate();
            return childIndex.get(childCenterX.get(childCount - 1 - i));

        }
    }
}
