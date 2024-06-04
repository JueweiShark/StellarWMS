package com.example.wmsspringbootproject.model.query;

import com.example.wmsspringbootproject.common.base.BasePageQuery;
import lombok.Data;

@Data
public class FlightInformationQuery extends BasePageQuery {
    private String id;
    private String startDate;
    private int neededTime;
    private String startCity;
    private String endCity;
    private int seatNum;
    private int deleted;
}
