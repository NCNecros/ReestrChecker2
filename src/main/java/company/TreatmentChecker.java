//package company;
//todo del
//import company.entity.Error;
//import company.entity.Service;
//import company.entity.Treatment;
//import company.entity.Uslugi307;
//import org.apache.commons.collections4.CollectionUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * Created by Necros on 03.06.2015.
// */
//@org.springframework.stereotype.Service
//@Scope(value = "singleton")
//public class TreatmentChecker {
////    List<company.entity.Error> result;
//    Uslugi307List uslugi307List;
//    List<String> services;
////    Treatment treatment;
//
//    @Autowired
//    public TreatmentChecker(Uslugi307List uslugi307List) {
//        this.uslugi307List = uslugi307List;
//    }
//
//    public TreatmentChecker() {
//    }
//
////    public List<Error> checkUslugi(Treatment treatment) {
////        this.treatment = treatment;
////        result = new ArrayList<>();
////        services = treatment.getUslugi().values().stream()
////                .map(Service::getKusl)
////                .collect(Collectors.toList());
////
////        for (Service service : treatment.getUslugi().values()) {
////            result.addAll(UslugaChecker.check(service));
////        }
////
////        checkForServiceDuplicates();
////        checkForMissedService();
////        checkIncorrectDateOfService();
////      //  checkForReduantService();
////
////        return result;
////    }
//
//    private void checkForReduantService() {
//        for (Uslugi307 uslugi307 : uslugi307List.getUslugi()) {
//            checkForReduantDoctorService(uslugi307.getUslugi(), uslugi307.getObrashenie(), uslugi307.getDoctor());
//        }
//    }
//
//    private void checkForMissedService() {
//
//        for (Uslugi307 uslugi307 : uslugi307List.getUslugi()) {
//            checkMissedUslugi(uslugi307.getUslugi(), uslugi307.getObrashenie(), uslugi307.getDoctor());
//        }
//    }
//
//    private void checkForServiceDuplicates() {
//        List<Service> allServices = new ArrayList<>(treatment.getUslugi().values());
//        List<Service> servicesWithOutDuplicates = allServices.stream()
//                .filter(e -> Collections.frequency(allServices, e) == 1)
//                .collect(Collectors.toList());
//
//        if (allServices.size() != servicesWithOutDuplicates.size()) {
//            addError("содержит дубликаты услуг");
//        }
//    }
//
//    private void addError(String error) {
//        result.add(new Error(treatment.getParent(), treatment, error));
//    }
//
//    private void checkMissedUslugi(List<String> uslugi, String obrashenie, String doctor) {
//        if (services.size() > 1 && (CollectionUtils.containsAny(services, uslugi)) && !services.contains(obrashenie)) {
//            addErrorForMissedObrashenie(doctor);
//        }
//    }
//
//    private void addErrorForMissedObrashenie(String doctor) {
//        addError("отсутствует обращение - врач-" + doctor);
//    }
//
//    private void checkIncorrectDateOfService() {
//        List<Date> dates = treatment.getUslugi().values().stream().map(Service::getDatn).collect(Collectors.toList());
//        if (!dates.contains(treatment.getDatn())) {
//            addError("нет услуги совпадающей с датой начала лечения");
//        }
//        if (!dates.contains(treatment.getDato())) {
//            addError("нет услуги совпадающей с датой окончания лечения");
//        }
//        if (treatment.getDato() == null) {
//            addError("дата окончания лечения не проставлена");
//        }
//    }
//
//    private void addErrorForReduantObrashenie(String doctor) {
//        addError("лишнее обращение - врач-" + doctor);
//    }
//
//    private void checkForReduantDoctorService(List<String> uslugi, String obrashenie, String doctor) {
//
//        if (CollectionUtils.containsAny(services, uslugi) && services.contains(obrashenie) && services.size() == 2) {
//            addErrorForReduantObrashenie(doctor);
//        }
//    }
//}
