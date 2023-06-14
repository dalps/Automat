package com.monopalla.automat.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.google.android.material.progressindicator.CircularProgressIndicator;

public class AnimUtils {
    public static void switchViewsWithCircularReveal(View view1, View view2) {
        int cx1 = view1.getWidth()/2;
        int cy1 = view1.getHeight()/2;

        int cx2 = view2.getWidth()/2;
        int cy2 = view2.getHeight()/2;

        float initialRadius1 = (float) Math.hypot(cx1, cy1);
        float finalRadius2 = (float) Math.hypot(cx2, cy2);

        Animator animHide = ViewAnimationUtils.createCircularReveal(
                view1, cx1, cy1, initialRadius1, 0f);
        Animator animShow = ViewAnimationUtils.createCircularReveal(
                view2, cx2, cy2, 0f, finalRadius2);

        animHide.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view1.setVisibility(View.INVISIBLE);
                view2.setVisibility(View.VISIBLE);
                animShow.start();
            }
        });

        animHide.start();
    }

    public static void switchViewsWithCircularRevealAndDelay(View view1, View view2, CircularProgressIndicator spinwheel) {
        int cx1 = view1.getWidth()/2;
        int cy1 = view1.getHeight()/2;

        int cx2 = view2.getWidth()/2;
        int cy2 = view2.getHeight()/2;

        float initialRadius1 = (float) Math.hypot(cx1, cy1);
        float finalRadius2 = (float) Math.hypot(cx2, cy2);

        Animator animHide = ViewAnimationUtils.createCircularReveal(
                view1, cx1, cy1, initialRadius1, 0f);
        Animator animShow = ViewAnimationUtils.createCircularReveal(
                view2, cx2, cy2, 0f, finalRadius2);

        animHide.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view1.setVisibility(View.INVISIBLE);

                new CountDownTimer(1000, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        view2.setVisibility(View.VISIBLE);
                        animShow.start();
                        spinwheel.hide();
                    }

                }.start();
            }
        });

        spinwheel.show();
        animHide.start();
    }

    public static void showViewWithCircularReveal(View view) {
        int cx = view.getWidth()/2;
        int cy = view.getHeight()/2;
        float finalRadius = (float) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(
                view, cx, cy, 0f, finalRadius);

        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    public static void hideViewWithCircularReveal(View view) {
        int cx = view.getWidth()/2;
        int cy = view.getHeight()/2;
        float initialRadius = (float) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(
                view, cx, cy, initialRadius, 0f);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });

        anim.start();
    }
}
