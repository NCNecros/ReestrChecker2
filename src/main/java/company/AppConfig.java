package company;

import company.entity.NewHuman;
import company.entity.NewService;
import company.entity.NewVisit;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by Necros on 31.03.2015.
 */
@Configuration
@ComponentScan(basePackages = "company")
public class AppConfig {
//todo переделать в отдельный класс с методами и сортировками
    @Bean(name = "mapNewHuman")
    @Scope(value = "singleton")
    public Map<String,NewHuman> mapNewHuman(){
        Map<String,NewHuman> mapNewHuman = new LinkedHashMap<>();
        return mapNewHuman;
    }
    //todo переделать в отдельный класс с методами и сортировками
    @Bean(name = "mapNewVisit")
    @Scope(value = "singleton")
    public Map<Double,NewVisit> mapNewVisit(){
        Map<Double,NewVisit> mapNewVisit = new LinkedHashMap<>();
        return mapNewVisit;
    }
    //todo переделать в отдельный класс с методами и сортировками
    @Bean(name = "mapNewService")
    @Scope(value = "singleton")
    public Map<String,NewService> mapNewService(){
        Map<String,NewService> mapNewService = new LinkedHashMap<>();
        return mapNewService;
    }

//    @Bean(name = "errorMap")
//    @Scope(value = "singleton")
//    public List<Error> errorMap(){
//        return new ArrayList<>();
//    }

    @Bean(name = "uslugi307")
    public Uslugi307List uslugi307() throws Exception {
        Reader reader = new FileReader(getClass().getClassLoader().getResource("Uslugi307.xml").getFile());
        Serializer serializer = new Persister();
        return serializer.read(Uslugi307List.class, reader);
    }
    @Bean(name = "mkbSet")
    public Set<String> mkbSet(){
        Set<String> mkbSet = null;
        try {
            mkbSet = new HashSet<>(Files.readAllLines(new File(getClass().getClassLoader().getResource("mkb").getFile()).toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mkbSet;
    }
}

