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
import java.util.NoSuchElementException;
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
    @Resource
    private List<Spr69Value> spr69List;

    public ControllerHelper() {
    }

    private void unpackZip(File zipFileName, StringBuffer pFile, StringBuffer uFile, StringBuffer dFile) throws IOException, ZipException {
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
            if (header.getFileName().startsWith("D")) {
                dFile.append(outdir).append(File.separator).append(header.getFileName());
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
            files.forEach(e -> logger.info("Найден файл {}", e.getName()));
            if (files.isEmpty()) {
                logger.error("Ни один файл не найден");
            }
            for (File f : files) {
                processFile(f);
            }
        }
    }

    public void process(File file) throws IOException, ZipException {
        if (Files.isDirectory(file.toPath())) {
            processDir(file);
        } else {
            processFile(file);
        }
    }

    private void processFile(File file) throws IOException, ZipException {
        StringBuffer pFile = new StringBuffer();
        StringBuffer uFile = new StringBuffer();
        StringBuffer dFile = new StringBuffer();

        unpackZip(file, pFile, uFile, dFile);

        helper.readFromP(pFile.toString());
        helper.readFromU(uFile.toString());
        helper.readFromD(dFile.toString());

        helper.readFromSpr69();

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

    private void check() {
        try {
            String codeMo = data.getVisits().stream().findFirst().orElseThrow(NoSuchElementException::new).getCodeMo();
//            errorChecker.checkForIncorrectDoctorSnils(data.getDoctors());
//            errorChecker.checkForIncorrectDoctorDant(data.getDoctors());
            errorChecker.checkForIncorrectSpr69(data.getVisits());
            if (codeMo.equals("06008")) {
//                errorChecker.checkForIncorrectSpecForChildrenStac(data.getServices());
//                errorChecker.checkForIncorrectSpecAndProfilStac(data.getServices());
            } else if (codeMo.equals("06006") || codeMo.equals("06005") || codeMo.equals("06007")) {
                errorChecker.checkForMoreThanOneVisit(data.getHumans());
                errorChecker.checkForMissedService(data.getHumans());
                errorChecker.checkForRedundantService(data.getHumans());
                errorChecker.checkForIncorrectAgeForThisMKBWhenAgeIsIncorrect(data.getServices());
            } else if (codeMo.equals("06020") || codeMo.equals("06021")) {
                errorChecker.checkForIncorrectMKBAtDispan(data.getServices());
                errorChecker.checkForIncorrectDateForUslugaDispan(data.getVisits());
            }
        } catch (NoSuchElementException e) {
            System.err.println("Ошибка, не удалось определить код медицинской организации");
        }
    }

    private void checkForErrors(File file) {
        errorChecker.setSmo(getSMO(file));
//     errorChecker.checkForError903(data.getVisits());
        check();
    }

    private String getSMO(File file) {
        return file.getName().substring(0, 4);
    }

    public void setSaver(ExcelSaver saver) {
        this.saver = saver;
    }
}
