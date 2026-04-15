package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.model.Reservation;
import com.tencent.wxcloudrun.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    /**
     * 导入Excel文件并插入数据库
     */
    @PostMapping("/import")
    public ApiResponse importExcel(@RequestParam("file") MultipartFile file) {
        try {
            int count = reservationService.importExcel(file);
            return ApiResponse.ok("成功导入 " + count + " 条数据");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 根据手机号或姓名查询（支持模糊搜索）
     */
    @GetMapping("/search")
    public ApiResponse searchReservation(
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "name", required = false) String name) {
        try {
            List<Reservation> reservations = reservationService.findByPhoneOrName(phone, name);
            return ApiResponse.ok(reservations);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 修改预订信息
     */
    @PutMapping
    public ApiResponse updateReservation(@RequestBody Reservation reservation) {
        try {
            int count = reservationService.updateReservation(reservation);
            if (count > 0) {
                return ApiResponse.ok("更新成功");
            } else {
                return ApiResponse.error("更新失败，未找到对应记录");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("更新失败：" + e.getMessage());
        }
    }
}
