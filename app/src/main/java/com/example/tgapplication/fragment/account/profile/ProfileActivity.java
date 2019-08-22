package com.example.tgapplication.fragment.account.profile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends BaseActivity {

    ViewPager viewPager;
    CustomAdapter adapter;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.profile_details)
    ConstraintLayout profileDetails;
    //    @BindView(R.id.bottomNav)
//    ConstraintLayout bottomNav;
    private int[] images = {R.drawable.image1, R.drawable.login_bg, R.drawable.image1, R.drawable.login_bg, R.drawable.image1};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        ButterKnife.bind(this);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int i = position + 1;
                Log.i(TAG, "onCreate: " + i);
                textView.setText(i + " / " + images.length);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        adapter = new CustomAdapter(this, images);
        viewPager.setAdapter(adapter);

    }

    @OnClick({R.id.iv_info, R.id.iv_msg, R.id.iv_trip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_info:
                Log.i(TAG, "onViewClicked: " + constraintLayout.getHeight());
                if (constraintLayout.getTranslationY() != 0) {
                    constraintLayout.animate().translationY(0).alpha(1).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            profileDetails.setVisibility(View.GONE);
                        }
                    });
//                    bottomNav.animate().translationY(0);


                } else {
                    constraintLayout.animate().translationY(180 - constraintLayout.getHeight()).alpha(0.5f).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            profileDetails.setVisibility(View.VISIBLE);
                        }
                    });

//                    bottomNav.animate().translationY(100-constraintLayout.getHeight());
                }

              /*  Animation animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
                constraintLayout.startAnimation(animSlideUp);*/
//                Need to change

                break;
            case R.id.iv_msg:
                if (constraintLayout.getVisibility() == View.GONE) {
                    constraintLayout.setVisibility(View.VISIBLE);
                } else {
                    constraintLayout.setVisibility(View.GONE);
                }

                break;
            case R.id.iv_trip:
                if (constraintLayout.getVisibility() == View.GONE) {
                    constraintLayout.setVisibility(View.VISIBLE);
                } else {
                    constraintLayout.setVisibility(View.GONE);
                }

                break;
        }
    }
}
