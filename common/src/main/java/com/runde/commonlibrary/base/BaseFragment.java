package com.runde.commonlibrary.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.runde.commonlibrary.RDLibrary;
import com.runde.commonlibrary.server.ServiceFactory;
import com.runde.commonlibrary.R;

import com.runde.commonlibrary.customview.StatePage;
import com.runde.commonlibrary.global.ApplicationContext;
import com.runde.commonlibrary.global.CommonLibrarySDKManager;
import com.runde.commonlibrary.interfaces.CommonClickListener;
import com.runde.commonlibrary.net.BaseResponse;
import com.runde.commonlibrary.net.HttpResponseCode;
import com.runde.commonlibrary.net.exception.ExceptionHandle;
import com.runde.commonlibrary.utils.CustomProgressDialog;
import com.runde.commonlibrary.utils.LazyOnClickListener;
import com.squareup.leakcanary.RefWatcher;

/**
 * Create by: xiaoguoqing
 * Date: 2018/12/24 0024
 * description:
 */
public class BaseFragment extends BaseFragmentBackPress implements IBaseView, IToolbar, IStatePage {

    private boolean isOnActivityCreated;
    private boolean isVisible;
    private boolean isFirstVisible;
    private FrameLayout flBaseUiContent;
    protected ViewGroup flBaseUiState;
    private RelativeLayout rlBaseUiRoot;
    public MenuItem actionMain;
    private TextView tvTitleSubject;
    private Toolbar tbCommonToolbar;
    private TextView tvCommonTitle;
    private TextView tvCommonIconText;
    private StatePage statePage;
    private boolean isGetDatas = false;
    private CustomProgressDialog customProgressDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_base_ui, container, false);
        rlBaseUiRoot = view.findViewById(R.id.rl_base_ui_root);
        flBaseUiContent = view.findViewById(R.id.fl_base_ui_content);
        flBaseUiState = view.findViewById(R.id.fl_base_ui_state);
        tbCommonToolbar = view.findViewById(R.id.common_toolbar);
        tvCommonTitle = view.findViewById(R.id.common_title);
        tvCommonIconText = view.findViewById(R.id.common_icon_text);
        initToolbar(tbCommonToolbar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //??????????????????????????????????????????ViewModel?????????View??????????????????
        initViewObservable();
        //?????????????????????
        initParams();
        initViews();
        initDatas();
        setListener();
    }

    public void setListener() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isOnActivityCreated = true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.clear();
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //        //?????????activity????????????fragment????????????activity
        //        if (!mIsNotToFinishActivity)
        //            if (getFragmentManager() != null && getFragmentManager().getFragments().size() == 0 && getActivity() != null) {
        //                getActivity().finish();
        //            }
        if (customProgressDialog != null) {
            customProgressDialog.hideProgressDialog();
        }
        if (statePage != null) {
            statePage.onDestroy();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (RDLibrary.isDebug()) {
            RefWatcher refWatcher = CommonLibrarySDKManager.getRefWatcher();
            if (refWatcher != null) {
                refWatcher.watch(this);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    /**
     * ???????????????Fragment??????????????????.
     * ?????????????????????????????????onActivityCreated??????????????????????????????
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
            if (isOnActivityCreated && !isFirstVisible) {
                lazyLoad();
                isFirstVisible = true;
            }
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected boolean isFragmentVisible() {
        return isVisible;
    }

    protected boolean isOnActivityCreated() {
        return isOnActivityCreated;
    }

    protected void onVisible() {
    }

    protected void onInvisible() {
    }

    //???????????????
    public void lazyLoad() {

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
        errorLiveData.observe(this, stringMyResponse -> {
            onApiErrorCallBack(stringMyResponse.getCode(), stringMyResponse.getRepType(),
                    stringMyResponse.getMsg(), stringMyResponse.getData());
            onApiErrorCallBack(stringMyResponse);
        });

        succeedLiveData.observe(this, objectMyResponse -> {
            onApiSuccessCallBack(objectMyResponse.getCode(), objectMyResponse.getRepType(),
                    objectMyResponse.getData());
            onApiSuccessCallBack(objectMyResponse);
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
    public void initViews() {

    }

    //?????????????????????
    @Override
    public void initParams() {

    }

    //
    @Override
    public void onEnterAnimationEnds() {
    }


    //?????????????????????
    @Override
    public void initDatas() {
        if (!isGetDatas) {
            isGetDatas = true;
            //?????????????????????
            onEnterAnimationEnds();
        }
    }

    //??????????????????????????????????????????ViewModel?????????View??????????????????
    @Override
    public void initViewObservable() {

    }

    public View setCenterView(int layout) {
        if (flBaseUiContent != null) {
            flBaseUiContent.removeAllViews();
            View addView = getLayoutInflater().inflate(layout, flBaseUiContent, false);
            flBaseUiContent.addView(addView);
            return addView;
        }
        return null;
    }

    public void setCenterView(View childView) {
        if (flBaseUiContent != null) {
            flBaseUiContent.removeAllViews();
            flBaseUiContent.addView(childView);
        }
    }

    /**
     * @param isShowNormal  true??????????????????????????????false?????????????????????????????????????????????loading
     * @param strLoadingTip
     */
    /*statePage ?????? start*/
    @Override
    public void showDialog(boolean isShowNormal, String strLoadingTip) {
        if (!isShowNormal && flBaseUiState != null) {
            checkStatePage();
            statePage.showLoading(strLoadingTip);
            flBaseUiState.setVisibility(View.VISIBLE);
        } else if (isShowNormal) {
            if (customProgressDialog == null) {
                customProgressDialog = new CustomProgressDialog(getContext());
            }
            customProgressDialog.showProgressDialog(strLoadingTip);
        }
    }

    @Override
    public void dismissDialog() {
        if (customProgressDialog != null) {
            customProgressDialog.hideProgressDialog();
        }
        setPageSuccess();
    }

    @Override
    public synchronized void checkStatePage() {
        if (null == statePage) {
            if (flBaseUiState != null && getActivity() != null) {
                statePage = new StatePage(getActivity());
                flBaseUiState.setVisibility(View.GONE);
                flBaseUiState.addView(statePage);
            }
        }
    }

    protected void setStatePageBg(int color) {
        checkStatePage();
        statePage.setStatePageBg(color);
    }


    @Override
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

    @Override
    public void setPageSuccess() {
        if (flBaseUiContent != null) {
            flBaseUiContent.setVisibility(View.VISIBLE);
        }

        if (flBaseUiState != null) {
            checkStatePage();
            statePage.setPageSuccess();
            flBaseUiState.setVisibility(View.GONE);
        }
    }

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
        statePage.resetEmptyBackgroud(ContextCompat.getDrawable(getContext(), drawableId));
        statePage.setPageEmpty(tip, option, onClickListener);
        flBaseUiState.setVisibility(View.VISIBLE);
    }

    /* statePage ?????? end */


    /*Toolbar star*/
    @Override
    public void initToolbar(Toolbar toolbar) {
        if (this.tbCommonToolbar != null) {
            this.tbCommonToolbar.setVisibility(View.GONE);//?????????????????????
        }
        // ????????????toolbar??????????????????????????????
        // ???????????????????????????????????????initView???????????????????????????????????????????????????initToolbar????????????????????????
        toolbar.setNavigationOnClickListener(new LazyOnClickListener() {
            @Override
            public void onLazyClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        toolbar.inflateMenu(R.menu.menu_title);
        actionMain = toolbar.getMenu()
                .findItem(R.id.action_main)
                .setVisible(false);
        toolbar.setBackgroundColor(ApplicationContext.getColor(R.color.white));
        toolbar.setNavigationIcon(ApplicationContext.getDrawable(R.drawable.app_toolbar_back));
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
        if (tbCommonToolbar != null) {
            tbCommonToolbar.setBackgroundColor(color);
        }
    }

    //??????????????????icon
    @Override
    public void setToolbarMainMenuItem(Drawable icon, MenuItem.OnMenuItemClickListener listener) {
        actionMain.setVisible(true);
        actionMain.setIcon(icon);
        actionMain.setOnMenuItemClickListener(listener);
    }

    //??????toolbar??????????????????icon?????????
    public void setToolbarIconText(int iconId, String title, int textColor, int padding, View.OnClickListener listener) {
        tvCommonIconText.setText(title);
        tvCommonIconText.setTextColor(ApplicationContext.getColor(textColor));
        Drawable drawable = ApplicationContext.getDrawable(iconId);
        tvCommonIconText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        tvCommonIconText.setCompoundDrawablePadding(padding);
        tvCommonIconText.setOnClickListener(listener);
        tvCommonIconText.setVisibility(View.VISIBLE);
    }

    //????????????????????????
    @Override
    public void setToolbarMainMenuItem(String title, MenuItem.OnMenuItemClickListener listener) {
        actionMain.setVisible(true);
        actionMain.setTitle(title);
        actionMain.setOnMenuItemClickListener(listener);
    }

    public void setToolbarDownPull(int drawableId) {
        Drawable drawabledown = getResources().getDrawable(drawableId);
        tvCommonTitle.setCompoundDrawablesWithIntrinsicBounds(null,
                null, drawabledown, null);
        tvCommonTitle.setCompoundDrawablePadding(8);
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
            tbCommonToolbar.setElevation(value);
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

    public void closeSoftInput(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && ((Activity) context).getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private static final int MIN_DELAY_TIME = 500;  // ??????????????????????????????500ms
    private static long lastClickTime;

    /**
     * ?????????????????????ButterKnife????????????onViewClicked???????????????
     *
     * @return
     */
    public boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }


}
