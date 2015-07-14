package company;

import company.entity.Error;
import company.entity.*;
import org.apache.commons.collections4.CollectionUtils;
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
    @Resource(name = "uslugi307")
    private Uslugi307List uslugi307List;

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


    /**
     * Проверка на некорректно заполненное поле ВМП
     */
    public void checkForIncorrectVMP() {
        mapNewService.values().stream().filter(e -> e.getSpec().equals("1134") && !e.getVmp().equals("12")).forEach(e -> errors.add(new company.entity.Error(e, "Некорректный вид МП")));
    }

    /**
     * Проверка на ошибку 307, когда человек пришел несколько раз к одному доктору
     * а его оформили в разные талоны
     */
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

    /**
     * Проверка на наличие услуг совпадающих с датой окончания лечения
     */
    public void checkForIncorrectDatO() {
        List<NewVisit> visits = new ArrayList<>(mapNewVisit.values());
        for (NewVisit visit : visits) {
            List<Date> datos = visit.getServices().stream().map(NewService::getDato).collect(Collectors.toList());
            if (!datos.contains(visit.getDato())) {
                errors.add(new Error(visit, " нет услуги совпадающей с датой окончания"));
            }

        }
    }

    /**
     * Проверка на наличие услуг совпадающих с датой начала лечения
     */
    public void checkForIncorrectDatN() {
        List<NewVisit> visits = new ArrayList<>(mapNewVisit.values());
        for (NewVisit visit : visits) {
            List<Date> datns = visit.getServices().stream().map(NewService::getDatn).collect(Collectors.toList());
            if (!datns.contains(visit.getDatn())) {
                errors.add(new Error(visit, "нет услуги совпадающей с датой начала"));
            }
        }
    }

    /**
     * Проверка на отсутствие в талоне необходимых услуг
     */
    public void checkForMissedService() {

        for (Uslugi307 uslugi307 : uslugi307List.getUslugi()) {
            checkMissedUslugi(uslugi307.getUslugi(), uslugi307.getObrashenie(), uslugi307.getDoctor());
        }
    }

    /**
     * Проверка на наличие краевых пациентов
     */
    public void checkForCorrectOkatoForStrangers() {
        List<NewVisit> visits = mapNewVisit.values().stream().filter(newVisit -> newVisit.getOkatoOms().startsWith("03") && newVisit.getPlOgrn().equals("1022301607393")).collect(Collectors.toList());
        visits.forEach(newVisit -> errors.add(new Error(newVisit, "краевой в счете для инокраевых")));
    }

    /**
     * Проверка на наличие территории у инокраевых
     */
    public void checkForIncorrectOkato() {
        mapNewVisit.values().stream().filter(newVisit -> newVisit.getOkatoOms().isEmpty() && newVisit.getPlOgrn().equals("1022301607393")).collect(Collectors.toList())
                .stream().forEach(newVisit1 -> errors.add(new Error(newVisit1, "не указана территория для инокраевого")));
    }

    /**
     * Проверка на правильность номера полиса
     */
    public void checkForIncorrectPolisNumber() {
        mapNewVisit.values().stream().filter(newVisit -> (newVisit.getSpv() == 3 && newVisit.getSpn().length() != 16) || (newVisit.getSpv() == 2 && newVisit.getSpn().length() != 9)).collect(Collectors.toList()).stream().forEach(e -> errors.add(new Error(e, "некорректно заполнен номер полиса")));
    }

    /**
     * Проверка на правильность типа полиса
     */
    public void checkForIncorrectPolisType() {
        mapNewVisit.values().stream().filter(newVisit -> !(newVisit.getSpv() == 3 || newVisit.getSpv() == 2 || newVisit.getSpv() == 1 || newVisit.getSpv() == 4)).collect(Collectors.toList()).stream().forEach(e -> errors.add(new Error(e, "не указан тип полиса")));
    }


    private void checkMissedUslugi(List<String> uslugi, String obrashenie, String doctor) {
        for (NewHuman human : mapNewHuman.values()) {
            for (NewVisit visit : human.getAllVisits().values()) {
                List<String> services = visit.getServices().stream().map(NewService::getKusl).collect(Collectors.toList());
                if (services.size() > 1 && (CollectionUtils.containsAny(services, uslugi)) && !services.contains(obrashenie)) {
                    errors.add(new Error(visit, "отсутствует обращение к врачу" + doctor));
                }
            }
        }
    }

    /**
     * Проверка на лишние услуги
     */
    public void checkForReduantService() {
        for (Uslugi307 uslugi307 : uslugi307List.getUslugi()) {
            checkForReduantDoctorService(uslugi307.getUslugi(), uslugi307.getObrashenie(), uslugi307.getDoctor());
        }
    }

    private void checkForReduantDoctorService(List<String> uslugi, String obrashenie, String doctor) {
        for (NewHuman human : mapNewHuman.values()) {
            for (NewVisit visit : human.getAllVisits().values()) {
                List<String> services = visit.getServices().stream().map(NewService::getKusl).collect(Collectors.toList());
                if (CollectionUtils.containsAny(services, uslugi) && services.contains(obrashenie) && services.size() == 2) {
                    errors.add(new Error(visit, "содержит лишнее обращение"));
                }
            }
        }
    }

    public void checkForIncorrectDocument(){
        List<NewHuman> incorrectDocuments  = mapNewHuman.values().stream()
                .filter(
                        newHuman ->
                                (newHuman.getcDoc() != null && newHuman.getcDoc() > 0) && (
                                        newHuman.getsDoc().isEmpty() ||
                                                newHuman.getnDoc().isEmpty())).collect(Collectors.toList());

        incorrectDocuments.stream().forEach(e -> errors.add(new Error(e, "неправильно заполнена серия или номер документа")));
    }


}
