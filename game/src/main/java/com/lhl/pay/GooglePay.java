package com.lhl.pay;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.lhl.listener.PayListener;

import java.util.ArrayList;
import java.util.List;

public class GooglePay extends AbsPay implements PurchasesUpdatedListener {

    public BillingClient mBillingClient;
    private String passThrough;
    private String goods;

    public GooglePay(Activity activity, PayListener listener) {
        super(activity, listener);
        mBillingClient = BillingClient.newBuilder(activity).setListener(this).enablePendingPurchases().build();
    }

    //    passthrough
    @Override
    public void pay(int num, String goods, float price, String currency, String passThrough) {
        this.passThrough = passThrough;
        this.goods = goods;
        if (mBillingClient.isReady()) {
            querySkuDetailsAsync();
            return;
        }
        mBillingClient.startConnection(new BillingClientStateListener() {  //重新连接
            @Override
            public void onBillingServiceDisconnected() {
                //尝试重新启动连接的下一个请求
                //谷歌通过调用startConnection()方法进行播放。

            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {

                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    //BillingClient已经准备好。 你可以在这里查询购买情况。
                    querySkuDetailsAsync();
                    return;
                }
                listener.onPayFailure("链接google商店失败");
            }
        });
    }


    void querySkuDetailsAsync() {
        List<String> skuList = new ArrayList<>();
        skuList.add(goods);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        mBillingClient.querySkuDetailsAsync(params.build(), (billingResult, skuDetailsList) -> {

            if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
                listener.onPayFailure("获取google商品列表错误");
                return;
            }
            if (skuDetailsList == null || skuDetailsList.isEmpty()) {
                listener.onPayFailure("没有获取到google商品列表，请确认是否配置了商品和测试人员");
                return;
            }
            boolean pay = false;
            for (SkuDetails skuDetails : skuDetailsList) {
                if (!skuDetails.getSku().equals(goods))
                    continue;
                //发起支付
                pay = true;
                mBillingClient.launchBillingFlow(activity,
                        BillingFlowParams
                                .newBuilder()
                                .setSkuDetails(skuDetails)
                                .setObfuscatedAccountId(passThrough)//这里本来的意思存放用户信息，类似于国内的透传参数，我这里传的我们的订单号。老版本使用DeveloperPayload字段，最新版本中这个字段已不可用了
                                .build()
                );
            }
            if (pay) {
                StringBuilder sb = new StringBuilder("没有匹配到对应的商品，请确认商品是否正确").append("，您输入的订单为：").append(goods).append("，获取的商品列表为：[");
                for (SkuDetails skuDetails : skuDetailsList) {
                    sb.append(skuDetails.getSku()).append(",");
                }
                sb.append(']');
                listener.onPayFailure(sb.toString());
            }
        });
    }


    public void consumePurchase(final Purchase purchase) {
        if (mBillingClient == null || purchase == null || purchase.getPurchaseState() != Purchase.PurchaseState.PURCHASED)
            return;
        Log.i("Tag", "消耗商品：商品id：" + purchase.getSkus() + "商品OrderId：" + purchase.getOrderId() + "token:" + purchase.getPurchaseToken());
        listener.onPaySuccess(purchase.getOrderId(), purchase.getSkus().get(0), passThrough);
        ConsumeParams consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken())
                .build();
        mBillingClient.consumeAsync(consumeParams, (billingResult, purchaseToken) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ERROR) {
                //消费失败将商品重新放入消费队列

                return;
            }
            Log.i("TAG", "消费成功: ");
        });
    }

    /**
     * 补单操作 查询已支付的商品，并通知服务器后消费（google的支付里面，没有消费的商品，不能再次购买）
     */
    private void queryPurchases() {
        PurchasesResponseListener mPurchasesResponseListener = new PurchasesResponseListener() {

            @Override
            public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> purchasesResult) {
                if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK || purchasesResult == null)
                    return;
                for (Purchase purchase : purchasesResult) {
                    if (purchase == null || purchase.getPurchaseState() != Purchase.PurchaseState.PURCHASED)
                        continue;
                    consumePurchase(purchase);
                    //这里处理已经支付过的订单，通知服务器去验证  并消耗商品
                }
            }
        };
        mBillingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP, mPurchasesResponseListener);
    }


    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
        String debugMessage = billingResult.getDebugMessage();
        int code = billingResult.getResponseCode();
        if (list != null && list.size() > 0) {
            if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
                listener.onPayFailure(new StringBuilder("未知错误,错误码：").append(code).append("，调试信息：").append(debugMessage).toString());
                return;
            }
            for (Purchase purchase : list) {
                if (purchase == null || purchase.getPurchaseState() != Purchase.PurchaseState.PURCHASED)
                    continue;
                consumePurchase(purchase);
            }
            return;
        }
        if (code == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            queryPurchases();
            return;
        }
        listener.onPayFailure(code2string(code));
    }

    private String code2string(int code) {
        if (code == BillingClient.BillingResponseCode.SERVICE_TIMEOUT)
            return "服务连接超时";
        if (code == BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED)
            return "功能不支持";
        if (code == BillingClient.BillingResponseCode.SERVICE_DISCONNECTED)
            return "服务未连接";
        if (code == BillingClient.BillingResponseCode.USER_CANCELED)
            return "用户取消支付";
        if (code == BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE)
            return "服务不可用";
        if (code == BillingClient.BillingResponseCode.BILLING_UNAVAILABLE)
            return "购买不可用";
        if (code == BillingClient.BillingResponseCode.ITEM_UNAVAILABLE)
            return "商品不存在";
        if (code == BillingClient.BillingResponseCode.DEVELOPER_ERROR)
            return "提供给 API 的无效参数";
        if (code == BillingClient.BillingResponseCode.ERROR)
            return "错误";
        if (code == BillingClient.BillingResponseCode.ITEM_NOT_OWNED)
            return "不可购买";
        return new StringBuilder("未知错误，错误码是：").append(code).toString();
    }
}
