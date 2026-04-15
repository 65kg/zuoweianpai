package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.ReservationMapper;
import com.tencent.wxcloudrun.model.Reservation;
import com.tencent.wxcloudrun.service.ReservationService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationMapper reservationMapper;

    @Override
    public int importExcel(MultipartFile file) {
        List<Reservation> reservations = new ArrayList<>();
        
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            
            // 跳过表头，从第二行开始读取
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                Reservation reservation = new Reservation();
                
                // 姓名
                if (row.getCell(1) != null) {
                    reservation.setName(row.getCell(1).getStringCellValue());
                }
                
                // 手机号码
                if (row.getCell(2) != null) {
                    reservation.setPhone(row.getCell(2).getStringCellValue());
                }
                
                // 来宾人数
                if (row.getCell(3) != null) {
                    reservation.setGuestCount((int) row.getCell(3).getNumericCellValue());
                }
                
                // 房间需求数
                if (row.getCell(4) != null) {
                    reservation.setRoomCount((int) row.getCell(4).getNumericCellValue());
                }
                
                // 房间类型 - 大床房
                if (row.getCell(5) != null) {
                    reservation.setRoomTypeSingle("是".equals(row.getCell(5).getStringCellValue()));
                }
                
                // 房间类型 - 标准间
                if (row.getCell(6) != null) {
                    reservation.setRoomTypeStandard("是".equals(row.getCell(6).getStringCellValue()));
                }
                
                // 房间类型 - 套房
                if (row.getCell(7) != null) {
                    reservation.setRoomTypeSuite("是".equals(row.getCell(7).getStringCellValue()));
                }
                
                // 接站地
                if (row.getCell(8) != null) {
                    reservation.setPickupLocation(row.getCell(8).getStringCellValue());
                }
                
                // 到达日期
                if (row.getCell(9) != null) {
                    Cell dateCell = row.getCell(9);
                    if (dateCell.getCellType() == CellType.NUMERIC) {
                        // Excel日期格式
                        reservation.setArrivalDate(dateCell.getLocalDateTimeCellValue().toLocalDate());
                    } else if (dateCell.getCellType() == CellType.STRING) {
                        // 字符串日期格式
                        reservation.setArrivalDate(LocalDate.parse(dateCell.getStringCellValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                }
                
                // 到达时间
                if (row.getCell(10) != null) {
                    Cell timeCell = row.getCell(10);
                    if (timeCell.getCellType() == CellType.NUMERIC) {
                        // Excel时间格式
                        reservation.setArrivalTime(timeCell.getLocalDateTimeCellValue().toLocalTime());
                    } else if (timeCell.getCellType() == CellType.STRING) {
                        // 字符串时间格式
                        reservation.setArrivalTime(LocalTime.parse(timeCell.getStringCellValue(), DateTimeFormatter.ofPattern("HH:mm")));
                    }
                }
                
                // 酒店
                if (row.getCell(11) != null) {
                    reservation.setHotel(row.getCell(11).getStringCellValue());
                }
                
                // 房间号
                if (row.getCell(12) != null) {
                    if (row.getCell(12).getCellType() == CellType.NUMERIC) {
                        reservation.setRoomNumber(String.valueOf((int) row.getCell(12).getNumericCellValue()));
                    } else {
                        reservation.setRoomNumber(row.getCell(12).getStringCellValue());
                    }
                }
                
                // 桌号
                if (row.getCell(13) != null) {
                    if (row.getCell(13).getCellType() == CellType.NUMERIC) {
                        reservation.setTableNumber(String.valueOf((int) row.getCell(13).getNumericCellValue()));
                    } else {
                        reservation.setTableNumber(row.getCell(13).getStringCellValue());
                    }
                }
                
                // 备注
                if (row.getCell(14) != null) {
                    reservation.setRemark(row.getCell(14).getStringCellValue());
                }
                
                reservations.add(reservation);
            }
            
            // 批量插入数据
            if (!reservations.isEmpty()) {
                return reservationMapper.batchInsert(reservations);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return 0;
    }

    @Override
    public List<Reservation> findByPhoneOrName(String phone, String name) {
        return reservationMapper.findByPhoneOrName(phone, name);
    }

    @Override
    public int updateReservation(Reservation reservation) {
        return reservationMapper.updateById(reservation);
    }
}
