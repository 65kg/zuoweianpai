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
                return cell.getStringCellValue().trim();
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

    @Override
    public int importExcel(MultipartFile file) {
        List<Reservation> reservations = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Reservation reservation = new Reservation();

                reservation.setName(getCellStringValue(row.getCell(1)));

                reservation.setPhone(getCellStringValue(row.getCell(2)));

                String guestCountStr = getCellStringValue(row.getCell(3));
                if (guestCountStr != null && !guestCountStr.isEmpty()) {
                    reservation.setGuestCount(Integer.parseInt(guestCountStr));
                }

                String roomCountStr = getCellStringValue(row.getCell(4));
                if (roomCountStr != null && !roomCountStr.isEmpty()) {
                    reservation.setRoomCount(Integer.parseInt(roomCountStr));
                }

                String singleStr = getCellStringValue(row.getCell(5));
                reservation.setRoomTypeSingle("是".equals(singleStr) || "1".equals(singleStr) || "true".equalsIgnoreCase(singleStr));

                String standardStr = getCellStringValue(row.getCell(6));
                reservation.setRoomTypeStandard("是".equals(standardStr) || "1".equals(standardStr) || "true".equalsIgnoreCase(standardStr));

                String suiteStr = getCellStringValue(row.getCell(7));
                reservation.setRoomTypeSuite("是".equals(suiteStr) || "1".equals(suiteStr) || "true".equalsIgnoreCase(suiteStr));

                reservation.setPickupLocation(getCellStringValue(row.getCell(8)));

                reservation.setArrivalDate(parseDate(row.getCell(9)));

                reservation.setArrivalTime(parseTime(row.getCell(10)));

                reservation.setHotel(getCellStringValue(row.getCell(11)));

                reservation.setRoomNumber(getCellStringValue(row.getCell(12)));

                reservation.setTableNumber(getCellStringValue(row.getCell(13)));

                reservation.setRemark(getCellStringValue(row.getCell(14)));

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
