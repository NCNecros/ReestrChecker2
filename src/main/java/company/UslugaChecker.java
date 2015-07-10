//package company;
//TODO Удалить
//import company.entity.Error;
//import company.entity.Human;
//import company.entity.Service;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Necros on 20.03.2015.
// */
//public class UslugaChecker {
//    public UslugaChecker() {
//    }
//
//    public static List<company.entity.Error> check(Service service) {
//        List<Error> result = new ArrayList<>();
//        if (service.getDatn() == null || service.getDato() == null) {
//            Human human = service.getParent().getParent();
//            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
//            result.add(new Error(human, null, "содержит обращение с незакрытыми мероприятиями"));
//        }
//        return result;
//    }
//}
