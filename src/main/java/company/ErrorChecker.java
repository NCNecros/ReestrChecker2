package company;

import company.entity.*;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс проверяющий ошибки.
 */
@Service
public class ErrorChecker {

    static final String TFOMS_OGRN = "1022301607393";
    static final String ALPHA_OGRN = "1047100775963";
    static final String VTB_OGRN = "1027739815245";
    static final String MSK_ORGN = "1032304935871";
    static final String RGS_OGRN = "1027806865481";

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
    // FIXME: 02.12.2015 проверить починить. у женской здесь вываливает кучу ошибок, а страховые находят всего несколько
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
                if ((visit.getMKB().equalsIgnoreCase("Z34.0")) && ginecologServices.size() > 0 && ginecologServices.contains(obrashenie)) {
                    errors.addError(visit, "содержит лишнее обращение");
                }
            }
        }
    }

    public void checkForIncorrectUslugaSpecialnost(Collection<NewService> services) {
        Map<String, List<String>> listOfSpec = getListOfSpec();
        for (NewService service : services) {
            if (listOfSpec.containsKey(service.getKusl())) {
                if (!listOfSpec.get(service.getKusl()).contains(service.getSpec()) && !service.getSpec().isEmpty()) {
                    if (service.getDato().before(new LocalDate(2016, 1, 31).toDate())) {
                        errors.addError(service, service.getUid() + "специальность д.б." + listOfSpec.get(service.getKusl()).get(0));
                    } else {
                        errors.addError(service, service.getUid() + "специальность д.б." + listOfSpec.get(service.getKusl()).get(1));
                    }
                }
            }
        }
    }

    /**
     * Проверка на отсутствующий МКБ при проставленом исполнителе.
     *
     * @param services
     */
    public void checkForIncorrectMKBAtDispan(Collection<NewService> services) {
        for (NewService service : services) {
            if (!service.getSpec().isEmpty() && service.getMkbх().isEmpty() && !service.getKusl().equals("A06.09.006") && service.getKusl().startsWith("B04.047")) {
                errors.addError(service, "отсутствует МКБ");
            }
        }
    }

    /**
     * Проверка на неправильные даты услуг в диспансеризации
     *
     * @param visits
     */
    public void checkForIncorrectDateForUslugaDispan(Collection<NewVisit> visits) {
        for (NewVisit visit : visits) {
            if (visit.getServices().stream().filter(e -> e.getKusl().equalsIgnoreCase("A01.30.009")).count() > 0) {
                NewService serviceA = visit.getServices().stream().filter(e -> e.getKusl().equalsIgnoreCase("A01.30.009")).findFirst().get();
                for (NewService service : visit.getServices()) {
                    if (!service.getDocTabn().isEmpty() && service.getDatn().before(serviceA.getDato())) {
                        errors.addError(visit, "содержит услугу раньше анкетирования");
                    }
                }
            }
            if (visit.getqG().equals("6")) {
                if (visit.getServices().stream().filter(e -> !e.getDocTabn().isEmpty()).map(NewService::getDatn).distinct().count() > 1) {
                    errors.addError(visit, "даты услуг отличаются");
                }
            }
        }
    }

    /**
     * Проверка на соответсвие возраста пациента и специальности врача
     *
     * @param services
     */
    public void checkForIncorrectSpecForChildrenStac(Collection<NewService> services) {
        for (NewService service : services) {
            LocalDate now = new LocalDate(service.getVisit().getDatps());
            LocalDate birthDate = new LocalDate(service.getVisit().getParent().getDatr());
            Years years = Years.yearsBetween(birthDate, now);
            if (years.getYears() < 18) {
                if (service.getKusl().startsWith("G10.2716")) {
                    if (!service.getSpec().equals("1134") && !service.getSpec().equalsIgnoreCase("22")) {
                        errors.addError(service, "(" + service.getUid().intValue() + ") специальность не соответствует возрасту д.б. педиатрия(22|68)");
                    }
                }
                if (service.getKusl().startsWith("G10.3116")) {
                    if (!service.getSpec().equals("1134") && !(service.getSpec().equalsIgnoreCase("11") || service.getSpec().equalsIgnoreCase("28"))) {
                        errors.addError(service, "(" + service.getUid().intValue() + ") специальность не соответствует возрасту д.б. хирургия(11|20)");
                    }
                }
            }
        }
    }

    private Map<String, List<String>> getListOfSpec() {
        Map<String, List<String>> listOfSpec = new HashMap<>();
        listOfSpec.put("A01.30.009", Arrays.asList("1122", "27"));
        listOfSpec.put("A02.12.002", Arrays.asList("1122", "27"));
        listOfSpec.put("A04.15.001", Arrays.asList("112211", "129"));
        listOfSpec.put("A04.20.001", Arrays.asList("112211", "129"));
        listOfSpec.put("A04.21.001", Arrays.asList("112211", "129"));
        listOfSpec.put("A04.28.002.001", Arrays.asList("112211", "129"));
        listOfSpec.put("A05.10.002.003", Arrays.asList("112212", "131"));
        listOfSpec.put("A06.09.006", Arrays.asList("1118", "24"));
        listOfSpec.put("A06.09.006.001", Arrays.asList("1118", "24"));
        listOfSpec.put("A06.20.004", Arrays.asList("1118", "24"));
        listOfSpec.put("A09.05.023.007", Arrays.asList("1107", "13"));
        listOfSpec.put("A09.05.024", Arrays.asList("1107", "13"));
        listOfSpec.put("A09.05.026.005", Arrays.asList("1107", "13"));
        listOfSpec.put("A09.19.001", Arrays.asList("2012", "13"));
        listOfSpec.put("A12.26.005", Arrays.asList("1122", "20"));
        listOfSpec.put("B03.016.002", Arrays.asList("1702", "189"));
        listOfSpec.put("B03.016.003", Arrays.asList("1702", "189"));
        listOfSpec.put("B03.016.004", Arrays.asList("1107", "189"));
        listOfSpec.put("B03.016.006", Arrays.asList("1702", "189"));
        listOfSpec.put("B03.069.003", Arrays.asList("1122", "118"));
        listOfSpec.put("B04.001.009", Arrays.asList("2003", "8"));
        listOfSpec.put("B04.015.007", Arrays.asList("1122", "118"));
        listOfSpec.put("B04.015.008", Arrays.asList("1122", "118"));
        listOfSpec.put("B04.047.001.01", Arrays.asList("1122", "27"));
        listOfSpec.put("B04.047.001.02", Arrays.asList("1122", "27"));
        listOfSpec.put("B04.047.001.03", Arrays.asList("1122", "27"));
        listOfSpec.put("B04.047.001.04", Arrays.asList("1122", "27"));
        listOfSpec.put("B04.047.001.05", Arrays.asList("1122", "27"));
        listOfSpec.put("B04.047.001.06", Arrays.asList("1122", "27"));
        listOfSpec.put("B04.047.001.07", Arrays.asList("1122", "27"));
        listOfSpec.put("B04.047.001.08", Arrays.asList("1122", "27"));
        listOfSpec.put("B04.047.001.09", Arrays.asList("1122", "27"));
        listOfSpec.put("B04.047.001.10", Arrays.asList("1122", "27"));
        listOfSpec.put("B04.047.001.11", Arrays.asList("1122", "27"));
        listOfSpec.put("B04.047.001.12", Arrays.asList("1122", "27"));
        listOfSpec.put("B04.047.001.61", Arrays.asList("1122", "27"));
        listOfSpec.put("B04.047.001.62", Arrays.asList("1122", "27"));
        listOfSpec.put("B04.047.001.63", Arrays.asList("1122", "27"));
        listOfSpec.put("B04.047.001.64", Arrays.asList("1122", "27"));
        listOfSpec.put("B04.047.001.65", Arrays.asList("1122", "27"));
        listOfSpec.put("B04.047.001.66", Arrays.asList("1122", "27"));
        return listOfSpec;
    }

    void checkForIncorrectDocument(Collection<NewHuman> humanCollection) {
        List<NewHuman> incorrectDocuments = humanCollection.stream()
                .filter(
                        newHuman ->
                                (newHuman.getcDoc() != null && newHuman.getcDoc() > 0) && (
                                        newHuman.getsDoc().isEmpty() ||
                                                newHuman.getnDoc().isEmpty())).collect(Collectors.toList());

        incorrectDocuments.stream().forEach(e -> errors.addError(e, "неправильно заполнена серия или номер документа"));
    }

    void checkForInсorrectMKB(Collection<NewService> serviceCollection) {
        List<NewService> services = serviceCollection.stream().filter(newService -> !mkbSet.contains(newService.getMkbх())).collect(Collectors.toList());
        services.stream().forEach(newService -> errors.addError(newService, "неправильный МКБ"));
    }

    void checkReduandOGRN(Collection<NewVisit> visitCollection) {
        for (NewVisit visit : visitCollection) {
            String ogrn = visit.getPlOgrn();
            switch (getSmo()) {
                case "1207":
                    if (!ogrn.equals(ALPHA_OGRN)) {
                        errors.addError(visit, "неправильный ОГРН");
                    }
                    break;
                case "1507":
                    if (!ogrn.equals(VTB_OGRN)) {
                        errors.addError(visit, "неправильный ОГРН");
                    }
                    break;
                case "4407":
                    if (!ogrn.equals(RGS_OGRN)) {
                        errors.addError(visit, "неправильный ОГРН");
                    }
                    break;
                case "1807":
                    if (!ogrn.equals(MSK_ORGN)) {
                        errors.addError(visit, "неправильный ОГРН");
                    }
                    break;
                case "9007":
                    if (!ogrn.equals(TFOMS_OGRN)) {
                        errors.addError(visit, "неправильный ОГРН");
                    }
                    break;
            }

        }
    }


    private String getSmo() {
        return smo;
    }

    void setSmo(String smo) {
        this.smo = smo;
    }

    private String getMKBByMonth(int months) {
        String m = "";
        if (months >= 0 && months <= 47) {
            m = "Z00.1";
        }
        if (months >= 48 && months <= 143) {
            m = "Z00.2";
        }
        if (months >= 144 && months <= 216) {
            m = "Z00.3";
        }
        return m;
    }
}
