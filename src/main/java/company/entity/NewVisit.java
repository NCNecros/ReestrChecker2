package company.entity;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс-сущность Посещение
 */
public class NewVisit {
    Set<NewService> services = new HashSet<>(0);
    private Long id;
    //номер реестра счетов (Номер счета и реестра счетов для медицинской организации уникальны в течение года.
    private Double ns;
    //тип реестра счетов (Медицинская организация проставляет значения 2, 4, 5, 6, 8, 9. После проверки реестра счетов Плательщик изменяет значение поля VS на 3, 7 для возвратных счетов.| Одинаково для всех персональных счетов в реестре.) SPR21
    private String vs;
    //дата формирования реестра счетов (дата формирования реестра счетов соответствует последнему числу отчетного месяца и совпадает с датой счета на бумажном носителе.; Одинаково для всех персональных счетов в реестре.)
    private Date dats;
    //номер персонального счета
    private Double sn;
    //Дата формирования персонального счета (При формировании повторного реестра дата формирования персонального счета остается неизменной и содержит в себе первоначальные значения персонального счета.)
    private Date datps;
    //код МО, оказавшей медицинскую помощь SPR01
    private String codeMo;
    //ОГРН плательщика (Одинаково для всех персональных счетов в реестре.) SPR02
    private String plOgrn;
    //код ОКАТО территории страхования по ОМС Да (для инокраевых) SPR39
    private String okatoOms;
    //тип документа, подтверждающего факт страхования по ОМС (Заполняется следующими значениями (1 - Полис ОМС старого образца; 2 - Временное свидетельство; 3 - Полис ОМС единого образца; 4 – Электронный полис ОМС единого образца; 5 – Полис ОМС в составе УЭК.)
    private Double spv;
    //серия документа, подтверждающего факт страхования по ОМС
    private String sps;
    //номер документа, подтверждающего факт страхования по ОМС (В поле SPN необходимо указывать: - для временных свидетельств девятизначный цифровой код (например, ХХХХХХХХХ, где Х – число от 0 до 9); - для полисов единого образца необходимо указывать шестнадцатизначный цифровой код (ХХХХХХХХХХХХХХХХ, где Х – число от 0 до 9).)
    private String spn;
    //статус представителя пациента SPR41
    @Deprecated
    private String statP;
    //признак "Особый случай" при регистрации  обращения за медицинской помощью (Признак "Особый случай" заполняется в соответствии со справочником, по шаблону: XXXX. Поле содержит до 5 особых случаев (максимальное количество). Если имеется одновременно несколько особых случаев, коды проставляются в порядке возрастания (от "1" до "6", например: 134). Если признак "Особый случай" отсутствует, то поле не заполняется.) SPR42
    private String qG;
    //признак новорожденного (Заполняется значением ПДДММГГН, где П - пол ребенка (1 – мужской, 2 - женский), ДДММГГ – дата рождения, Н – порядковый номер ребенка в случае двойни (тройни) (до двух знаков).)
    private String novor;
    //фамилия представителя пациента (Фамилия, имя, отчество записываются полностью буквами русского алфавита. Двойные фамилии, имена, отчества записываются через дефис (-) без пропусков или через один пробел, согласно написанию в предъявленном документе. Допускается использование знаков "-" (двойные фамилии, имена, составные отчества). Ошибочными считаются записи файла, в которых: - имя/фамилия не указаны (варианты заполнения "Нет", "Неизвестно", "Не идентифицирован" и т.п. равносильны пустому значению поля);  - отчество пациента (представителя) не указано, и его отсутствие не подтверждено соответствующим значением признака "Особый случай"; использован один из вариантов заполнения: "Нет", "Неизвестно", "Не идентифицирован", "Без отчества" и т.п., что приравнивается к пустому значению поля.) Да (для инокраевых, при statP <> “0”)
    private String famp;
    //имя представителя пациента (Фамилия, имя, отчество записываются полностью буквами русского алфавита. Двойные фамилии, имена, отчества записываются через дефис (-) без пропусков или через один пробел, согласно написанию в предъявленном документе. Допускается использование знаков "-" (двойные фамилии, имена, составные отчества). Ошибочными считаются записи файла, в которых: - имя/фамилия не указаны (варианты заполнения "Нет", "Неизвестно", "Не идентифицирован" и т.п. равносильны пустому значению поля);  - отчество пациента (представителя) не указано, и его отсутствие не подтверждено соответствующим значением признака "Особый случай"; использован один из вариантов заполнения: "Нет", "Неизвестно", "Не идентифицирован", "Без отчества" и т.п., что приравнивается к пустому значению поля.) Да (для инокраевых, при statP <> “0”)
    private String imp;
    //отчество представителя пациента (Фамилия, имя, отчество записываются полностью буквами русского алфавита. Двойные фамилии, имена, отчества записываются через дефис (-) без пропусков или через один пробел, согласно написанию в предъявленном документе. Допускается использование знаков "-" (двойные фамилии, имена, составные отчества). Ошибочными считаются записи файла, в которых: - имя/фамилия не указаны (варианты заполнения "Нет", "Неизвестно", "Не идентифицирован" и т.п. равносильны пустому значению поля);  - отчество пациента (представителя) не указано, и его отсутствие не подтверждено соответствующим значением признака "Особый случай"; использован один из вариантов заполнения: "Нет", "Неизвестно", "Не идентифицирован", "Без отчества" и т.п., что приравнивается к пустому значению поля.)
    private String otp;
    //пол представителя пациента (М/Ж) (Пол пациента заполняется прописными русскими буквами: М (мужской) или Ж (женский).) Да (для инокраевых, при statP <> “0”)
    private String polp;
    //дата рождения представителя пациента (Дата рождения пациента заполняется по документу, удостоверяющему личность, или по его полису ОМС.) Да (для инокраевых, при statP <> “0”)
    private Date datrp;
    //код страховщика (Краснодарского края) SPR03
    private String ksmo;
    //код направившей МО Да (по направлениям и для телемедицины) SPR01
    private String naprMo;
    //номер направления (Уникально в течение года. Заполняется значением ККККК_ ХХХХХХХ, где ККККК - код медицинской организации в системе ОМС, ХХХХХХХ – порядковый номер направления от 0 до 9999999.) Да (по направлениям на плановую госпититализацию)
    private String naprN;
    //дата начала лечения
    private Date datn;
    //дата окончания лечения
    private Date dato;
    //код исхода заболевания SPR11
    private String ishl;
    //код исхода обращения SPR12
    private String ishob;
    //код вида обращения SPR14
    private String mp;
    //сумма к оплате по ОМС по случаю заболевания пациента
    private Double summaI;
    //коды причин возврата (Заполняется для возвратных счетов в соответствии со справочником, по шаблону: XХХ_ХХX_XХХ_XХХ ("_" - символ пробела).) Да (для возвратных счетов) SPR15
    private String pv;
    //дата возврата Да (для возвратных счетов)
    private Date dvozvrat;
    //Человек которому принадлежит посещение
    private NewHuman parent;

