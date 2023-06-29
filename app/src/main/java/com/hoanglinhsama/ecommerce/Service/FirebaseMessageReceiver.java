package com.hoanglinhsama.ecommerce.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.activity.MainActivity;

/**
 * Xu ly tin nhan nhan duoc tu Firebase Cloud Message (Service nay tu dong duoc goi khi nhan duoc message tu FCM)
 */
public class FirebaseMessageReceiver extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        if (message.getNotification() != null) {
            showNotification(message.getNotification().getTitle(), message.getNotification().getBody());
        }
        super.onMessageReceived(message);
    }

    private void showNotification(String title, String body) {
        String channelId = "notification1";

        /* Dieu huong mac dinh den MainActivity khi click vao thong bao cho du dang o bat ky activity nao khong phai la MainActivity */
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE); // PendingIntent de start activity khi event xay ra, requestCode = 0 de khong can doi ket qua tra ve
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setContent(customView(title, body));

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        /* Tao notification channel, tu android 8.0, he thong yeu cau cac ung dung phai dung chanelnotification */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "Firebase Notify", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(0, builder.build());
    }

    /**
     * Tao giao dien cho cho thong bao
     */
    private RemoteViews customView(String title, String body) {
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.text_view_title_notification, title);
        remoteViews.setTextViewText(R.id.text_view_body_notification, body);
        remoteViews.setImageViewResource(R.id.image_view_notification, R.drawable.ic_notification); // Thay bang icon app cho dong bo
        return remoteViews;
    }
}
