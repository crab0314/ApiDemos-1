/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.apis.app;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.

import com.example.android.apis.R;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


/**
 * <p>Example of using a custom animation when transitioning between activities.</p>
 */
public class Animation extends Activity {
    private static final String TAG = "Animation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_animation);

        // Watch for button clicks.
        Button button = (Button) findViewById(R.id.fade_animation);
        button.setOnClickListener(mFadeListener);
        button = (Button) findViewById(R.id.zoom_animation);
        button.setOnClickListener(mZoomListener);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            button = (Button) findViewById(R.id.modern_fade_animation);
            button.setOnClickListener(mModernFadeListener);
            button = (Button) findViewById(R.id.modern_zoom_animation);
            button.setOnClickListener(mModernZoomListener);
            button = (Button) findViewById(R.id.scale_up_animation);
            button.setOnClickListener(mScaleUpListener);
            button = (Button) findViewById(R.id.zoom_thumbnail_animation);
            button.setOnClickListener(mZoomThumbnailListener);
            button = (Button) findViewById(R.id.no_animation);
            button.setOnClickListener(mNoAnimationListener);
        } else {
            findViewById(R.id.modern_fade_animation).setEnabled(false);
            findViewById(R.id.modern_zoom_animation).setEnabled(false);
            findViewById(R.id.scale_up_animation).setEnabled(false);
            findViewById(R.id.zoom_thumbnail_animation).setEnabled(false);
        }
    }

    @Override
    public void onEnterAnimationComplete() {
        Log.i(TAG, "onEnterAnimationComplete");
    }

    private OnClickListener mFadeListener = new OnClickListener() {
        public void onClick(View v) {
            Log.i(TAG, "Starting fade-in animation...");
            // Request the next activity transition (here starting a new one).
            startActivity(new Intent(Animation.this, AlertDialogSamples.class));
            // Supply a custom animation.  This one will just fade the new
            // activity on top.  Note that we need to also supply an animation
            // (here just doing nothing for the same amount of time) for the
            // old activity to prevent it from going away too soon.
            /**
             * 参一是动画进入的模式，fromAlpha 0到1 通道设置透明度
             * 参二是动画退出的模式，fromXDelta 0到0 没有发生变化
             */
            overridePendingTransition(R.anim.fade, R.anim.hold);
        }
    };

    private OnClickListener mZoomListener = new OnClickListener() {
        public void onClick(View v) {
            Log.i(TAG, "Starting zoom-in animation...");
            // Request the next activity transition (here starting a new one).
            startActivity(new Intent(Animation.this, AlertDialogSamples.class));
            // This is a more complicated animation, involving transformations
            // on both this (exit) and the new (enter) activity.  Note how for
            // the duration of the animation we force the exiting activity
            // to be Z-ordered on top (even though it really isn't) to achieve
            // the effect we want.
            overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        }
    };

    private OnClickListener mModernFadeListener = new OnClickListener() {
        public void onClick(View v) {
            Log.i(TAG, "Starting modern-fade-in animation...");
            // Create the desired custom animation, involving transformations
            // on both this (exit) and the new (enter) activity.  Note how for
            // the duration of the animation we force the exiting activity
            // to be Z-ordered on top (even though it really isn't) to achieve
            // the effect we want.
            ActivityOptions opts = ActivityOptions.makeCustomAnimation(Animation.this,
                    R.anim.fade, R.anim.hold);
            // Request the activity be started, using the custom animation options.
            startActivity(new Intent(Animation.this, AlertDialogSamples.class), opts.toBundle());
        }
    };

    private OnClickListener mModernZoomListener = new OnClickListener() {
        public void onClick(View v) {
            Log.i(TAG, "Starting modern-zoom-in animation...");
            // Create a more complicated animation, involving transformations
            // on both this (exit) and the new (enter) activity.  Note how for
            // the duration of the animation we force the exiting activity
            // to be Z-ordered on top (even though it really isn't) to achieve
            // the effect we want.
            ActivityOptions opts = ActivityOptions.makeCustomAnimation(Animation.this,
                    R.anim.zoom_enter, R.anim.zoom_enter);
            // Request the activity be started, using the custom animation options.
            startActivity(new Intent(Animation.this, AlertDialogSamples.class), opts.toBundle());
        }
    };

    private OnClickListener mScaleUpListener = new OnClickListener() {
        public void onClick(View v) {
            Log.i(TAG, "Starting scale-up animation...");
            // Create a scale-up animation that originates at the button
            // being pressed.
            //说明下几个参数，第1个参数是scale哪个view的大小，
            // 第2和3个参数是以view为基点，从哪开始动画，这里是该view的中心，
            // 4和5参数是新的activity从多大开始放大，这里是从无到有的过程。
            ActivityOptions opts = ActivityOptions.makeScaleUpAnimation(
                    v, 0, 0, v.getWidth(), v.getHeight());
            // Request the activity be started, using the custom animation options.
            startActivity(new Intent(Animation.this, AlertDialogSamples.class), opts.toBundle());
        }
    };

    private OnClickListener mZoomThumbnailListener = new OnClickListener() {
        public void onClick(View v) {
            Log.i(TAG, "Starting thumbnail-zoom animation...");
            // Create a thumbnail animation.  We are going to build our thumbnail
            // just from the view that was pressed.  We make sure the view is
            // not selected, because by the time the animation starts we will
            // have finished with the selection of the tap.
            v.setDrawingCacheEnabled(true);
            v.setPressed(false);
            v.refreshDrawableState();
            //调用getDrawingCache一定要setDrawingCacheEnabled(true)
            Bitmap bm = v.getDrawingCache();
            Canvas c = new Canvas(bm);
            //c.drawARGB(255, 255, 0, 0);
            //第2个参数是指那个图片要放大，
            // 3和4参数表示从哪开始动画
            ActivityOptions opts = ActivityOptions.makeThumbnailScaleUpAnimation(
                    v, bm, 0, 0);
            // Request the activity be started, using the custom animation options.
            startActivity(new Intent(Animation.this, AlertDialogSamples.class), opts.toBundle());
            v.setDrawingCacheEnabled(false);
        }
    };

    private OnClickListener mNoAnimationListener = new OnClickListener() {
        public void onClick(View v) {
            Log.i(TAG, "Starting no animation transition...");
            // Request the next activity transition (here starting a new one).
            startActivity(new Intent(Animation.this, AlertDialogSamples.class));
            overridePendingTransition(0, 0);
        }
    };
}
