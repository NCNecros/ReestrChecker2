package company.entity;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ListOfError {
    private List<Error> errorList;

    public ListOfError() {
        this.errorList = new ArrayList<>();
    }

    public void clear() {
        errorList.clear();
    }

    public void addError(NewHuman human, String textError) {
        errorList.add(new Error(human, textError));
    }

    public void addError(Doctor doctor, String textError) {
        errorList.add(new Error(doctor, textError));
    }

    public void addError(NewVisit visit, String textError) {
        errorList.add(new Error(visit, textError));
    }

    public void addError(NewService service, String textError) {
        errorList.add(new Error(service, textError));
    }

    public Stream<Error> stream() {
        return errorList.stream();
    }

}
