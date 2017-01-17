package company.entity;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;

import java.util.Date;
import java.util.Objects;

/**
 * Created by Necros on 16.01.2017.
 */
public class Doctor implements Comparable<Doctor> {
    private String codeMo, docTabn, snils, fio, ima, otch, pol;
    private Date datr, datn, dato;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("code_mo", codeMo)
                .add("doc_tabn", docTabn)
                .add("snils", snils)
                .add("fio", fio)
                .add("ima", ima)
                .add("otch", otch)
                .add("pol", pol)
                .add("datr", datr)
                .add("datn", datn)
                .add("dato", dato).toString();
    }

    public String getCodeMo() {
        return codeMo;
    }

    public void setCodeMo(String codeMo) {
        this.codeMo = codeMo;
    }

    public String getDocTabn() {
        return docTabn;
    }

    public void setDocTabn(String docTabn) {
        this.docTabn = docTabn;
    }

    public String getSnils() {
        return snils;
    }

    public void setSnils(String snils) {
        this.snils = snils;
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

    @Override
    public int compareTo(Doctor otherDoctor) {
        return ComparisonChain.start()
                .compare(this.codeMo, otherDoctor.codeMo)
                .compare(docTabn, otherDoctor.docTabn)
                .compare(snils, otherDoctor.snils)
                .compare(fio, otherDoctor.fio)
                .compare(ima, otherDoctor.ima)
                .compare(otch, otherDoctor.otch)
                .compare(pol, otherDoctor.pol)
                .compare(datr, otherDoctor.datr)
                .result();
    }

    @Override
    public int hashCode() {
        return Objects.hash(docTabn, snils, fio, ima, otch, datr);
    }

}
