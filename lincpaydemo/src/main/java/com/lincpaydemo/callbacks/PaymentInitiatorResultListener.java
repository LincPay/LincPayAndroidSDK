package com.lincpaydemo.callbacks;

import com.lincpaydemo.model.PaymentRequestResponse;

public interface PaymentInitiatorResultListener {

    void onSuccess(Object object);

    void onError(String message);
}
