package company.entity;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Necros on 10.11.2015.
 */
@Service
public class Data {
    Map<String, NewHuman> mapNewHuman = new HashMap<>();
    Map<Integer, NewVisit> visitMap = new HashMap<>();
    Map<Integer, NewService> serviceMap = new HashMap<>();
    Map<String, Doctor> doctorMap = new HashMap<>();

    public boolean containsHuman(String isti) {
        return mapNewHuman.containsKey(isti);
    }

    public NewHuman getHumanByIsti(String isti) {
        return mapNewHuman.get(isti);
    }

    public void add(NewHuman human){
        mapNewHuman.put(human.getIsti(),human);
    }

    public Collection<NewService> getServices(){
        return serviceMap.values();
    }

    public Collection<NewHuman> getHumans(){
        return mapNewHuman.values();
    }

    public Collection<NewVisit> getVisits(){
        return visitMap.values();
    }

    public NewVisit getVisitBySn(Integer sn) {
        return visitMap.get(sn);
    }

    public Collection<Doctor> getDoctors() {
        return doctorMap.values();
    }

    public NewService getServiceByUid(Integer uid) {
        return serviceMap.get(uid);
    }

    public void add(NewVisit visit, String humanIsti){
        visitMap.put(visit.getSn(), visit);
        getHumanByIsti(humanIsti).addVisit(visit);
    }

    public void add(NewService service){
        serviceMap.put(service.getUid(),service);
        getVisitBySn(service.getSn()).addService(service);
    }

    public void add(Doctor doctor) {
        doctorMap.put(doctor.getDocTabn(), doctor);
    }

    public void clear(){
        mapNewHuman.clear();
        serviceMap.clear();
        visitMap.clear();
    }

    public boolean containsVisit(Integer sn) {
        return visitMap.containsKey(sn);
    }
}
