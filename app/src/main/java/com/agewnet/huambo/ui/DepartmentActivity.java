package com.agewnet.huambo.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agewnet.huambo.R;
import com.agewnet.huambo.app.CommonConstant;
import com.agewnet.huambo.databinding.ActivityDepartmentBinding;
import com.agewnet.huambo.entity.MailBean;
import com.agewnet.huambo.entity.TitleBarBean;
import com.agewnet.huambo.entity.UserListBean;
import com.agewnet.huambo.util.ToolToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;


/**
 * 部门详情
 */
public class DepartmentActivity extends AppCompatActivity {
    ActivityDepartmentBinding mActivityDepartmentBinding;
    DepartmentAdapter mDepartmentAdapter;

    MailBean mMailBean;
    Presenter mPresenter;
    static ArrayList<UserListBean> mUserListBeans;
    static ArrayList<UserListBean> mTempUserListBeans;
    TitleBarBean mTitleBarBean;

    public static void newInstance(Context context, MailBean mailBean) {
        Intent intent = new Intent(context, DepartmentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CommonConstant.DEPARTMENTLIST, mailBean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityDepartmentBinding = DataBindingUtil.setContentView(this, R.layout.activity_department);
        Bundle bundle = getIntent().getExtras();
        mPresenter = new Presenter();
        mUserListBeans = new ArrayList<>();
        mTempUserListBeans = new ArrayList<>();
        mTitleBarBean = new TitleBarBean();
        mTitleBarBean.setCenterText("通讯录");
        mDepartmentAdapter = new DepartmentAdapter();
        mMailBean = new MailBean();

        mActivityDepartmentBinding.rvMailList.setLayoutManager(new LinearLayoutManager(this));
        mActivityDepartmentBinding.rvMailList.setAdapter(mDepartmentAdapter);
        mActivityDepartmentBinding.setPresenter(mPresenter);
        mActivityDepartmentBinding.titleBar.ivTitleBarLeftImg.setOnClickListener(view -> finish());
        if (null != bundle) {
            mMailBean = bundle.getParcelable(CommonConstant.DEPARTMENTLIST);
            if (null != mMailBean) {
                mPresenter.mTitle.set(mMailBean.getKSName());
                mUserListBeans = (ArrayList<UserListBean>) mMailBean.getUserList();
                mTempUserListBeans.addAll(mUserListBeans);
                mDepartmentAdapter.notifyDataSetChanged();
            }
        } else {
            ToolToast.error("请重试...");
            finish();
        }
    }

    public class Presenter {
        public ObservableField<String> mTitle = new ObservableField<>();
        public ObservableField<String> mSearch = new ObservableField<>();
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //搜索框文本改变事件
            if (null != mTempUserListBeans) {
                mTempUserListBeans.clear();
                for (UserListBean user : mUserListBeans) {
                    if (user.getUserName().indexOf(s.toString()) != -1) {
                        mTempUserListBeans.add(user);
                    }
                }
                mDepartmentAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Adapter
     */
    class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    DepartmentActivity.this).inflate(R.layout.child_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(mTempUserListBeans.get(position).getUserName());
            holder.itemView.setOnClickListener(arg0 -> {
                DepartmentInfoActivity.newInstance(DepartmentActivity.this, mMailBean.getKSName(), mTempUserListBeans.get(position));
            });
        }

        @Override
        public int getItemCount() {
            return mTempUserListBeans == null ? 0 : mTempUserListBeans.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv;
            public MyViewHolder(View view) {
                super(view);
                tv = view.findViewById(R.id.child_title);
            }
        }
    }
}
