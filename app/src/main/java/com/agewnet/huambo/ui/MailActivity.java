package com.agewnet.huambo.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agewnet.huambo.R;
import com.agewnet.huambo.app.CommonConstant;
import com.agewnet.huambo.contract.MailContract;
import com.agewnet.huambo.databinding.ActivityMailBinding;
import com.agewnet.huambo.entity.LoginBean;
import com.agewnet.huambo.entity.MailBean;
import com.agewnet.huambo.entity.TitleBarBean;
import com.agewnet.huambo.entity.UserListBean;
import com.agewnet.huambo.presenter.MailPresenter;
import com.agewnet.huambo.util.ToolToast;
import com.agewnet.huambo.util.UserCache;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

/**
 * 通讯录列表
 */
public class MailActivity extends AppCompatActivity implements MailContract.View {
    /**
     * 列表
     */
    private RecyclerView mRecyclerView;
    /**
     * Adapter
     */
    HomeAdapter mHomeAdapter;
    /**
     * Binding view
     */
    ActivityMailBinding mActivityMailBinding;
    /**
     * 上下文
     */
    Context mContext;
    /**
     * 获取列表 Presenter
     */
    MailPresenter mMailPresenter;
    /**
     * 数据集合
     */
    static List<MailBean> mUserListBeen;
    static List<MailBean> mTempUserListBeen;
    /**
     * Title 对象
     */
    TitleBarBean mTitleBarBean;
    /**
     * 获取用户缓存信息
     */
    LoginBean mLoginBean;
    public UserListBean mUserBean;
    RxPermissions mRxPermissions;
    Presenter mPresenter;
    public static void newInstance(Context context) {
        Intent intent = new Intent(context, MailActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMailBinding = DataBindingUtil.setContentView(this, R.layout.activity_mail);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        mTitleBarBean = new TitleBarBean();
        mUserBean = new UserListBean();
        mRxPermissions = new RxPermissions(this); // where this is an Activity instance
        mContext = this;
        //   mMyExpandableListViewAdapter = new MyExpandableListViewAdapter();
        //  初始化Presenter
        mMailPresenter = new MailPresenter();
        mPresenter = new Presenter();
        mUserListBeen = new ArrayList<>();
        mTempUserListBeen = new ArrayList<>();
        //  绑定View
        mMailPresenter.attach(this);
        //绑定标题
        mActivityMailBinding.titleBar.setTitle(mTitleBarBean);
        //设置标题
        mTitleBarBean.setCenterText("通讯录");
        //获取用户数据
        mLoginBean = (LoginBean) UserCache.getSingleton(this).getObject(CommonConstant.USER_LOGINCACHE, LoginBean.class);
        mActivityMailBinding.titleBar.lyLeftContainer.setOnClickListener(view -> {
            finish();
        });
        //获取 expandablelistview
        mRecyclerView = mActivityMailBinding.rvMailList;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mHomeAdapter = new HomeAdapter();
        mRecyclerView.setAdapter(mHomeAdapter);
        mActivityMailBinding.setPresenter(mPresenter);
        //获取通讯录列表
        mMailPresenter.getMailList(mLoginBean.getUserName());
    }
    /**
     * 获取列表成功
     *
     * @param mailBean
     */
    @Override
    public void onSuccess(List<MailBean> mailBean) {
        mTempUserListBeen = mailBean;
        mUserListBeen.addAll(mTempUserListBeen);
        //刷新
        mHomeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(String errorMsg) {
        //数据获取失败
        ToolToast.error(errorMsg);
    }

    public class Presenter {

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (null != mUserListBeen) {
                mUserListBeen.clear();
                for (MailBean user : mTempUserListBeen) {
                    if (user.getKSName().indexOf(s.toString()) != -1) {
                        mUserListBeen.add(user);
                    }
                }
                mHomeAdapter.notifyDataSetChanged();
            }
        }
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    MailActivity.this).inflate(R.layout.parent_item, parent,
                    false));
            return holder;
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(mUserListBeen.get(position).getKSName());
            holder.itemView.setOnClickListener(arg0 -> {
                DepartmentActivity.newInstance(MailActivity.this, mUserListBeen.get(position));
            });
        }

        @Override
        public int getItemCount() {
            return mUserListBeen == null ? 0 : mUserListBeen.size();
        }
        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv;
            public MyViewHolder(View view) {
                super(view);
                tv = view.findViewById(R.id.parent_title);
            }
        }
    }
}
