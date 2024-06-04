package com.example.wmsspringbootproject.model.form;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.wmsspringbootproject.common.base.BasePageQuery;
import lombok.Data;

@Data
public class FlightInformationForm extends BasePageQuery {
    private String id;
    private String startDate;
    private int neededTime;
    private String startCity;
    private String endCity;
    private int seatNum;
    private int deleted;
}
