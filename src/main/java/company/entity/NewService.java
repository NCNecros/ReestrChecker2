package company.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Necros on 06.07.2015.
 */
@Entity
@Table(name = "service")
public class NewService {
    //уникальный номер записи об оказанной медицинской услуге в пределах реестра (Уникальный номер записи об оказанной медицинской услуге используется для идентификации записи о медицинской услуге в пределах реестра, для повторных реестров должен совпадать с первоначальным номером.)
    @Column
    private Double uid;

    //код МО, оказавшей медицинскую помощь SPR01
    @Column
    private String codeMo;

    //номер реестра счетов
    @Column
    private Double ns;

    //номер персонального счета
    @Column
    private Double sn;

    //код отделения (Для услуг, выполненных вне данной МО (IS_OUT = “1”),  все обязательные реквизиты (код отделения, код профиля койки, код диагноза основного, и т.п.) заполняются данными на момент обращения в МО. , Заполняется данными из поля DOC_TABN на основании данных из файла DXXXXX.) SPR07
    @Column
    private String kotd;

    //код профиля койки (Для услуг, выполненных вне данной МО (IS_OUT = “1”),  все обязательные реквизиты (код отделения, код профиля койки, код диагноза основного, и т.п.) заполняются данными на момент обращения в МО. ) Да (для стационаров) SPR08
    @Column
    private String kpk;

    //код диагноза основного заболевания по МКБ–Х (Для услуг, выполненных вне данной МО (IS_OUT = “1”),  все обязательные реквизиты (код отделения, код профиля койки, код диагноза основного, и т.п.) заполняются данными на момент обращения в МО. ) Да (кроме диагностических услуг) SPR20
    @Column
    private String mkbх;

    //код диагноза сопутствующего заболевания по МКБ–Х (Для услуг, выполненных вне данной МО (IS_OUT = “1”),  все обязательные реквизиты (код отделения, код профиля койки, код диагноза основного, и т.п.) заполняются данными на момент обращения в МО. ) SPR20
    @Column
    private String mkbхs;

    //код стандарта оказания медицинской помощи Да (для  нозологий, у которых разработан СОМП) SPR38
    @Column
    private String kstand;

    //код условия оказания медицинской помощи (Для услуг, выполненных вне данной МО (IS_OUT = “1”),  все обязательные реквизиты (код отделения, код профиля койки, код диагноза основного, и т.п.) заполняются данными на момент обращения в МО. ) SPR13
    @Column
    private String vp;

    //код медицинской услуги SPR18
    @Column
    private String kusl;

    //количество услуг
    @Column
    private Double kolu;

    //количество койко-дней (дней лечения) Да (для стационаров всех типов)
    @Column
    private Double kd;

    //дата начала выполнения услуги
    @Column
    private Date datn;

    //дата окончания выполнения услуги
    @Column
    private Date dato;

    //тариф на оплату по ОМС (Тариф на оплату по ОМС состоит из суммы базовой части тарифа услуги по ОМС, тарифа на оплату дополнительных статей расходов и тарифа стимулирующих выплат медицинскому персоналу участковой службы по ОМС.) SPR22
    @Column
    private Double taru;

    //базовая часть тарифа услуги по ОМС  SPR22
    @Column
    private Double taruB;

    //в т.ч. доплата по выполнению СОМП и повышению доступности АПМП SPR22
    @Column
    private Double taruDm;

    //тариф на оплату дополнительных статей расходов по ОМС SPR22
    @Column
    private Double taruD;

    //тариф стимулирующих выплат медицинскому персоналу участковой службы по ОМС SPR22
    @Column
    private Double taruUc;

    //сумма к оплате по ОМС
    @Column
    private Double summ;

    //_B сумма по базовой части тарифа к оплате по ОМС
    @Column
    private Double summB;

    // в т.ч. доплата по выполнению СОМП и повышению доступности АПМП
    @Column
    private Double summDm;

    //сумма дополнительных статей расходов к оплате по ОМС
    @Column
    private Double summD;

