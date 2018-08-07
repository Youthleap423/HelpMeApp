package com.veeritsolutions.uhelpme.utility;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by VEER7 on 7/29/2017.
 */

public class ToolBarBehaviour extends CoordinatorLayout.Behavior<Toolbar> {

    public ToolBarBehaviour(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, Toolbar child, View dependency) {

        if (dependency instanceof AppBarLayout) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            int fabBottomMargin = lp.bottomMargin;
            int distanceToScroll = child.getHeight() + fabBottomMargin;
            float ratio = dependency.getY() / (float) child.getHeight();
            child.setTranslationY(-distanceToScroll * ratio);
        }
        return true;
    }


}
