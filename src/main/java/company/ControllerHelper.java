package company;

import company.entity.*;
import company.entity.Error;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope(value = "singleton")
public class ControllerHelper {
    @Autowired
    DBFHelper helper;
    @Resource(name = "mapNewHuman")
    Map<String, NewHuman> mapNewHuman;
    @Resource(name = "mapNewVisit")
    Map<Double, NewVisit> mapNewVisit;
    @Resource(name = "mapNewService")
    Map<Double, NewService> mapNewService;
    @Resource(name = "listOfError")
    ListOfError errors;
    @Resource
    ErrorChecker errorChecker;
    private Controller controller;

    public ControllerHelper() {
    }

    @Autowired
    @Lazy
    public ControllerHelper(Controller controller) {
        this.controller = controller;
    }


    public void processFile(File file) throws IOException, ZipException {

        Map<String, String> filelist = new HashMap<>();

        Path outdir = Files.createTempDirectory("_tmp" + Math.random());
        // TODO: 10.11.2015 Вынести извлечение файлов в отдельный метод
        ZipFile zipFile = new ZipFile(file);
        for (Object obj : zipFile.getFileHeaders()) {
            FileHeader header = (FileHeader) obj;
            if (header.getFileName().startsWith("P")) {
                filelist.put("P", header.getFileName());
                zipFile.extractFile(header, outdir.toString());
            }
            if (header.getFileName().startsWith("U")) {
                filelist.put("U", header.getFileName());
                zipFile.extractFile(header, outdir.toString());
            }
        }


        helper.readFromP(outdir + File.separator + filelist.get("P"));
        helper.readFromU(outdir + File.separator + filelist.get("U"));

        errors.clear();
        errorChecker.setSmo(file.getName().substring(0,4));
        errorChecker.checkForIncorrectDatN(mapNewVisit.values());
        errorChecker.checkForMoreThanOneVisit(new ArrayList<>(mapNewHuman.values()));
        errorChecker.checkForIncorrectVMP(mapNewService.values());
        errorChecker.checkForMissedService(mapNewHuman.values());
        errorChecker.checkForCorrectOkatoForStrangers(mapNewVisit.values());
        errorChecker.checkForIncorrectPolisNumber(mapNewVisit.values());
        errorChecker.checkForIncorrectPolisType(mapNewVisit.values());
        errorChecker.checkForIncorrectDocument(mapNewHuman.values());
        errorChecker.checkInvorrectMKB(mapNewService.values());
        errorChecker.checkReduandOGRN(mapNewVisit.values());
        errorChecker.checkForRedundantService(mapNewHuman.values());
//        errorChecker.checkForIncorrectIshob(mapNewVisit);


        String pathToFile = file.getParentFile().getAbsolutePath();
        String fileName = file.getName().replace(".zip","");
        saveErrorsToExcel(errors, pathToFile + File.separator + fileName + "_ошибки.xls");
        controller.addTextToTextArea(fileName + " проверка завершена");
    }

//    todo возможно сделать одно сохранение для всех реестров
    private void saveErrorsToExcel(ListOfError errors, String filename) {
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
        List<Error> errorList = errors.stream()
                .sorted()
                .distinct()
                .collect(Collectors.toList());
        for (Error error : errors.stream()
                .distinct()
                .sorted((error1, error2) -> error1.getHuman().compareTo(error2.getHuman()))
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
            controller.addTextToTextArea("Ошибка записи файла с ошибками: " + e.getLocalizedMessage());
        }
    }
}
