package company;

import company.entity.EntityFactory;
import org.jamel.dbf.DbfReader;
import org.jamel.dbf.structure.DbfRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;


@org.springframework.stereotype.Service
public class DBFHelper {
    private static final Logger logger = LoggerFactory.getLogger(DBFHelper.class);
    DbfReader reader;
    @Resource
    private DataFiller dataFiller;
    @Resource
    private List<Spr69Value> spr69List;

    public DBFHelper() {
    }

    public DBFHelper(DataFiller dataFiller) {
        this.dataFiller = dataFiller;
    }

    void readFromP(String filename/*, Map<String, Human> humanMap*/) throws FileNotFoundException {
        dataFiller.clearData();
        DbfRow row;
        reader = new DbfReader(new FileInputStream(filename));

        while ((row = reader.nextRow()) != null) {
            dataFiller.addHumanAndVisit(row);
        }
    }

    void readFromU(String filename) throws FileNotFoundException {
        DbfRow row;
        reader = new DbfReader(new FileInputStream(filename));

        while ((row = reader.nextRow()) != null) {
            dataFiller.addService(row);
        }
    }

    void readFromD(String filename) throws FileNotFoundException {
        DbfRow row;
        reader = new DbfReader(new FileInputStream(filename));

        while ((row = reader.nextRow()) != null) {
            dataFiller.addDoctor(row);
        }
    }


    void readFromSpr69() throws FileNotFoundException {
        DbfRow row;
        reader = new DbfReader(new FileInputStream(getClass().getClassLoader().getResource("SPR69.dbf").getFile()));

        while ((row = reader.nextRow()) != null) {
            spr69List.add(EntityFactory.buildSpr69ValueFromRow(row));
        }
    }
}