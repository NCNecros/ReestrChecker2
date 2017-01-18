package company;

import company.entity.*;
import org.jamel.dbf.structure.DbfRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.Charset;

/**
 * Класс содержит методы превращения данных из дбф в внутреннюю структуру классов
 * Так как класс по работе с ДБФ не должен, очевидно, этим заниматься, а куда еще их запихать я не знаю.
 */
@Service
public class DataFiller {
    private static final Logger logger = LoggerFactory.getLogger(DataFiller.class);
    @Resource
    private
    Data data;
    private String ENCODING = "cp866";
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

    public void addHumanAndVisit(DbfRow row) {
        String isti = row.getString("ISTI", Charset.forName(ENCODING));
        NewHuman human;

        if (data.containsHuman(isti)) {
            human = data.getHumanByIsti(isti);
        } else {
            human = EntityFactory.buildHumanFromDbfRow(row);
            data.add(human);
        }
        NewVisit visit = EntityFactory.buildVisitFromRow(row);

        data.add(visit, human.getIsti());
    }

    public void addDoctor(DbfRow row) {
        Doctor doctor = EntityFactory.buildDoctorFromDbfRow(row);
        data.add(doctor);
    }

    public void addService(DbfRow row) {
        final Integer sn = row.getInt("SN");
        if (data.containsVisit(sn)) {
            NewService service = EntityFactory.buildServiceFromRow(row);
            data.add(service);
        } else {
            logger.error("Возникла ошибка при загрузке файла U. В системе отсутствует нужный талон.");
        }
    }
}
