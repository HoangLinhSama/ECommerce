package com.hoanglinhsama.ecommerce.zalopay.Helper;

import android.annotation.SuppressLint;

import com.hoanglinhsama.ecommerce.zalopay.Helper.HMac.HMacUtil;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Helpers {
    private static int transIdDefault = 1;

    @NotNull
    @SuppressLint("DefaultLocale")
    public static String getAppTransId() {
        if (transIdDefault >= 100000) {
            transIdDefault = 1;
        }

        transIdDefault += 1;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatDateTime = new SimpleDateFormat("yyMMdd_hhmmss");
        String timeString = formatDateTime.format(new Date());
        return String.format("%s%06d", timeString, transIdDefault);
    }

    /**
     * Lay ra message authentication code (MAC) tu viec ma hoa data bang cach dung thuat toan HMAC
     */
    @NotNull
    public static String getMac(@NotNull String key, @NotNull String data) {
        return Objects.requireNonNull(HMacUtil.HMacHexStringEncode(HMacUtil.HMACSHA256, key, data));
    }
}
