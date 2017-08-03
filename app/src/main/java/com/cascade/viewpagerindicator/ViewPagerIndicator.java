package com.cascade.viewpagerindicator;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ViewPagerIndicator extends RelativeLayout {
    private static final int LINE_SIZE_IN_DP = 4;
    private static final int INDICATOR_SIZE_IN_DP = 30;

    private int colorAccent;
    private int colorAccentTranslucent;

    private LinearLayout layoutIndicators;

    private ViewPagerIndicator(Context context) {
        super(context);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void initialize(ViewPager viewPager) {
        if (viewPager != null) {
            float density = getContext().getResources().getDisplayMetrics().density;

            int lineSize = (int) (LINE_SIZE_IN_DP * density);
            int indicatorSize = (int) (INDICATOR_SIZE_IN_DP * density);

            colorAccent = ContextCompat.getColor(getContext(), R.color.colorAccent);
            colorAccentTranslucent = ColorUtils.setAlphaComponent(colorAccent, 126);

            layoutIndicators = new LinearLayout(getContext());
            layoutIndicators.setOrientation(LinearLayout.HORIZONTAL);
            layoutIndicators.setGravity(CENTER_HORIZONTAL);
            addView(layoutIndicators);

            int itemCount = viewPager.getAdapter().getCount();
            for (int i = 0; i < itemCount; i++) {
                LinearLayout.LayoutParams paramsDot = new LinearLayout.LayoutParams(indicatorSize, indicatorSize);

                View viewIndicator = new View(getContext());
                viewIndicator.setBackground(buildIndicatorBackground());
                layoutIndicators.addView(viewIndicator);
                viewIndicator.setLayoutParams(paramsDot);

                if (i != itemCount - 1) {
                    View viewIndicatorLine = new View(getContext());
                    viewIndicatorLine.setBackgroundColor(colorAccentTranslucent);
                    LinearLayout.LayoutParams paramsLine = new LinearLayout.LayoutParams(indicatorSize / 2, lineSize);

                    paramsLine.gravity = Gravity.CENTER_VERTICAL;
                    viewIndicatorLine.setLayoutParams(paramsLine);
                    layoutIndicators.addView(viewIndicatorLine);
                }
            }

            setSelectedDot(viewPager.getCurrentItem());
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    setSelectedDot(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }
    }

    private GradientDrawable buildIndicatorBackground() {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(colorAccent);

        return shape;
    }

    private void setSelectedDot(int position) {
        for (int i = 0; i < layoutIndicators.getChildCount(); i++) {
            if (i < position * 2) {
                if (i % 2 == 0) {
                    GradientDrawable currentItem = (GradientDrawable) layoutIndicators.getChildAt(i).getBackground();
                    currentItem.setColor(colorAccent);
                } else {
                    layoutIndicators.getChildAt(i).setBackgroundColor(colorAccent);
                }
            } else if (i == position * 2) {
                GradientDrawable currentItem = (GradientDrawable) layoutIndicators.getChildAt(position * 2).getBackground();
                currentItem.setColor(colorAccent);
            } else {
                if (i % 2 == 0) {
                    GradientDrawable currentItem = (GradientDrawable) layoutIndicators.getChildAt(i).getBackground();
                    currentItem.setColor(colorAccentTranslucent);
                } else {
                    ColorDrawable currentItem = (ColorDrawable) layoutIndicators.getChildAt(i).getBackground();
                    currentItem.setColor(colorAccentTranslucent);
                }
            }
        }
    }
}