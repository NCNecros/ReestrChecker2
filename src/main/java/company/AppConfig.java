package company;

import company.entity.NewHuman;
import company.entity.NewService;
import company.entity.NewVisit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


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

    @Bean(name = "errorMap")
    @Scope(value = "singleton")
    public List<Error> errorMap(){
        return new ArrayList<>();
    }
}

