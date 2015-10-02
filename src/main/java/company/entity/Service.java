//package company.entity;
//todo del
//import java.util.Date;
//
///**
// * Created by Necros on 19.03.2015.
// */
//public class Service {
//    Date datn;
//    Treatment parent;
//    Date dato;
//    String kusl;
//
//    public Service(String kusl, Date datn, Date dato, Treatment parent) {
//        this.kusl = kusl;
//        this.datn = datn;
//        this.parent = parent;
//        this.dato = dato;
//    }
//    public Service(){}
//
//    public Treatment getParent() {
//        return parent;
//    }
//
//    public void setParent(Treatment parent) {
//        this.parent = parent;
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
//    public Date getDato() {
//        return dato;
//    }
//
//    public void setDato(Date dato) {
//        this.dato = dato;
//    }
//
//    public String getKusl() {
//        return kusl;
//    }
//
//    public void setKusl(String kusl) {
//        this.kusl = kusl;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        Service otheService = (Service) obj;
//        try {
//            return getDatn().equals(otheService.getDatn()) && getKusl().equalsIgnoreCase(otheService.getKusl());
//        }catch (Exception e){
//            return false;
//        }
//    }
//}
