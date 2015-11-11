package company;

import company.entity.*;
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
    private static final Logger logger = LoggerFactory.getLogger(DataFiller.class);
    @Resource
    Data data;

    public DataFiller() {
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void clearData() {
        data.clear();
    }

    public void addHumanAndVisit(Object[] row, Map<String, Integer> fieldList) {
        String isti = ((String) row[fieldList.get("ISTI")]).trim();
        NewHuman human;

        if (data.containsHuman(isti)) {
            human = data.getHumanByIsti(isti);
        } else {
            human = EntityFactory.buildHumanFromDbfRow(row, fieldList);
            data.add(human);
        }
        NewVisit visit = EntityFactory.buildVisitFromRow(row, fieldList);

        data.add(visit, human.getIsti());
    }

    public void addService(Object[] row, Map<String, Integer> fieldList) {
        final Double sn = ((Double) row[fieldList.get("SN")]);
        if (data.containsVisit(sn)) {
            NewService service = EntityFactory.buildServiceFromRow(row, fieldList);
            data.add(service);
        } else {
            logger.error("Возникла ошибка при загрузке файла U. В системе отсутствует нужный талон.");
        }
    }
}
