package com.example.wmsspringbootproject.Service.studyTest.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.Service.studyTest.FlightCityService;
import com.example.wmsspringbootproject.Service.studyTest.FlightInformationService;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.mapper.FlightCityMapper;
import com.example.wmsspringbootproject.mapper.FlightInformationMapper;
import com.example.wmsspringbootproject.model.entity.FlightCity;
import com.example.wmsspringbootproject.model.entity.FlightInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightCityServiceImpl extends ServiceImpl<FlightCityMapper, FlightCity> implements FlightCityService {
    @Override
    public Result<List<FlightCity>> getCityList() {
        LambdaQueryWrapper<FlightCity> queryWrapper = new LambdaQueryWrapper<FlightCity>();
        List<FlightCity> list = this.list(queryWrapper);
        return Result.success(list);
    }
}
