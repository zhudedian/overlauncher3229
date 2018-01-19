//package com.ider.overlauncher.utils;
//
//import android.animation.ObjectAnimator;
//import android.animation.PropertyValuesHolder;
//import android.os.Build;
//import android.view.View;
//
///**
// * Created by ider-eric on 2016/12/29.
// */
//
//public class EntryAnimation {
//
//    public static ObjectAnimator createFocusAnimator(View view) {
//        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.1f);
//        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.1f);
//        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY);
//        animator.setDuration(200);
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////            view.setTranslationZ(1);
////        }else {
////            view.bringToFront();
////        }
//        return animator;
//    }
//
//    public static ObjectAnimator createLoseFocusAnimator(View view) {
//        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1.1f, 1f);
//        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1.1f, 1f);
//       ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY);
//       animator.setDuration(100);
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////            view.setTranslationZ(0);
////        }
//        return animator;
//    }
//
//}
