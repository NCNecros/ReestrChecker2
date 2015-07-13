package company.entity;

/**
 * Created by Necros on 03.06.2015.
 */
public class Error {
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

//        if (!error.equals(error1.error)) return false;
//        if (!human.equals(error1.human)) return false;
//        if (service != null ? !service.equals(error1.service) : error1.service != null) return false;
//        if (visit != null ? !visit.equals(error1.visit) : error1.visit != null) return false;

        return this.error.equals(error1.error)&&this.human.equals();
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
        return "Error{" +
                "human=" + human +
                ", visit=" + visit +
                ", error='" + error + '\'' +
                '}';
    }
}
