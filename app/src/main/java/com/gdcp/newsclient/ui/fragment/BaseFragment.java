package com.gdcp.newsclient.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.callback.EmptyCallback;
import com.gdcp.newsclient.callback.ErrorCallback;
import com.gdcp.newsclient.callback.LoadingCallback;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.callback.SuccessCallback;
import com.kingja.loadsir.core.Convertor;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

/**
 * Created by asus- on 2017/6/27.
 */

public abstract class BaseFragment extends Fragment {
    protected View mView;
    protected LoadService loadService;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView==null){
           mView= View.inflate(getActivity(),getLayoutRes(), null);
            loadService = LoadSir.getDefault().register(mView, new Callback.OnReloadListener() {
                @Override
                public void onReload(View v) {
                    loadService.showCallback(LoadingCallback.class);
                    reloadData();

                }}, new Convertor<Integer>() {
                @Override
                public Class<? extends Callback> map(Integer integer) {
                    Class<? extends Callback> resultCode = SuccessCallback.class;
                    switch (integer) {
                        case 100://成功回调
                            resultCode = SuccessCallback.class;
                            break;
                        case 0:
                            resultCode = ErrorCallback.class;
                            break;
                        case 25:
                            resultCode = EmptyCallback.class;
                            break;
                    }
                    return resultCode;
                }
            });
            initView();
            initData();
            //第三步：返回LoadSir生成的LoadLayout
        }

        return loadService.getLoadLayout();

    }

    protected abstract void reloadData();

    public abstract int getLayoutRes() ;

    public abstract void initView() ;

    public abstract void initData() ;
    private Toast mToast;
    protected void showToast(String msg){
        if (mToast==null){
            mToast= Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }

    protected void startActivity(boolean isFinish,Class activity){
        Intent intent=new Intent(getActivity(),activity);
        startActivity(intent);
        if (isFinish){
            getActivity().finish();
        }
    }
    /*// 判断当前网络状态是否为连接状态
    protected boolean isNetworkAvailable(Activity activity){
        Context context=activity.getApplicationContext();
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
        {
            return false;
        }
        else {
            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
            if (networkInfos != null && networkInfos.length > 0) {
                for (int i = 0; i < networkInfos.length; i++) {
                    if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }

        return false;

    }*/


}
