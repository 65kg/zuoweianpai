package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * 预订信息实体类
 * 用于存储来宾的住宿和餐饮预订信息
 */
@Data
public class Reservation {
    
    /** 序号 - 主键，自增 */
    private Integer id;
    
    /** 姓名 - 来宾姓名 */
    private String name;
    
    /** 手机号码 - 11位手机号 */
    private String phone;
    
    /** 来宾人数 - 参加活动的来宾数量 */
    private Integer guestCount;
    
    /** 房间需求数 - 需要预订的房间数量 */
    private Integer roomCount;
    
    /** 房间类型-大床房 - 是否需要大床房（true=是，false=否） */
    private Boolean roomTypeSingle;
    
    /** 房间类型-标准间 - 是否需要标准间（true=是，false=否） */
    private Boolean roomTypeStandard;
    
    /** 房间类型-套房 - 是否需要套房（true=是，false=否） */
    private Boolean roomTypeSuite;
    
    /** 接站地 - 需要接站的地点 */
    private String pickupLocation;
    
    /** 到达日期 - 来宾到达的日期（格式：yyyy-MM-dd） */
    private LocalDate arrivalDate;
    
    /** 到达时间 - 来宾到达的时间（格式：HH:mm） */
    private LocalTime arrivalTime;
    
    /** 酒店 - 预订的酒店名称 */
    private String hotel;
    
    /** 房间号 - 分配的房间号码 */
    private String roomNumber;
    
    /** 桌号 - 餐饮安排的桌号 */
    private String tableNumber;
    
    /** 备注 - 其他需要说明的信息 */
    private String remark;
    
    /** 创建时间 - 记录创建的时间 */
    private LocalDateTime createdAt;
    
    /** 更新时间 - 记录最后更新的时间 */
    private LocalDateTime updatedAt;
}
