/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ldm.controllers;

import com.ldm.configs.LoggerConfig;
import com.ldm.dto.CourseSessionStatsDTO;
import com.ldm.dto.GradeStatsDTO;
import com.ldm.pojo.CourseSession;
import com.ldm.services.CourseService;
import com.ldm.services.StatsService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

    @GetMapping("/courseSessionStats")
    public String courseSessionStats(Model model,
            @RequestParam(name = "courseId", required = false) Integer courseId,
            @RequestParam(name = "year", required = false) Integer year) {
        List<Object[]> courseNames = courseService.getAllCourseNames();

        int startYear = 2020;
        int currentYear = Year.now().getValue();

        List<Integer> availableYears = IntStream.rangeClosed(startYear, currentYear)
                .boxed()
                .collect(Collectors.toList());

        model.addAttribute("availableYears", availableYears);
        model.addAttribute("courseNames", courseNames);
        model.addAttribute("selectedCourseId", courseId);
        model.addAttribute("selectedYear", year);

        if (courseId != null) {
            String selectedCourseName = courseNames.stream()
                    .filter(c -> courseId.equals(c[0]))
                    .map(c -> (String) c[1])
                    .findFirst()
                    .orElse(null);

            model.addAttribute("selectedCourseName", selectedCourseName);
            List<CourseSessionStatsDTO> stats = statsService.countEnrollmentsByCourse(courseId, year);
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

    @GetMapping("/gradeStats")
    public String gradeStats(Model model,
            @RequestParam(name = "courseId", required = false) Integer courseId,
            @RequestParam(name = "year", required = false) Integer year) {
        List<Object[]> courseNames = courseService.getAllCourseNames();

        int startYear = 2020;
        int currentYear = Year.now().getValue();

        List<Integer> availableYears = IntStream.rangeClosed(startYear, currentYear)
                .boxed()
                .collect(Collectors.toList());

        model.addAttribute("availableYears", availableYears);
        model.addAttribute("courseNames", courseNames);
        model.addAttribute("selectedCourseId", courseId);
        model.addAttribute("selectedYear", year);

        if (courseId != null) {
            String selectedCourseName = courseNames.stream()
                    .filter(c -> courseId.equals(c[0]))
                    .map(c -> (String) c[1])
                    .findFirst()
                    .orElse(null);

            model.addAttribute("selectedCourseName", selectedCourseName);
            List<GradeStatsDTO> stats = statsService.studentPerformanceStats(courseId, year);
            model.addAttribute("stats", stats);

            // đếm tổng
            int totalExcellent = 0;
            int totalGood = 0;
            int totalAverage = 0;
            int totalWeak = 0;

            for (GradeStatsDTO stat : stats) {
                totalExcellent += stat.getExcellentCount();
                totalGood += stat.getGoodCount();
                totalAverage += stat.getAverageCount();
                totalWeak += stat.getWeakCount();
            }

            model.addAttribute("totalExcellent", totalExcellent);
            model.addAttribute("totalGood", totalGood);
            model.addAttribute("totalAverage", totalAverage);
            model.addAttribute("totalWeak", totalWeak);

//            List<String> labels = new ArrayList<>();
//            List<Long> data = new ArrayList<>();
//
//            for (CourseSessionStatsDTO s : stats) {
//                labels.add(s.getCourseSessionCode());
//                data.add(s.getEnrollmentCount());
//            }
//
//            model.addAttribute("labels", labels);
//            model.addAttribute("data", data);
        }

        return "gradeStats";
    }

    @GetMapping("/courseSessionStats/export")
    public void exportToExcelInCourseSessionStats(HttpServletResponse response, @RequestParam(name = "courseId") Integer courseId,
            @RequestParam(name = "courseName") String courseName,
            @RequestParam(name = "year", required = false) Integer year) throws IOException {
        List<CourseSessionStatsDTO> stats = statsService.countEnrollmentsByCourse(courseId, year);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Báo Cáo Đăng Ký");

        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);

        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        if (year != null) {
            titleCell.setCellValue(String.format("Báo cáo đăng ký môn %s năm %d", courseName, year));
        } else {
            titleCell.setCellValue(String.format("Báo cáo đăng ký môn %s", courseName));
        }
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

        int courseSessionCount = rowNum - 3;

        rowNum += 1;

        XSSFDrawing drawing = sheet.createDrawingPatriarch();

        rowNum += 1;
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, rowNum, 6, rowNum + 10);

        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText("Thống kê đăng ký môn học");
        chart.setTitleOverlay(false);

        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.RIGHT);

        XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
                new CellRangeAddress(2, courseSessionCount + 2, 2, 2));
        
        XDDFDataSource<String> labels = XDDFDataSourcesFactory.fromStringCellRange(sheet,
                new CellRangeAddress(2, courseSessionCount+2, 0, 0));

        // Pie chart
        XDDFChartData data = chart.createData(ChartTypes.PIE, null, null);
        XDDFChartData.Series series = data.addSeries(labels, values);
        series.setTitle("Thống kê đăng ký", null);

        // Vẽ 
        chart.plot(data);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=bao_cao_dang_ky.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @GetMapping("/gradeStats/export")
    public void exportToExcelInGradeStats(HttpServletResponse response, @RequestParam(name = "courseId") Integer courseId,
            @RequestParam(name = "courseName") String courseName,
            @RequestParam(name = "year", required = false) Integer year) throws IOException {
        List<GradeStatsDTO> stats = statsService.studentPerformanceStats(courseId, year);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Báo Cáo Điểm");

        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);

        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        if (year != null) {
            titleCell.setCellValue(String.format("Báo cáo điểm môn %s năm %d", courseName, year));
        } else {
            titleCell.setCellValue(String.format("Báo cáo điểm môn %s", courseName));
        }
        titleCell.setCellStyle(titleStyle);

        // Merge
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6)); // từ cột 0 đến 2

        String[] columns = {"Mã buổi học", "Giảng viên phụ trách", "Trạng thái điểm", "Giỏi", "Khá", "Trung bình", "Yếu"};
        Row headerRow = sheet.createRow(1);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        int rowNum = 2;
        for (GradeStatsDTO stat : stats) {
            boolean isLocked = stat.getGradeStatus().equals(CourseSession.LOCKED);
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(stat.getCourseSessionCode());
            row.createCell(1).setCellValue(stat.getTeacherName());
            row.createCell(2).setCellValue(isLocked ? "Khóa điểm" : "Chưa khóa điểm");
            row.createCell(3).setCellValue(isLocked ? String.valueOf(stat.getExcellentCount()) : "--");
            row.createCell(4).setCellValue(isLocked ? String.valueOf(stat.getGoodCount()) : "--");
            row.createCell(5).setCellValue(isLocked ? String.valueOf(stat.getAverageCount()) : "--");
            row.createCell(6).setCellValue(isLocked ? String.valueOf(stat.getWeakCount()) : "--");
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        int totalExcellent = 0;
        int totalGood = 0;
        int totalAverage = 0;
        int totalWeak = 0;

        for (GradeStatsDTO stat : stats) {
            totalExcellent += stat.getExcellentCount();
            totalGood += stat.getGoodCount();
            totalAverage += stat.getAverageCount();
            totalWeak += stat.getWeakCount();
        }
        String[] labels = {"Giỏi", "Khá", "Trung Bình", "Yếu"};
        int[] values = {totalExcellent, totalGood, totalAverage, totalWeak};

        rowNum += 1;

        Row header = sheet.createRow(rowNum++);
        header.createCell(0).setCellValue("Xếp loại");
        header.createCell(1).setCellValue("Số lượng");
        
        int rowNumToRead=rowNum;

        for (int i = 0; i < labels.length; i++) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(labels[i]);
            row.createCell(1).setCellValue(values[i]);
        }

        XSSFDrawing drawing = sheet.createDrawingPatriarch();

        rowNum += 1;
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, rowNum, 6, rowNum + 10);

        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText("Thống kê điểm sinh viên");
        chart.setTitleOverlay(false);

        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.RIGHT);

        XDDFDataSource<String> categories = XDDFDataSourcesFactory.fromStringCellRange(sheet,
                new CellRangeAddress(rowNumToRead, rowNumToRead+3, 0, 0)); // 4 dòng dữ liệu, cột A
        XDDFNumericalDataSource<Double> valuesData = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
                new CellRangeAddress(rowNumToRead, rowNumToRead+3, 1, 1)); // 4 dòng dữ liệu, cột B

        // Pie chart
        XDDFChartData data = chart.createData(ChartTypes.PIE, null, null);
        XDDFChartData.Series series = data.addSeries(categories, valuesData);
        series.setTitle("Số lượng sinh viên", null);

        // Vẽ 
        chart.plot(data);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=bao_cao_diem.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
