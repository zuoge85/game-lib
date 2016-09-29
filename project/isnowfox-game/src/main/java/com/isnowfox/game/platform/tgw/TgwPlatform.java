package com.isnowfox.game.platform.tgw;

import com.isnowfox.core.net.message.MessageProtocol;
import com.isnowfox.game.platform.*;
import com.isnowfox.util.JsonUtils;
import com.isnowfox.util.ObjectUtils;
import com.qq.open.OpenApiV3;
import com.qq.open.OpensnsException;
import gnu.trove.map.hash.TIntIntHashMap;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author zuoge85 on 2015/2/4.
 */
public class TgwPlatform extends PlatformAdapter {
    private final static Logger log = LoggerFactory.getLogger(TgwPlatform.class);

    public static final String HEAD = "tgw_l7_forward";
    public static final String MASK = "\r\n";

    private String key;
    private boolean check;

    private String apiAppKey;
    private String apiAppId;
    private String apiUrl;
    private String apiPayZoneid;
    private GamePayResult gamePayResult;

    public ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> loginInfo = new ConcurrentHashMap<>();

    @Override
    public boolean onIn(ByteBuf in, User user) throws Exception {
//        tgw_l7_forward\r\nHost:app12345.qzoneapp.com:80\r\n\r\n
        if (user.isCheckConnect()) {
            return true;
        } else {
            int readerIndex = in.readerIndex();
            int readableLen = in.readableBytes();
            if (readableLen >= MessageProtocol.HEAD_LENGTH) {
                byte[] data = new byte[readableLen];
                in.readBytes(data);
                if (data[0] == 't' && data[1] == 'g' && data[2] == 'w') {
                    String str = new String(data, "ascii");
                    int index = 0;
                    for (int i = 0; i < 3; i++) {
                        index = str.indexOf(MASK, index + MASK.length());
                        if (index == -1) {
                            in.readerIndex(readerIndex);
                            return false;
                        } else if (i == 2) {
                            in.readerIndex(readerIndex + index + MASK.length());
                            user.setCheckConnect(true);
                            return true;
                        }
                    }
                } else {
                    user.setCheckConnect(true);
                }
            }
            in.readerIndex(readerIndex);
        }
        return false;
    }

