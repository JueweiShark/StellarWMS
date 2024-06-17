package com.example.wmsspringbootproject.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.Service.HistoryInfoService;
import com.example.wmsspringbootproject.mapper.HistoryInfoMapper;
import com.example.wmsspringbootproject.model.entity.HistoryInfo;
import org.springframework.stereotype.Service;

@Service
public class HistoryInfoServiceImpl extends ServiceImpl<HistoryInfoMapper, HistoryInfo> implements HistoryInfoService {

}
