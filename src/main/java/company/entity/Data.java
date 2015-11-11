package company.entity;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Necros on 10.11.2015.
 */
@Service
public class Data {
    Map<String, NewHuman> mapNewHuman = new HashMap<>();
    Map<Double, NewVisit> visitMap = new HashMap<>();
    Map<Double, NewService> serviceMap = new HashMap<>();

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
    public NewVisit getVisitBySn(Double sn){
        return visitMap.get(sn);
    }

    public NewService getServiceByUid(Double uid){
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

    public void clear(){
        mapNewHuman.clear();
    }

    public boolean containsVisit(Double sn){
        return visitMap.containsKey(sn);
    }
}
