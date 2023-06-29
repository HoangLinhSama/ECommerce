package com.hoanglinhsama.ecommerce.retrofit2;

import com.hoanglinhsama.ecommerce.model.NotificationReceiveData;
import com.hoanglinhsama.ecommerce.model.NotificationSendData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Gui request den Firebase Cloud Message, va nhan response tra ve
 */
public interface DataPushNotification {
    @Headers(
            {
                    "Content-Type: application/json" // kieu muon gui la json
                    , "Authorization: key=AAAA8Too4Nc:APA91bHIbk6Bb03AyrgnBpUzZ_ijUuIFSmmNy7nS41DtlmuSpiCcaDpfDRBSWIygcDDe74vbcjIjQK-VqUGZIS9xzhbI6AW-stBv3T0oOi7wYq3qBCh9pMoweQOKWLJxSWN6hsvHIlhl"

            }
    )


    @POST("fcm/send")
    Call<NotificationReceiveData> sendNotification(@Body NotificationSendData notificationSendData);
}
