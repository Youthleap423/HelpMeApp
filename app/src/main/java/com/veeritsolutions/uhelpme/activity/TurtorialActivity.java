package com.veeritsolutions.uhelpme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.rd.PageIndicatorView;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.adapters.AdpTutorial;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;

import java.util.ArrayList;
import java.util.List;

public class TurtorialActivity extends AppCompatActivity implements OnClickEvent {
    private PageIndicatorView pageIndicatorView;
    private ViewPager pager;
    private Button nextBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turtorial);
        nextBtn = (Button)findViewById(R.id.btn_next_tutorial);
        initViews();
    }

    @SuppressWarnings("ConstantConditions")
    private void initViews() {
        AdpTutorial adapter = new AdpTutorial();
        adapter.setData(createPageList());

        pager = findViewById(R.id.tutorial_viewPager);
        pager.setAdapter(adapter);

//        pageIndicatorView = findViewById(R.id.pageIndicatorView);
//        pageIndicatorView.setViewPager(pager);
    }

    @NonNull
    private List<ImageView> createPageList() {
        List<ImageView> pageList = new ArrayList<>();
        pageList.add(createPageView(R.drawable.image_tutorial_1));
        pageList.add(createPageView(R.drawable.image_tutorial_2));
        pageList.add(createPageView(R.drawable.image_tutorial_3));
        pageList.add(createPageView(R.drawable.image_tutorial_4));
        pageList.add(createPageView(R.drawable.image_tutorial_5));
        pageList.add(createPageView(R.drawable.image_tutorial_6));

        return pageList;
    }

    @NonNull
    private ImageView createPageView(int image) {
        ImageView view = new ImageView(this);
        view.setImageResource(image);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_next_tutorial){
            int nIndex = pager.getCurrentItem();
            if (nIndex < 5){
                pager.setCurrentItem(nIndex + 1, true);
            }else{
                Intent intent = new Intent(TurtorialActivity.this, SignInActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }

        }
    }
}
