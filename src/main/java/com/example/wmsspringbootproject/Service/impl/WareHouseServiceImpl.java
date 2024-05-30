package com.example.wmsspringbootproject.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.Service.WareHouseService;
import com.example.wmsspringbootproject.Utils.TextUtil;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class WareHouseServiceImpl extends ServiceImpl<WareHouseMapper, Warehouses> implements WareHouseService {

    private final WareHouseConverter converter;

    @Override
    public Result<List<WareHouseVO>> warehouseList(WarehouseQuery query) {

        LambdaQueryWrapper<Warehouses> queryWrapper=new LambdaQueryWrapper<>();
        Page<Warehouses> warehousesPage=new Page<>(query.getPageNum(),query.getPageSize());
        if (query != null) {
            queryWrapper.gt(Warehouses::getId,0);

            if (!TextUtil.textIsEmpty(query.getName())) {
                queryWrapper.or(wrapper -> wrapper.like(Warehouses::getName, query.getName()));
            }
            if (!TextUtil.textIsEmpty(query.getAddress())) {
                queryWrapper.or(wrapper -> wrapper.like(Warehouses::getAddress, query.getAddress()));
            }
            if (!TextUtil.textIsEmpty(query.getContactPhone())) {
                queryWrapper.or(wrapper -> wrapper.like(Warehouses::getContactPhone, query.getContactPhone()));
            }
            if (!TextUtil.textIsEmpty(query.getContactPerson())) {
                queryWrapper.or(wrapper -> wrapper.like(Warehouses::getContactPerson, query.getContactPerson()));
            }
            if (!TextUtil.textIsEmpty(query.getStatus())) {
                queryWrapper.or(wrapper -> wrapper.eq(Warehouses::getStatus, query.getStatus()));
            }
        }
        IPage<Warehouses> warehousesList =this.page(warehousesPage,queryWrapper);
        List<WareHouseVO> wareHouseVOList= warehousesList.getRecords().stream().map(converter::entity2Vo).toList();

        return Result.success(wareHouseVOList);
    }

    @Override
    public Result<Boolean> saveWareHouseInfo(WareHouseForm form) {
        Warehouses warehouses =converter.form2Entity(form);
        warehouses.setCreateTime(TextUtil.formatDate(new Date()));
        warehouses.setDeleted(Constants.Default.DELETED);
        warehouses.setStatus(Constants.Default.PREPARED);
        Boolean result=this.save(warehouses);
        return Result.success(result);
    }

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
    public Result<Boolean> removeWareHouseInfo(String ids) {
        String[] idArray=ids.split(",");
        if(idArray.length>1){
            for (String id : idArray) {
                this.removeById(id);
            }
            return Result.success();
        }else{
            Boolean result=this.removeById(ids);
            return result ? Result.success(result) : Result.failed("删除仓库信息失败");
        }
    }

    @Override
    public Result<WareHouseVO> getWareHouseDetails(Integer id) {
        Warehouses warehouses =this.getById(id);
        return warehouses ==null ? Result.failed("没有该仓库") : Result.success(converter.entity2Vo(warehouses));
    }
}
