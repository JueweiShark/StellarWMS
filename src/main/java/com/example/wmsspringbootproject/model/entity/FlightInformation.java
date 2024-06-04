package com.example.wmsspringbootproject.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class FlightInformation {
    @TableId(type= IdType.AUTO)
    private String id;
    private String startDate;
    private int neededTime;
    private String startCity;
    private String endCity;
    private int seatNum;
    private int deleted;
}
