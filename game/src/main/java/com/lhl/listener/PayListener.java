package com.lhl.listener;

public interface PayListener {
    void onPayFailure(String msg);

    void onPaySuccess(String orderId,String  goods,String passThrough);
}
