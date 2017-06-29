package com.gdcp.newsclient.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gdcp.newsclient.R;

/**
 * Created by asus- on 2017/6/27.
 */

public abstract class BaseFragment extends Fragment {
    protected View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView==null){
            mView= LayoutInflater.from(getActivity()).inflate(getLayoutRes(),container,false);
            initView();
            initData();
        }
        return mView;
    }

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
}
