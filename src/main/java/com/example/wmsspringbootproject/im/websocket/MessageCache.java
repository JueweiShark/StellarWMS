package com.example.wmsspringbootproject.im.websocket;

import com.example.wmsspringbootproject.Utils.EmailSender;
import com.example.wmsspringbootproject.im.http.entity.ImChatUserGroupMessage;
import com.example.wmsspringbootproject.im.http.entity.ImChatUserMessage;
import com.example.wmsspringbootproject.im.http.service.ImChatUserGroupMessageService;
import com.example.wmsspringbootproject.im.http.service.ImChatUserMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>
 * 消息缓存 处理类
 * </p>
 *
 * @author 初秋
 * @since 2024-06-10
 */
@Component
@Slf4j
public class MessageCache {

    @Autowired
    private ImChatUserMessageService imChatUserMessageService;

    @Autowired
    private ImChatUserGroupMessageService imChatUserGroupMessageService;

    @Autowired
    private EmailSender emailSender;

    //用户消息 缓存列表
    private final List<ImChatUserMessage> userMessage = new ArrayList<>();

    //群消息 缓存列表
    private final List<ImChatUserGroupMessage> groupMessage = new ArrayList<>();

    //读写锁对象
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public void putUserMessage(ImChatUserMessage message) {
        readWriteLock.readLock().lock();
        try {
            userMessage.add(message);
        } finally {
            readWriteLock.readLock().unlock();
        }

        try {
            // FIXME 新增发送消息的方法
            emailSender.send();
        } catch (Exception e) {
            log.error("发送IM邮件失败：", e);
        }
    }

    public void putGroupMessage(ImChatUserGroupMessage message) {
        readWriteLock.readLock().lock();
        try {
            groupMessage.add(message);
        } finally {
            readWriteLock.readLock().unlock();
        }

    }

    @Scheduled(fixedDelay = 5000)
    public void saveUserMessage() {
        readWriteLock.writeLock().lock();
        try {
            if (!CollectionUtils.isEmpty(userMessage)) {
                imChatUserMessageService.saveBatch(userMessage);
                userMessage.clear();
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Scheduled(fixedDelay = 10000)
    public void saveGroupMessage() {
        readWriteLock.writeLock().lock();
        try {
            if (!CollectionUtils.isEmpty(groupMessage)) {
                imChatUserGroupMessageService.saveBatch(groupMessage);
                groupMessage.clear();
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
}
