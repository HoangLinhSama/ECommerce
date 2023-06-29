package com.hoanglinhsama.ecommerce.zalopay.Api;

import com.hoanglinhsama.ecommerce.zalopay.Constant.AppInfo;
import com.hoanglinhsama.ecommerce.zalopay.Helper.Helpers;

import org.json.JSONObject;

import java.util.Date;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class CreateOrder {
    private class CreateOrderData {
        String AppId; // dinh danh cho ung dung da duoc cap khi dag ky zalopay
        String AppUser; // dinh danh cho nguoi dung thanh toan don hang
        String AppTime;
        String Amount;
        String AppTransId; // ma giao dich cua don hang
        String EmbedData; // du lieu rieng cho don hang, duoc callback lai cho AppServer
        String Items; // item cua don hang (order_detail)
        String BankCode; // ma ngan hang thuc hien thanh toan zalopay
        String Description;
        String Mac; // thong tin chung thuc cua don hang

        private CreateOrderData(String amount, String appUser) throws Exception {
            long appTime = new Date().getTime();
            AppId = String.valueOf(AppInfo.APP_ID);
            AppUser = appUser;
            AppTime = String.valueOf(appTime);
            Amount = amount;
            AppTransId = Helpers.getAppTransId();
            EmbedData = "{}";
            Items = "[]";
            BankCode = "zalopayapp"; // mac dinh
            Description = "ECommerce thanh toán cho đơn hàng #" + Helpers.getAppTransId();
            String inputHMac = String.format("%s|%s|%s|%s|%s|%s|%s",
                    this.AppId,
                    this.AppTransId,
                    this.AppUser,
                    this.Amount,
                    this.AppTime,
                    this.EmbedData,
                    this.Items);

            Mac = Helpers.getMac(AppInfo.MAC_KEY, inputHMac);
        }
    }

    public JSONObject createOrder(String amount, String appUser) throws Exception {
        CreateOrderData input = new CreateOrderData(amount, appUser);

        /* HTTP Request su dung okhttp3 */
        RequestBody formBody = new FormBody.Builder()
                .add("app_id", input.AppId)
                .add("app_user", input.AppUser)
                .add("app_time", input.AppTime)
                .add("amount", input.Amount)
                .add("app_trans_id", input.AppTransId)
                .add("embed_data", input.EmbedData)
                .add("item", input.Items)
                .add("bank_code", input.BankCode)
                .add("description", input.Description)
                .add("mac", input.Mac)
                .build();

        JSONObject data = HttpProvider.sendPost(AppInfo.URL_CREATE_ORDER, formBody);
        return data;
    }
}

