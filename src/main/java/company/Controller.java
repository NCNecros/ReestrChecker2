package company;

//import company.entity.Person;
//import company.repos.PersonRepository;
//import company.repos.UslugiRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import net.lingala.zip4j.exception.ZipException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@Scope
public class Controller implements Initializable{
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    private ControllerHelper controllerHelper;
//    @Autowired
//    private PersonRepository personRepository;
//    @Autowired
//    private UslugiRepository uslugiRepository;

    @Autowired
    public Controller(ControllerHelper controllerHelper){
        this.controllerHelper = controllerHelper;
    }
    @FXML
    private TextArea textArea;

    @FXML
    public void selectFile(ActionEvent actionEvent) throws IOException, ZipException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("d:\\Temp\\check"));
        fileChooser.setTitle("Выберите файл счета");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            logger.debug("Файл выбран"+this.toString());
            controllerHelper.processFile(file);
        }
    }

    public Controller() {
    }

    @FXML
    public void selectDir(ActionEvent actionEvent) throws IOException, ZipException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Выберите каталог с файлами");
        directoryChooser.setInitialDirectory(new File("d:\\Temp\\check"));
        File file = directoryChooser.showDialog(null);
        if (file != null) {
            logger.debug("Выбран каталог {}", file.getName());
            List<File> files = Files.list(file.toPath())
                    .map(Path::toFile)
                    .filter(c -> (c.getName().endsWith("zip") || c.getName().endsWith("ZIP"))
                            && (c.getName().startsWith("1207") || c.getName().startsWith("1507") || c.getName().startsWith("4307") || c.getName().startsWith("1807") || c.getName().startsWith("4407") || c.getName().startsWith("9007")))
                    .collect(Collectors.toList());
            files.forEach(e-> logger.debug("Найден файл {}", e.getName()));
            for (File f : files) {
                controllerHelper.processFile(f);
            }
        }
    }

    @PostConstruct
    public void init() {
        logger.debug(this.toString());
    }

    public void addTextToTextArea(String text){
        textArea.appendText(text+"\n");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
