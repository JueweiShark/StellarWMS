package com.example.wmsspringbootproject.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class FlightCity {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String city;
}
