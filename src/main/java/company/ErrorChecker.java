package company;

import company.entity.*;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.LocalDate;
import org.joda.time.Months;
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
    @Resource(name = "spr69List")
    private List<Spr69Value> spr69List;
    private List<String> spr69HashsWithMkb;
    private List<String> spr69HashsWithOutMkb;

    public ErrorChecker() {
    }

    public ErrorChecker(ListOfError errors, Uslugi307List uslugi307List, Set<String> mkbSet, String smo) {
        this.errors = errors;
        this.uslugi307List = uslugi307List;
        this.mkbSet = mkbSet;
        this.smo = smo;
    }

    @Deprecated
    /**
     * Проверка на некорректно заполненное поле ВМП
     */
    public void checkForIncorrectVMP(Collection<NewService> serviceCollection) {
        serviceCollection.stream()
                .filter(e -> (e.getSpec().equals("1134") || e.getSpec().equals("1122")) && !e.getVmp().equals("12"))
                .forEach(e -> errors.addError(e, "Некорректный вид МП"));
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
                    Boolean spec = visit.getServices().stream().map(NewService::getSpec).findFirst().orElse("")
                            .equalsIgnoreCase(otherVisit.getServices().stream().map(NewService::getSpec).findFirst().orElse(""));
                    if (((visit.getMKB().equalsIgnoreCase(otherVisit.getMKB()) && spec) || res) && visit.getServices().stream().anyMatch(e -> !e.getKusl().startsWith("B04"))) {
                        errors.addError(visit, "(307) содержит более одного обращения");
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
    @Deprecated
    public void checkForCorrectOkatoForStrangers(Collection<NewVisit> visitCollection) {
        List<NewVisit> visits = visitCollection.stream().filter(newVisit -> newVisit.getOkatoOms().startsWith("03") && newVisit.getPlOgrn().equals(TFOMS_OGRN)).collect(Collectors.toList());
        visits.forEach(newVisit -> errors.addError(newVisit, "краевой в счете для инокраевых"));
    }

    /**
     * Проверка на наличие территории у инокраевых
     *
     * @param visitCollection
     */
    @Deprecated
    public void checkForIncorrectOkato(Collection<NewVisit> visitCollection) {
        visitCollection.stream().filter(newVisit -> newVisit.getOkatoOms().isEmpty() && newVisit.getPlOgrn().equals(TFOMS_OGRN)).collect(Collectors.toList()).forEach(newVisit1 -> errors.addError(newVisit1, "не указана территория для инокраевого"));
    }

    private boolean ishobIsCorrect(NewVisit visit) {
        Map<String, List<String>> ishobIshlMap = getIshobIshl();
        return (ishobIshlMap.get(visit.getIshob()).contains(visit.getIshl()));
    }

    public Map<String, List<String>> getIshobIshl() {
        Map<String, List<String>> ishobIshlMap = new HashMap<>(20);
        ishobIshlMap.put("101", Arrays.asList("101", "102"));
        ishobIshlMap.put("102", Arrays.asList("102", "103", "104"));
        ishobIshlMap.put("103", Collections.singletonList("102"));
        ishobIshlMap.put("104", Arrays.asList("102", "103", "104"));
        ishobIshlMap.put("105", Collections.singletonList("105"));
        ishobIshlMap.put("106", Collections.singletonList("105"));
        ishobIshlMap.put("107", Arrays.asList("102", "103", "104"));
        ishobIshlMap.put("108", Arrays.asList("102", "103"));
        ishobIshlMap.put("110", Arrays.asList("102", "103", "104"));
        ishobIshlMap.put("201", Arrays.asList("201", "202"));
        ishobIshlMap.put("202", Arrays.asList("202", "203", "204"));
        ishobIshlMap.put("203", Arrays.asList("203", "204"));
        ishobIshlMap.put("204", Arrays.asList("202", "203", "204"));
        ishobIshlMap.put("205", Collections.singletonList("205"));
        ishobIshlMap.put("206", Collections.singletonList("205"));
        ishobIshlMap.put("207", Arrays.asList("202", "203", "204"));
        ishobIshlMap.put("208", Arrays.asList("202", "203"));
        ishobIshlMap.put("301", Arrays.asList("301", "302"));
        ishobIshlMap.put("302", Arrays.asList("303", "304", "305"));
        ishobIshlMap.put("304", Arrays.asList("303", "304"));
        ishobIshlMap.put("305", Arrays.asList("304", "305"));
        ishobIshlMap.put("306", Arrays.asList("304", "305"));
        ishobIshlMap.put("307", Arrays.asList("304", "305"));
        ishobIshlMap.put("308", Arrays.asList("304", "305"));
        ishobIshlMap.put("309", Arrays.asList("304", "305"));
        ishobIshlMap.put("310", Arrays.asList("303", "304"));
        ishobIshlMap.put("311", Arrays.asList("302", "303"));
        ishobIshlMap.put("312", Collections.singletonList("306"));
        ishobIshlMap.put("313", Collections.singletonList("307"));
        ishobIshlMap.put("314", Collections.singletonList("306"));
        ishobIshlMap.put("315", Arrays.asList("304", "305"));
        ishobIshlMap.put("316", Arrays.asList("304", "305"));
        ishobIshlMap.put("317", Collections.singletonList("306"));
        ishobIshlMap.put("318", Collections.singletonList("306"));
        ishobIshlMap.put("319", Collections.singletonList("306"));
        ishobIshlMap.put("320", Collections.singletonList("306"));
        ishobIshlMap.put("321", Collections.singletonList("306"));
        ishobIshlMap.put("355", Collections.singletonList("306"));
        ishobIshlMap.put("356", Collections.singletonList("306"));
        ishobIshlMap.put("401", Arrays.asList("401", "402"));
        ishobIshlMap.put("402", Arrays.asList("402", "403"));
        ishobIshlMap.put("403", Arrays.asList("402", "403"));
        ishobIshlMap.put("404", Arrays.asList("402", "403"));
        ishobIshlMap.put("405", Collections.singletonList("404"));
        ishobIshlMap.put("406", Collections.singletonList("404"));
        return ishobIshlMap;
    }

    /**
     * Проверка на ошибку 903
     */
    public void checkForError903(Collection<NewVisit> visitCollection) {
        visitCollection.stream().filter(
                newVisit -> !ishobIsCorrect(newVisit)
        ).forEach(newVisit -> errors.addError(newVisit, "903 исход лечения не соответствует исходу обращения"));
    }

    /**
     * Проверка на правильность номера полиса
     *
     * @param visitCollection
     */
    @Deprecated
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
    @Deprecated
    public void checkForIncorrectPolisType(Collection<NewVisit> visitCollection) {
        visitCollection.stream()
                .filter(newVisit -> !Arrays.asList(1, 2, 3, 4).contains(newVisit.getSpv()))
                .collect(Collectors.toList()).forEach(e -> errors.addError(e, "не указан тип полиса"));
    }

    private void checkMissedUslugi(List<String> uslugi, String obrashenie, String doctor, Collection<NewHuman> humanCollection) {
        for (NewHuman human : humanCollection) {
            for (NewVisit visit : human.getAllVisits().values()) {
                List<String> services = visit.getServices().stream().map(NewService::getKusl).collect(Collectors.toList());
                if (services.size() > 1) {
                    if (CollectionUtils.containsAny(services, uslugi)) {
                        if (!services.contains(obrashenie)) {
                            if (!visit.getMKB().equalsIgnoreCase("Z34.0")) {
                                errors.addError(visit, "отсутствует обращение к врач-" + doctor);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Проверка на лишние услуги
     *
     * @param humanCollection
     */
    void checkForRedundantService(Collection<NewHuman> humanCollection) {
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
//                if ((visit.getMKB().equalsIgnoreCase("Z34.0") || visit.getMKB().equalsIgnoreCase("O99.8")) && ginecologServices.size() > 0 && ginecologServices.contains(obrashenie)) {
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
            if (!service.getSpec().isEmpty()) {
                if (service.getMkbх().isEmpty()) {
                    if (service.getKusl().startsWith("B04.047")) {
                        errors.addError(service, "отсутствует МКБ");
                    }
                }
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
                    if (!service.getDocTabn().isEmpty()) {
                        if (service.getDatn().before(serviceA.getDato())) {
                            errors.addError(visit, "содержит услугу раньше анкетирования");
                        }
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
     * Проверка на соответствие возраста пациента и специальности врача
     *
     * @param services
     */
    public void checkForIncorrectSpecForChildrenStac(Collection<NewService> services) {
        for (NewService service : services) {
            LocalDate now = new LocalDate(service.getVisit().getDatps());
            LocalDate birthDate = new LocalDate(service.getVisit().getParent().getDatr());
            Years years = Years.yearsBetween(birthDate, now);
            if (years.getYears() < 18) {
                if (service.getKusl().startsWith("G10.27") || service.getKusl().startsWith("G10.22")) {
                    if (!service.getSpec().equalsIgnoreCase("22")) {
                        errors.addError(service, "(" + service.getUid().intValue() + ") специальность не соответствует возрасту д.б. педиатрия(22|68)");
                    }
                } else if (service.getKusl().startsWith("G10.31") || service.getKusl().startsWith("G10.10")) {
                    if (!(service.getSpec().equalsIgnoreCase("11") || service.getSpec().equalsIgnoreCase("22"))) {
                        errors.addError(service, "(" + service.getUid().intValue() + ") специальность не соответствует возрасту д.б. дет хирургия(11|20)");
                    }
                } else if (service.getKusl().startsWith("G10.09")) {
                    if (!(service.getSpec().equalsIgnoreCase("42") || service.getSpec().equalsIgnoreCase("22"))) {
                        errors.addError(service, "(" + service.getUid().intValue() + ") специальность не соответствует возрасту д.б. дет. ур-андр(42|19)");
                    }
                } else if (service.getKusl().startsWith("G10")) {
                    if (service.getVp().equals("12")
                            && (service.getSpec().equalsIgnoreCase("30") || service.getSpec().equalsIgnoreCase("27"))) {
                        errors.addError(service, "(" + service.getUid().intValue() + ") специальность не соответствует возрасту");
                    }
                }
            }
        }
    }

    /**
     * Проверка на соответсвие специальности врача и профиля услуги
     *
     * @param services
     */
    public void checkForIncorrectSpecAndProfilStac(Collection<NewService> services) {
        for (NewService service : services) {
            if (service.getSpec().equalsIgnoreCase("22")) {
                if (!service.getProfil().equalsIgnoreCase("68")) {
                    errors.addError(service, "(" + service.getUid().intValue() + ") несоответствие специальности и профиля д.б. педиатрия(22|68)");
                }
            }
        }
    }

    public void checkForIncorrectDoctorSnils(Collection<Doctor> doctors) {
        for (Doctor doctor : doctors) {
            if (doctor.getSnils().isEmpty()) {
                errors.addError(doctor, "у доктора отсутствует СНИЛС");
            }
        }
    }

    public void checkForIncorrectDoctorDant(Collection<Doctor> doctors) {
        for (Doctor doctor : doctors) {
            if (Objects.isNull(doctor.getDatn())) {
                errors.addError(doctor, "у доктора отсутствует дата начала работы");
            }
        }
    }

    public void checkForIncorrectNaprMoCodeAndNumber(Collection<NewVisit> visits) {
        for (NewVisit visit : visits) {
            if (visit.getMp().equals("1")) {
                if (visit.getNaprMo().isEmpty() || visit.getNaprN().isEmpty()) {
                    errors.addError(visit, "неправильное МО или номер направления");
                }
            }
        }
    }

    // TODO: 07.02.2017 Неправильно сделанная логика. Пересмотреть

    /**
     * Проверка на ошибку 347 "Указанные КСГ не соответствует страховому случаю SPR69, SPR70
     * @param visits
     */
    void checkForIncorrectSpr69(Collection<NewVisit> visits) {
        if (visits.isEmpty()) {
            return;
        }

        if (visits.stream().findFirst().get().getServices().stream().filter(e->e.getKusl().startsWith("G")).count()==0) {
            return;
        }

        Map<String, Map<String, Set<String>>> collect = null;

        if (Objects.isNull(collect)) {
            collect = spr69List.stream().collect(Collectors.groupingBy(e -> e.getKsgcode(), Collectors.groupingBy(o -> o.getKusl(), Collectors.mapping(o -> o.getMkbx(), Collectors.toSet()))));
        }

        for (NewVisit visit : visits) {
            boolean hasError = true;
            String operation = "";
            String mkb = visit.getMKB();
            String ksg = "";
            for (NewService service : visit.getServices()) {
                if (service.getKusl().startsWith("A")) {
                    operation = service.getKusl();
                } else if (service.getKusl().startsWith("G")) {
                    ksg = service.getKusl();
                }
            }
            if (!collect.containsKey(ksg)){
                continue;
            }
            if (collect.containsKey(ksg)){
                if (collect.get(ksg).containsKey(operation)){
                    if (collect.get(ksg).get(operation).contains(mkb)){
                        hasError = false;
                    }
                }
            }
            if (hasError) {
                errors.addError(visit, "КСГ не соответствует SPR69");
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

    public void checkForIncorrectAgeForThisMKBWhenAgeIsIncorrect(Collection<NewService> newServices) {
        List<String> mkbxList = Arrays.asList("Z00.1", "Z00.2", "Z00.3");
        List<String> listOfUsluga = Arrays.asList("B04.028.003", "B04.031.004");

        for (NewService service : newServices) {
            Date birthDate = service.getVisit().getParent().getDatr();
            Date serviceDate = service.getDatn();
            if (mkbxList.contains(service.getMkbх()) && listOfUsluga.contains(service.getKusl())) {
                int month = Months.monthsBetween(LocalDate.fromDateFields(birthDate), LocalDate.fromDateFields(serviceDate)).getMonths();
                if (!service.getMkbх().equalsIgnoreCase(getMKBByMonth(month))) {
                    errors.addError(service, "диагноз не соответствует возрасту");
                }
            }
        }
    }

    public void setSpr69List(List<Spr69Value> spr69List) {
        this.spr69List = spr69List;
    }
}
