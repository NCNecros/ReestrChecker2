package company.ui;

import com.linuxense.javadbf.DBFException;
import company.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;


public class Main{

    public static void main(String[] args) throws FileNotFoundException, DBFException, UnsupportedEncodingException {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        context.getBean(Stage.class).start();
    }
}
