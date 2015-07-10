package company;

import company.entity.NewHuman;
import company.entity.NewService;
import company.entity.NewVisit;
import company.entity.Error;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.File;
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
    @Resource(name = "errorMap")
    List<Error> errors;
    private Controller controller;

    public ControllerHelper() {
    }

    @Autowired
    @Lazy
    public ControllerHelper(Controller controller) {
        this.controller = controller;
    }

    @Transactional
    public void processFile(File file) throws IOException, ZipException {

        Path outdir = Files.createTempDirectory("_tmp" + Math.random());
        ZipFile zipFile = new ZipFile(file);
        Map<String, String> filelist = new HashMap<>();
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

        //Загрузка данных
        helper.readFromP(outdir + File.separator + filelist.get("P")/*, humanMap*/);
        helper.readFromU(outdir + File.separator + filelist.get("U")/*, humanMap*/);


        List<NewVisit> visits = new ArrayList<>(mapNewVisit.values());
        for (NewVisit visit: visits){
            List<Date> dates = visit.getServices().stream().map(NewService::getDatn).collect(Collectors.toList());
            if (!dates.contains(visit.getDatn())){
                errors.add(new Error(visit," нет услуги совпадающей с датой начала"));
            }
            if (!dates.contains(visit.getDato())){
                errors.add(new Error(visit," нет услуги совпадающей с датой окончания"));
            }

        }

        for (NewHuman human : mapNewHuman.values()) {
            for (NewVisit visit : human.getAllVisits().values()) {
                List<NewVisit> visitsWithOutCurrent = new ArrayList<>();
                visitsWithOutCurrent.addAll(human.getAllVisits().values());
                visitsWithOutCurrent.remove(visit);
                for (NewVisit otherVisit : visitsWithOutCurrent) {
                    Boolean res = otherVisit.getMKB().equalsIgnoreCase(visit.getMKB());
                    if (res && visit.getServices().stream().anyMatch(e -> !e.getKusl().startsWith("B04"))) {
                        errors.add(new Error(visit," содержит более одного обращения"));
                    }
                }
            }
        }

        mapNewService.values().stream().filter(e->e.getSpec().equals("1134") && !e.getVmp().equals("12")).forEach(e-> errors.add(new Error(e,"Некорректный вид МП")));

        errors.stream().distinct().forEach(System.out::println);
        System.out.println(errors.stream().distinct().filter(e->e.getError().equals("Некорректный вид МП")).count());
        //Проверяем загруженое
        System.out.println("Готово");


//todo починить проверку на ОГРН
//        String pathToFile = file.getParentFile().getAbsolutePath();
//        String fileName = file.getName();
//        saveErrorsToExcel(errors, pathToFile + File.separator + fileName + "_ошибки.xls");
//        controller.addTextToTextArea(fileName + " проверка завершена");
    }

    //todo починить сохранение. возможно сделать одно сохранение для всех реестров
//    private void saveErrorsToExcel(List<Error> errors, String filename) {
//        Workbook wb = new HSSFWorkbook();
//        Sheet sheet = wb.createSheet("Ошибки");
//        sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
//
//        int counter = 0;
//        Row row = sheet.createRow(0);
//        row.createCell(0).setCellValue("Номер карты");
//        row.createCell(1).setCellValue("ФИО");
//        row.createCell(2).setCellValue("Дата рождения");
//        row.createCell(3).setCellValue("Ошибка");
//        for (Error error : errors.stream()
//                .distinct()
//                .sorted((error1, error2) -> error1.getHuman().compareTo(error2.getHuman()))
//                .collect(Collectors.toList())) {
//            counter++;
//            row = sheet.createRow(counter);
//            try {
//                row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue(error.getHuman().getIsti());
//            } catch (NullPointerException e) {
//                row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("");
//            }
//            try {
//                row.createCell(1).setCellValue(error.getHuman().getFullName());
//            } catch (NullPointerException e) {
//                row.createCell(1).setCellValue("");
//            }
//            try {
//                row.createCell(2).setCellValue(error.getHuman().getReadableDatr());
//            } catch (NullPointerException e) {
//                row.createCell(2).setCellValue("");
//            }
//            try {
//                row.createCell(3).setCellValue("(" + error.getTreatment().getReadableDatN() + ") " + error.getError());
//            } catch (NullPointerException e) {
//                if (error.getError() != null) {
//                    row.createCell(3).setCellValue(error.getError());
//                } else {
//                    row.createCell(3).setCellValue("");
//                }
//            }
//        }
//        try {
//            FileOutputStream fos = new FileOutputStream(filename);
//            sheet.autoSizeColumn(0);
//            sheet.autoSizeColumn(1);
//            sheet.autoSizeColumn(2);
//            sheet.autoSizeColumn(3);
//            wb.write(fos);
//            fos.close();
//        } catch (IOException e) {
//            controller.addTextToTextArea("Ошибка записи файла с ошибками: " + e.getLocalizedMessage());
//        }
//    }
}
