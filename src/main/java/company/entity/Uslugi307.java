package company.entity;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by necros on 02.04.15.
 */
@Default
public class Uslugi307 {
    @ElementList(type = String.class)
    private List<String> uslugi;
    private String obrashenie;
    private String doctor;

    public Uslugi307() {
    }

    public List<String> getUslugi() {
        return uslugi;
    }

    public void setUslugi(List<String> uslugi) {
        this.uslugi = uslugi;
    }

    public String getObrashenie() {
        return obrashenie;
    }

    public void setObrashenie(String obrashenie) {
        this.obrashenie = obrashenie;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

}
