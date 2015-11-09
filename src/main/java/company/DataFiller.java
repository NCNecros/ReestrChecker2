package company;

import company.entity.EntityFactory;
import company.entity.NewHuman;
import company.entity.NewService;
import company.entity.NewVisit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Класс содержит методы превращения данных из дбф в внутреннюю структуру классов
 * Так как класс по работе с ДБФ не должен, очевидно, этим заниматься, а куда еще их запихать я не знаю.
 */
@Service
public class DataFiller {
    @Resource(name = "mapNewHuman")
    Map<String, NewHuman> mapNewHuman;
    @Resource(name = "mapNewVisit")
    Map<Double, NewVisit> mapNewVisit;
    @Resource(name = "mapNewService")
    Map<Double, NewService> mapNewService;

    private static final Logger logger = LoggerFactory.getLogger(DataFiller.class);

    public DataFiller() {
    }

    public void clearData() {
        mapNewHuman.clear();
        mapNewService.clear();
        mapNewVisit.clear();
    }

    public DataFiller(Map<String, NewHuman> mapNewHuman, Map<Double, NewVisit> mapNewVisit, Map<Double, NewService> mapNewService) {
        this.mapNewHuman = mapNewHuman;
        this.mapNewVisit = mapNewVisit;
        this.mapNewService = mapNewService;
    }

    public void addHumanAndVisit(Object[] row, Map<String, Integer> fieldList) {
        String isti = ((String) row[fieldList.get("ISTI")]).trim();
        NewHuman human;

        if (mapNewHuman.containsKey(isti)) {
            human = mapNewHuman.get(isti);
        } else {
            human = EntityFactory.buildHumanFromDbfRow(row, fieldList);
            mapNewHuman.put(human.getIsti(), human);
        }

        NewVisit visit = EntityFactory.buildVisitFromRow(row, fieldList);
        human.addVisit(visit);
        mapNewVisit.put(visit.getSn(), visit);
    }

    public void addService(Object[] row, Map<String, Integer> fieldList){
        final Double sn = ((Double) row[fieldList.get("SN")]);
        NewVisit visit;
        if (mapNewVisit.containsKey(sn)) {
            visit = mapNewVisit.get(sn);
            NewService service = EntityFactory.buildServiceFromRow(row, fieldList);
            visit.addService(service);
            mapNewService.put(service.getUid(), service);
        } else {
            logger.error("Возникла ошибка при загрузке файла U. В системе отсутствует нужный талон.");
        }
    }
}
