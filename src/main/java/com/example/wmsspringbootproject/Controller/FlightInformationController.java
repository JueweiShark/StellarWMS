package com.example.wmsspringbootproject.Controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.wmsspringbootproject.Service.studyTest.FlightCityService;
import com.example.wmsspringbootproject.Service.studyTest.FlightInformationService;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.entity.FlightCity;
import com.example.wmsspringbootproject.model.entity.FlightInformation;
import com.example.wmsspringbootproject.model.form.FlightInformationForm;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "00.航班信息管理系统")
@RestController
@RequiredArgsConstructor
@CrossOrigin

@RequestMapping("/flightInformation")

public class FlightInformationController {
    private final FlightInformationService flightInformationService;
    private final FlightCityService flightCityService;
    @GetMapping("/list")
    public Result<IPage<FlightInformation>> flightInformationList(
            @ParameterObject FlightInformationForm flightInformation
    ) {
        System.out.println("+++++++++++++++++++++++++"+flightInformation.getPageNum()+"pageSize:"+flightInformation.getPageSize()+"date:"+flightInformation.getStartDate());

        return flightInformationService.getList(flightInformation);
    }
    @GetMapping("/cityList")
    public Result<List<FlightCity>> cityList(){
        return flightCityService.getCityList();
    }
    @PostMapping("/add")
    public Result add(
            @RequestBody FlightInformationForm flightInformation
    ){
        System.out.println(flightInformation);
        return flightInformationService.add(flightInformation);
    }

    @PostMapping("/update")
    public Result update(
            @RequestBody FlightInformationForm flightInformation
    ){
        System.out.println(flightInformation);
        return flightInformationService.update(flightInformation);
    }

    @PostMapping("/delete/{id}")
    public Result delete(
            @Parameter @PathVariable("id") String id
    ){

        return flightInformationService.delete(id);
    }
}
