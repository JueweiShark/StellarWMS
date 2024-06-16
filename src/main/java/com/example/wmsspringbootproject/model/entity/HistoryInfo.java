package com.example.wmsspringbootproject.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 历史信息
 * </p>
 *
 * @author 初秋
 * @since 2024-06-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("history_info")
public class HistoryInfo implements Serializable {

    private static final long serialVersionUID = 1L;


    @Schema(description="id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description="用户id")
    @TableField("user_id")
    private Integer userId;

    @Schema(description="ip地址")
    @TableField("ip")
    private String ip;

    @Schema(description="国家")
    @TableField("nation")
    private String nation;

    @Schema(description="省份")
    @TableField("province")
    private String province;

    @Schema(description="城市")
    @TableField("city")
    private String city;

    @Schema(description="创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;


}
