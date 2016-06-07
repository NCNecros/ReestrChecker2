package company;

import company.entity.Error;
import company.entity.ListOfError;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Файл занимается сохранением в Excel
 */
@Service
public class ExcelSaver {

    public void saveErrorsToExcel(ListOfError errors, String filename) throws IOException {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Ошибки");
        sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);

        int counter = 0;
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Номер карты");
        row.createCell(1).setCellValue("ФИО");
        row.createCell(2).setCellValue("Дата рождения");
        row.createCell(3).setCellValue("Дата обращения");
        row.createCell(4).setCellValue("Ошибка");
        for (Error error : errors.stream()
                .distinct()
//                .sorted((error1, error2) -> error1.getHuman().compareTo(error2.getHuman()))
                .sorted((error1, error2) -> getSnFromString(error1.getError()).compareTo(getSnFromString(error2.getError())))
                .collect(Collectors.toList())) {
            counter++;
            row = sheet.createRow(counter);
            try {
                row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue(error.getHuman().getIsti());
            } catch (NullPointerException e) {
                row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("");
            }
            try {
                row.createCell(1).setCellValue(error.getHuman().getFullName());
            } catch (NullPointerException e) {
                row.createCell(1).setCellValue("");
            }
            try {
                row.createCell(2).setCellValue(error.getHuman().getReadableDatr());
            } catch (NullPointerException e) {
                row.createCell(2).setCellValue("");
            }
            try {
                row.createCell(3).setCellValue(error.getVisit().getReadableDatN());
            } catch (NullPointerException e) {
                row.createCell(3).setCellValue("");
            }
            try {
                row.createCell(4).setCellValue(error.getError());
            } catch (NullPointerException e) {
                row.createCell(4).setCellValue("");
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            wb.write(fos);
            fos.close();
        } catch (IOException e) {
            throw new IOException("Ошибка записи файла с ошибками: " + e.getLocalizedMessage());
        }
    }

    private Integer getSnFromString(String string) {
        Integer result = Integer.parseInt(string.split("[()]")[1]);
        return result;

    }

}
