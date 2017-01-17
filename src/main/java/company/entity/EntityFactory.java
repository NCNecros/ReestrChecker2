package company.entity;

import company.Spr69Value;

import java.util.Date;
import java.util.Map;

/**
 * Фабрика сущностей
 */
public class EntityFactory {
    public static NewHuman buildHumanFromDbfRow(Object[] row, Map<String, Integer> fieldList) {
        NewHuman human = new NewHuman();
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
        return human;
    }

    public static Spr69Value buildSpr69ValueFromRow(Object[] row, Map<String, Integer> fieldList) {
        Spr69Value value = new Spr69Value();
        value.setVpname(((String) row[fieldList.get("VPNAME")]).trim());
        value.setMkbx(((String) row[fieldList.get("MKBX")]).trim());
        value.setMkbx2(((String) row[fieldList.get("MKBX2")]).trim());
        value.setKusl(((String) row[fieldList.get("KUSL")]).trim());
        value.setAge(((String) row[fieldList.get("AGE")]).trim());
        value.setPol(((String) row[fieldList.get("POL")]).trim());
        value.setDlit(((String) row[fieldList.get("DLIT")]).trim());
        value.setKsgcode(((String) row[fieldList.get("KSGCODE")]).trim());
        value.setKsgkoef(((Double) row[fieldList.get("KSGKOEF")]));
        value.setDatn(((Date) row[fieldList.get("DATN")]));
        value.setDato(((Date) row[fieldList.get("DATO")]));
        return value;
    }

    public static Doctor buildDoctorFromDbfRow(Object[] row, Map<String, Integer> fieldList) {
        Doctor doctor = new Doctor();
        doctor.setCodeMo(((String) row[fieldList.get("CODE_MO")]).trim());
        doctor.setDocTabn(((String) row[fieldList.get("DOC_TABN")]).trim());
        doctor.setSnils(((String) row[fieldList.get("SNILS")]).trim());
        doctor.setFio(((String) row[fieldList.get("FIO")]).trim());
        doctor.setIma(((String) row[fieldList.get("IMA")]).trim());
        doctor.setOtch(((String) row[fieldList.get("OTCH")]).trim());
        doctor.setPol(((String) row[fieldList.get("POL")]).trim());
        doctor.setDatr(((Date) row[fieldList.get("DATR")]));
        doctor.setDatn(((Date) row[fieldList.get("DATN")]));
        doctor.setDato(((Date) row[fieldList.get("DATO")]));
        return doctor;
    }

    public static NewVisit buildVisitFromRow(Object[] row, Map<String, Integer> fieldList) {
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
//        visit.setStatP(((String) row[fieldList.get("STAT_P")]).trim());
        visit.setqG(((String) row[fieldList.get("Q_G")]).trim());
        visit.setNovor(((String) row[fieldList.get("NOVOR")]).trim());
        visit.setFamp(((String) row[fieldList.get("FAMP")]).trim());
        visit.setImp(((String) row[fieldList.get("IMP")]).trim());
        visit.setOtp(((String) row[fieldList.get("OTP")]).trim());
        visit.setPolp(((String) row[fieldList.get("POLP")]).trim());
        visit.setDatrp((Date) row[fieldList.get("DATRP")]);
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
        return visit;
    }

    public static NewService buildServiceFromRow(Object[] row, Map<String, Integer> fieldList) {
        NewService service = new NewService();
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
        service.setSumm(((Double) row[fieldList.get("SUMM")]));
        service.setIsOut(((Double) row[fieldList.get("IS_OUT")]));
        service.setOutMo(((String) row[fieldList.get("OUT_MO")]).trim());
        service.setDocTabn(((String) row[fieldList.get("DOC_TABN")]).trim());
        service.setSpec(((String) row[fieldList.get("SPEC")]).trim());
        service.setProfil(((String) row[fieldList.get("PROFIL")]).trim());
        service.setVmp(((String) row[fieldList.get("VMP")]).trim());
        service.setKSO(((String) row[fieldList.get("KSO")]).trim());
        return service;
    }
}
