package com.example.wmsspringbootproject.im.http.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.im.http.dao.ImChatUserGroupMessageMapper;
import com.example.wmsspringbootproject.im.http.entity.ImChatUserGroupMessage;
import com.example.wmsspringbootproject.im.http.service.ImChatUserGroupMessageService;
import org.springframework.stereotype.Service;

@Service
public class ImChatUserGroupMessageServiceImpl extends ServiceImpl<ImChatUserGroupMessageMapper, ImChatUserGroupMessage>
        implements ImChatUserGroupMessageService {

}
