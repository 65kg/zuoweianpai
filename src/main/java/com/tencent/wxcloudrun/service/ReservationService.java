package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.model.Reservation;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReservationService {
    // 导入Excel文件并插入数据库
    int importExcel(MultipartFile file);
    
    // 根据手机号或姓名查询（支持模糊搜索）
    List<Reservation> findByPhoneOrName(String phone, String name);
    
    // 修改预订信息
    int updateReservation(Reservation reservation);
}
