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

    private String getCellStringValue(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING:
                String str = cell.getStringCellValue().trim();
                if ("/".equals(str) || str.isEmpty()) return null;
                return str;
            case NUMERIC:
                double val = cell.getNumericCellValue();
                if (val == Math.floor(val) && !Double.isInfinite(val)) {
                    return String.valueOf((long) val);
                }
                return String.valueOf(val);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BLANK:
                return null;
            default:
                return null;
        }
    }

    private LocalDate parseDate(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }
        String str = getCellStringValue(cell);
        if (str != null && !str.isEmpty()) {
            try {
                return LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (Exception e) {
                try {
                    return LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy/M/d"));
                } catch (Exception ex) {
                    return null;
                }
            }
        }
        return null;
    }

    private LocalTime parseTime(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalTime();
        }
        String str = getCellStringValue(cell);
        if (str != null && !str.isEmpty()) {
            try {
                return LocalTime.parse(str, DateTimeFormatter.ofPattern("HH:mm"));
            } catch (Exception e) {
                try {
                    return LocalTime.parse(str, DateTimeFormatter.ofPattern("H:mm"));
                } catch (Exception ex) {
                    return null;
                }
            }
        }
        return null;
    }

    private boolean parseBoolean(Cell cell) {
        String str = getCellStringValue(cell);
        return "1".equals(str) || "是".equals(str) || "true".equalsIgnoreCase(str);
    }

    @Override
    public int importExcel(MultipartFile file) {
        List<Reservation> reservations = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String name = getCellStringValue(row.getCell(1));
                String phone = getCellStringValue(row.getCell(2));
                if (name == null && phone == null) continue;

                Reservation reservation = new Reservation();

                reservation.setName(name);

                reservation.setPhone(phone);

                String guestCountStr = getCellStringValue(row.getCell(3));
                if (guestCountStr != null) {
                    reservation.setGuestCount(Integer.parseInt(guestCountStr));
                }

                String roomCountStr = getCellStringValue(row.getCell(4));
                if (roomCountStr != null) {
                    reservation.setRoomCount(Integer.parseInt(roomCountStr));
                }

                // 列5: 房间类型（可多选）- 描述性文字，跳过
                // 列6: 大床房, 列7: 标准间, 列8: 套房
                reservation.setRoomTypeSingle(parseBoolean(row.getCell(6)));
                reservation.setRoomTypeStandard(parseBoolean(row.getCell(7)));
                reservation.setRoomTypeSuite(parseBoolean(row.getCell(8)));

                reservation.setPickupLocation(getCellStringValue(row.getCell(9)));

                reservation.setArrivalDate(parseDate(row.getCell(10)));

                reservation.setArrivalTime(parseTime(row.getCell(11)));

                reservation.setHotel(getCellStringValue(row.getCell(12)));

                reservation.setRoomNumber(getCellStringValue(row.getCell(13)));

                reservation.setTableNumber(getCellStringValue(row.getCell(14)));

                reservation.setRemark(getCellStringValue(row.getCell(15)));

                reservations.add(reservation);
            }

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
