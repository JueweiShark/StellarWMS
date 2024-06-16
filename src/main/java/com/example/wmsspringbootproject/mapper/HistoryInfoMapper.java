package com.example.wmsspringbootproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.wmsspringbootproject.model.entity.HistoryInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 历史信息 Mapper 接口
 * </p>
 *
 * @author 初秋
 * @since 2024-06-09
 */
@Mapper
public interface HistoryInfoMapper extends BaseMapper<HistoryInfo> {

    /**
     * TODO 访问IP最多的10个省
     */
    @Select("select nation, province, count(distinct ip) as num" +
            " from history_info" +
            " where nation is not null and province is not null" +
            " group by nation, province" +
            " order by num desc" +
            " limit 10")
    List<Map<String, Object>> getHistoryByProvince();

    /**
     * TODO 访问次数最多的10个IP
     */
    @Select("select ip, count(*) as num" +
            " from history_info" +
            " group by ip" +
            " order by num desc" +
            " limit 10")
    List<Map<String, Object>> getHistoryByIp();

    /**
     * TODO 访问24小时内的数据
     */
    @Select("select ip, user_id, nation, province" +
            " from history_info" +
            " where create_time >= (now() - interval 24 hour)")
    List<Map<String, Object>> getHistoryBy24Hour();

    /**
     * TODO 总访问量
     */
    @Select("select count(*) from history_info")
    Long getHistoryCount();
}
