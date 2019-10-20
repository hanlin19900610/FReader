package com.feng.freader.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.feng.freader.R;
import com.feng.freader.base.BaseActivity;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.view.fragment.BookshelfFragment;
import com.feng.freader.view.fragment.DiscoveryFragment;
import com.feng.freader.view.fragment.MoreFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "fzh";
    private static final int DUR_BOTTOM_BAR_ICON_ANIM = 500;
    private static final int FG_BOOKSHELF = 0;
    private static final int FG_DISCOVERY = 1;
    private static final int FG_MORE = 2;

    private View mBookshelfBg;
    private View mDiscoveryBg;
    private View mMoreBg;
    private ImageView mBookshelfBeforeIv;
    private ImageView mDiscoveryBeforeIv;
    private ImageView mMoreBeforeIv;
    private ImageView mBookshelfAfterIv;
    private ImageView mDiscoveryAfterIv;
    private ImageView mMoreAfterIv;

    private Animator mBookshelfAnim;
    private Animator mDiscoveryAnim;
    private Animator mMoreAnim;
    private TimeInterpolator mTimeInterpolator = new AccelerateDecelerateInterpolator();

    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private Fragment mBookshelfFragment;
    private Fragment mDiscoveryFragment;
    private Fragment mMoreFragment;
    private Fragment mCurrFragment; // 当前正在显示的 Fragment

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mBookshelfBg = findViewById(R.id.v_main_bottom_bar_bookshelf_bg);
        mBookshelfBg.setOnClickListener(this);
        mDiscoveryBg = findViewById(R.id.v_main_bottom_bar_discovery_bg);
        mDiscoveryBg.setOnClickListener(this);
        mMoreBg = findViewById(R.id.v_main_bottom_bar_more_bg);
        mMoreBg.setOnClickListener(this);

        mBookshelfBeforeIv = findViewById(R.id.iv_main_bottom_bar_bookshelf_before);
        mDiscoveryBeforeIv = findViewById(R.id.iv_main_bottom_bar_discovery_before);
        mMoreBeforeIv = findViewById(R.id.iv_main_bottom_bar_more_before);
        mBookshelfAfterIv= findViewById(R.id.iv_main_bottom_bar_bookshelf_after);
        mDiscoveryAfterIv = findViewById(R.id.iv_main_bottom_bar_discovery_after);
        mMoreAfterIv = findViewById(R.id.iv_main_bottom_bar_more_after);
    }

    @Override
    protected void doAfterInit() {
        changeFragment(FG_BOOKSHELF);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_main_bottom_bar_bookshelf_bg:
                // 如果已经点击了该菜单项，无视该操作
                if (mBookshelfAfterIv.getVisibility() == View.VISIBLE) {
                    break;
                }
                // 在开启当前菜单项的动画前，先切换其他菜单项的 icon
                mDiscoveryBeforeIv.setVisibility(View.VISIBLE);
                mDiscoveryAfterIv.setVisibility(View.INVISIBLE);
                mMoreBeforeIv.setVisibility(View.VISIBLE);
                mMoreAfterIv.setVisibility(View.INVISIBLE);
                // 开启当前菜单项的动画
                initBookshelfShowAnim();
                mBookshelfAfterIv.setVisibility(View.VISIBLE);
                mBookshelfAnim.start();
                // 切换 Fragment
                changeFragment(FG_BOOKSHELF);
                break;
            case R.id.v_main_bottom_bar_discovery_bg:
                // 如果已经点击了该菜单项，无视该操作
                if (mDiscoveryAfterIv.getVisibility() == View.VISIBLE) {
                    break;
                }
                // 在开启当前菜单项的动画前，先切换其他菜单项的 icon
                mBookshelfBeforeIv.setVisibility(View.VISIBLE);
                mBookshelfAfterIv.setVisibility(View.INVISIBLE);
                mMoreBeforeIv.setVisibility(View.VISIBLE);
                mMoreAfterIv.setVisibility(View.INVISIBLE);
                // 开启当前菜单项的动画
                initDiscoveryShowAnim();
                mDiscoveryAfterIv.setVisibility(View.VISIBLE);
                mDiscoveryAnim.start();
                // 切换 Fragment
                changeFragment(FG_DISCOVERY);
                break;
            case R.id.v_main_bottom_bar_more_bg:
                // 如果已经点击了该菜单项，无视该操作
                if (mMoreAfterIv.getVisibility() == View.VISIBLE) {
                    break;
                }
                // 在开启当前菜单项的动画前，先切换其他菜单项的 icon
                mBookshelfBeforeIv.setVisibility(View.VISIBLE);
                mBookshelfAfterIv.setVisibility(View.INVISIBLE);
                mDiscoveryBeforeIv.setVisibility(View.VISIBLE);
                mDiscoveryAfterIv.setVisibility(View.INVISIBLE);
                // 开启当前菜单项的动画
                initMoreShowAnim();
                mMoreAfterIv.setVisibility(View.VISIBLE);
                mMoreAnim.start();
                // 切换 Fragment
                changeFragment(FG_MORE);
                break;
            default:
                break;
        }
    }

    /**
     * 初始化“书架”图标的显示动画
     */
    private void initBookshelfShowAnim() {
        int cx = mBookshelfAfterIv.getMeasuredWidth() / 2;   // 揭露动画中心点的 x 坐标
        int cy = mBookshelfAfterIv.getMeasuredHeight() / 2;  // 揭露动画中心点的 y 坐标
        float startRadius = 0f;     // 开始半径
        float endRadius = (float) Math.max(mBookshelfAfterIv.getWidth()
                , mBookshelfAfterIv.getHeight()) / 2;  // 结束半径
        mBookshelfAnim = ViewAnimationUtils
                .createCircularReveal(mBookshelfAfterIv, cx, cy, startRadius, endRadius); // 创建揭露动画
        mBookshelfAnim.setDuration(DUR_BOTTOM_BAR_ICON_ANIM);
        mBookshelfAnim.setInterpolator(mTimeInterpolator);
        mBookshelfAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束时，隐藏 before icon
                if (mBookshelfAfterIv.getVisibility() == View.VISIBLE) {
                    mBookshelfBeforeIv.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    /**
     * 初始化“发现”图标的显示动画
     */
    private void initDiscoveryShowAnim() {
        int cx = mDiscoveryAfterIv.getMeasuredWidth() / 2;   // 揭露动画中心点的 x 坐标
        int cy = mDiscoveryAfterIv.getMeasuredHeight() / 2;  // 揭露动画中心点的 y 坐标
        float startRadius = 0f;     // 开始半径
        float endRadius = (float) Math.max(mDiscoveryAfterIv.getWidth()
                , mDiscoveryAfterIv.getHeight()) / 2;  // 结束半径
        mDiscoveryAnim = ViewAnimationUtils
                .createCircularReveal(mDiscoveryAfterIv, cx, cy, startRadius, endRadius); // 创建揭露动画
        mDiscoveryAnim.setDuration(DUR_BOTTOM_BAR_ICON_ANIM);
        mDiscoveryAnim.setInterpolator(mTimeInterpolator);
        mDiscoveryAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束时，隐藏 before icon
                if (mDiscoveryAfterIv.getVisibility() == View.VISIBLE) {
                    mDiscoveryBeforeIv.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    /**
     * 初始化“更多”图标的显示动画
     */
    private void initMoreShowAnim() {
        int cx = mMoreAfterIv.getMeasuredWidth() / 2;   // 揭露动画中心点的 x 坐标
        int cy = mMoreAfterIv.getMeasuredHeight() / 2;  // 揭露动画中心点的 y 坐标
        float startRadius = 0f;     // 开始半径
        float endRadius = (float) Math.max(mMoreAfterIv.getWidth()
                , mMoreAfterIv.getHeight()) / 2;  // 结束半径
        mMoreAnim = ViewAnimationUtils
                .createCircularReveal(mMoreAfterIv, cx, cy, startRadius, endRadius); // 创建揭露动画
        mMoreAnim.setDuration(DUR_BOTTOM_BAR_ICON_ANIM);
        mMoreAnim.setInterpolator(mTimeInterpolator);
        mMoreAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束时，隐藏 before icon
                if (mMoreAfterIv.getVisibility() == View.VISIBLE) {
                    mMoreBeforeIv.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    /**
     * 切换 Fragment
     *
     * @param i 切换后新的 Fragment
     *
     * 可选值：
     * @see #FG_BOOKSHELF 书架页面（BookshelfFragment）
     * @see #FG_DISCOVERY 发现页面（DiscoveryFragment）
     * @see #FG_MORE 更多页面（MoreFragment）
     */
    private void changeFragment(int i) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment showFragment = null;
        switch (i) {
            case FG_BOOKSHELF:
                if (mBookshelfFragment == null) {
                    mBookshelfFragment = new BookshelfFragment();
                    ft.add(R.id.fv_main_fragment_container, mBookshelfFragment);
                }
                showFragment = mBookshelfFragment;
                break;
            case FG_DISCOVERY:
                if (mDiscoveryFragment == null) {
                    mDiscoveryFragment = new DiscoveryFragment();
                    ft.add(R.id.fv_main_fragment_container, mDiscoveryFragment);
                }
                showFragment = mDiscoveryFragment;
                break;
            case FG_MORE:
                if (mMoreFragment == null) {
                    mMoreFragment = new MoreFragment();
                    ft.add(R.id.fv_main_fragment_container, mMoreFragment);
                }
                showFragment = mMoreFragment;
                break;
            default:
                break;
        }
        // 隐藏当前的 Fragment，显示新的 Fragment
        if (mCurrFragment != null) {
            ft.hide(mCurrFragment);
        }
        if (showFragment != null) {
            ft.show(showFragment);
        }
        mCurrFragment = showFragment;

        ft.commit();
    }
}
