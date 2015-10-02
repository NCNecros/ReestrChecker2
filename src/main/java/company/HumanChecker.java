//package company;
//TODO Удалить или переделать
//
//import company.entity.Error;
//import company.entity.Human;
//import company.entity.Treatment;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@org.springframework.stereotype.Service()
//@Scope(value = "singleton")
//public class HumanChecker {
//    private TreatmentChecker treatmentChecker;
//
//    @Autowired
//    public HumanChecker(TreatmentChecker treatmentChecker) {
//        this.treatmentChecker = treatmentChecker;
//    }
//
//    public HumanChecker() {
//    }
//
//    public List<company.entity.Error> checkErrors(Human human) {
//        List<Error> result = new ArrayList<>();
//        List<Treatment> list = new ArrayList<>(human.getTreatmentList().values());
//
//        for (Treatment treatment : list) {
//            result.addAll(treatmentChecker.checkUslugi(treatment));
//            List<Treatment> obrasheniesWithOutCurrent = new ArrayList<>(list);
//            obrasheniesWithOutCurrent.remove(treatment);
//            for (Treatment otherObr : obrasheniesWithOutCurrent) {
//                Boolean res = otherObr.getDoctor().equalsIgnoreCase(treatment.getDoctor()) &&
//                        otherObr.getMkb().equalsIgnoreCase(treatment.getMkb());
//                if (res && treatment.getUslugi().values().stream().anyMatch(e -> !e.getKusl().startsWith("B04"))) {
//                    result.add(new Error(treatment.getParent(), treatment, "более одного обращения"));
//                }
//            }
//        }
//        return result;
//    }
//}