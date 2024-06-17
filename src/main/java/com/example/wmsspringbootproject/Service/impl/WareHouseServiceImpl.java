package com.example.wmsspringbootproject.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.Service.WareHouseService;
import com.example.wmsspringbootproject.Utils.TextUtil;
import com.example.wmsspringbootproject.common.Annotation.DataPermission;
import com.example.wmsspringbootproject.common.Annotation.LogNote;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.constants.Constants;
import com.example.wmsspringbootproject.converter.WareHouseConverter;
import com.example.wmsspringbootproject.mapper.WareHouseMapper;
import com.example.wmsspringbootproject.model.entity.Warehouses;
import com.example.wmsspringbootproject.model.form.WareHouseForm;
import com.example.wmsspringbootproject.model.query.WarehouseQuery;
import com.example.wmsspringbootproject.model.vo.WareHouseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WareHouseServiceImpl extends ServiceImpl<WareHouseMapper, Warehouses> implements WareHouseService {

    private final WareHouseConverter converter;

    @LogNote(description = "获取仓库列表")
    @Override
    @DataPermission(warehouseIdColumnName="id")
    public Result<IPage<WareHouseVO>> warehouseList(WarehouseQuery query) {
        LambdaQueryWrapper<Warehouses> queryWrapper=new LambdaQueryWrapper<>();
        Page<Warehouses> warehousesPage=new Page<>(query.getPageNum(),query.getPageSize());
        if (query != null) {
            queryWrapper.gt(Warehouses::getId,0);

            if (!TextUtil.textIsEmpty(query.getName())) {
                queryWrapper.and(wrapper -> wrapper.like(Warehouses::getName, query.getName()));
            }
            if (!TextUtil.textIsEmpty(query.getAddress())) {
                queryWrapper.and(wrapper -> wrapper.like(Warehouses::getAddress, query.getAddress()));
            }
            if (!TextUtil.textIsEmpty(query.getContactPhone())) {
                queryWrapper.and(wrapper -> wrapper.like(Warehouses::getContactPhone, query.getContactPhone()));
            }
            if (!TextUtil.textIsEmpty(query.getContactPerson())) {
                queryWrapper.and(wrapper -> wrapper.like(Warehouses::getContactPerson, query.getContactPerson()));
            }
            if (query.getStatus()!=-1) {
                queryWrapper.and(wrapper -> wrapper.eq(Warehouses::getStatus, query.getStatus()));
            }
            if(TextUtil.isNotEmpty(query.getDeleted())) {
                queryWrapper.and(wrapper -> wrapper.eq(Warehouses::getDeleted, query.getDeleted()));
            }else {
                queryWrapper.and(wrapper -> wrapper.eq(Warehouses::getDeleted, 1));
            }
        }
        IPage<Warehouses> warehousesList =this.page(warehousesPage,queryWrapper);
        IPage<WareHouseVO> wareHouseVOIPage = new Page<>();
        wareHouseVOIPage.setRecords(warehousesList.getRecords().stream().map(converter::entity2Vo).toList());
        wareHouseVOIPage.setPages(warehousesList.getPages());
        wareHouseVOIPage.setCurrent(warehousesList.getCurrent());
        wareHouseVOIPage.setSize(warehousesList.getSize());
        wareHouseVOIPage.setTotal(warehousesList.getTotal());
        return Result.success(wareHouseVOIPage);
    }

    @LogNote(description = "添加仓库记录")
    @Override
    public Result<Boolean> saveWareHouseInfo(WareHouseForm form) {
        Warehouses warehouses =converter.form2Entity(form);
        warehouses.setCreateTime(TextUtil.formatDate(new Date()));
        warehouses.setDeleted(Constants.Default.DELETED);
        Boolean result=this.save(warehouses);
        return Result.success(result);
    }

    @LogNote(description = "更新仓库记录")
    @Override
    public Result<Boolean> updateWareHouseInfo(WareHouseForm form) {
        Warehouses warehouses =this.getById(form.getId());
        if(warehouses !=null){
            Warehouses target=converter.form2Entity(form);
            target.setStatus(TextUtil.textIsEmpty(form.getStatus()) ? warehouses.getStatus() : form.getStatus());
            target.setDeleted(TextUtil.textIsEmpty(form.getDeleted()) ? warehouses.getDeleted() : form.getDeleted());
            target.setCreateTime(TextUtil.textIsEmpty(form.getCreateTime()) ? warehouses.getCreateTime() : form.getCreateTime());
            Boolean result=this.baseMapper.updateById(target)>0;
            return result ? Result.success(result) : Result.failed("更新仓库信息失败");
        }
        return Result.failed("仓库信息不存在");
    }


    @Override
    @LogNote(description = "删除仓库信息")
    public Result<Boolean> removeWareHouseInfo(String ids) {
        String[] idArray=ids.split(",");
        if(idArray.length>1){
            for (String id : idArray) {
                Warehouses warehouses=this.getById(id);
                warehouses.setDeleted(2);
                this.baseMapper.updateById(warehouses);
            }
            return Result.success();
        }else{
            Warehouses warehouses=this.getById(ids);
            warehouses.setDeleted(2);
            Boolean result=this.baseMapper.updateById(warehouses)>0;
            return result ? Result.success(result) : Result.failed("删除仓库信息失败");
        }
    }

    @LogNote(description = "获取仓库详情")
    @Override
    public Result<WareHouseVO> getWareHouseDetails(Integer id) {
        Warehouses warehouses =this.getById(id);
        return warehouses ==null ? Result.failed("没有该仓库") : Result.success(converter.entity2Vo(warehouses));
    }

    @LogNote(description = "获取仓库分布统计数据")
    @Override
    public Result<Map<String, Integer>> getWarehouseDistribute() {

        Map<String, Integer> dist = new HashMap<>();
        List<Warehouses> dataList=this.baseMapper.selectList(new LambdaQueryWrapper<Warehouses>()
                .select(Warehouses::getAddress));
        List<String> regionList=dataList.stream().map(Warehouses::getAddress)
                .map(item->
                        item.replaceAll("省.*|自治区.*|特别行政区.*",""))
                .map(i->
                        i.replaceAll(".*族",i+"自治区")).toList();
        for (String region : regionList) {
            if(!dist.containsKey(region)){
                dist.put(region,1);
            }else{
                dist.put(region,dist.get(region)+1);
            }
        }
        return Result.success(dist);
    }
}
