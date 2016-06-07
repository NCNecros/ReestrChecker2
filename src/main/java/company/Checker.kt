package company

import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import org.apache.commons.cli.PosixParser
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import java.io.File
import java.io.OutputStream
import java.io.PrintWriter
import java.nio.file.Files

object Checker {

    @JvmStatic fun main(args: Array<String>) {

        val options = Options()
        val filename = Option("p", "path", true, "Путь к файлу или директории")
        filename.args = 1
        filename.argName = "path"
        options.addOption(filename)
        val commandLineParser = PosixParser()
        val commandLine = commandLineParser.parse(options, args)
        if (commandLine.hasOption("p")) {
            val arguments = commandLine.getOptionValues("p")
            val file = File(arguments[0])
            if (Files.exists(file.toPath())) {
                val context = AnnotationConfigApplicationContext(AppConfig::class.java)
                context.getBean(ControllerHelper::class.java).process(file)
            }
        } else {
            printHelp(options, 80, "Options", "", 3, 5, true, System.out)
        }
    }


    fun printHelp(
            options: Options,
            printedRowWidth: Int,
            header: String,
            footer: String,
            spacesBeforeOption: Int,
            spacesBeforeOptionDescription: Int,
            displayUsage: Boolean,
            out: OutputStream) {
        val commandLineSyntax = "java -jar CheckServices.jar"//подсказка по запуску самой программы
        val writer = PrintWriter(out)// куда печатаем help
        val helpFormatter = HelpFormatter()// создаем объект для вывода help`а
        helpFormatter.printHelp(
                writer,
                printedRowWidth,
                commandLineSyntax,
                header,
                options,
                spacesBeforeOption,
                spacesBeforeOptionDescription,
                footer,
                displayUsage)//формирование справки
        writer.flush() // вывод
    }
}
