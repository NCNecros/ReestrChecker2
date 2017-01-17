package company;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import company.entity.EntityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@org.springframework.stereotype.Service
public class DBFHelper {
    private static final Logger logger = LoggerFactory.getLogger(DBFHelper.class);
    @Resource
    private DataFiller dataFiller;
    @Resource
    private List<Spr69Value> spr69List;
    private DBFReader dbfReader;

    public DBFHelper() {
    }

    public DBFHelper(DataFiller dataFiller) {
        this.dataFiller = dataFiller;
    }

    public void readFromP(String filename/*, Map<String, Human> humanMap*/) throws FileNotFoundException, DBFException {
        dataFiller.clearData();
        Object[] row;
        dbfReader = new DBFReader(new FileInputStream(filename));
        Map<String, Integer> fieldList = getFieldListFromDBFReader(dbfReader);
        dbfReader.setCharactersetName("cp866");

        while ((row = dbfReader.nextRecord()) != null) {
            dataFiller.addHumanAndVisit(row,fieldList);
        }
    }

    public void readFromU(String filename/*, Map<String, Human> humanMap*/) throws FileNotFoundException, DBFException {
        Object[] row;
        dbfReader = new DBFReader(new FileInputStream(filename));
        dbfReader.setCharactersetName("cp866");

        Map<String, Integer> fieldList = getFieldListFromDBFReader(dbfReader);

        while ((row = dbfReader.nextRecord()) != null) {
            dataFiller.addService(row, fieldList);
        }
    }

    public void readFromD(String filename/*, Map<String, Human> humanMap*/) throws FileNotFoundException, DBFException {
        Object[] row;
        dbfReader = new DBFReader(new FileInputStream(filename));
        dbfReader.setCharactersetName("cp866");

        Map<String, Integer> fieldList = getFieldListFromDBFReader(dbfReader);

        while ((row = dbfReader.nextRecord()) != null) {
            dataFiller.addDoctor(row, fieldList);
        }
    }

    private Map<String, Integer> getFieldListFromDBFReader(DBFReader dbfReader) throws DBFException {
        Map<String, Integer> fieldList = new HashMap<>();
        for (Integer i = 0; i < dbfReader.getFieldCount(); i++) {
            DBFField field = dbfReader.getField(i);
            fieldList.put(field.getName(), i);
        }
        return fieldList;
    }


    public void readFromSpr69() throws FileNotFoundException, DBFException {
        Object[] row;
        dbfReader = new DBFReader(new FileInputStream(getClass().getClassLoader().getResource("SPR69.dbf").getFile()));
        dbfReader.setCharactersetName("cp866");

        Map<String, Integer> fieldList = getFieldListFromDBFReader(dbfReader);

        while ((row = dbfReader.nextRecord()) != null) {
            spr69List.add(EntityFactory.buildSpr69ValueFromRow(row, fieldList));
        }
    }
}