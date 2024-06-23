package com.example.wmsspringbootproject.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.wmsspringbootproject.Utils.HttpUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.net.URLEncoder;

@RestController
@RequiredArgsConstructor
@Tag(name = "12.邮箱验证")
@CrossOrigin

@RequestMapping("/wx")
public class WechatLoginController {
    /**
     * 获取配置文件的相应参数配置
     */
    private String appid= "wxe5db40b615ef8316";//微信开放平台生成的appid

    private String appsecret = "d2583a619db19cf41b6a37307d4e0910";//微信开放平台生成的appsecret

    private String http = "http://localhost/scanCodeLogin";//回调地址,也就是页面的地址。localhost也就是我微信开放平台上的域名回调域地址，浏览器默认80 所以我域名就用的localhost,自己踩了很多坑，这个回调地址不能加端口，不然会出现redirect uri 参数错误


    /**
     * 生成微信登录二维码
     * 这里主要是去调用微信的接口，并配上自己认证应用的appid、secret以及回url，生成回调需要用到的url。需要注意，这里的回调url是用作接下来微信授权的关键。
     */
    @PostMapping(value = "/getWechatQrCode")
    public String getWechatQrCode() {
        try {
            String oauthUrl = "https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
            String redirect_uri = URLEncoder.encode(http, "utf-8");
            System.out.println("redirect_uri:" + redirect_uri);
            oauthUrl = oauthUrl.replace("APPID", appid).replace("REDIRECT_URI", redirect_uri).replace("SCOPE", "snsapi_login");
            return oauthUrl;
        } catch (Exception e) {
            System.out.println("二维码生成失败");
            return null;
        }
    }

    /**
     * 微信认证/微信扫二维码登录的回调方法
     * 根据code获取获取access_token和openId
     * 再根据access_token和openId获取用户信息
     */
    @PostMapping(value = "/wxCallBack")
    public JSONObject wxCallBack(@RequestParam(value = "code") String code){
        //获取access_token
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=" + appid +
                "&secret=" + appsecret +
                "&code=" + code +
                "&grant_type=authorization_code";
        JSONObject resultObject = HttpUtils.httpGet(url);

        //请求获取userInfo
        String infoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                "?access_token=" + resultObject.getString("access_token") +
                "&openid=" + resultObject.getString("openid") +
                "&lang=zh_CN";

        JSONObject resultInfo = HttpUtils.httpGet(infoUrl);

        //此时已获取到userInfo，再根据业务进行处理
        System.err.println("请求获取userInfo:" + resultInfo.toString());


        //做登录或注册操作，编写自己的业务
        //JSONObject jsonObject = new JSONObject();
        //jsonObject.put("openId",resultObject.getString("openid"));
        return resultInfo;
    }


    //用的也就上面这两个接口，下面这个微信认证授权用不到

    /**
     * 微信认证授权
     */
    @PostMapping(value = "/login")
    public String login(){
        String url ="https://open.weixin.qq.com/connect/oauth2/authorize" +
                "?appid=" + appid + "" +
                "&redirect_uri=" + http + "" +
                "&response_type=code" +
                "&scope=snsapi_userinfo" +
                "&state=STATE#wechat_redirect";
        return url;
    }
}