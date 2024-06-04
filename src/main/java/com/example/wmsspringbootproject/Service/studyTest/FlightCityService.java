package com.example.wmsspringbootproject.Service.studyTest;

import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.entity.FlightCity;

import java.util.List;

public interface FlightCityService {
    Result<List<FlightCity>> getCityList();
}
