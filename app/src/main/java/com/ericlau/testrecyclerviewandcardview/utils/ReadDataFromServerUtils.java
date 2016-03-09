package com.ericlau.testrecyclerviewandcardview.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Eric.Lau on 16/3/8.
 */
public class ReadDataFromServerUtils {

    //服务器地址
    String SERVER = "144.214.97.208";


    public void readSalesText(String address) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(SERVER + "address", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

            }
        });
    }
}
