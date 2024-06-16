package com.example.wmsspringbootproject.config;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.example.wmsspringbootproject.Service.UserService;
import com.example.wmsspringbootproject.Utils.WmsCache;
import com.example.wmsspringbootproject.constants.Constants;
import com.example.wmsspringbootproject.im.websocket.TioUtil;
import com.example.wmsspringbootproject.im.websocket.TioWebsocketStarter;
import com.example.wmsspringbootproject.mapper.HistoryInfoMapper;
import com.example.wmsspringbootproject.model.entity.HistoryInfo;
import com.example.wmsspringbootproject.model.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@Component
public class WmsImApplicationRunner implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private HistoryInfoMapper historyInfoMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Users admin = userService.getRootUser();
        WmsCache.put(Constants.RoleType.ROOT.getType(), admin);

        List<HistoryInfo> infoList = new LambdaQueryChainWrapper<>(historyInfoMapper)
                .select(HistoryInfo::getIp, HistoryInfo::getUserId)
                .ge(HistoryInfo::getCreateTime, LocalDateTime.now().with(LocalTime.MIN))
                .list();

        WmsCache.put(Constants.history.IP_HISTORY, new CopyOnWriteArraySet<>(infoList.stream()
                .map(info -> info.getIp() + (info.getUserId() != null ? "_" + info.getUserId().toString() : ""))
                .collect(Collectors.toList())));

        Map<String, Object> history = new HashMap<>();
        history.put(Constants.history.IP_HISTORY_PROVINCE, historyInfoMapper.getHistoryByProvince());
        history.put(Constants.history.IP_HISTORY_IP, historyInfoMapper.getHistoryByIp());
        history.put(Constants.history.IP_HISTORY_HOUR, historyInfoMapper.getHistoryBy24Hour());
        history.put(Constants.history.IP_HISTORY_COUNT, historyInfoMapper.getHistoryCount());
        WmsCache.put(Constants.history.IP_HISTORY_STATISTICS, history);

        TioUtil.buildTio();
        TioWebsocketStarter websocketStarter = TioUtil.getTio();
        if (websocketStarter != null) {
            websocketStarter.start();
        }
    }
}
