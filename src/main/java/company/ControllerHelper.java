package company;

import company.entity.Data;
import company.entity.ListOfError;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Scope(value = "singleton")
public class ControllerHelper {
    private static final Logger logger = LoggerFactory.getLogger(ControllerHelper.class);
    @Autowired
    DBFHelper helper;
    @Resource
    Data data;
    @Resource(name = "listOfError")
    ListOfError errors;
    @Resource
    ErrorChecker errorChecker;
    @Resource
    private ExcelSaver saver;

    public ControllerHelper() {
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

    private void processDir(File dir) throws IOException, ZipException {
        if (dir != null) {
            logger.debug("Выбран каталог {}", dir.getName());
            List<File> files = Files.list(dir.toPath())
                    .map(Path::toFile)
                    .filter(c -> (c.getName().endsWith("zip") || c.getName().endsWith("ZIP"))
                            && (c.getName().startsWith("1207") || c.getName().startsWith("1507") || c.getName().startsWith("4307") || c.getName().startsWith("1807") || c.getName().startsWith("4407") || c.getName().startsWith("9007")))
                    .collect(Collectors.toList());
            files.forEach(e-> logger.info("Найден файл {}", e.getName()));
            for (File f : files) {
                processFile(f);
            }
        }
    }

    public void process(File file) throws IOException, ZipException {
        if (Files.isDirectory(file.toPath())){
            processDir(file);
        }else {
            processFile(file);
        }
    }

    private void processFile(File file) throws IOException, ZipException {
        StringBuffer pFile = new StringBuffer();
        StringBuffer uFile = new StringBuffer();

        unpackZip(file, pFile, uFile);

        helper.readFromP(pFile.toString());
        helper.readFromU(uFile.toString());

        errors.clear();
        checkForErrors(file);

        String pathToFile = file.getParentFile().getAbsolutePath();
        String fileName = file.getName().replace(".zip", "");

        try {
            saver.saveErrorsToExcel(errors, pathToFile + File.separator + fileName + "_ошибки.xls");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        logger.info(fileName + " проверка завершена");
    }

    private void checkForErrors(File file) {
        errorChecker.setSmo(getSMO(file));
//        errorChecker.checkForIncorrectDatN(data.getVisits());
//        errorChecker.checkForIncorrectDatO(data.getVisits());
//        errorChecker.checkForIncorrectDocument(data.getHumans());
//        errorChecker.checkForIncorrectIshob(data.getVisits());
//        errorChecker.checkForInсorrectMKB(data.getServices());
//        errorChecker.checkForIncorrectOkato(data.getVisits());
//        errorChecker.checkForIncorrectPolisNumber(data.getVisits());
//        errorChecker.checkForIncorrectPolisType(data.getVisits());
//        errorChecker.checkForIncorrectVMP(data.getServices());
//        errorChecker.checkForMoreThanOneVisit(data.getHumans());
//        errorChecker.checkForMissedService(data.getHumans());
//        errorChecker.checkForCorrectOkatoForStrangers(data.getVisits());
//        errorChecker.checkForRedundantService(data.getHumans());
//        errorChecker.checkReduandOGRN(data.getVisits());
//        errorChecker.checkForIncorrectUslugaSpecialnost(data.getServices());
//        errorChecker.checkForIncorrectMKBAtDispan(data.getServices());
//        errorChecker.checkForIncorrectDateForUslugaDispan(data.getVisits());
        errorChecker.checkForIncorrectSpecForChildrenStac(data.getServices());
    }

    private String getSMO(File file) {
        return file.getName().substring(0, 4);
    }

    public void setSaver(ExcelSaver saver) {
        this.saver = saver;
    }
}
