package com.example.wmsspringbootproject.im.websocket;

import cn.hutool.extra.spring.SpringUtil;

/**
 * <p>
 * tio类
 * </p>
 *
 * @author 初秋
 * @since 2024-06-10
 */
public class TioUtil {

    private static TioWebsocketStarter tioWebsocketStarter;

    public static void buildTio() {
        System.out.println(tioWebsocketStarter);
        TioWebsocketStarter websocketStarter = null;
        try {
            websocketStarter = SpringUtil.getBean(TioWebsocketStarter.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        TioUtil.tioWebsocketStarter = websocketStarter;
    }

    public static TioWebsocketStarter getTio() {
        return TioUtil.tioWebsocketStarter;
    }
}
