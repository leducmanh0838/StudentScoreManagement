/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.controllers;

import com.ldm.configs.LoggerConfig;
import com.ldm.dto.CourseSessionStatsDTO;
import com.ldm.services.CourseService;
import com.ldm.services.StatsService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author PC
 */
@Controller
public class StatsController {

    @Autowired
    StatsService statsService;
    @Autowired
    CourseService courseService;

    @GetMapping("/stats")
    public String stats(Model model, @RequestParam(name = "courseId", required = false) Integer courseId) {
        List<Object[]> courseNames = courseService.getAllCourseNames();
        model.addAttribute("courseNames", courseNames);
        model.addAttribute("selectedCourseId", courseId);

        if (courseId != null) {
            String selectedCourseName = courseNames.stream()
                    .filter(c -> courseId.equals(c[0]))
                    .map(c -> (String) c[1])
                    .findFirst()
                    .orElse(null);

            model.addAttribute("selectedCourseName", selectedCourseName);
        }

//        List<String> labels = Arrays.asList("TH001-IT1000", "TH002-IT1000", "TH003-IT1000", "TH004-IT1000");
//        List<Integer> data = Arrays.asList(4, 8, 8, 8);
//
//        model.addAttribute("labels", labels);
//        model.addAttribute("data", data);
        if (courseId != null) {
            List<CourseSessionStatsDTO> stats = statsService.countEnrollmentsByCourse(courseId);
            model.addAttribute("stats", stats);

            List<String> labels = new ArrayList<>();
            List<Long> data = new ArrayList<>();

            for (CourseSessionStatsDTO s : stats) {
                labels.add(s.getCourseSessionCode());    
                data.add(s.getEnrollmentCount());       
            }

            model.addAttribute("labels", labels);
            model.addAttribute("data", data);
        }

        return "stats";
    }

    @GetMapping("/stats/export")
    public void exportToExcel(HttpServletResponse response, @RequestParam(name = "courseId") Integer courseId, 
            @RequestParam(name = "courseName") String courseName) throws IOException {
        List<CourseSessionStatsDTO> stats = statsService.countEnrollmentsByCourse(courseId);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Báo Cáo Đăng Ký");

        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);

        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(String.format("Báo cáo đăng ký môn %s", courseName));
        titleCell.setCellStyle(titleStyle);

        // Merge
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2)); // từ cột 0 đến 2

        String[] columns = {"Mã buổi học", "Giảng viên phụ trách", "Số lượng đăng ký"};
        Row headerRow = sheet.createRow(1);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        int rowNum = 2;
        for (CourseSessionStatsDTO stat : stats) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(stat.getCourseSessionCode());
            row.createCell(1).setCellValue(stat.getTeacherName());
            row.createCell(2).setCellValue(stat.getEnrollmentCount());
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=bao_cao_dang_ky.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
