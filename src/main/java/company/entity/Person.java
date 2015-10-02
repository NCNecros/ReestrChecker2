//package company.entity;
//TODO Удалить
//
//import org.hibernate.annotations.GenericGenerator;
//
//import javax.persistence.*;
//import java.util.Date;
//
///**
// * Created by Necros on 06.07.2015.
// */
//@Entity
//@Table(name = "person")
//public class Person {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//    @Column
//    private String vs;
//    @Column
//    private String codeMo;
//    @Column
//    private String plOgrn;
//    @Column
//    private String fio;
//    @Column
//    private String ima;
//    @Column
//    private String otch;
//    @Column
//    private String pol;
//    @Column
//    private String kat;
//    @Column
//    private String snils;
//    @Column
//    private String okatoOms;
//    @Column
//    private String sps;
//    @Column
//    private String spn;
//    @Column
//    private String statP;
//    @Column
//    private String qG;
//    @Column
//    private String novor;
//    @Column
//    private String famp;
//    @Column
//    private String imp;
//    @Column
//    private String otp;
//    @Column
//    private String polp;
//    @Column
//    private String sDoc;
//    @Column
//    private String nDoc;
//    @Column
//    private String ksmo;
//    @Column
//    private String naprMo;
//    @Column
//    private String naprN;
//    @Column
//    private String isti;
//    @Column
//    private String ishl;
//    @Column
//    private String ishob;
//    @Column
//    private String mp;
//    @Column
//    private String pv;
//    @Column
//    private Double ns;
//    @Column
//    private Double cDoc;
//    @Column
//    private Double sn;
//    @Column
//    private Double spv;
//    @Column
//    private Double summaI;
//    @Column
//    private Date datr;
//    @Column
//    private Date datps;
//    @Column
//    private Date datrp;
//    @Column
//    private Date dats;
//    @Column
//    private Date datn;
//    @Column
//    private Date dvozvrat;
//    @Column
//    private Date dato;
//    @OneToMany
//
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getVs() {
//        return vs;
//    }
//
//    public void setVs(String vs) {
//        this.vs = vs;
//    }
//
//    public String getCodeMo() {
//        return codeMo;
//    }
//
//    public void setCodeMo(String codeMo) {
//        this.codeMo = codeMo;
//    }
//
//    public String getPlOgrn() {
//        return plOgrn;
//    }
//
//    public void setPlOgrn(String plOgrn) {
//        this.plOgrn = plOgrn;
//    }
//
//    public String getFio() {
//        return fio;
//    }
//
//    public void setFio(String fio) {
//        this.fio = fio;
//    }
//
//    public String getIma() {
//        return ima;
//    }
//
//    public void setIma(String ima) {
//        this.ima = ima;
//    }
//
//    public String getOtch() {
//        return otch;
//    }
//
//    public void setOtch(String otch) {
//        this.otch = otch;
//    }
//
//    public String getPol() {
//        return pol;
//    }
//
//    public void setPol(String pol) {
//        this.pol = pol;
//    }
//
//    public String getKat() {
//        return kat;
//    }
//
//    public void setKat(String kat) {
//        this.kat = kat;
//    }
//
//    public String getSnils() {
//        return snils;
//    }
//
//    public void setSnils(String snils) {
//        this.snils = snils;
//    }
//
//    public String getOkatoOms() {
//        return okatoOms;
//    }
//
//    public void setOkatoOms(String okatoOms) {
//        this.okatoOms = okatoOms;
//    }
//
//    public String getSps() {
//        return sps;
//    }
//
//    public void setSps(String sps) {
//        this.sps = sps;
//    }
//
//    public String getSpn() {
//        return spn;
//    }
//
//    public void setSpn(String spn) {
//        this.spn = spn;
//    }
//
//    public String getStatP() {
//        return statP;
//    }
//
//    public void setStatP(String statP) {
//        this.statP = statP;
//    }
//
//    public String getqG() {
//        return qG;
//    }
//
//    public void setqG(String qG) {
//        this.qG = qG;
//    }
//
//    public String getNovor() {
//        return novor;
//    }
//
//    public void setNovor(String novor) {
//        this.novor = novor;
//    }
//
//    public String getFamp() {
//        return famp;
//    }
//
//    public void setFamp(String famp) {
//        this.famp = famp;
//    }
//
//    public String getImp() {
//        return imp;
//    }
//
//    public void setImp(String imp) {
//        this.imp = imp;
//    }
//
//    public String getOtp() {
//        return otp;
//    }
//
//    public void setOtp(String otp) {
//        this.otp = otp;
//    }
//
//    public String getPolp() {
//        return polp;
//    }
//
//    public void setPolp(String polp) {
//        this.polp = polp;
//    }
//
//    public String getsDoc() {
//        return sDoc;
//    }
//
//    public void setsDoc(String sDoc) {
//        this.sDoc = sDoc;
//    }
//
//    public String getnDoc() {
//        return nDoc;
//    }
//
//    public void setnDoc(String nDoc) {
//        this.nDoc = nDoc;
//    }
//
//    public String getKsmo() {
//        return ksmo;
//    }
//
//    public void setKsmo(String ksmo) {
//        this.ksmo = ksmo;
//    }
//
//    public String getNaprMo() {
//        return naprMo;
//    }
//
//    public void setNaprMo(String naprMo) {
//        this.naprMo = naprMo;
//    }
//
//    public String getNaprN() {
//        return naprN;
//    }
//
//    public void setNaprN(String naprN) {
//        this.naprN = naprN;
//    }
//
//    public String getIsti() {
//        return isti;
//    }
//
//    public void setIsti(String isti) {
//        this.isti = isti;
//    }
//
//    public String getIshl() {
//        return ishl;
//    }
//
//    public void setIshl(String ishl) {
//        this.ishl = ishl;
//    }
//
//    public String getIshob() {
//        return ishob;
//    }
//
//    public void setIshob(String ishob) {
//        this.ishob = ishob;
//    }
//
//    public String getMp() {
//        return mp;
//    }
//
//    public void setMp(String mp) {
//        this.mp = mp;
//    }
//
//    public String getPv() {
//        return pv;
//    }
//
//    public void setPv(String pv) {
//        this.pv = pv;
//    }
//
//    public Double getNs() {
//        return ns;
//    }
//
//    public void setNs(Double ns) {
//        this.ns = ns;
//    }
//
//    public Double getcDoc() {
//        return cDoc;
//    }
//
//    public void setcDoc(Double cDoc) {
//        this.cDoc = cDoc;
//    }
//
//    public Double getSn() {
//        return sn;
//    }
//
//    public void setSn(Double sn) {
//        this.sn = sn;
//    }
//
//    public Double getSpv() {
//        return spv;
//    }
//
//    public void setSpv(Double spv) {
//        this.spv = spv;
//    }
//
//    public Double getSummaI() {
//        return summaI;
//    }
//
//    public void setSummaI(Double summaI) {
//        this.summaI = summaI;
//    }
//
//    public Date getDatr() {
//        return datr;
//    }
//
//    public void setDatr(Date datr) {
//        this.datr = datr;
//    }
//
//    public Date getDatps() {
//        return datps;
//    }
//
//    public void setDatps(Date datps) {
//        this.datps = datps;
//    }
//
//    public Date getDatrp() {
//        return datrp;
//    }
//
//    public void setDatrp(Date datrp) {
//        this.datrp = datrp;
//    }
//
//    public Date getDats() {
//        return dats;
//    }
//
//    public void setDats(Date dats) {
//        this.dats = dats;
//    }
//
//    public Date getDatn() {
//        return datn;
//    }
//
//    public void setDatn(Date datn) {
//        this.datn = datn;
//    }
//
//    public Date getDvozvrat() {
//        return dvozvrat;
//    }
//
//    public void setDvozvrat(Date dvozvrat) {
//        this.dvozvrat = dvozvrat;
//    }
//
//    public Date getDato() {
//        return dato;
//    }
//
//    public void setDato(Date dato) {
//        this.dato = dato;
//    }
//}
