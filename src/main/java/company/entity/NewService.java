package company.entity;

import java.util.Date;
import java.util.Objects;

public class NewService {
    //уникальный номер записи об оказанной медицинской услуге в пределах реестра (Уникальный номер записи об оказанной медицинской услуге используется для идентификации записи о медицинской услуге в пределах реестра, для повторных реестров должен совпадать с первоначальным номером.)
    private Integer uid;

    //код МО, оказавшей медицинскую помощь SPR01
    private String codeMo;

    //номер реестра счетов
    private Integer ns;

    //номер персонального счета
    private Integer sn;

    //код отделения (Для услуг, выполненных вне данной МО (IS_OUT = “1”),  все обязательные реквизиты (код отделения, код профиля койки, код диагноза основного, и т.п.) заполняются данными на момент обращения в МО. , Заполняется данными из поля DOC_TABN на основании данных из файла DXXXXX.) SPR07
    private String kotd;

    //код профиля койки (Для услуг, выполненных вне данной МО (IS_OUT = “1”),  все обязательные реквизиты (код отделения, код профиля койки, код диагноза основного, и т.п.) заполняются данными на момент обращения в МО. ) Да (для стационаров) SPR08
    private String kpk;

    //код диагноза основного заболевания по МКБ–Х (Для услуг, выполненных вне данной МО (IS_OUT = “1”),  все обязательные реквизиты (код отделения, код профиля койки, код диагноза основного, и т.п.) заполняются данными на момент обращения в МО. ) Да (кроме диагностических услуг) SPR20
    private String mkbх;

    //код диагноза сопутствующего заболевания по МКБ–Х (Для услуг, выполненных вне данной МО (IS_OUT = “1”),  все обязательные реквизиты (код отделения, код профиля койки, код диагноза основного, и т.п.) заполняются данными на момент обращения в МО. ) SPR20
    private String mkbхs;

    //код стандарта оказания медицинской помощи Да (для  нозологий, у которых разработан СОМП) SPR38
    private String kstand;

    //код условия оказания медицинской помощи (Для услуг, выполненных вне данной МО (IS_OUT = “1”),  все обязательные реквизиты (код отделения, код профиля койки, код диагноза основного, и т.п.) заполняются данными на момент обращения в МО. ) SPR13
    private String vp;

    //код медицинской услуги SPR18
    private String kusl;

    //количество услуг
    private Integer kolu;

    //количество койко-дней (дней лечения) Да (для стационаров всех типов)
    private Integer kd;

    //дата начала выполнения услуги
    private Date datn;

    //дата окончания выполнения услуги
    private Date dato;

    //тариф на оплату по ОМС (Тариф на оплату по ОМС состоит из суммы базовой части тарифа услуги по ОМС, тарифа на оплату дополнительных статей расходов и тарифа стимулирующих выплат медицинскому персоналу участковой службы по ОМС.) SPR22
    private Double taru;


    //сумма к оплате по ОМС
    private Double summ;

    //признак: услуга оказана в другой МО (Поле содержит значение “1”, если услуга оказана в другом медицинском учреждении, значение “0” в остальных случаях.)
    private Integer isOut;

    //код МО, оказавшей услугу (Указывается код медицинской организации, выполнившей внешнюю услугу. Заполняется для тех случаев, если IS_OUT = “1”.) SPR01
    private String outMo;

    //табельный номер сотрудника, оказавшего услугу (Заполняется данными из поля DOC_TABN на основании данных из файла DXXXXX.) Да (для услуг с тарифом ОМС больше 0, а также для посещений врача стоматолога,  ФАПов и ССМП)
    private String docTabn;

    //код специальности специалиста, оказавшего услугу SPR46
    private String spec;

    //профиль оказанной медицинской помощи SPR60
    private String profil;

    //вид медицинской помощи SPR59
    private String vmp;

    //способ оплаты медицинской помощи SPR17
    private String KSO;

    private Long id;
    private NewVisit visit;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getCodeMo() {
        return codeMo;
    }

    public void setCodeMo(String codeMo) {
        this.codeMo = codeMo;
    }

    public Integer getNs() {
        return ns;
    }

