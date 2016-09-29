package com.isnowfox.game.platform.tgw;

import com.google.common.collect.Maps;
import com.isnowfox.game.platform.GamePayResult;
import com.isnowfox.game.platform.Platform;
import com.isnowfox.web.Context;
import com.isnowfox.web.Request;
import com.isnowfox.web.annotation.Action;
import com.isnowfox.web.annotation.NoCache;
import com.isnowfox.web.annotation.result.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.Map;

public class PayAction {
    private final static Logger log = LoggerFactory.getLogger(PayAction.class);

    private Platform platform;
    private GamePayResult gamePayResult;

    public PayAction() {
        log.info("腾讯请求充值！");
    }

    @Action("pay")
    @JsonResult
    @NoCache
    public Object pay() throws FileNotFoundException {
        Request request = Context.getInstance().getRequest();

        int localPort = request.getLocalPort();
        if (localPort != 9001) {
            throw new FileNotFoundException("pay.do找不到");
        }
        Map<String, String> allParams = request.getParamsMap();
        log.info("腾讯请求充值！{}", allParams);
        //腾讯请求充值！{amt=[400], appid=[1104202590], billno=[-APPDJSX28625-20150311-2031318333], openid=[3C25D82322A600DAEAEB789962CE8846], payamt_coins=[0], paychannel=[qqpoint], paychannelsubid=[1], payitem=[G5*50*1], providetype=[0], pubacct_payamt_coins=[], token=[D420AD4443F835F624A71CC9C8BB876A14974], ts=[1426077092], version=[v3], wbazinga=[1], zoneid=[1], sig=[VgbqcsvMyesPviNBDec2o5fre1w=]}

//        amt,openid,payamt_coins,paychannel，payitem,
//                pubacct_payamt_coins,
        platform.payResult(allParams);
        Map<String, Object> map = Maps.newHashMap();
        map.put("ret", 0);
        map.put("msg", "OK");
        return map;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    @Action("error")
    @JsonResult
    @NoCache
    public Object error() {
        Request request = Context.getInstance().getRequest();
        Map<String, String> allParams = request.getParamsMap();
        gamePayResult.error(
                allParams, request.getRemoteInfo(), request.getHeader("user-agent")
        );
        return "ok";
    }

    @Action("gamelogin")
    @JsonResult
    @NoCache
    public String gamelogin() {
        Request request = Context.getInstance().getRequest();
        Map<String, String> allParams = request.getParamsMap();
        gamePayResult.login(
                allParams, request.getRemoteInfo(), request.getHeader("user-agent")
        );
        return "ok";
    }

    public void setGamePayResult(GamePayResult gamePayResult) {
        this.gamePayResult = gamePayResult;
    }
}
