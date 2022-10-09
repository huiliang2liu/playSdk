package com.lhl.pay;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.iap.Iap;
import com.huawei.hms.iap.IapApiException;
import com.huawei.hms.iap.entity.ConsumeOwnedPurchaseReq;
import com.huawei.hms.iap.entity.ConsumeOwnedPurchaseResult;
import com.huawei.hms.iap.entity.InAppPurchaseData;
import com.huawei.hms.iap.entity.OrderStatusCode;
import com.huawei.hms.iap.entity.PurchaseIntentReq;
import com.huawei.hms.iap.entity.PurchaseIntentResult;
import com.huawei.hms.iap.entity.PurchaseResultInfo;
import com.huawei.hms.support.api.client.Status;
import com.lhl.App;
import com.lhl.listener.PayListener;
import com.lhl.result.activity.ResultCallback;

import org.json.JSONException;

public class HuaweiPay extends AbsPay {
    public String goods;
    public String passThrough;
    public int payType = 0;//0：消耗型商品; 1：非消耗型商品; 2：订阅型商品
    ResultCallback callback = new ResultCallback() {
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode != 0)
                return;
            if (resultCode != Activity.RESULT_OK) {
                listener.onPayFailure("支付失败");
                return;
            }
            if (data == null) {
                listener.onPayFailure("data is null");
                return;
            }
            PurchaseResultInfo purchaseResultInfo = Iap.getIapClient(activity).parsePurchaseResultInfoFromIntent(data);
            int code = purchaseResultInfo.getReturnCode();
            if (code == OrderStatusCode.ORDER_STATE_SUCCESS) {
                String inAppPurchaseData = purchaseResultInfo.getInAppPurchaseData();
                String inAppPurchaseDataSignature = purchaseResultInfo.getInAppDataSignature();
                // 使用您应用的IAP公钥验证签名
                // 若验签成功，则进行发货
                // 若用户购买商品为消耗型商品，您需要在发货成功后调用consumeOwnedPurchase接口进行消耗
                listener.onPaySuccess(inAppPurchaseDataSignature, goods, passThrough);
                if (payType != 0)
                    return;
                // 构造ConsumeOwnedPurchaseReq对象
                ConsumeOwnedPurchaseReq req = new ConsumeOwnedPurchaseReq();
                try {
                    // purchaseToken需从购买信息InAppPurchaseData中获取
                    InAppPurchaseData inAppPurchaseDataBean = new InAppPurchaseData(inAppPurchaseData);
                    String purchaseToken = inAppPurchaseDataBean.getPurchaseToken();
                    req.setPurchaseToken(purchaseToken);
                    Task<ConsumeOwnedPurchaseResult> task = Iap.getIapClient(activity).consumeOwnedPurchase(req);
                    task.addOnSuccessListener(new OnSuccessListener<ConsumeOwnedPurchaseResult>() {
                        @Override
                        public void onSuccess(ConsumeOwnedPurchaseResult result) {
                            // 获取接口请求结果
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            if (e instanceof IapApiException) {
                                IapApiException apiException = (IapApiException) e;
                                Status status = apiException.getStatus();
                                int returnCode = apiException.getStatusCode();
                            } else {
                                // 其他外部错误
                            }
                        }
                    });
                } catch (JSONException e) {
                }

                return;
            }
            if (OrderStatusCode.ORDER_STATE_CANCEL == code) {
                listener.onPayFailure("用户取消了");
                return;
            }
        }
    };

    public HuaweiPay(Activity activity, PayListener listener) {
        super(activity, listener);
    }

    @Override
    public void pay(int num, String goods, float price, String currency, String passThrough) {
        this.goods = goods;
        this.passThrough = passThrough;
        PurchaseIntentReq req = new PurchaseIntentReq();
// 通过createPurchaseIntent接口购买的商品必须是您在AppGallery Connect网站配置的商品
        req.setProductId(goods);
// priceType: 0：消耗型商品; 1：非消耗型商品; 2：订阅型商品
        req.setPriceType(payType);
        req.setDeveloperPayload(passThrough);
// 获取调用接口的Activity对象
// 调用createPurchaseIntent接口创建托管商品订单
        Task<PurchaseIntentResult> task = Iap.getIapClient(activity).createPurchaseIntent(req);
        task.addOnSuccessListener(new OnSuccessListener<PurchaseIntentResult>() {
            @Override
            public void onSuccess(PurchaseIntentResult result) {
                // 获取创建订单的结果
                Status status = result.getStatus();
                PendingIntent pendingIntent = status.getResolution();
                if (pendingIntent != null) {
                    try {
                        App.getApp().startIntentSenderForResult(pendingIntent.getIntentSender(), 0, null, 0, 0, 0, null, callback);
                    } catch (IntentSender.SendIntentException e) {
                        listener.onPayFailure(e.toString());
                    }
                    return;
                }
                Intent intent = status.getResolutionIntent();
                if (intent != null) {
                    App.getApp().startActivityForResult(0, intent, callback);
                    return;
                }
                listener.onPayFailure("支付异常");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof IapApiException) {
                    listener.onPayFailure(e.toString());
                    return;
                }
                listener.onPayFailure("外部错误");
            }
        });
    }
}
