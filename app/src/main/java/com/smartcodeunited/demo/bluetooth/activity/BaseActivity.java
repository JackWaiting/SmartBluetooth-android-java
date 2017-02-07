/*
 * Copyright (C) 2016 SmartCodeUnited http://www.smartcodeunited.com
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
package com.smartcodeunited.demo.bluetooth.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.smartcodeunited.demo.bluetooth.R;
import com.smartcodeunited.demo.bluetooth.utils.NetworkUtil;
import com.smartcodeunited.demo.bluetooth.utils.SystemBarTintManager;

import java.lang.reflect.Type;

public abstract class BaseActivity extends FragmentActivity implements
        OnClickListener {
    private Toast mToast;
    protected TextView titleTv;
    private SystemBarTintManager tintManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(getResources().getColor(R.color.color_bar));
        initBase();
        initUI();
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * If there is a virtual buttons, may have a miscalculation
     *
     * @return
     */
    private boolean hasNavigationBar() {
        boolean hasMenuKey = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display d = getWindowManager().getDefaultDisplay();
            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);
            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);
            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;
            return realWidth > displayWidth || realHeight > displayHeight;
        }

        boolean hasPhysicsBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasPhysicsHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
        if (hasPhysicsBackKey && hasPhysicsHomeKey) {
            return false;
        } else {
            return true;
        }
    }


    public int getNavigationBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0 && hasNavigationBar()) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    protected int statusBarHeight;
    protected View rlTitle;
    private View leftBtn, rightBtn;
    private RelativeLayout rootLayout;
    protected View titleLayout;
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootLayout = (RelativeLayout) findViewById(R.id.root);
        rootLayout.setPadding(0, 0, 0, getNavigationBarHeight());
        int titleLayoutRes = getTitleLayoutId();
        if (titleLayoutRes > 0) {
            titleLayout = inflater.inflate(titleLayoutRes, rootLayout, false);
            titleLayout.setPadding(0, getStatusBarHeight(), 0, 0);
            if(titleLayoutRes == R.layout.common_title_layout){
                initTitleCenterLayout(inflater);
            }
            titleLayout.setId(R.id.title);
            rootLayout.addView(titleLayout);
        }

        View contentView = inflater.inflate(getContentLayoutId(), rootLayout, false);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        if (titleLayout != null) {
            if (contentView.getTag() ==null) {
                Object tag = contentView.getTag();
                params.addRule(RelativeLayout.BELOW, R.id.title);
            }
        }
        rootLayout.addView(contentView, params);

        if (titleLayout != null && contentView.getTag() == null) {
            titleLayout.bringToFront();
            rootLayout.invalidate();
        }

        leftBtn = findViewById(R.id.btn_left);

        if (leftBtn != null) {
            leftBtn.setOnClickListener(this);
        }
        rightBtn = findViewById(R.id.btn_right);
        if (rightBtn != null) {
            rightBtn.setOnClickListener(this);
        }
        titleTv = (TextView) findViewById(R.id.tv_title);
        rlTitle = findViewById(R.id.rl_title_base);
    }

    private View titleCenterView;
    private void initTitleCenterLayout(LayoutInflater inflater){
        RelativeLayout vg = (RelativeLayout) titleLayout;
        int titleCenter = getTitleCenterLayoutId();
        if(titleCenter > 0){
            titleCenterView = inflater.inflate(titleCenter, rootLayout, false);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            vg.addView(titleCenterView, params);
        }
    }


    /**
     * Set the visibility on the left side of the button
     *
     * @param visibility
     */
    protected void setLeftBtnVisibility(boolean visibility) {
        setButtonVisibility(leftBtn, visibility);
    }

    private int drawableHeight;
    private Drawable getCompondDrawable(int drawableResId) {
        if (drawableResId <= 0) {
            return null;
        }
        Drawable drawable = getResources().getDrawable(drawableResId);
        if (drawable != null) {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            if(height > drawableHeight){
                drawableHeight = height;
            }
            drawable.setBounds(0, 0, width, height);
        }
        return drawable;
    }

    /**
     * @param textResId         Words, if the < = 0 is not display text
     * @param drawableLeftResId Word on the left side of the picture, if < = 0 is not displayed
     */
    protected void setLeftBtn(int textResId, int drawableLeftResId) {
        setButton(leftBtn, 0, textResId, drawableLeftResId);
    }


    /**
     * Set the button on the right side visibility
     *
     * @param visibility
     */
    protected void setRightBtnVisibility(boolean visibility) {
        setButtonVisibility(rightBtn, visibility);
    }

    /**
     * @param textResId         Words, if the < = 0 is not display text
     * @param drawableRightResId Word on the left side of the picture, if < = 0 is not displayed
     */
    protected void setRightBtn(int textResId, int drawableRightResId) {
        setButton(rightBtn, 1, textResId, drawableRightResId);
    }

    /**
     * @param view
     * @param position To the left of the image position 0 and 1 on the right side
     * @param textResId
     * @param drawableResId
     */
    private void setButton(View view, int position, int textResId, int drawableResId) {
        if (view != null) {
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                if (textResId > 0) {
                    tv.setText(textResId);
                } else {
                    tv.setText("");
                }
                if(drawableResId > 0){
                    Drawable drawable = getCompondDrawable(drawableResId);
                    if (position == 0) {
                        tv.setCompoundDrawables(drawable, null, null, null);
                    } else {
                        tv.setCompoundDrawables(null, null, drawable, null);
                    }
                }else{
                    tv.setCompoundDrawables(null, null, null, null);
                }
            } else if (view instanceof ImageView) {
                ImageView iv = (ImageView) view;
                if(drawableResId > 0){
                    iv.setImageResource(drawableResId);
                }else{
                    iv.setImageDrawable(null);
                }
            }
        }
    }

    private void setButtonVisibility(View button, boolean visibility) {
        int visible = visibility ? View.VISIBLE : View.INVISIBLE;
        if (button != null) {
            button.setVisibility(visible);
        }
    }

    /**
     * To obtain the title layout involves id for tv_title TextView, otherwise can't set title text
     *
     * @return
     */
    protected int getTitleLayoutId() {
        return R.layout.common_title_layout;
    }

    protected int getTitleCenterLayoutId(){
        return R.layout.title_with_sub;
    }

    // 获取布局文件
    protected abstract int getContentLayoutId();

    // 初始换数据
    protected abstract void initBase();

    // 初始化控件
    protected abstract void initUI();


    protected void setTitleText(String title) {
        if (titleTv != null) {
            titleTv.setText(title);
        }
    }

    protected void setTitleText(int resId) {
        setTitleText(getString(resId));
    }

    /**
     * Toast, you can call in the child thread
     *
     * @param resId
     */
    public void showToast(final int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                makeToast(resId);
            }
        });
    }

    /**
     * Toast, you can call in the child thread
     *
     * @param content
     */
    public void showToast(final String content) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                makeToast(content);
            }
        });
    }

    /**
     * Cancel toast
     */
    public void cancelToast() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                }
            }
        });
    }

    public void startActivity(Class<? extends Activity> c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    @SuppressLint("InflateParams")
    private void makeToast(String text) {
        mToast = Toast.makeText(BaseActivity.this, text, Toast.LENGTH_SHORT);
        mToast.show();
    }

    private void makeToast(int resId) {
        makeToast(getResources().getString(resId));
    }

    /**
     * Check the network connection
     *
     * @param showToast If there is no network, whether to Toast
     * @return
     */
    public boolean checkNetwork(boolean showToast) {
        if (NetworkUtil.isAvailable(this)) {
            return true;
        } else if (showToast) {

        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
        }
    }

    protected static <T> T parse(String json, Type cls) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Hide the input method
     */
    public void hideInputMethod(View view) {
        InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null && m.isActive()) {
            if (view == null) {
                view = getCurrentFocus();
            }
            m.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
