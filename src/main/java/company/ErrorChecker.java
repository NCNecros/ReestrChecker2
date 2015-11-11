package company;

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

    static String TFOMS_OGRN = "1022301607393";
    static String ALPHA_OGRN = "1047100775963";
    static String VTB_OGRN = "1027739815245";
    static String MSK_ORGN = "1032304935871";
    static String RGS_OGRN = "1027806865481";


    @Resource(name = "listOfError")
    private ListOfError errors;
    @Resource(name = "uslugi307")
    private Uslugi307List uslugi307List;
    @Resource(name = "mkbSet")
    private Set<String> mkbSet;
    private String smo;

    public ErrorChecker() {
    }

    public ErrorChecker(ListOfError errors, Uslugi307List uslugi307List, Set<String> mkbSet, String smo) {
        this.errors = errors;
        this.uslugi307List = uslugi307List;
        this.mkbSet = mkbSet;
        this.smo = smo;
    }


    /**
     * Проверка на некорректно заполненное поле ВМП
     */
    public void checkForIncorrectVMP(Collection<NewService> serviceCollection) {
        serviceCollection.stream().filter(e -> (e.getSpec().equals("1134") || e.getSpec().equals("1122")) && !e.getVmp().equals("12")).forEach(e -> errors.addError(e, "Некорректный вид МП"));
    }

    /**
     * Проверка на ошибку 307, когда человек пришел несколько раз к одному доктору
     * а его оформили в разные талоны
     */
    public void checkForMoreThanOneVisit(Collection<NewHuman> newHumanList) {
        for (NewHuman human : newHumanList) {
            for (NewVisit visit : human.getAllVisits().values()) {
                List<NewVisit> visitsWithOutCurrent = new ArrayList<>();
                visitsWithOutCurrent.addAll(human.getAllVisits().values());
                visitsWithOutCurrent.remove(visit);
                for (NewVisit otherVisit : visitsWithOutCurrent) {
                    // Проверка с разными врачами и одним диагнозом
                    // Boolean res = otherVisit.getMKB().equalsIgnoreCase(visit.getMKB());
                    Boolean res = otherVisit.getMKB().equalsIgnoreCase(visit.getMKB()) && otherVisit.getDoctor().equalsIgnoreCase(visit.getDoctor());
                    if (res && visit.getServices().stream().anyMatch(e -> !e.getKusl().startsWith("B04"))) {
                        errors.addError(visit, "содержит более одного обращения");
                    }
                }
            }
        }
    }

    /**
     * Проверка на наличие услуг совпадающих с датой окончания лечения
     *
     * @param visitCollection
     */
    public void checkForIncorrectDatO(Collection<NewVisit> visitCollection) {
        for (NewVisit visit : visitCollection) {
            List<Date> datos = visit.getServices().stream().map(NewService::getDato).collect(Collectors.toList());
            if (!datos.contains(visit.getDato())) {
                errors.addError(visit, "нет услуги совпадающей с датой окончания");
            }
        }
    }

    /**
     * Проверка на наличие услуг совпадающих с датой начала лечения
     *
     * @param visitCollection
     */
    public void checkForIncorrectDatN(Collection<NewVisit> visitCollection) {
        for (NewVisit visit : visitCollection) {
            List<Date> datns = visit.getServices().stream().map(NewService::getDatn).collect(Collectors.toList());
            if (!datns.contains(visit.getDatn())) {
                errors.addError(visit, "нет услуги совпадающей с датой начала");
            }
        }
    }

    /**
     * Проверка на отсутствие в талоне необходимых услуг
     *
     * @param humanCollection Список Human
     */
    public void checkForMissedService(Collection<NewHuman> humanCollection) {
        for (Uslugi307 uslugi307 : uslugi307List.getUslugi()) {
            checkMissedUslugi(uslugi307.getUslugi(), uslugi307.getObrashenie(), uslugi307.getDoctor(), humanCollection);
        }
    }

    /**
     * Проверка на правильность заполнения результата обращения у профилактических приемов
     */
    public void checkForIncorrectIshob(Collection<NewVisit> visitCollection) {
        List<NewVisit> visits = new ArrayList<>(visitCollection.stream().filter(newVisit -> newVisit.getServices().stream()
                .map(NewService::getMkbх)
                .filter(s -> s.equalsIgnoreCase("Z01.4")).count() > 0)
                .collect(Collectors.toList()));
        visits.stream()
                .filter(e -> !e.getIshob().equals("314"))
                .forEach(newVisit -> errors.addError(newVisit, "неправильный исход обращения"));
    }

    /**
     * Проверка на наличие краевых пациентов
     *
     * @param visitCollection
     */
    public void checkForCorrectOkatoForStrangers(Collection<NewVisit> visitCollection) {
        List<NewVisit> visits = visitCollection.stream().filter(newVisit -> newVisit.getOkatoOms().startsWith("03") && newVisit.getPlOgrn().equals(TFOMS_OGRN)).collect(Collectors.toList());
        visits.forEach(newVisit -> errors.addError(newVisit, "краевой в счете для инокраевых"));
    }

    /**
     * Проверка на наличие территории у инокраевых
     *
     * @param visitCollection
     */
    public void checkForIncorrectOkato(Collection<NewVisit> visitCollection) {
        visitCollection.stream().filter(newVisit -> newVisit.getOkatoOms().isEmpty() && newVisit.getPlOgrn().equals(TFOMS_OGRN)).collect(Collectors.toList())
                .stream().forEach(newVisit1 -> errors.addError(newVisit1, "не указана территория для инокраевого"));
    }

    /**
     * Проверка на правильность номера полиса
     *
     * @param visitCollection
     */
    public void checkForIncorrectPolisNumber(Collection<NewVisit> visitCollection) {
        visitCollection.stream().filter(
                newVisit ->
                        (newVisit.getSpv() == 3 && newVisit.getSpn().length() != 16) ||
                                (newVisit.getSpv() == 2 && newVisit.getSpn().length() != 9)).collect(Collectors.toList()).stream().forEach(e -> errors.addError(e, "некорректно заполнен номер полиса"));

    }

    /**
     * Проверка на правильность типа полиса
     *
     * @param visitCollection
     */
    public void checkForIncorrectPolisType(Collection<NewVisit> visitCollection) {
        visitCollection.stream()
                .filter(newVisit -> !(newVisit.getSpv() == 3 || newVisit.getSpv() == 2 || newVisit.getSpv() == 1 || newVisit.getSpv() == 4))
                .collect(Collectors.toList()).stream().forEach(e -> errors.addError(e, "не указан тип полиса"));
    }


    private void checkMissedUslugi(List<String> uslugi, String obrashenie, String doctor, Collection<NewHuman> humanCollection) {
        for (NewHuman human : humanCollection) {
            for (NewVisit visit : human.getAllVisits().values()) {
                List<String> services = visit.getServices().stream().map(NewService::getKusl).collect(Collectors.toList());
                if (services.size() > 1 && (CollectionUtils.containsAny(services, uslugi)) && !services.contains(obrashenie)) {
                    errors.addError(visit, "отсутствует обращение к врач-" + doctor);
                }
            }
        }
    }

    /**
     * Проверка на лишние услуги
     *
     * @param humanCollection
     */
    public void checkForRedundantService(Collection<NewHuman> humanCollection) {
        for (Uslugi307 uslugi307 : uslugi307List.getUslugi()) {
            checkForRedundantDoctorService(uslugi307.getUslugi(), uslugi307.getObrashenie(), humanCollection);
        }
    }

    private void checkForRedundantDoctorService(List<String> uslugi, String obrashenie, Collection<NewHuman> humanCollection) {
        for (NewHuman human : humanCollection) {
            for (NewVisit visit : human.getAllVisits().values()) {
                List<String> services = visit.getServices().stream()
                        .map(NewService::getKusl)
                        .collect(Collectors.toList());
                List<String> ginecologServices = visit.getServices().stream()
                        .map(NewService::getKusl)
                        .filter(e -> (e.startsWith("B01.001.") || e.startsWith("B04.001")))
                        .collect(Collectors.toList());
                if (CollectionUtils.containsAny(services, uslugi) && services.contains(obrashenie) && services.size() == 2) {
                    errors.addError(visit, "содержит лишнее обращение");
                }
                if ((visit.getMKB().equalsIgnoreCase("Z34.0")) && ginecologServices.size()>0 && ginecologServices.contains(obrashenie)){
                    errors.addError(visit, "содержит лишнее обращение");
                }
            }
        }
    }

    public void checkForIncorrectDocument(Collection<NewHuman> humanCollection) {
        List<NewHuman> incorrectDocuments = humanCollection.stream()
                .filter(
                        newHuman ->
                                (newHuman.getcDoc() != null && newHuman.getcDoc() > 0) && (
                                        newHuman.getsDoc().isEmpty() ||
                                                newHuman.getnDoc().isEmpty())).collect(Collectors.toList());

        incorrectDocuments.stream().forEach(e -> errors.addError(e, "неправильно заполнена серия или номер документа"));
    }

    public void checkInvorrectMKB(Collection<NewService> serviceCollection) {
        List<NewService> services = serviceCollection.stream().filter(newService -> !mkbSet.contains(newService.getMkbх())).collect(Collectors.toList());
        services.stream().forEach(newService -> errors.addError(newService, "неправильный МКБ"));
    }

    public void checkReduandOGRN(Collection<NewVisit> visitCollection) {
        for (NewVisit visit : visitCollection) {
            String ogrn = visit.getPlOgrn();
            if (getSmo().equals("1207") && !ogrn.equals(ALPHA_OGRN)) {
                errors.addError(visit, "неправильный ОГРН");
            }
            if (getSmo().equals("1507") && !ogrn.equals(VTB_OGRN)) {
                errors.addError(visit, "неправильный ОГРН");
            }
            if (getSmo().equals("1807") && !ogrn.equals(MSK_ORGN)) {
                errors.addError(visit, "неправильный ОГРН");
            }
            if (getSmo().equals("4407") && !ogrn.equals(RGS_OGRN)) {
                errors.addError(visit, "неправильный ОГРН");
            }
            if (getSmo().equals("9007") && !ogrn.equals(TFOMS_OGRN)) {
                errors.addError(visit, "неправильный ОГРН");
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
