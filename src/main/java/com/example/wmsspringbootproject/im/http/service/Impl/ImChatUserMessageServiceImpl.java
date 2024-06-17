package com.example.wmsspringbootproject.im.http.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.im.http.dao.ImChatUserMessageMapper;
import com.example.wmsspringbootproject.im.http.entity.ImChatUserMessage;
import com.example.wmsspringbootproject.im.http.service.ImChatUserMessageService;
import org.springframework.stereotype.Service;

@Service
public class ImChatUserMessageServiceImpl extends ServiceImpl<ImChatUserMessageMapper, ImChatUserMessage>
        implements ImChatUserMessageService {
}
