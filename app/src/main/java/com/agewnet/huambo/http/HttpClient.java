package com.agewnet.huambo.http;

import android.text.TextUtils;

import com.agewnet.huambo.app.CommonConstant;
import com.agewnet.huambo.app.HuamBoApplication;
import com.agewnet.huambo.util.JsonConvert;
import com.agewnet.huambo.util.ToolLOG;
import com.agewnet.huambo.util.ToolToast;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Dumpling on 2017/10/11.
 * <p>
 * 网络请求类
 */
public class HttpClient implements NetClient {

    //静态变量
    private volatile static HttpClient singleton;
    //Retrofit
    private static Retrofit mRetrofit;
    //请求地址
    private static String mRequestUrl;
    //返回回调
    private ResponesEntity mResponesEntity;
    //请求方式
    private RequestType mRequestType = RequestType.GET;

    private Type mConvertType;


    static int mTempCode = 200;

    static String mTempMsg = "";

    public static HttpClient getInstance() {
        if (singleton == null) {  //第一层校验
            synchronized (HttpClient.class) {
                if (singleton == null) {  //第二层校验
                    singleton = new HttpClient();
                }
            }
        }
        return singleton;
    }

    public HttpClient() {
        mRetrofit = new Retrofit.Builder().baseUrl(RequestApi.BASE_URL)
                .client(new OkHttpClient.Builder().
                        connectTimeout(1000, TimeUnit.SECONDS).
                        readTimeout(1000, TimeUnit.SECONDS).
                        writeTimeout(1000, TimeUnit.SECONDS).build())
                .addConverterFactory(ScalarsConverterFactory.create())
                // .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    @Override
    public NetClient setRequestUrl(String url) {
        this.mRequestUrl = url;
        return this;
    }

    @Override
    public NetClient setResponseConver(Type type) {
        this.mConvertType = type;
        return this;
    }


    @Override
    public NetClient setRequestType(RequestType requestType) {
        this.mRequestType = requestType;

        return this;
    }


    @Override
    public void sendRequest(final RequestListener requestlistener) {

        if (!HuamBoApplication.isNetworkAvailable(HuamBoApplication.getContext())) {
            requestlistener.error("请检查网络连接");
            return;


        }
        //接收类
        mResponesEntity = new ResponesEntity();
        //请求回调
        Call<String> call = null;
        //获取 Retrofit 对象
        RetrofitService retrofitService = mRetrofit.create(RetrofitService.class);
        //请求 获取  Call
        switch (mRequestType) {
            case POST:
                call = retrofitService.requestPost(mRequestUrl);
                break;
            case GET:
                call = retrofitService.requestPost(mRequestUrl);
                break;

            case PUT:

                break;
        }
        if (null != call) {
            //执行Call  的回调
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, final Response<String> response) {
                    ToolLOG.D(mRequestUrl + response.headers().toString() + response.code());
                    ToolLOG.D(response.headers().toString());
                    //处理返回数据
                    Observable.create((ObservableOnSubscribe<ResponesEntity>) e -> {
                        mTempCode = response.code();
                        mTempMsg = response.message();
                        //解析 返回JSON　
                        String tempResponse = response.body();
                        //判断返回数据
                        if (!TextUtils.isEmpty(tempResponse)) {
                            ToolLOG.D(tempResponse);
                            mResponesEntity.setCode(mTempCode);
                            mResponesEntity.setMessage(mTempMsg);
                            mResponesEntity.setData(JsonConvert.analysisJson(tempResponse, mConvertType));
                            e.onNext(mResponesEntity);
                        } else {
                            e.onError(new Exception("请重试..."));
                        }
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .unsubscribeOn(Schedulers.io())
                            .subscribe(new Observer<ResponesEntity>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {

                                }

                                /**
                                 * 发送成功
                                 * @param responesEntity
                                 */
                                @Override
                                public void onNext(@NonNull ResponesEntity responesEntity) {
                                    if (responesEntity.getData() != null) {
                                        requestlistener.Success(responesEntity);
                                    } else {
                                        requestlistener.error(responesEntity.getMessage());
                                    }
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    requestlistener.error(e.getMessage());
                                }

                                @Override
                                public void onComplete() {

                                }
                            });


                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    requestlistener.error(t.getMessage());
                }
            });
        } else {
            requestlistener.error("网络开小差了...");
        }
    }
}
