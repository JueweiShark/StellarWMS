package com.example.wmsspringbootproject.im.http.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.im.http.dao.ImChatGroupMapper;
import com.example.wmsspringbootproject.im.http.entity.ImChatGroup;
import com.example.wmsspringbootproject.im.http.service.ImChatGroupService;
import org.springframework.stereotype.Service;

@Service
public class ImChatGroupServiceImpl extends ServiceImpl<ImChatGroupMapper, ImChatGroup>
        implements ImChatGroupService {
}