    @Override
    public UserInfo login(String info) {
        Map<String, Object> map = JsonUtils.deserialize(info, HashMap.class);
        //$sign = md5(md5($openid.$key.$is_year.$is_year.$is_yellow));
        String openid = String.valueOf(map.get("openid"));
        //WMGS97f2P5tLbra7DM3L5k4UN752vJqH
//        String key = String.valueOf(map.get("key"));
        String yellow = String.valueOf(map.get("yellow"));
        String year = String.valueOf(map.get("year"));
        String sign = String.valueOf(map.get("sign"));
        try {
            if(check)
            {
                String curSign = DigestUtils.md5DigestAsHex(openid.getBytes("ascii"));
                curSign = DigestUtils.md5DigestAsHex((curSign + key + year + yellow).getBytes("ascii"));
                curSign = DigestUtils.md5DigestAsHex(curSign.getBytes("ascii"));
                if(!sign.equals(curSign)) {
                   return null;
                }
            }
            UserInfo userInfo = new UserInfo();
            userInfo.setParam(map);
            userInfo.setUuid(openid);
            userInfo.setYear(year.equals("1"));
            userInfo.setYellow(NumberUtils.toInt(yellow, 0));

            loginInfo.put(openid, new ConcurrentHashMap<>(map));
            return userInfo;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
//        yellow          : "<?php echo $is_yellow?>",
//                blue            : "<?php echo $is_blue?>",
//                year            : "<?php echo $is_year?>",
    }

    @Override
    public void logout(String openId) {
        loginInfo.remove(openId);
    }



    private ScheduledExecutorService asyncExecutor;

    private static final TIntIntHashMap rmbMap= new TIntIntHashMap(new int[]{5, 10, 50, 100, 500, 1000},new int[]{50, 105, 550, 1150, 6000, 12500});
    @Override
    public void pay(final int rmb, final String openId, final ApiCallback callback) {
        getAsyncExecutor().submit(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> objectMap = loginInfo.get(openId);
                if(objectMap == null) {
                    log.error("不存在玩家信息!");
                    callback.callback(false, null);
                    return;
                }
                if(!rmbMap.containsKey(rmb)){
                    log.error("错误的套餐!");
                    callback.callback(false, null);
                    return;
                }
                int gold = rmbMap.get(rmb);
                OpenApiV3 api = new OpenApiV3(apiAppId, apiAppKey);

                api.setServerName(apiUrl);
                String scriptName = "/v3/pay/buy_goods";
//        api.


                // 指定HTTP请求协议类型
                String protocol = "https";

                // 填充URL请求参数
                HashMap<String,String> params = new HashMap<String, String>();
                params.put("openid", openId);
                params.put("openkey", String.valueOf(objectMap.get("openkey")));
                params.put("pf", String.valueOf(objectMap.get("pf")));


                params.put("pfkey", String.valueOf(objectMap.get("pfkey")));

                params.put("amt", (rmb * 10) + "*1");
                params.put("ts", String.valueOf(System.currentTimeMillis() / 1000));

                params.put("payitem", "" +rmb+ "*" +  (rmb * 10)+ "*1");

                params.put("appmode", "1");



                params.put("goodsmeta", gold +"金币宝箱*" + gold + "金币宝箱！");

                params.put("goodsurl", "http://app1104202590.imgcache.qzoneapp.com/app1104202590/img/pay" + rmb +".png");
                params.put("zoneid", apiPayZoneid);
                try
                {
                    String resp = api.api(scriptName, params, protocol);
                    Map<String, Object> map = JsonUtils.deserialize(resp, HashMap.class);
                    if(0 == ObjectUtils.toInt(map.get("ret"))){
//                        ConcurrentHashMap<String,String> tokenMap= (ConcurrentHashMap) objectMap.get("tokenMap");
//                        if(tokenMap == null){
//                            tokenMap = new ConcurrentHashMap<String, String>();
//                            objectMap.put("tokenMap", tokenMap);
//                        }
//                        tokenMap.put()
                        callback.callback(true, map);
                    } else {
                        log.error("充值错误错误:{}", resp);
                        callback.callback(false, null);
                    }
                }
                catch (OpensnsException e)
                {
                    log.error("Request Failed.msg:", e.getMessage(), e);
                    callback.callback(false, null);
                }
            }
        });
    }

    public static void main(String[] args) {
        System.out.println(getGold("50*50*1"));
    }

    protected static int getGold(String payitem) {
        int index = payitem.indexOf("*");
        if(index < 0){
            return -1;
        }
        int rmb = NumberUtils.toInt(payitem.substring(0, index), -1);
        if(!rmbMap.containsKey(rmb)){
            log.error("错误的套餐!");
            return -1;
        }
        return rmbMap.get(rmb);
    }

    protected static int getRmb(String payitem) {
        int index = payitem.indexOf("*");
        if(index < 0){
            return -1;
        }
        int rmb = NumberUtils.toInt(payitem.substring(0, index), -1);

        return rmb;
    }


    @Override
    public void payResult(final Map<String, String> allParams) {
        log.info("不存在玩家信息!{}", allParams);
        final String openId = allParams.get("openid");
        if(openId == null) {
            return;
        }
//        /v3/pay/confirm_delivery
        final Map<String, Object> objectMap = loginInfo.get(openId);
        if(objectMap == null) {
            log.error("不存在玩家信息!");
            return;
        }
        String payitem = allParams.get("payitem");
//        [G5*50*1]
        int gold = getGold(payitem);
        if(gold == -1){
            log.error("错误的套餐!{}", allParams);
            return;
        }
        int rmb = getRmb(payitem);

        gamePayResult.pay(allParams, rmb, gold, openId, new ApiCallback() {
            @Override
            public void callback(boolean result, Map<String, Object> map) {
                if(!result) {
                    return;
                }
                Runnable runnable = new Runnable() {
                    private volatile int  nums = 10;
                    @Override
                    public void run() {
                        log.info("开始确认回调不存在玩家信息!{}", allParams);
                        OpenApiV3 api = new OpenApiV3(apiAppId, apiAppKey);
                        api.setServerName(apiUrl);
                        String scriptName = "/v3/pay/confirm_delivery";


                        // 指定HTTP请求协议类型
                        String protocol = "https";

                        // 填充URL请求参数
                        HashMap<String,String> params = new HashMap<String, String>();
                        params.put("openid", openId);
                        params.put("openkey", String.valueOf(objectMap.get("openkey")));
                        params.put("pf", String.valueOf(objectMap.get("pf")));


                        params.put("ts", String.valueOf(System.currentTimeMillis() / 1000));
                        params.put("payitem", allParams.get("payitem"));
                        params.put("token_id", allParams.get("token"));
                        String billno = allParams.get("billno");
                        params.put("billno", billno);
                        params.put("zoneid", allParams.get("zoneid"));
                        params.put("providetype", allParams.get("providetype"));

                        params.put("provide_errno", allParams.get("provide_errno"));
                        params.put("amt", allParams.get("amt"));
                        params.put("payamt_coins", allParams.get("payamt_coins"));
                        params.put("pubacct_payamt_coins", allParams.get("pubacct_payamt_coins"));


                        try
                        {
                            String resp = api.api(scriptName, params, protocol);
                            Map<String, Object> map = JsonUtils.deserialize(resp, HashMap.class);
                            log.info("参数:{}", map);
                            if(0 == ObjectUtils.toInt(map.get("ret"))){
                                log.error("充值确认成功：{}", resp);
                                gamePayResult.paySuccess(openId, billno);
                            } else {
                                if(nums >0){
                                    nums--;
                                    log.error("充值错误错误:{}，准备重试", resp);
                                    getAsyncExecutor().schedule(this, 5, TimeUnit.SECONDS);
                                } else {
                                    log.error(" 充值失败10次:{}，不在重试", resp);
                                }
                            }
                        }
                        catch (OpensnsException e) {
                            log.error("Request Failed.msg:", e.getMessage(), e);
                        }
                    }
                };
                getAsyncExecutor().schedule(runnable, 2, TimeUnit.SECONDS);
            }
        });
    }


    private ScheduledExecutorService getAsyncExecutor() {
        if(asyncExecutor == null){
            synchronized (this){
                if(asyncExecutor == null){
                    final Thread.UncaughtExceptionHandler exceptionHandler = new Thread.UncaughtExceptionHandler() {
                        @Override
                        public void uncaughtException(Thread t, Throwable e) {
                            log.error("线程池错误，会恢复！", e);
                        }
                    };
                    asyncExecutor = Executors.newScheduledThreadPool(16, new ThreadFactory() {
                        @Override
                        public Thread newThread(Runnable r) {
                            Thread thread = new Thread(r, "tgw AsyncThread");
                            thread.setUncaughtExceptionHandler(exceptionHandler);
                            return thread;
                        }
                    });
                }
                return asyncExecutor;
            }
        }
        return asyncExecutor;
    }


    //    $seqid    = _get("seqid");
//    $openid   = _get("openid");
//    $openkey  = _get("openkey");
//    $platform = _get("platform");
//    $pf       = _get("pf");
//    $pfkey    = _get("pfkey");
//    $sid      = _get("serverid");
//    $s                = _get("s");
//
//    $iopenid  = _get("iopenid");
//    $invkey   = _get("invkey");
//    $itime    = _get("itime");
//    $app_custom = _get("app_custom");
//    $isid = 0;



    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean getCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public void setApiAppId(String apiAppId) {
        this.apiAppId = apiAppId;
    }

    public void setApiAppKey(String apiAppKey) {
        this.apiAppKey = apiAppKey;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public void setApiPayZoneid(String apiPayZoneid) {
        this.apiPayZoneid = apiPayZoneid;
    }

    public void setGamePayResult(GamePayResult gamePayResult) {
        this.gamePayResult = gamePayResult;
    }

    public void close() throws InterruptedException {
        synchronized (this){
            asyncExecutor.shutdown();
            asyncExecutor.awaitTermination(10, TimeUnit.SECONDS);
        }
    }
}