    public Set<NewService> getServices() {
        return services;
    }

    public void setServices(Set<NewService> services) {
        this.services = services;
    }

    public Double getNs() {
        return ns;
    }

    public void setNs(Double ns) {
        this.ns = ns;
    }

    public String getVs() {
        return vs;
    }

    public void setVs(String vs) {
        this.vs = vs;
    }

    public Date getDats() {
        return dats;
    }

    public void setDats(Date dats) {
        this.dats = dats;
    }

    public Double getSn() {
        return sn;
    }

    public void setSn(Double sn) {
        this.sn = sn;
    }

    public Date getDatps() {
        return datps;
    }

    public void setDatps(Date datps) {
        this.datps = datps;
    }

    public String getCodeMo() {
        return codeMo;
    }

    public void setCodeMo(String codeMo) {
        this.codeMo = codeMo;
    }

    public String getPlOgrn() {
        return plOgrn;
    }

    public void setPlOgrn(String plOgrn) {
        this.plOgrn = plOgrn;
    }

    public String getOkatoOms() {
        return okatoOms;
    }

    public void setOkatoOms(String okatoOms) {
        this.okatoOms = okatoOms;
    }

    public Double getSpv() {
        return spv;
    }

    public void setSpv(Double spv) {
        this.spv = spv;
    }

    public String getSps() {
        return sps;
    }

    public void setSps(String sps) {
        this.sps = sps;
    }

    public String getSpn() {
        return spn;
    }

    public void setSpn(String spn) {
        this.spn = spn;
    }

    public String getStatP() {
        return statP;
    }

    public void setStatP(String statP) {
        this.statP = statP;
    }

    public String getqG() {
        return qG;
    }

    public void setqG(String qG) {
        this.qG = qG;
    }

    public String getNovor() {
        return novor;
    }

    public void setNovor(String novor) {
        this.novor = novor;
    }

    public String getFamp() {
        return famp;
    }

    public void setFamp(String famp) {
        this.famp = famp;
    }

