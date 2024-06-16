package com.example.wmsspringbootproject.im.http.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.wmsspringbootproject.im.http.entity.ImChatUserMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImChatUserMessageMapper extends BaseMapper<ImChatUserMessage> {
}
