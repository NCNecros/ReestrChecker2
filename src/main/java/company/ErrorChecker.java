package company;

import company.entity.Error;
import company.entity.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс проверяющий ошибки.
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
    @Resource(name = "mkbSet")
    private Set<String> mkbSet;
    private String smo;

    public ErrorChecker() {
    }

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
        mapNewService.values().stream().filter(e -> (e.getSpec().equals("1134") || e.getSpec().equals("1122")) && !e.getVmp().equals("12")).forEach(e -> errors.add(new company.entity.Error(e, "Некорректный вид МП")));
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
                    // Проверка с разными врачами и одним диагнозом
                    // Boolean res = otherVisit.getMKB().equalsIgnoreCase(visit.getMKB());
                    Boolean res = otherVisit.getMKB().equalsIgnoreCase(visit.getMKB()) && otherVisit.getDoctor().equalsIgnoreCase(visit.getDoctor());
                    if (res && visit.getServices().stream().anyMatch(e -> !e.getKusl().startsWith("B04"))) {
                        errors.add(new Error(visit, "содержит более одного обращения"));
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
                errors.add(new Error(visit, "нет услуги совпадающей с датой окончания"));
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
     * Проверка на правильность заполнения результата обращения у профилактических приемов
     */
    public void checkForIncorrectIshob() {
        List<NewVisit> visits = new ArrayList<>(mapNewVisit.values().stream().filter(newVisit -> newVisit.getServices().stream().map(NewService::getMkbх).filter(s -> s.equalsIgnoreCase("Z01.4")).count() > 0).collect(Collectors.toList()));
        visits.stream().forEach(newVisit -> errors.add(new Error(newVisit, "неправильный исход обращения")));
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
                    errors.add(new Error(visit, "отсутствует обращение к врач-" + doctor));
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

    public void checkForIncorrectDocument() {
        List<NewHuman> incorrectDocuments = mapNewHuman.values().stream()
                .filter(
                        newHuman ->
                                (newHuman.getcDoc() != null && newHuman.getcDoc() > 0) && (
                                        newHuman.getsDoc().isEmpty() ||
                                                newHuman.getnDoc().isEmpty())).collect(Collectors.toList());

        incorrectDocuments.stream().forEach(e -> errors.add(new Error(e, "неправильно заполнена серия или номер документа")));
    }

    public void checkInvorrectMKB() {
        List<NewService> services = getMapNewService().values().stream().filter(newService -> !mkbSet.contains(newService.getMkbх())).collect(Collectors.toList());
        services.stream().forEach(newService -> errors.add(new Error(newService, "неправильный МКБ")));
    }

    public void checkReduandOGRN() {
        for (NewVisit visit : mapNewVisit.values()) {
            String ogrn = visit.getPlOgrn();
            switch (getSmo()) {
                case "1207":
                    if (ogrn.equalsIgnoreCase("1047100775963")) {
                        break;
                    }
                case "1507":
                    if (ogrn.equalsIgnoreCase("1027739815245")) {
                        break;
                    }
                case "1807":
                    if (ogrn.equalsIgnoreCase("1032304935871")) {
                        break;
                    }
                case "4407":
                    if (ogrn.equalsIgnoreCase("1027806865481")) {
                        break;
                    }
                case "9007":
                    if (ogrn.equalsIgnoreCase("1022301607393")) {
                        break;
                    }
                default:
                    errors.add(new Error(visit,"неправильный ОГРН"));
                    break;
            }
        }
    }


    public String getSmo() {
        return smo;
    }

    public void setSmo(String smo) {
        this.smo = smo;
    }
}
