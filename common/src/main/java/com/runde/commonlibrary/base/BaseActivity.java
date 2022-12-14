package com.runde.commonlibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.launcher.ARouter;
import com.runde.commonlibrary.R;
import com.runde.commonlibrary.RDLibrary;
import com.runde.commonlibrary.constants.BaseRouterPathConstants;
import com.runde.commonlibrary.customview.StatePage;
import com.runde.commonlibrary.global.AppManager;
import com.runde.commonlibrary.global.ApplicationContext;
import com.runde.commonlibrary.global.CommonLibrarySDKManager;
import com.runde.commonlibrary.interfaces.CommonClickListener;
import com.runde.commonlibrary.net.BaseResponse;
import com.runde.commonlibrary.net.HttpResponseCode;
import com.runde.commonlibrary.net.exception.ExceptionHandle;
import com.runde.commonlibrary.server.ServiceFactory;
import com.runde.commonlibrary.utils.CustomProgressDialog;
import com.runde.commonlibrary.utils.DisplayUtil;
import com.runde.commonlibrary.utils.LLog;
import com.runde.commonlibrary.utils.LazyOnClickListener;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

public class BaseActivity extends AppCompatActivity implements IBaseView, IToolbar, IStatePage {

    private FrameLayout flBaseUiContent;
    protected ViewGroup flBaseUiState;
    private MenuItem actionMain;
    protected Toolbar tbCommonToolbar;
    protected TextView tvCommonTitle;
    protected TextView tvCommonIconText;
    private StatePage statePage;
    private boolean isGetDatas = false;
    private CustomProgressDialog customProgressDialog;
    private TextView mCloseTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //???????????????????????????????????????????????????????????????????????????????????????????????????
        if (savedInstanceState != null) {
            AppManager.getAppManager().finishAllActivity();
            ARouter.getInstance().build(BaseRouterPathConstants.SPLASH_ACTIVITY)
                    .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .navigation(this);
        }
        //??????????????????????????????????????????ViewModel?????????View??????????????????
        initViewObservable();
        //?????????????????????
        initParams();
        initViews();
        initDatas();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customProgressDialog != null) {
            customProgressDialog.hideProgressDialog();
        }
        if (statePage != null) {
            statePage.onDestroy();
        }
        if (RDLibrary.isDebug()) {
            RefWatcher refWatcher = CommonLibrarySDKManager.getRefWatcher();
            if (refWatcher != null) {
                refWatcher.watch(this);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // ?????????Activity??????Fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments == null) {
            return;
        }
        // ?????????Fragment???onRequestPermissionsResult???????????????
        for (Fragment fragment : fragments) {
            if (fragment != null) {
                // ????????????????????????Fragment??????onRequestPermissionsResult??????
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.clear();
        super.onSaveInstanceState(outState);
    }

    @Override
    public void registerViewModelObserver(BaseViewModel viewModel) {
        observeLiveData(viewModel.getErrorLiveData(), viewModel.getSuccessLiveData());
    }

    @Override
    public void registerViewModelObserver(BaseViewModelV2 baseViewModel) {
        observeLiveData(baseViewModel.getErrorLiveData(), baseViewModel.getSuccessLiveData());
    }

    private void observeLiveData(MutableLiveData<BaseResponse<Object>> errorLiveData,
                                 MutableLiveData<BaseResponse<Object>> succeedLiveData) {
        errorLiveData.observe(this, new Observer<BaseResponse<Object>>() {
            @Override
            public void onChanged(@NonNull BaseResponse<Object> stringMyResponse) {
                onApiErrorCallBack(stringMyResponse.getCode(), stringMyResponse.getRepType(),
                        stringMyResponse.getMsg(), stringMyResponse.getData());
                onApiErrorCallBack(stringMyResponse);
            }
        });

        succeedLiveData.observe(this, new Observer<BaseResponse<Object>>() {
            @Override
            public void onChanged(@NonNull BaseResponse<Object> objectMyResponse) {
                onApiSuccessCallBack(objectMyResponse.getCode(), objectMyResponse.getRepType(),
                        objectMyResponse.getData());
                onApiSuccessCallBack(objectMyResponse);
            }
        });
    }

    public <T extends ViewModel> T createViewModel(@NonNull Class<T> modelClass) {
        return ViewModelProviders.of(this).get(modelClass);
    }

    /**
     * ???????????????????????????
     *
     * @param respondCode ?????????
     * @param respondType ?????????
     * @param message     ????????????
     * @param data
     */
    @Override
    public void onApiErrorCallBack(int respondCode, @Nullable String respondType, @Nullable String message, @Nullable Object data) {

    }

    @Override
    public void onApiErrorCallBack(BaseResponse<Object> baseResponse) {

    }

    /**
     * ???????????????????????????
     *
     * @param respondCode ?????????
     * @param respondType ?????????
     * @param data        View????????????????????????????????????????????????
     */
    @Override
    public void onApiSuccessCallBack(int respondCode, @Nullable String respondType, @Nullable Object data) {

    }

    @Override
    public void onApiSuccessCallBack(BaseResponse<Object> baseResponse) {

    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        for (Fragment fragment : fragments) {
            /*????????????????????????Fragment?????????  ????????????????????????????????????*/
            if (fragment instanceof BaseFragmentBackPress) {
                if (((BaseFragmentBackPress) fragment).onBackPressed()) {
                    /*???Fragment?????????????????????*/
                    return;
                }
            }
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        for (Fragment fragment : fragments) {
            if (fragment instanceof BaseFragment) {
                (fragment).onActivityResult(requestCode, resultCode, data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    //??????????????????????????????????????????ViewModel?????????View??????????????????
    @Override
    public void initViewObservable() {
    }

    //?????????????????????
    @Override
    public void initParams() {

    }

    @Override
    public void onEnterAnimationEnds() {

    }

    @Override
    public void initViews() {
        super.setContentView(R.layout.activity_base_ui);
        flBaseUiContent = findViewById(R.id.fl_base_ui_content);
        flBaseUiState = findViewById(R.id.fl_base_ui_state);
        tbCommonToolbar = findViewById(R.id.common_toolbar);
        tvCommonTitle = findViewById(R.id.common_title);
        tvCommonIconText = findViewById(R.id.common_icon_text);
        mCloseTv = findViewById(R.id.close_tv);
        initToolbar(tbCommonToolbar);
        //        checkStatePage();
    }


    //?????????????????????
    @Override
    public void initDatas() {
        onEnterAnimationEnds();
    }

    public void setCenterView(int layout) {
        if (flBaseUiContent != null) {
            flBaseUiContent.removeAllViews();
            View addView = getLayoutInflater().inflate(layout, flBaseUiContent,
                    false);
            flBaseUiContent.addView(addView);
        }
    }

    public void setCenterView(View view) {
        if (flBaseUiContent != null) {
            flBaseUiContent.removeAllViews();
            flBaseUiContent.addView(view);
        }
    }

    /**
     * @param isShowNormal  true??????????????????????????????false?????????????????????????????????????????????loading
     * @param strLoadingTip
     */
    /*statePage ?????? start*/
    //??????loading
    @Override
    public void showDialog(boolean isShowNormal, String strLoadingTip) {
        if (!isShowNormal && flBaseUiState != null) {
            checkStatePage();
            statePage.showLoading(strLoadingTip);
            flBaseUiState.setVisibility(View.VISIBLE);
        } else if (isShowNormal) {
            if (customProgressDialog == null) {
                customProgressDialog = new CustomProgressDialog(this);
            }
            customProgressDialog.showProgressDialog(strLoadingTip);

        }
    }


    public void showDialog(Activity activity, String strLoadingTip) {
        if (customProgressDialog == null) {
            customProgressDialog = new CustomProgressDialog(activity);
        }
        customProgressDialog.showProgressDialog(strLoadingTip);
    }

    //??????loading
    @Override
    public void dismissDialog() {
        if (customProgressDialog != null) {
            customProgressDialog.hideProgressDialog();
        }
        setPageSuccess();
    }

    public synchronized void checkStatePage() {
        if (null == statePage) {
            if (flBaseUiState != null) {
                statePage = new StatePage(this);
                flBaseUiState.setVisibility(View.GONE);
                flBaseUiState.addView(statePage);
            }
        }
    }

    /**
     * ??????????????????ViewGroup
     */
    public void resetStatePageParent(ViewGroup parent, int index) {
        checkStatePage();
        statePage.setVisibility(View.VISIBLE);
        flBaseUiState.setVisibility(View.GONE);
        if (!flBaseUiState.equals(parent)) {
            flBaseUiState.removeAllViews();
            flBaseUiState = parent;
            if (flBaseUiState.getChildCount() == index) {
                flBaseUiState.addView(statePage, index);
            }
        }
    }

    public void showCloseTv() {
        mCloseTv.setVisibility(View.VISIBLE);
        mCloseTv.setOnClickListener(new LazyOnClickListener() {
            @Override
            public void onLazyClick(View v) {
                finish();
            }
        });
    }

    public void hideCloseTv() {
        mCloseTv.setVisibility(View.GONE);
    }

    /**
     * ??????????????????????????????UI(??????????????????)
     */
    public void setPageSuccess() {
        if (flBaseUiContent != null) {
            checkStatePage();
            flBaseUiContent.setVisibility(View.VISIBLE);
            statePage.setPageSuccess();
            flBaseUiState.setVisibility(View.GONE);
        }
    }


    /**
     * ????????????????????????????????????UI
     */
    @Override
    public void setPageError(int responCode, String errorMessage, CommonClickListener commonClickListener) {
        checkStatePage();
        flBaseUiState.setVisibility(View.VISIBLE);
        if (responCode == ExceptionHandle.ERROR.NETWORK_DISABLE) {
            statePage.resetErrorBackgroud(ServiceFactory.getInstance().getCommonService().getStatepageConnectWrong());
            statePage.setPageError(ApplicationContext.getString(R.string.statepage_no_network),
                    new LazyOnClickListener() {
                        @Override
                        public void onLazyClick(View v) {
                            commonClickListener.click(v);
                        }
                    });
        } else if (responCode == HttpResponseCode.NODATAS) {
            setPageEmpty(ServiceFactory.getInstance().getCommonService().getStatepageNodata(), errorMessage,
                    getString(R.string.statepage_reload),
                    new LazyOnClickListener() {
                        @Override
                        public void onLazyClick(View v) {
                            commonClickListener.click(v);
                        }
                    });
        } else {
            setPageEmpty(ServiceFactory.getInstance().getCommonService().getStatepageConnectWrong(), errorMessage,
                    getString(R.string.statepage_reload),
                    new LazyOnClickListener() {
                        @Override
                        public void onLazyClick(View v) {
                            commonClickListener.click(v);
                        }
                    });
        }
    }

    /**
     * ??????????????????????????????????????????UI
     */
    @Override
    public void setPageEmpty(@Nullable Drawable drawable, CharSequence tip) {
        checkStatePage();
        statePage.resetEmptyBackgroud(drawable);
        statePage.setPageEmpty(tip);
        flBaseUiState.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPageEmpty(@Nullable Drawable drawable, CharSequence tip, CharSequence option, View.OnClickListener
            onClickListener) {
        checkStatePage();
        statePage.resetEmptyBackgroud(drawable);
        statePage.setPageEmpty(tip, option, onClickListener);
        flBaseUiState.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPageEmpty(int drawableId, CharSequence tip, CharSequence option, View.OnClickListener
            onClickListener) {

        checkStatePage();
        statePage.resetEmptyBackgroud(ContextCompat.getDrawable(this, drawableId));
        statePage.setPageEmpty(tip, option, onClickListener);
        flBaseUiState.setVisibility(View.VISIBLE);

    }
    /* statePage ?????? end */

    public void closeSoftInput(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && ((Activity) context).getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /*Toolbar star*/
    @Override
    public void initToolbar(Toolbar toolbar) {
        // ????????????toolbar??????????????????????????????
        // ???????????????????????????????????????initView???????????????????????????????????????????????????initToolbar????????????????????????
        toolbar.setNavigationIcon(ApplicationContext.getDrawable(R.drawable.app_toolbar_back));
        toolbar.setNavigationOnClickListener(new LazyOnClickListener() {
            @Override
            public void onLazyClick(View v) {
                closeSoftInput(BaseActivity.this);
                onBackPressed();
            }
        });

        toolbar.inflateMenu(R.menu.menu_title);
        actionMain = toolbar.getMenu()
                .findItem(R.id.action_main)
                .setVisible(false);
        toolbar.setBackgroundColor(ApplicationContext.getColor(R.color.white));
    }

    public TextView getTvCommonIconText() {
        return tvCommonIconText;
    }

    //??????????????????
    @Override
    public void setToolbarTopTitle(String strTitle) {
        tvCommonTitle.setText(strTitle);
    }

    @Override
    public TextView getToolbarTopTitle() {
        return tvCommonTitle;
    }

    //??????????????????????????????
    @Override
    public void setToolbarTopTitleColor(int colorId) {
        tvCommonTitle.setTextColor(colorId);
    }

    //???????????????????????????
    @Override
    public void setToolbarBackgroudColor(int color) {
        tbCommonToolbar.setBackgroundColor(color);
    }

    //??????????????????icon
    @Override
    public void setToolbarMainMenuItem(Drawable icon, MenuItem.OnMenuItemClickListener listener) {
        actionMain.setVisible(true);
        actionMain.setIcon(icon);
        actionMain.setOnMenuItemClickListener(listener);
    }

    //????????????????????????
    @Override
    public void setToolbarMainMenuItem(String title, MenuItem.OnMenuItemClickListener listener) {
        actionMain.setVisible(true);
        actionMain.setTitle(title);
        actionMain.setOnMenuItemClickListener(listener);
    }

    public void setToolbarMainMenuItem(String title) {
        if (actionMain != null) {
            actionMain.setVisible(true);
            actionMain.setTitle(title);
        }
    }

    public String getMenuItemTitle() {
        if (actionMain != null && actionMain.getTitle() != null) {
            return actionMain.getTitle().toString();
        } else {
            return "";
        }
    }

    //???????????????????????????????????????
    @Override
    public void setToolbarMainMenuItem(String title, int color,
                                       MenuItem.OnMenuItemClickListener listener) {
        actionMain.setVisible(true);
        actionMain.setTitle(title);
        actionMain.setOnMenuItemClickListener(listener);
        hackSetToolbarMenuItemTextColor(tbCommonToolbar, R.id.action_main, color);
    }

    @Override
    public void hideToolbarMainMenuItem() {
        actionMain.setVisible(false);
    }

    @Override
    public void showToolbarMainMenuItem() {
        actionMain.setVisible(true);
    }

    //??????toolbar???????????????
    @Override
    public Toolbar getToolbar() {
        return tbCommonToolbar;
    }

    @Override
    public void setToolbarElevation(float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tbCommonToolbar.setElevation(DisplayUtil.dip2px(value));
        }
    }

    /**
     * @param toolbar  ?????????menuitem???toolbar
     * @param actionId menuitem?????????id
     * @param color    ??????????????????
     * @Description hack????????????menuItem?????????
     */
    @Override
    public void hackSetToolbarMenuItemTextColor(Toolbar toolbar, int actionId, int color) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            final View v = toolbar.getChildAt(i);
            if (v instanceof androidx.appcompat.widget.ActionMenuView) {
                int childCount = ((androidx.appcompat.widget.ActionMenuView) v).getChildCount();
                for (int j = 0; j < childCount; j++) {
                    final View innerView = ((androidx.appcompat.widget.ActionMenuView) v).getChildAt(j);
                    if (innerView instanceof ActionMenuItemView) {
                        ActionMenuItemView amiv = (ActionMenuItemView) innerView;
                        int itemId = amiv.getId();
                        if (itemId == actionId) {
                            amiv.setTextColor(color);
                        }
                    }
                }
            }
        }
    }
    /*Toolbar end*/
}
