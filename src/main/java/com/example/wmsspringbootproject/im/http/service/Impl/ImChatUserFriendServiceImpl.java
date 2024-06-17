package com.example.wmsspringbootproject.im.http.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.im.http.dao.ImChatUserFriendMapper;
import com.example.wmsspringbootproject.im.http.entity.ImChatUserFriend;
import com.example.wmsspringbootproject.im.http.service.ImChatUserFriendService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ImChatUserFriendServiceImpl extends ServiceImpl<ImChatUserFriendMapper, ImChatUserFriend>
        implements ImChatUserFriendService {
}
