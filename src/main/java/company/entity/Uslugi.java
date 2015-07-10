package company.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Necros on 06.07.2015.
 */
@Entity
@Table(name = "uslugi")
public class Uslugi {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", length = 6, nullable = false)
    private Long id;
    @Column
    private String codeMo;
    @Column
    private String kotd;
    @Column
    private String kpk;
    @Column
    private String mkbx;
    @Column
    private String mkbxs;
    @Column
    private String kstand;
    @Column
    private String spec;
    @Column
    private String outMo;
    @Column
    private String docTabn;
    @Column
    private String kso;
    @Column
    private String vmp;
    @Column
    private String profil;
    @Column
    private String vp;
    @Column
    private String kusl;
    @Column
    private Double ns;
    @Column
    private Double sn;
    @Column
    private Double kolu;
    @Column
    private Double uid;
    @Column
    private Double kd;
    @Column
    private Double taru;
    @Column
    private Double taruB;
    @Column
    private Double taruDm;
    @Column
    private Double taruD;
    @Column
    private Double taruUc;
    @Column
    private Double summ;
    @Column
    private Double summB;
    @Column
    private Double summDm;
    @Column
    private Double summD;
    @Column
    private Double summK;
    @Column
    private Double summUc;
    @Column
    private Double isOut;
    @Column
    private Date dato;
    @Column
    private Date datn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeMo() {
        return codeMo;
    }

    public void setCodeMo(String codeMo) {
        this.codeMo = codeMo;
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

    public String getMkbx() {
        return mkbx;
    }

    public void setMkbx(String mkbx) {
        this.mkbx = mkbx;
    }

    public String getMkbxs() {
        return mkbxs;
    }

    public void setMkbxs(String mkbxs) {
        this.mkbxs = mkbxs;
    }

    public String getKstand() {
        return kstand;
    }

    public void setKstand(String kstand) {
        this.kstand = kstand;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
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

    public String getKso() {
        return kso;
    }

    public void setKso(String kso) {
        this.kso = kso;
    }

    public String getVmp() {
        return vmp;
    }

    public void setVmp(String vmp) {
        this.vmp = vmp;
    }

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
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

    public Double getKolu() {
        return kolu;
    }

    public void setKolu(Double kolu) {
        this.kolu = kolu;
    }

    public Double getUid() {
        return uid;
    }

    public void setUid(Double uid) {
        this.uid = uid;
    }

    public Double getKd() {
        return kd;
    }

    public void setKd(Double kd) {
        this.kd = kd;
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

    public Date getDato() {
        return dato;
    }

    public void setDato(Date dato) {
        this.dato = dato;
    }

    public Date getDatn() {
        return datn;
    }

    public void setDatn(Date datn) {
        this.datn = datn;
    }
}
