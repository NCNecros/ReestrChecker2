package company.entity;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;

import java.util.Objects;

/**
 * Created by Necros on 03.06.2015.
 */
public class Error implements Comparable {
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

    private NewHuman human;
    private NewVisit visit;
    private NewService service;
    private String error;

    public Error(NewHuman human, String error) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Error error1 = (Error) o;
        return Objects.equals(human,error1.getHuman())
                && Objects.equals(visit, error1.getVisit())
                && Objects.equals(error, error1.error);
    }

    @Override
    public int hashCode() {

        int result = human.hashCode();
        result = 31 * result + (visit != null ? visit.hashCode() : 0);
        result = 31 * result + (service != null ? service.hashCode() : 0);
        result = 31 * result + error.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("human",human)
                .add("visit",visit)
                .add("error",error).toString();
    }

    @Override
    public int compareTo(Object o) {
        Error otherError = (Error) o;
        return ComparisonChain.start().compare(human,otherError.getHuman()).compare(error,otherError.getError()).result();
    }
}
