package company;

import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.cli.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class Checker {

    public static void main(String[] args) throws ParseException, IOException, ZipException {

        Options options = new Options();
        Option filename = new Option("p", "path", true, "Путь к файлу или директории");
        filename.setArgs(1);
        filename.setArgName("path");
        options.addOption(filename);
        CommandLineParser commandLineParser = new PosixParser();
        CommandLine commandLine = commandLineParser.parse(options, args);
        if (commandLine.hasOption("p")) {
            String[] arguments = commandLine.getOptionValues("p");
            File file = new File(arguments[0]);
            if (Files.exists(file.toPath())) {
                ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
                context.getBean(ControllerHelper.class).process(file);
            }
        } else {
            printHelp(options, 80, "Options", "", 3, 5, true, System.out);
        }
    }


    public static void printHelp(
            final Options options,
            final int printedRowWidth,
            final String header,
            final String footer,
            final int spacesBeforeOption,
            final int spacesBeforeOptionDescription,
            final boolean displayUsage,
            final OutputStream out) {
        final String commandLineSyntax = "java -jar CheckServices.jar";//подсказка по запуску самой программы
        final PrintWriter writer = new PrintWriter(out);// куда печатаем help
        final HelpFormatter helpFormatter = new HelpFormatter();// создаем объект для вывода help`а
        helpFormatter.printHelp(
                writer,
                printedRowWidth,
                commandLineSyntax,
                header,
                options,
                spacesBeforeOption,
                spacesBeforeOptionDescription,
                footer,
                displayUsage);//формирование справки
        writer.flush(); // вывод
    }
}
