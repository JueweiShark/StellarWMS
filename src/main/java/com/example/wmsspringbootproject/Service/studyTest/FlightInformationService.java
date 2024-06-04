package com.example.wmsspringbootproject.Service.studyTest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.entity.FlightCity;
import com.example.wmsspringbootproject.model.entity.FlightInformation;
import com.example.wmsspringbootproject.model.form.FlightInformationForm;

import java.util.List;

public interface FlightInformationService {
    Result<IPage<FlightInformation>> getList(FlightInformationForm flightInformation);

    Result add(FlightInformationForm flightInformation);

    Result update(FlightInformationForm flightInformation);

    Result delete(String id);
}
