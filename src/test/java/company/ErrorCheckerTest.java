package company;

import company.entity.NewHuman;
import company.entity.NewService;
import company.entity.NewVisit;
import org.junit.Before;
import company.entity.Error;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ErrorCheckerTest {
    private Map<String, NewHuman> mapNewHuman=new LinkedHashMap<>();
    private Map<Double, NewService> mapNewService = new LinkedHashMap<>();
    private Map<Double, NewVisit> mapNewVisit= new LinkedHashMap<>();
    private List<Error> errors = new ArrayList<>();
    private ErrorChecker errorChecker = new ErrorChecker();
    @Before
    public void setUp() throws Exception {
        errorChecker.setMapNewHuman(mapNewHuman);
        errorChecker.setMapNewService(mapNewService);
        errorChecker.setMapNewVisit(mapNewVisit);
        errorChecker.setErrors(errors);
    }

    @Test
    public void testForIncorrectDatn(){
        NewHuman human = getCorrectHuman();
        NewVisit visit = new NewVisit();
        NewService service = new NewService();
        wire(human, service, visit);
        service.setSn(1d);
        visit.setSn(1d);
        visit.setDatn(new Date(2015, 1, 2));
        service.setDatn(new Date(2015, 1, 3));
        mapNewHuman.put(human.getIsti(), human);
        mapNewService.put(service.getSn(), service);
        mapNewVisit.put(visit.getSn(), visit);
        Error error = new Error(service, " нет услуги совпадающей с датой начала");
        errorChecker.checkForIncorrectDatN();
        assertEquals(errors.size(), 1);
        assertEquals(error,errors.get(0));
    }

    private NewHuman getCorrectHuman(){
        NewHuman human = new NewHuman();
        human.setIsti("12345");
        human.setFio("Иванов");
        human.setIma("Иван");
        human.setOtch("Иванович");
        return human;
    }

    private void wire(NewHuman human, NewService service, NewVisit visit){
        human.addVisit(visit);
        visit.setParent(human);
        visit.getServices().add(service);
        service.setVisit(visit);
    }

}