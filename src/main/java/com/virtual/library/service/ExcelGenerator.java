package com.virtual.library.service;

import com.virtual.library.model.Book;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ExcelGenerator {
    private static final Logger log = LoggerFactory.getLogger(ExcelGenerator.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ExcelGenerator() {
        workbook = new XSSFWorkbook();
    }

    private void writeHeader() {
        sheet = workbook.createSheet("Book");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(11);
        style.setFont(font);
        createCell(row, 0, "ID", style);
        createCell(row, 1, "UUID", style);
        createCell(row, 2, "Title", style);
        createCell(row, 3, "Data publication", style);
        createCell(row, 4, "Author ID", style);
    }

    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof UUID) {
            cell.setCellValue(String.valueOf(valueOfCell));
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else if (valueOfCell instanceof LocalDate) {
            LocalDate datePub = (LocalDate) valueOfCell;
            Instant instant = Instant.from(datePub.atStartOfDay(ZoneId.of("GMT")));
            Date date = Date.from(instant);
            cell.setCellValue(date);
        }
        cell.setCellStyle(style);
    }

    private void write(List<Book> bookList) {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(11);
        style.setFont(font);
        CreationHelper createHelper = workbook.getCreationHelper();
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(createHelper
                .createDataFormat()
                .getFormat("dd/MM/yyyy"));
        for (Book record : bookList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, record.getId(), style);
            createCell(row, columnCount++, record.getUuid(), style);
            createCell(row, columnCount++, record.getTitle(), style);
            createCell(row, columnCount++, record.getDataPublication(), cellStyle);
            createCell(row, columnCount++, record.getAuthor().getId(), style);
        }
    }

    public void generateExcelFile(HttpServletResponse response, List<Book> bookList) throws IOException {
        writeHeader();
        write(bookList);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}