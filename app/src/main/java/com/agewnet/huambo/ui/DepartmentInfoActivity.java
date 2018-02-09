package com.agewnet.huambo.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;

import com.agewnet.huambo.R;
import com.agewnet.huambo.app.CommonConstant;
import com.agewnet.huambo.databinding.ActivityDepartmentInfoBinding;
import com.agewnet.huambo.entity.TitleBarBean;
import com.agewnet.huambo.entity.UserListBean;
import com.agewnet.huambo.util.ToolToast;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * 详情
 */
public class DepartmentInfoActivity extends AppCompatActivity {
    ActivityDepartmentInfoBinding mActivityDepartmentInfoBinding;
    RxPermissions mRxPermissions;
    UserListBean mUserListBean;
    PopupContactsInfoControl mPopupContactsInfoControl;
    TitleBarBean mTitleBarBean;

    public static void newInstance(Context context, String department, UserListBean userListBean) {
        Intent inetent = new Intent(context, DepartmentInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CommonConstant.DEPARTMENTLIST, userListBean);
        bundle.putString(CommonConstant.DEPARTMENTDEPARTMENT, department);
        inetent.putExtras(bundle);
        context.startActivity(inetent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityDepartmentInfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_department_info);
        //动态权限
        mRxPermissions = new RxPermissions(this);
        mTitleBarBean = new TitleBarBean();
        mTitleBarBean.setCenterText("通讯录");
        Bundle bundle = getIntent().getExtras();
        mUserListBean = new UserListBean();
        mPopupContactsInfoControl = new PopupContactsInfoControl();
        if (null != bundle) {
            mPopupContactsInfoControl.department.set(bundle.getString(CommonConstant.DEPARTMENTDEPARTMENT));
            mUserListBean = bundle.getParcelable(CommonConstant.DEPARTMENTLIST);
        }
        mActivityDepartmentInfoBinding.titleBar.setTitle(mTitleBarBean);
        mActivityDepartmentInfoBinding.setUserBean(mUserListBean);
        mActivityDepartmentInfoBinding.setPopupControl(mPopupContactsInfoControl);
        mActivityDepartmentInfoBinding.titleBar.ivTitleBarLeftImg.setOnClickListener(view -> finish());
    }

    /**
     * 弹出联系人点击事件
     */
    public class PopupContactsInfoControl {
        public ObservableField<String> department = new ObservableField<>();

        /**
         * 打电话
         */
        public void Call(String tel) {
            //动态权限
            mRxPermissions
                    .request(Manifest.permission.CALL_PHONE)
                    .subscribe(granted -> {
                        if (granted) { // Always true pre-M
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            Uri data = Uri.parse("tel:" + tel);
                            intent.setData(data);
                            startActivity(intent);
                        } else {
                            // Oups permission denied
                            ToolToast.error("未授权...");
                        }
                    });
        }

        /**
         * 发短信
         */
        public void SendMessage(String tel) {
            // dialogHidden();
            //    ToolToast.success(tel);
            mRxPermissions
                    .request(Manifest.permission.SEND_SMS)
                    .subscribe(granted -> {
                        if (granted) { // Always true pre-M
                            // I can control the camera now
                            if (PhoneNumberUtils.isGlobalPhoneNumber(tel)) {
                                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + tel));
                                intent.putExtra("sms_body", "");
                                startActivity(intent);
                            }

                        } else {
                            // Oups permission denied
                            ToolToast.error("未授权...");
                        }
                    });
        }
    }
}
