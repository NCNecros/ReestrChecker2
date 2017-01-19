package company.entity;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;

import java.util.Objects;

/**
 * Created by Necros on 03.06.2015.
 */
public class Error implements Comparable<Error> {
    private NewHuman human;
    private NewVisit visit;
    private NewService service;
    private String error;
    private String description;

    public Error(NewHuman human, String error) {
        this.human = human;
        this.error = error;
    }

    public Error(Doctor doctor, String error) {
        NewHuman human = new NewHuman();
        human.setFio(doctor.getFio());
        human.setDatr(doctor.getDatr());
        human.setIma(doctor.getIma());
        human.setOtch(doctor.getOtch());
        this.human = human;
        this.error = error;
    }

    public Error(NewVisit visit, String error) {
        this.human = visit.getParent();
        this.visit = visit;
        this.error = error;
    }

    public Error(NewService service, String error) {
        this.human = service.getVisit().getParent();
        this.visit = service.getVisit();
        this.service = service;
        this.error = error;
    }

    public NewHuman getHuman() {
        return human;
    }

    public NewVisit getVisit() {
        return visit;
    }

    public NewService getService() {
        return service;
    }

    public String getError() {
        return error;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Boolean result = true;
        Error error1 = (Error) o;
        result = Objects.equals(human.getFullName(), error1.getHuman().getFullName());
        result &= Objects.equals(human.getReadableDatr(), error1.getHuman().getReadableDatr());
        if (Objects.nonNull(visit) && Objects.nonNull(error1.visit)) {
            result &= Objects.equals(visit.getReadableDatN(), error1.visit.getReadableDatN());
        }
        result &= Objects.equals(error, error1.error);
        return result;
    }

    @Override
    public int hashCode() {

        int result = human.hashCode();
        result = 31 * result + (human != null ? human.getIsti().hashCode() : 0);
        result = 31 * result + (human != null ? human.getFullName().hashCode() : 0);
        result = 31 * result + (visit != null ? visit.getReadableDatN().hashCode() : 0);
        result = 31 * result + error.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("human", human)
                .add("visit", visit)
                .add("error", error).toString();
    }

    @Override
    public int compareTo(Error o) {
        Error otherError = (Error) o;
        return ComparisonChain.start().compare(human, otherError.getHuman()).compare(error, otherError.getError()).result();
    }

    public String getDescription() {
        return description;
    }
}