    //сумма расходов на оплату коммунальных услуг (Расчетное значение на основании утвержденных справочников SPR22, SPR58, на момент окончания услуги, рассчитывается по формуле: Sk = (T * K)окр * KK, где: T – базовая часть тарифа медицинской услуги по ОМС (SPR22), K – коэффициент для расходов на коммунальные услуги (SPR58),  KK – количество услуг.)
    @Column
    private Double summK;

    //сумма расходов на оплату стимулирующих выплат медицинскому персоналу участковой службы по ОМС (Расчетное значение на основании утвержденных справочников SPR22, SPR66, на момент окончания услуги, рассчитывается по формуле: Sk = (T * K)окр * KK, где: T – базовая часть тарифа медицинской услуги по ОМС (SPR22), K – коэффициент для расходов на стимулирующие выплаты медицинскому персоналу участковой службы (SPR66), KK – количество услуг.)
    @Column
    private Double summUc;

    //признак: услуга оказана в другой МО (Поле содержит значение “1”, если услуга оказана в другом медицинском учреждении, значение “0” в остальных случаях.)
    @Column
    private Double isOut;

    //код МО, оказавшей услугу (Указывается код медицинской организации, выполнившей внешнюю услугу. Заполняется для тех случаев, если IS_OUT = “1”.) SPR01
    @Column
    private String outMo;

    //табельный номер сотрудника, оказавшего услугу (Заполняется данными из поля DOC_TABN на основании данных из файла DXXXXX.) Да (для услуг с тарифом ОМС больше 0, а также для посещений врача стоматолога,  ФАПов и ССМП)
    @Column
    private String docTabn;

    //код специальности специалиста, оказавшего услугу SPR46
    @Column
    private String spec;

    //профиль оказанной медицинской помощи SPR60
    @Column
    private String profil;

    //вид медицинской помощи SPR59
    @Column
    private String vmp;

    //способ оплаты медицинской помощи SPR17
    @Column
    private String KSO;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "service_id")
    private Long id;

    public Double getUid() {
        return uid;
    }

    public void setUid(Double uid) {
        this.uid = uid;
    }

    public String getCodeMo() {
        return codeMo;
    }

    public void setCodeMo(String codeMo) {
        this.codeMo = codeMo;
    }

    public Double getNs() {
        return ns;
    }

    public void setNs(Double ns) {
        this.ns = ns;
    }

    public Double getSn() {
        return sn;
    }

    public void setSn(Double sn) {
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

    public Double getKolu() {
        return kolu;
    }

    public void setKolu(Double kolu) {
        this.kolu = kolu;
    }

    public Double getKd() {
        return kd;
    }

    public void setKd(Double kd) {
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

    public Double getTaruB() {
        return taruB;
    }

    public void setTaruB(Double taruB) {
        this.taruB = taruB;
    }

    public Double getTaruDm() {
        return taruDm;
    }

    public void setTaruDm(Double taruDm) {
        this.taruDm = taruDm;
    }

    public Double getTaruD() {
        return taruD;
    }

    public void setTaruD(Double taruD) {
        this.taruD = taruD;
    }

    public Double getTaruUc() {
        return taruUc;
    }

    public void setTaruUc(Double taruUc) {
        this.taruUc = taruUc;
    }

    public Double getSumm() {
        return summ;
    }

    public void setSumm(Double summ) {
        this.summ = summ;
    }

    public Double getSummB() {
        return summB;
    }

    public void setSummB(Double summB) {
        this.summB = summB;
    }

    public Double getSummDm() {
        return summDm;
    }

    public void setSummDm(Double summDm) {
        this.summDm = summDm;
    }

    public Double getSummD() {
        return summD;
    }

    public void setSummD(Double summD) {
        this.summD = summD;
    }

    public Double getSummK() {
        return summK;
    }

    public void setSummK(Double summK) {
        this.summK = summK;
    }

    public Double getSummUc() {
        return summUc;
    }

    public void setSummUc(Double summUc) {
        this.summUc = summUc;
    }

    public Double getIsOut() {
        return isOut;
    }

    public void setIsOut(Double isOut) {
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

    @ManyToOne
    @JoinColumn(name = "visit_id")
    private NewVisit visit;

}
