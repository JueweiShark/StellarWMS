package com.example.wmsspringbootproject.im.http.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.im.http.dao.ImChatGroupUserMapper;
import com.example.wmsspringbootproject.im.http.entity.ImChatGroupUser;
import com.example.wmsspringbootproject.im.http.service.ImChatGroupUserService;
import org.springframework.stereotype.Service;

@Service
public class ImChatGroupUserServiceImpl extends ServiceImpl<ImChatGroupUserMapper, ImChatGroupUser>
        implements ImChatGroupUserService {

}
