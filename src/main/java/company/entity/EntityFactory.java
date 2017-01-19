package company.entity;

import company.Spr69Value;
import org.jamel.dbf.structure.DbfRow;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * Фабрика сущностей
 */
public class EntityFactory {

    public static final Charset CHARSET = Charset.forName("cp866");

    public static NewHuman buildHumanFromDbfRow(DbfRow row) {
        NewHuman human = new NewHuman();
        human.setFio(row.getString("FIO", CHARSET));
        human.setIma(row.getString("IMA", CHARSET));
        human.setOtch(row.getString("OTCH", CHARSET));
        human.setPol(row.getString("POL", CHARSET));
        human.setDatr(getDate(row,"DATR"));
        human.setKat(row.getString("KAT", CHARSET));
        human.setSnils(row.getString("SNILS", CHARSET));
        human.setcDoc(row.getInt("C_DOC"));
        human.setsDoc(row.getString("S_DOC", CHARSET));
        human.setnDoc(row.getString("N_DOC", CHARSET));
        human.setIsti(row.getString("ISTI", CHARSET));
        return human;
    }

    public static Spr69Value buildSpr69ValueFromRow(DbfRow row) {
        Spr69Value value = new Spr69Value();
        value.setVpname(row.getString("VPNAME", CHARSET));
        value.setMkbx(row.getString("MKBX", CHARSET));
        value.setMkbx2(row.getString("MKBX2", CHARSET));
        value.setKusl(row.getString("KUSL", CHARSET));
        value.setAge(row.getString("AGE", CHARSET));
        value.setPol(row.getString("POL", CHARSET));
        value.setDlit(row.getString("DLIT", CHARSET));
        value.setKsgcode(row.getString("KSGCODE", CHARSET));
        value.setKsgkoef(row.getDouble("KSGKOEF"));
        value.setDatn(getDate(row,"DATN"));
        value.setDato(getDate(row,"DATO"));
        return value;
    }

    public static Doctor buildDoctorFromDbfRow(DbfRow row) {
        Doctor doctor = new Doctor();
        doctor.setCodeMo(row.getString("CODE_MO", CHARSET));
        doctor.setDocTabn(row.getString("DOC_TABN", CHARSET));
        doctor.setSnils(row.getString("SNILS", CHARSET));
        doctor.setFio(row.getString("FIO", CHARSET));
        doctor.setIma(row.getString("IMA", CHARSET));
        doctor.setOtch(row.getString("OTCH", CHARSET));
        doctor.setPol(row.getString("POL", CHARSET));
        doctor.setDatr(getDate(row,"DATR"));
        doctor.setDatn(getDate(row, "DATN"));
        doctor.setDato(getDate(row, "DATO"));
        return doctor;
    }

    private static Date getDate(DbfRow row, String fieldName) {
        Date date = row.getDate(fieldName);
        if (date.getTime() == -623616120000000L) {
            return null;
        }
        return date;
    }

    public static NewVisit buildVisitFromRow(DbfRow row) {
        NewVisit visit = new NewVisit();
        visit.setNs(row.getInt("NS"));
        visit.setVs(row.getString("VS", CHARSET));
        visit.setDats(getDate(row,"DATS"));
        visit.setSn(row.getInt("SN"));
        visit.setDatps(getDate(row,"DATPS"));
        visit.setCodeMo(row.getString("CODE_MO", CHARSET));
        visit.setPlOgrn(row.getString("PL_OGRN", CHARSET));
        visit.setOkatoOms(row.getString("OKATO_OMS", CHARSET));
        visit.setSpv(row.getInt("SPV"));
        visit.setSps(row.getString("SPS", CHARSET));
        visit.setSpn(row.getString("SPN", CHARSET));
//        visit.setStatP(row.getString("STAT_P",CHARSET));
        visit.setqG(row.getString("Q_G", CHARSET));
        visit.setNovor(row.getString("NOVOR", CHARSET));
        visit.setFamp(row.getString("FAMP", CHARSET));
        visit.setImp(row.getString("IMP", CHARSET));
        visit.setOtp(row.getString("OTP", CHARSET));
        visit.setPolp(row.getString("POLP", CHARSET));
        visit.setDatrp(getDate(row,"DATRP"));
        visit.setNaprMo(row.getString("NAPR_MO", CHARSET));
        visit.setNaprN(row.getString("NAPR_N", CHARSET));
        visit.setDatn(getDate(row,"DATN"));
        visit.setDato(getDate(row,"DATO"));
        visit.setIshl(row.getString("ISHL", CHARSET));
        visit.setIshob(row.getString("ISHOB", CHARSET));
        visit.setMp(row.getString("MP", CHARSET));
        visit.setSummaI(row.getDouble("SUMMA_I"));
        visit.setPv(row.getString("PV", CHARSET));
        visit.setDvozvrat(getDate(row,"DVOZVRAT"));
        return visit;
    }

    public static NewService buildServiceFromRow(DbfRow row) {
        NewService service = new NewService();
        service.setUid(row.getInt("UID"));
        service.setCodeMo(row.getString("CODE_MO", CHARSET));
        service.setNs(row.getInt("NS"));
        service.setSn(row.getInt("SN"));
        service.setKotd(row.getString("KOTD", CHARSET));
        service.setKpk(row.getString("KPK", CHARSET));
        service.setMkbх(row.getString("MKBX", CHARSET));
        service.setMkbхs(row.getString("MKBXS", CHARSET));

//        service.setKstand(row.getString("KSTAND", CHARSET)); //deprecated
        service.setVp(row.getString("VP", CHARSET));
        service.setKusl(row.getString("KUSL", CHARSET));
        service.setKolu(row.getInt("KOLU"));
        service.setKd(row.getInt("KD"));
        service.setDatn(getDate(row,"DATN"));
        service.setDato(getDate(row,"DATO"));
        service.setTaru(row.getDouble("TARU"));
        service.setSumm(row.getDouble("SUMM"));
        service.setIsOut(row.getInt("IS_OUT"));
        service.setOutMo(row.getString("OUT_MO", CHARSET));
        service.setDocTabn(row.getString("DOC_TABN", CHARSET));
        service.setSpec(row.getString("SPEC", CHARSET));
        service.setProfil(row.getString("PROFIL", CHARSET));
        service.setVmp(row.getString("VMP", CHARSET));
        service.setKSO(row.getString("KSO", CHARSET));
        return service;
    }
}
