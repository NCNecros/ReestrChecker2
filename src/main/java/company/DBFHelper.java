package company;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import company.entity.NewHuman;
import company.entity.NewService;
import company.entity.NewVisit;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@org.springframework.stereotype.Service
public class DBFHelper {
    @Resource(name = "mapNewHuman")
    Map<String, NewHuman> mapNewHuman;
    @Resource(name = "mapNewVisit")

    Map<Double, NewVisit> mapNewVisit;
    @Resource(name = "mapNewService")
    Map<Double, NewService> mapNewService;

    private DBFReader dbfReader;

    public DBFHelper() {

    }

    public void readFromP(String filename/*, Map<String, Human> humanMap*/) throws FileNotFoundException, DBFException {
        Object[] row;
        dbfReader = new DBFReader(new FileInputStream(filename));
        Map<String, Integer> fieldList = new HashMap<>();
        dbfReader.setCharactersetName("cp866");

        //Заполнение карты с полями, чтобы обращаться по имени поля
        for (Integer i = 0; i < dbfReader.getFieldCount(); i++) {
            DBFField field = dbfReader.getField(i);
            fieldList.put(field.getName(), i);
        }

        while ((row = dbfReader.nextRecord()) != null) {
            NewHuman human = mapNewHuman.get(((String) row[fieldList.get("ISTI")]).trim());
            if (human == null) {
                human = new NewHuman();
                human.setFio(((String) row[fieldList.get("FIO")]).trim());
                human.setIma(((String) row[fieldList.get("IMA")]).trim());
                human.setOtch(((String) row[fieldList.get("OTCH")]).trim());
                human.setPol(((String) row[fieldList.get("POL")]).trim());
                human.setDatr(((Date) row[fieldList.get("DATR")]));
                human.setKat(((String) row[fieldList.get("KAT")]).trim());
                human.setSnils(((String) row[fieldList.get("SNILS")]).trim());
                human.setcDoc(((Double) row[fieldList.get("C_DOC")]));
                human.setsDoc(((String) row[fieldList.get("S_DOC")]).trim());
                human.setnDoc(((String) row[fieldList.get("N_DOC")]).trim());
                human.setIsti(((String) row[fieldList.get("ISTI")]).trim());
                mapNewHuman.put(human.getIsti(), human);
            }
            NewVisit visit = new NewVisit();
            visit.setNs((Double) row[fieldList.get("NS")]);
            visit.setVs(((String) row[fieldList.get("VS")]).trim());
            visit.setDats((Date) row[fieldList.get("DATS")]);
            visit.setSn((Double) row[fieldList.get("SN")]);
            visit.setDatps((Date) row[fieldList.get("DATPS")]);
            visit.setCodeMo(((String) row[fieldList.get("CODE_MO")]).trim());
            visit.setPlOgrn(((String) row[fieldList.get("PL_OGRN")]).trim());
            visit.setOkatoOms(((String) row[fieldList.get("OKATO_OMS")]).trim());
            visit.setSpv((Double) row[fieldList.get("SPV")]);
            visit.setSps(((String) row[fieldList.get("SPS")]).trim());
            visit.setSpn(((String) row[fieldList.get("SPN")]).trim());
            visit.setStatP(((String) row[fieldList.get("STAT_P")]).trim());
            visit.setqG(((String) row[fieldList.get("Q_G")]).trim());
            visit.setNovor(((String) row[fieldList.get("NOVOR")]).trim());
            visit.setFamp(((String) row[fieldList.get("FAMP")]).trim());
            visit.setImp(((String) row[fieldList.get("IMP")]).trim());
            visit.setOtp(((String) row[fieldList.get("OTP")]).trim());
            visit.setPolp(((String) row[fieldList.get("POLP")]).trim());
            visit.setDatrp((Date) row[fieldList.get("DATRP")]);
            visit.setKsmo(((String) row[fieldList.get("KSMO")]).trim());
            visit.setNaprMo(((String) row[fieldList.get("NAPR_MO")]).trim());
            visit.setNaprN(((String) row[fieldList.get("NAPR_N")]).trim());
            visit.setDatn((Date) row[fieldList.get("DATN")]);
            visit.setDato((Date) row[fieldList.get("DATO")]);
            visit.setIshl(((String) row[fieldList.get("ISHL")]).trim());
            visit.setIshob(((String) row[fieldList.get("ISHOB")]).trim());
            visit.setMp(((String) row[fieldList.get("MP")]).trim());
            visit.setSummaI((Double) row[fieldList.get("SUMMA_I")]);
            visit.setPv(((String) row[fieldList.get("PV")]).trim());
            visit.setDvozvrat((Date) row[fieldList.get("DVOZVRAT")]);
            human.addVisit(visit);
            visit.setParent(human);
            mapNewVisit.put(visit.getSn(), visit);
        }
    }

