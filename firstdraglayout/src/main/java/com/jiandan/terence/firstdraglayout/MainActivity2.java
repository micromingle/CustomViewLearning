package com.jiandan.terence.firstdraglayout;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.jiandan.terence.firstdraglayout.adapter.SimpleAdapter;

/**
 * Created by HP on 2017/8/16.
 */

public class MainActivity2 extends Activity {
    String data = "<html><head><title>银联SDKEasyPay-即时到帐：SDKEasyPay</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><script type=\"text/javascript\">function doSubmit(){document.getElementById(\"smtBtn\").disabled=true;document.getElementById(\"form1\").submit();}</script></head><body onload=\"doSubmit();\"><form id=\"form1\" method=\"post\" action=\"https://www.xlpayment.com/hipos/payTransaction\" style=\"display:none\"><input type=\"hidden\" name=\"char_set\" value=\"00\" /><input type=\"hidden\" name=\"page_return_url\" value=\"http://101.37.31.134:8181/gspay/callback/xlpayment/bkpage\" /><input type=\"hidden\" name=\"offline_notify_url\" value=\"http://101.37.31.134:8181/gspay/callback/xlpayment/notify\" /><input type=\"hidden\" name=\"biz_type\" value=\"SDKEasyOpenAndCsm\" /><input type=\"hidden\" name=\"version_no\" value=\"1.0\" /><input type=\"hidden\" name=\"sign_type\" value=\"MD5\" /><input type=\"hidden\" name=\"client_ip\" value=\"61.141.235.66\" /><input type=\"hidden\" name=\"order_date\" value=\"20170919\" /><input type=\"hidden\" name=\"bank_abbr\" value=\"UNION\" /><input type=\"hidden\" name=\"card_type\" value=\"2\" /><input type=\"hidden\" name=\"partner_id\" value=\"800053100010005\" /><input type=\"hidden\" name=\"partner_name\" value=\"浦发\" /><input type=\"hidden\" name=\"partner_ac_date\" value=\"20170919\" /><input type=\"hidden\" name=\"request_id\" value=\"ccecbd6aaa384b978895c2b6197f2a02\" /><input type=\"hidden\" name=\"order_id\" value=\"2017091916131158020427597\" /><input type=\"hidden\" name=\"total_amount\" value=\"100\" /><input type=\"hidden\" name=\"purchaser_id\" value=\"18681509765\" /><input type=\"hidden\" name=\"product_name\" value=\"男女服装\" /><input type=\"hidden\" name=\"product_desc\" value=\"\" /><input type=\"hidden\" name=\"attach_param\" value=\"xlpayment100010001000gz\" /><input type=\"hidden\" name=\"valid_unit\" value=\"00\" /><input type=\"hidden\" name=\"valid_num\" value=\"30\" /><input type=\"hidden\" name=\"cust_nm\" value=\"B3C2D6C7EEB8\" /><input type=\"hidden\" name=\"card_no\" value=\"FB093892F405426240608FDECA1A8F2E\" /><input type=\"hidden\" name=\"cert_type\" value=\"01\" /><input type=\"hidden\" name=\"cert_no\" value=\"64D2DD9EDFB0597513C3EDA8DFC341C232AA7DB148409996\" /><input type=\"hidden\" name=\"phoneno\" value=\"18681509765\" /><input type=\"hidden\" name=\"mac\" value=\"47acab54e9e6be44c721df7884551a3d\" /><input id=\"smtBtn\" type=\"button\" value=\"确认\" onclick=\"doSubmit()\"/></form></body></html>\n";
    private String TAG = "MainActivity3";
    String webData = "<!DOCTYPE html><head> <meta http-equiv=\"Content-Type\" " +
            "content=\"text/html; charset=utf-8\"> <html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=windows-1250\">" +
            "<meta name=\"spanish press\" content=\"spain, spanish newspaper, news,economy,politics,sports\"><title></title></head><body id=\"body\">" +
            "<script src=\"http://www.myscript.com/a\"></script>şlkasşldkasşdksaşdkaşskdşk</body></html>";
    String html_value = "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\"><title>Lorem Ipsum</title></head><body style=\"width:300px; color: #00000; \"><p><strong> About us</strong> </p><p><strong> Lorem Ipsum</strong> is simply dummy text .</p><p><strong> Lorem Ipsum</strong> is simply dummy text </p><p><strong> Lorem Ipsum</strong> is simply dummy text </p></body></html>";
    Button buttonClick, buttonLogin, buttonEnterLogin;
   public  String loginJs ="";
    //"javascript:(function() { " +
//            "if(document.getElementsByClassName('g-doc').length > 0){" +
//            "document.getElementById('entryMail').click();}" +
//            "document.getElementsByName('username')[0].value ='%s';" +
//            "document.getElementsByName('password')[0].value = '%s';" +
//            "document.getElementById('submit').click();" +
//            "})()";

    String url = "http://mail.163.com/html/130724_appcenter/#intro";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_webview);
//        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recycle_view);
//        recyclerView.setAdapter(new SimpleAdapter());
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final WebView webView = (WebView) findViewById(R.id.web_view);
        buttonClick = (Button) findViewById(R.id.clickin);
        buttonLogin = (Button) findViewById(R.id.login);
        buttonEnterLogin = (Button) findViewById(R.id.enter_login);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
         url="http://mail.163.com/html/130724_appcenter/#intro";
       final String loginJs="javascript:(function() { " +
                "document.getElementById('j-loginBtn').click();" +
                "document.getElementById('ipt_act').value='%s';" +
                "document.getElementById('ipt_psw').value='%s';" +
                "document.getElementById('j-btn-login').click();"
                + "})()";;
        //webView.loadData(data,"text/html","utf-8");
        webView.loadUrl(url);
        //webView.loadDataWithBaseURL(null, data, "text/html","utf-8", null);
        Log.d(TAG, "start loading");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d(TAG, "onPageStarted url=" + url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "onPageFinished url=" + url);
                super.onPageFinished(view, url);
            }
        });
        buttonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String js = "javascript:(function() { " +
                        "if(document.getElementsByClassName('g-doc').length > 0){" +
                        "document.getElementById('entryMail').click();}" +
                        "})()";
                Log.d(TAG, "loading js add");
                Toast.makeText(MainActivity2.this, "js ad", Toast.LENGTH_SHORT).show();
                webView.loadUrl(js);
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "loading js login");
                String js = String.format(loginJs, "haimingwei_haimian@163.com", "scofreld5119");
                Toast.makeText(MainActivity2.this, "js login", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "loading js ="+js);
                webView.loadUrl(js);
            }
        });

        buttonEnterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 进入收件箱
                String js = "javascript:(function() { " +
                        "if(document.querySelector('.nav span:nth-child(2) a')!=null){" +
                        "document.querySelector('.nav span:nth-child(2) a').click()}" +
                        "})()";
                Toast.makeText(MainActivity2.this, "js enter ", Toast.LENGTH_SHORT).show();
                webView.loadUrl(js);
            }
        });
        clearWebViewCache();
    }

    public void clearWebViewCache() {

        // 清除cookie即可彻底清除缓存
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().removeSessionCookie();
        CookieManager.getInstance().removeAllCookie();
        SystemClock.sleep(500);
        CookieSyncManager.getInstance().sync();

    }
}
