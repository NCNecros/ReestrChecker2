package company;

import company.entity.Error;
import company.entity.NewHuman;
import company.entity.NewService;
import company.entity.NewVisit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by User on 10.07.2015.
 */
@Service
public class ErrorChecker {
    @Resource(name = "mapNewHuman")
    private Map<String, NewHuman> mapNewHuman;
    @Resource(name = "mapNewService")
    private Map<Double, NewService> mapNewService;
    @Resource(name = "mapNewVisit")
    private Map<Double, NewVisit> mapNewVisit;
    @Resource(name = "errorMap")
    private List<Error> errors;

    public Map<String, NewHuman> getMapNewHuman() {
        return mapNewHuman;
    }

    public void setMapNewHuman(Map<String, NewHuman> mapNewHuman) {
        this.mapNewHuman = mapNewHuman;
    }

    public Map<Double, NewService> getMapNewService() {
        return mapNewService;
    }

    public void setMapNewService(Map<Double, NewService> mapNewService) {
        this.mapNewService = mapNewService;
    }

    public Map<Double, NewVisit> getMapNewVisit() {
        return mapNewVisit;
    }

    public void setMapNewVisit(Map<Double, NewVisit> mapNewVisit) {
        this.mapNewVisit = mapNewVisit;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public void checkForIncorrectVMP() {
        mapNewService.values().stream().filter(e -> e.getSpec().equals("1134") && !e.getVmp().equals("12")).forEach(e -> errors.add(new company.entity.Error(e, "Некорректный вид МП")));
    }

    public void checkForMoreThanOneVisit() {
        for (NewHuman human : mapNewHuman.values()) {
            for (NewVisit visit : human.getAllVisits().values()) {
                List<NewVisit> visitsWithOutCurrent = new ArrayList<>();
                visitsWithOutCurrent.addAll(human.getAllVisits().values());
                visitsWithOutCurrent.remove(visit);
                for (NewVisit otherVisit : visitsWithOutCurrent) {
                    Boolean res = otherVisit.getMKB().equalsIgnoreCase(visit.getMKB());
                    if (res && visit.getServices().stream().anyMatch(e -> !e.getKusl().startsWith("B04"))) {
                        errors.add(new Error(visit, " содержит более одного обращения"));
                    }
                }
            }
        }
    }

    public void checkForIncorrectDatO() {
        List<NewVisit> visits = new ArrayList<>(mapNewVisit.values());
        for (NewVisit visit : visits) {
            List<Date> datos = visit.getServices().stream().map(NewService::getDato).collect(Collectors.toList());
            if (!datos.contains(visit.getDato())) {
                errors.add(new Error(visit, " нет услуги совпадающей с датой окончания"));
            }

        }
    }

    public void checkForIncorrectDatN(){
        List<NewVisit> visits = new ArrayList<>(mapNewVisit.values());
        for (NewVisit visit : visits) {
            List<Date> datns = visit.getServices().stream().map(NewService::getDatn).collect(Collectors.toList());
            if (!datns.contains(visit.getDatn())) {
                errors.add(new Error(visit, "нет услуги совпадающей с датой начала"));
            }
        }
    }

}