    public void setNs(Integer ns) {
        this.ns = ns;
    }

    public Integer getSn() {
        return sn;
    }

    public void setSn(Integer sn) {
        this.sn = sn;
    }

    public String getKotd() {
        return kotd;
    }

    public void setKotd(String kotd) {
        this.kotd = kotd;
    }

    public String getKpk() {
        return kpk;
    }

    public void setKpk(String kpk) {
        this.kpk = kpk;
    }

    public String getMkbх() {
        return mkbх;
    }

    public void setMkbх(String mkbх) {
        this.mkbх = mkbх;
    }

    public String getMkbхs() {
        return mkbхs;
    }

    public void setMkbхs(String mkbхs) {
        this.mkbхs = mkbхs;
    }

    public String getKstand() {
        return kstand;
    }

    public void setKstand(String kstand) {
        this.kstand = kstand;
    }

    public String getVp() {
        return vp;
    }

    public void setVp(String vp) {
        this.vp = vp;
    }

    public String getKusl() {
        return kusl;
    }

    public void setKusl(String kusl) {
        this.kusl = kusl;
    }

    public Integer getKolu() {
        return kolu;
    }

    public void setKolu(Integer kolu) {
        this.kolu = kolu;
    }

    public Integer getKd() {
        return kd;
    }

    public void setKd(Integer kd) {
        this.kd = kd;
    }

    public Date getDatn() {
        return datn;
    }

    public void setDatn(Date datn) {
        this.datn = datn;
    }

    public Date getDato() {
        return dato;
    }

    public void setDato(Date dato) {
        this.dato = dato;
    }

    public Double getTaru() {
        return taru;
    }

    public void setTaru(Double taru) {
        this.taru = taru;
    }


    public Double getSumm() {
        return summ;
    }

    public void setSumm(Double summ) {
        this.summ = summ;
    }

    public Integer getIsOut() {
        return isOut;
    }

    public void setIsOut(Integer isOut) {
        this.isOut = isOut;
    }

    public String getOutMo() {
        return outMo;
    }

    public void setOutMo(String outMo) {
        this.outMo = outMo;
    }

    public String getDocTabn() {
        return docTabn;
    }

    public void setDocTabn(String docTabn) {
        this.docTabn = docTabn;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public String getVmp() {
        return vmp;
    }

    public void setVmp(String vmp) {
        this.vmp = vmp;
    }

    public String getKSO() {
        return KSO;
    }

    public void setKSO(String KSO) {
        this.KSO = KSO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NewVisit getVisit() {
        return visit;
    }

    public void setVisit(NewVisit visit) {
        this.visit = visit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, codeMo, ns, sn, kotd, kpk, mkbх, mkbхs, kstand, vp, kusl, kolu, kd, datn, dato,
                taru,  summ, isOut, outMo,
                docTabn, spec, profil, vmp, KSO, id, visit);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        NewService other = (NewService) obj;
        return Objects.equals(codeMo, other.getCodeMo())
                && Objects.equals(ns, other.getNs())
                && Objects.equals(sn, other.getSn())
                && Objects.equals(kotd, other.getKotd())
                && Objects.equals(kpk, other.getKpk())
                && Objects.equals(mkbх, other.getMkbх())
                && Objects.equals(mkbхs, other.getMkbхs())
                && Objects.equals(kstand, other.getKstand())
                && Objects.equals(vp, other.getVp())
                && Objects.equals(kusl, other.getKusl())
                && Objects.equals(kolu, other.getKolu())
                && Objects.equals(kd, other.getKd())
                && Objects.equals(datn, other.getDatn())
                && Objects.equals(dato, other.getDato())
                && Objects.equals(taru, other.getTaru())
                && Objects.equals(summ, other.getSumm())
                && Objects.equals(isOut, other.getIsOut())
                && Objects.equals(outMo, other.getOutMo())
                && Objects.equals(docTabn, other.getDocTabn())
                && Objects.equals(spec, other.getSpec())
                && Objects.equals(profil, other.getProfil())
                && Objects.equals(vmp, other.getVmp())
                && Objects.equals(KSO, other.getKSO())
                && Objects.equals(visit, other.getVisit());
    }
}
