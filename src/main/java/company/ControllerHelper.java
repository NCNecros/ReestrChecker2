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
    @Resource
    Data data;
    @Resource(name = "listOfError")
    ListOfError errors;
    @Resource
    ErrorChecker errorChecker;
    private Controller controller;
    @Resource
    private ExcelSaver saver;

    public ControllerHelper() {
    }

    @Autowired
    @Lazy
    public ControllerHelper(Controller controller) {
        this.controller = controller;
    }

    private void unpackZip(File zipFileName, StringBuffer pFile, StringBuffer uFile) throws IOException, ZipException {
        Path outdir = Files.createTempDirectory("_tmp" + Math.random());
        ZipFile zipFile = new ZipFile(zipFileName);
        for (Object obj : zipFile.getFileHeaders()) {
            FileHeader header = (FileHeader) obj;
            if (header.getFileName().startsWith("P")) {
                pFile.append(outdir).append(File.separator).append(header.getFileName());
                zipFile.extractFile(header, outdir.toString());
            }
            if (header.getFileName().startsWith("U")) {
                uFile.append(outdir).append(File.separator).append(header.getFileName());
                zipFile.extractFile(header, outdir.toString());
            }
        }

    }

    public void processFile(File file) throws IOException, ZipException {

        StringBuffer pFile = new StringBuffer();
        StringBuffer uFile = new StringBuffer();

        unpackZip(file, pFile, uFile);

        helper.readFromP(pFile.toString());
        helper.readFromU(uFile.toString());

        // TODO: 10.11.2015 Придумать что-то здесь с проверкой. Она должна быть где-то в другом месте
        errors.clear();
        checkForErrors(file);

        String pathToFile = file.getParentFile().getAbsolutePath();
        String fileName = file.getName().replace(".zip", "");

        try {
            saver.saveErrorsToExcel(errors, pathToFile + File.separator + fileName + "_ошибки.xls");
        } catch (IOException e) {
            controller.addTextToTextArea(e.getMessage());
        }

        controller.addTextToTextArea(fileName + " проверка завершена");
    }

    private void checkForErrors(File file) {
        errorChecker.setSmo(getSMO(file));
        errorChecker.checkForIncorrectDatN(data.getVisits());
        errorChecker.checkForMoreThanOneVisit(data.getHumans());
        errorChecker.checkForIncorrectVMP(data.getServices());
        errorChecker.checkForMissedService(data.getHumans());
        errorChecker.checkForCorrectOkatoForStrangers(data.getVisits());
        errorChecker.checkForIncorrectPolisNumber(data.getVisits());
        errorChecker.checkForIncorrectPolisType(data.getVisits());
        errorChecker.checkForIncorrectDocument(data.getHumans());
        errorChecker.checkInvorrectMKB(data.getServices());
        errorChecker.checkReduandOGRN(data.getVisits());
        errorChecker.checkForRedundantService(data.getHumans());
        errorChecker.checkForIncorrectIshob(data.getVisits());
    }

    private String getSMO(File file) {
        return file.getName().substring(0, 4);
    }

    public void setSaver(ExcelSaver saver) {
        this.saver = saver;
    }
}
