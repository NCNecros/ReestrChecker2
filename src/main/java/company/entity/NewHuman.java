package company.entity;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Класс-сущность Человек
 */
public class NewHuman implements Comparable<NewHuman> {

    private Long id;
    //фамилия (Фамилия, имя, отчество записываются полностью буквами русского алфавита. Двойные фамилии, имена, отчества записываются через дефис (-) без пропусков или через один пробел, согласно написанию в предъявленном документе. Допускается использование знаков "-" (двойные фамилии, имена, составные отчества). Ошибочными считаются записи файла, в которых: - имя/фамилия не указаны (варианты заполнения "Нет", "Неизвестно", "Не идентифицирован" и т.п. равносильны пустому значению поля);  - отчество пациента (представителя) не указано, и его отсутствие не подтверждено соответствующим значением признака "Особый случай"; использован один из вариантов заполнения: "Нет", "Неизвестно", "Не идентифицирован", "Без отчества" и т.п., что приравнивается к пустому значению поля.)
    private String fio = "";
    //имя (Фамилия, имя, отчество записываются полностью буквами русского алфавита. Двойные фамилии, имена, отчества записываются через дефис (-) без пропусков или через один пробел, согласно написанию в предъявленном документе. Допускается использование знаков "-" (двойные фамилии, имена, составные отчества). Ошибочными считаются записи файла, в которых: - имя/фамилия не указаны (варианты заполнения "Нет", "Неизвестно", "Не идентифицирован" и т.п. равносильны пустому значению поля);  - отчество пациента (представителя) не указано, и его отсутствие не подтверждено соответствующим значением признака "Особый случай"; использован один из вариантов заполнения: "Нет", "Неизвестно", "Не идентифицирован", "Без отчества" и т.п., что приравнивается к пустому значению поля.)
    private String ima = "";
    //отчество (Фамилия, имя, отчество записываются полностью буквами русского алфавита. Двойные фамилии, имена, отчества записываются через дефис (-) без пропусков или через один пробел, согласно написанию в предъявленном документе. Допускается использование знаков "-" (двойные фамилии, имена, составные отчества). Ошибочными считаются записи файла, в которых: - имя/фамилия не указаны (варианты заполнения "Нет", "Неизвестно", "Не идентифицирован" и т.п. равносильны пустому значению поля);  - отчество пациента (представителя) не указано, и его отсутствие не подтверждено соответствующим значением признака "Особый случай"; использован один из вариантов заполнения: "Нет", "Неизвестно", "Не идентифицирован", "Без отчества" и т.п., что приравнивается к пустому значению поля.)
    private String otch = "";
    //пол (М/Ж) (Пол пациента заполняется прописными русскими буквами: М (мужской) или Ж (женский).)
    private String pol = "";
    //дата рождения (Дата рождения пациента заполняется по документу, удостоверяющему личность, или по его полису ОМС.)
    private Date datr;
    //категория граждан SPR09
    private String kat;
    //СНИЛС
    private String snils;
    //код типа документа, удостоверяющего личность (Код типа документа, серия и номер документа, удостоверяющего личность застрахованного по ОМС или представителя пациента (который может быть не застрахован по ОМС, но может представлять интересы пациента), заполняются в соответствии с кодификатором и шаблонами, приведенными в справочнике. Да (для инокраевых). При указании ЕНП может не заполняться. SPR43
    private Integer cDoc;
    //серия документа, удостоверяющего личность (Код типа документа, серия и номер документа, удостоверяющего личность застрахованного по ОМС или представителя пациента (который может быть не застрахован по ОМС, но может представлять интересы пациента), заполняются в соответствии с кодификатором и шаблонами, приведенными в справочнике. ) Да (для инокраевых, согласно SPR43).  При указании ЕНП может не заполняться.
    private String sDoc;
    //номер документа, удостоверяющего личность (Код типа документа, серия и номер документа, удостоверяющего личность застрахованного по ОМС или представителя пациента (который может быть не застрахован по ОМС, но может представлять интересы пациента), заполняются в соответствии с кодификатором и шаблонами, приведенными в справочнике. ) Да (для инокраевых, согласно SPR43).  При указании ЕНП может не заполняться.
    private String nDoc;
    //номер амбулаторной карты или истории болезни
    private String isti = "";
    //Визиты
    private Map<Integer, NewVisit> visits = new LinkedHashMap<>();

    @Override
    public String toString() {
//        return "NewHuman{" +
//                "fio='" + fio + '\'' +
//                ", ima='" + ima + '\'' +
//                ", otch='" + otch + '\'' +
//                ", pol='" + pol + '\'' +
//                ", datr=" + datr +
//                ", kat='" + kat + '\'' +
//                ", snils='" + snils + '\'' +
//                ", cDoc=" + cDoc +
//                ", sDoc='" + sDoc + '\'' +
//                ", nDoc='" + nDoc + '\'' +
//                ", isti='" + isti + '\'' +
//                '}';
        return MoreObjects.toStringHelper(this)
                .add("fio", fio)
                .add("ima", ima)
                .add("otch",otch)
                .add("pol",pol)
                .add("datr", datr)
                .add("isti", isti)
                .toString();
    }

    public void addVisit(NewVisit newVisit){
        visits.put(newVisit.getSn(), newVisit);
        newVisit.setParent(this);
    }
    public void removeVisit(NewVisit newVisit){
        visits.remove(newVisit.getSn());
    }

    public Map<Integer, NewVisit> getAllVisits() {
        return visits;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getIma() {
        return ima;
    }

    public void setIma(String ima) {
        this.ima = ima;
    }

    public String getOtch() {
        return otch;
    }

    public void setOtch(String otch) {
        this.otch = otch;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public Date getDatr() {
        return datr;
    }

    public void setDatr(Date datr) {
        this.datr = datr;
    }

    public String getKat() {
        return kat;
    }

    public void setKat(String kat) {
        this.kat = kat;
    }

    public String getSnils() {
        return snils;
    }

    public void setSnils(String snils) {
        this.snils = snils;
    }

    public Integer getcDoc() {
        return cDoc;
    }

    public void setcDoc(Integer cDoc) {
        this.cDoc = cDoc;
    }

    public String getsDoc() {
        return sDoc;
    }

    public void setsDoc(String sDoc) {
        this.sDoc = sDoc;
    }

    public String getnDoc() {
        return nDoc;
    }

    public void setnDoc(String nDoc) {
        this.nDoc = nDoc;
    }

    public String getIsti() {
        return isti;
    }

    public void setIsti(String isti) {
        this.isti = isti;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int compareTo(NewHuman o) {
        NewHuman otherHuman = o;
         return ComparisonChain.start()
                .compare(fio, otherHuman.getFio())
                .compare(ima, otherHuman.getIma())
                .compare(otch, otherHuman.getOtch())
                .compare(datr, otherHuman.getDatr()).result();
    }

    public String getFullName() {
        return fio+" "+ima+" "+otch;
    }

    public String getReadableDatr() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return simpleDateFormat.format(datr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fio, ima, otch, datr);
    }

    @Override
    public boolean equals(Object obj) {
        NewHuman otherHuman = (NewHuman) obj;
        return !(obj == null || getClass() != obj.getClass()) && Objects.equals(fio, otherHuman.getFio()) && Objects.equals(ima, otherHuman.getIma()) && Objects.equals(otch, otherHuman.getOtch()) && Objects.equals(datr, otherHuman.getDatr());

    }
}