    public String getImp() {
        return imp;
    }

    public void setImp(String imp) {
        this.imp = imp;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getPolp() {
        return polp;
    }

    public void setPolp(String polp) {
        this.polp = polp;
    }

    public Date getDatrp() {
        return datrp;
    }

    public void setDatrp(Date datrp) {
        this.datrp = datrp;
    }

    public String getKsmo() {
        return ksmo;
    }

    public void setKsmo(String ksmo) {
        this.ksmo = ksmo;
    }

    public String getNaprMo() {
        return naprMo;
    }

    public void setNaprMo(String naprMo) {
        this.naprMo = naprMo;
    }

    public String getNaprN() {
        return naprN;
    }

    public void setNaprN(String naprN) {
        this.naprN = naprN;
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

    public String getIshl() {
        return ishl;
    }

    public void setIshl(String ishl) {
        this.ishl = ishl;
    }

    public String getIshob() {
        return ishob;
    }

    public void setIshob(String ishob) {
        this.ishob = ishob;
    }

    public String getMp() {
        return mp;
    }

    public void setMp(String mp) {
        this.mp = mp;
    }

    public Double getSummaI() {
        return summaI;
    }

    public void setSummaI(Double summaI) {
        this.summaI = summaI;
    }

    public String getPv() {
        return pv;
    }

    public void setPv(String pv) {
        this.pv = pv;
    }

    public Date getDvozvrat() {
        return dvozvrat;
    }

    public void setDvozvrat(Date dvozvrat) {
        this.dvozvrat = dvozvrat;
    }

    public NewHuman getParent() {
        return parent;
    }

    public void setParent(NewHuman parent) {
        this.parent = parent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDoctor() {
        String doc = "";
        List<String> docs = getServices().stream().map(NewService::getDocTabn).collect(Collectors.toList());
        if (docs.size() > 0) {
            doc = docs.get(0);
        }
        return doc;
    }

    public String getMKB() {
        List<String> mkbs = getServices().stream().map(NewService::getMkbх).collect(Collectors.toList());
        return mkbs.size() > 0 ? mkbs.get(0) : "";
    }

    public void addService(NewService service){
        services.add(service);
        service.setVisit(this);
    }

    public String getReadableDatN() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(datn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ns, vs, dats, sn, datps, codeMo, plOgrn, okatoOms, spv, sps, spn, statP, qG, novor, famp, imp, otp, polp, datrp, ksmo, naprMo, naprN, datn, dato, ishl, ishob, mp, summaI, pv, dvozvrat);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        NewVisit otherVisit = (NewVisit) obj;
        return Objects.equals(id, otherVisit.getId())
                && Objects.equals(ns, otherVisit.getNs())
                && Objects.equals(vs, otherVisit.getVs())
                && Objects.equals(dats, otherVisit.getDats())
                && Objects.equals(sn, otherVisit.getSn())
                && Objects.equals(datps,otherVisit.getDatps())
                && Objects.equals(codeMo, otherVisit.getCodeMo())
                && Objects.equals(plOgrn, otherVisit.getPlOgrn())
                && Objects.equals(okatoOms, otherVisit.getOkatoOms())
                && Objects.equals(spv, otherVisit.getSpv())
                && Objects.equals(sps, otherVisit.getSps())
                && Objects.equals(spn, otherVisit.getSpn())
                && Objects.equals(statP, otherVisit.getStatP())
                && Objects.equals(qG, otherVisit.getqG())
                && Objects.equals(novor, otherVisit.getNovor())
                && Objects.equals(famp, otherVisit.getFamp())
                && Objects.equals(imp, otherVisit.getImp())
                && Objects.equals(otp, otherVisit.getOtp())
                && Objects.equals(polp, otherVisit.getPolp())
                && Objects.equals(datrp, otherVisit.getDatrp())
                && Objects.equals(ksmo, otherVisit.getKsmo())
                && Objects.equals(naprMo, otherVisit.getNaprMo())
                && Objects.equals(naprN, otherVisit.getNaprN())
                && Objects.equals(datn, otherVisit.getDatn())
                && Objects.equals(dato, otherVisit.getDato())
                && Objects.equals(ishl, otherVisit.getIshl())
                && Objects.equals(ishob, otherVisit.getIshob())
                && Objects.equals(mp, otherVisit.getMp())
                && Objects.equals(summaI, otherVisit.getSummaI())
                && Objects.equals(pv, otherVisit.getPv())
                && Objects.equals(dvozvrat, otherVisit.getDvozvrat());
    }
}
