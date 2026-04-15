package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Reservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReservationMapper {
    // 插入预订信息
    int insert(Reservation reservation);
    
    // 根据手机号或姓名查询（支持模糊搜索）
    List<Reservation> findByPhoneOrName(@Param("phone") String phone, @Param("name") String name);
    
    // 根据ID更新预订信息
    int updateById(Reservation reservation);
    
    // 批量插入预订信息
    int batchInsert(@Param("reservations") List<Reservation> reservations);
}
