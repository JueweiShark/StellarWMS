package com.example.wmsspringbootproject.Service.studyTest.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.Service.studyTest.FlightInformationService;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.mapper.FlightCityMapper;
import com.example.wmsspringbootproject.mapper.FlightInformationMapper;
import com.example.wmsspringbootproject.model.entity.FlightCity;
import com.example.wmsspringbootproject.model.entity.FlightInformation;
import com.example.wmsspringbootproject.model.form.FlightInformationForm;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightInformationServiceImpl extends ServiceImpl<FlightInformationMapper, FlightInformation> implements FlightInformationService {
    @Autowired
    private FlightCityMapper flightCityMapper;
    @Override
    public Result<IPage<FlightInformation>> getList(FlightInformationForm flightInformation) {
        LambdaQueryWrapper<FlightInformation> queryWrapper = new LambdaQueryWrapper<>();
        Page<FlightInformation> page = new Page<>(flightInformation.getPageNum(), flightInformation.getPageSize());
        if (flightInformation.getStartCity().equals("所有城市")){
            flightInformation.setStartCity("");
        }
        if (flightInformation.getEndCity().equals("所有城市")){
            flightInformation.setEndCity("");
        }
        if (flightInformation.getStartDate()!= null) {
            queryWrapper.ge(FlightInformation::getStartDate, flightInformation.getStartDate());
        }
        if (flightInformation.getStartCity()!= null) {
            queryWrapper.like(FlightInformation::getStartCity, flightInformation.getStartCity());
        }
        if (flightInformation.getEndCity()!= null) {
            queryWrapper.like(FlightInformation::getEndCity, flightInformation.getEndCity());
        }
        queryWrapper.eq(FlightInformation::getDeleted, 0);
        IPage<FlightInformation> pageResult = this.page(page, queryWrapper);
        return Result.success(pageResult);
    }

    @Override
    public Result add(FlightInformationForm flightInformationForm) {
        FlightInformation flightInformation = new FlightInformation();

        DateTime now = DateTime.now();
        String id = "CA"+now.toString("yyyyMMddHHmmss");
        flightInformation.setId(id);
        if(flightInformationForm.getStartDate()!= null&&flightInformationForm.getStartCity()!= null&&flightInformationForm.getEndCity()!= null&&flightInformationForm.getNeededTime()!= 0&&flightInformationForm.getSeatNum()!=0) {
            flightInformation.setStartDate(flightInformationForm.getStartDate());
            flightInformation.setStartCity(flightInformationForm.getStartCity());
            flightInformation.setEndCity(flightInformationForm.getEndCity());
            flightInformation.setNeededTime(flightInformationForm.getNeededTime());
            flightInformation.setSeatNum(flightInformationForm.getSeatNum());
            return Result.success(this.save(flightInformation));
        }else {
            return Result.failed("添加航班信息失败");
        }
    }

    @Override
    public Result update(FlightInformationForm flightInformation) {
        FlightInformation flightInformation1 = this.getById(flightInformation.getId());
        if(flightInformation1!= null) {
            flightInformation1.setStartDate(flightInformation.getStartDate());
            flightInformation1.setStartCity(flightInformation.getStartCity());
            flightInformation1.setEndCity(flightInformation.getEndCity());
            flightInformation1.setNeededTime(flightInformation.getNeededTime());
            flightInformation1.setSeatNum(flightInformation.getSeatNum());
            return Result.success(this.updateById(flightInformation1));
        }
        return null;
    }

    @Override
    public Result delete(String id) {
        FlightInformation flightInformation = this.getById(id);
        if(flightInformation!= null) {
            flightInformation.setDeleted(1);
            return Result.success(this.updateById(flightInformation));
        }else {
            return Result.failed("删除失败");
        }
    }
}