    public void readFromU(String filename/*, Map<String, Human> humanMap*/) throws FileNotFoundException, DBFException {
        Object[] row;
        dbfReader = new DBFReader(new FileInputStream(filename));
        dbfReader.setCharactersetName("cp866");
        Map<String, Integer> fieldList = new HashMap<>();

        for (Integer i = 0; i < dbfReader.getFieldCount(); i++) {
            DBFField field = dbfReader.getField(i);
            fieldList.put(field.getName(), i);
        }


        while ((row = dbfReader.nextRecord()) != null) {
            NewVisit visit = mapNewVisit.get(((Double) row[fieldList.get("SN")]));
            if (visit != null) {
                NewService service = new NewService();
                service.setVisit(visit);
                visit.getServices().add(service);
                service.setUid(((Double) row[fieldList.get("UID")]));
                service.setCodeMo(((String) row[fieldList.get("CODE_MO")]).trim());
                service.setNs(((Double) row[fieldList.get("NS")]));
                service.setSn(((Double) row[fieldList.get("SN")]));
                service.setKotd(((String) row[fieldList.get("KOTD")]).trim());
                service.setKpk(((String) row[fieldList.get("KPK")]).trim());
                service.setMkbх(((String) row[fieldList.get("MKBX")]).trim());
                service.setMkbхs(((String) row[fieldList.get("MKBXS")]).trim());
                service.setKstand(((String) row[fieldList.get("KSTAND")]).trim());
                service.setVp(((String) row[fieldList.get("VP")]).trim());
                service.setKusl(((String) row[fieldList.get("KUSL")]).trim());
                service.setKolu(((Double) row[fieldList.get("KOLU")]));
                service.setKd(((Double) row[fieldList.get("KD")]));
                service.setDatn(((Date) row[fieldList.get("DATN")]));
                service.setDato(((Date) row[fieldList.get("DATO")]));
                service.setTaru(((Double) row[fieldList.get("TARU")]));
                service.setTaruB(((Double) row[fieldList.get("TARU_B")]));
                service.setTaruDm(((Double) row[fieldList.get("TARU_DM")]));
                service.setTaruD(((Double) row[fieldList.get("TARU_D")]));
                service.setTaruUc(((Double) row[fieldList.get("TARU_UC")]));
                service.setSumm(((Double) row[fieldList.get("SUMM")]));
                service.setSummB(((Double) row[fieldList.get("SUMM_B")]));
                service.setSummDm(((Double) row[fieldList.get("SUMM_DM")]));
                service.setSummD(((Double) row[fieldList.get("SUMM_D")]));
                service.setSummK(((Double) row[fieldList.get("SUMM_K")]));
                service.setSummUc(((Double) row[fieldList.get("SUMM_UC")]));
                service.setIsOut(((Double) row[fieldList.get("IS_OUT")]));
                service.setOutMo(((String) row[fieldList.get("OUT_MO")]).trim());
                service.setDocTabn(((String) row[fieldList.get("DOC_TABN")]).trim());
                service.setSpec(((String) row[fieldList.get("SPEC")]).trim());
                service.setProfil(((String) row[fieldList.get("PROFIL")]).trim());
                service.setVmp(((String) row[fieldList.get("VMP")]).trim());
                service.setKSO(((String) row[fieldList.get("KSO")]).trim());
                mapNewService.put(service.getUid(), service);
            }
        }

    }

}