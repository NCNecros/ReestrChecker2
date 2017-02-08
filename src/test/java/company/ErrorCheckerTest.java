package company;

import company.entity.*;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.mockito.Mockito.*;


public class ErrorCheckerTest {

    private ErrorChecker errorChecker;
    private ListOfError listOfError;
    private Uslugi307List uslugi307List;
    private Set<String> mkb;

    @Before
    public void setUp() throws Exception {
        listOfError = mock(ListOfError.class);
        uslugi307List = mock(Uslugi307List.class);
        mkb = mock(Set.class);
        DBFHelper helper = new DBFHelper();
        helper.readFromSpr69();
        List<Spr69Value> spr69List = helper.getSpr69List();
        errorChecker = new ErrorChecker(listOfError, uslugi307List, mkb, null);
        errorChecker.setSpr69List(spr69List);
    }

    @Test
    public void testForIncorrectDatn() {
        NewHuman human = getCorrectHuman();
        NewVisit visit = new NewVisit();
        NewService service = new NewService();
        service.setSn(1);
        visit.setSn(1);
        visit.setDatn(new LocalDate(2015, 1, 2).toDate());
        service.setDatn(new LocalDate(2015, 1, 3).toDate());

        errorChecker.checkForIncorrectDatN(Collections.singletonList(visit));

        verify(listOfError).addError(visit, "нет услуги совпадающей с датой начала");
    }

    @Test
    public void testForIncorrectDato() {
        NewHuman human = getCorrectHuman();
        NewVisit visit = new NewVisit();
        NewService service = new NewService();
        service.setSn(1);
        visit.setSn(1);
        visit.setDato(new LocalDate(2015, 1, 2).toDate());
        service.setDato(new LocalDate(2015, 1, 3).toDate());
        errorChecker.checkForIncorrectDatO(Collections.singletonList(visit));
        verify(listOfError, only()).addError(visit, "нет услуги совпадающей с датой окончания");
    }

    @Test
    public void testForIncorrectVMP() {
        NewService service = new NewService();
        service.setVmp("11");
        service.setSpec("1134");
        errorChecker.checkForIncorrectVMP(Collections.singletonList(service));
        verify(listOfError).addError(service, "Некорректный вид МП");
        service.setSpec("1122");
        errorChecker.checkForIncorrectVMP(Collections.singletonList(service));
        verify(listOfError, times(2)).addError(service, "Некорректный вид МП");

    }

    @Test
    public void testError903IfIshobIsCorrect() {
        NewVisit visit = new NewVisit();
        for (Map.Entry<String, List<String>> elem : errorChecker.getIshobIshl().entrySet()) {
            visit.setIshob(elem.getKey());
            for (String s : elem.getValue()) {
                visit.setIshl(s);
                errorChecker.checkForError903(Collections.singletonList(visit));
                verify(listOfError, never()).addError(visit, "иход лечения не соответствует исходу обращения");
            }
        }
    }

    @Test

    public void testError903IfIshobIsIncorrect() {
        for (Map.Entry<String, List<String>> elem : errorChecker.getIshobIshl().entrySet()) {
            for (String s : elem.getValue()) {
                NewVisit visit = new NewVisit();
                visit.setIshob(elem.getKey());
                visit.setIshl(s + "0");
                errorChecker.checkForError903(Collections.singletonList(visit));
                verify(listOfError).addError(visit, "903 исход лечения не соответствует исходу обращения");
            }
        }
    }

    private NewHuman getCorrectHuman() {
        NewHuman human = new NewHuman();
        human.setIsti("12345");
        human.setFio("Иванов");
        human.setIma("Иван");
        human.setOtch("Иванович");
        return human;
    }

    @Test
    public void testCheckForMoreThanOneVisit() {
        NewHuman human = getCorrectHuman();

        NewVisit visitOne = new NewVisit();
        visitOne.setSn(12);
        NewService serviceOne = getServiceByTabnAndMkb("111", "O20.0", "B01.001");
        serviceOne.setSpec("1124");
        visitOne.addService(serviceOne);
        human.addVisit(visitOne);

        NewVisit visitTwo = new NewVisit();
        visitTwo.setSn(13);
        NewService serviceTwo = getServiceByTabnAndMkb("111", "O20.0", "B01.001");
        serviceTwo.setSpec("1124");
        visitTwo.addService(serviceTwo);
        human.addVisit(visitTwo);
        List<NewHuman> newHumans = new ArrayList<>();
        newHumans.add(human);

        errorChecker.checkForMoreThanOneVisit(newHumans);

        verify(listOfError, times(2)).addError(any(NewVisit.class), eq("(307) содержит более одного обращения"));
    }

    @Test
    public void testCheckForMoreThanOneVisitWhenMKBNotSame() {
        NewHuman human = getCorrectHuman();

        NewVisit visitOne = new NewVisit();
        visitOne.setSn(12);
        NewService serviceOne = getServiceByTabnAndMkb("111", "O20.0", "B01.001");
        serviceOne.setSpec("1124");
        visitOne.addService(serviceOne);
        human.addVisit(visitOne);

        NewVisit visitTwo = new NewVisit();
        visitTwo.setSn(13);
        NewService serviceTwo = getServiceByTabnAndMkb("111", "O21.0", "B01.001");
        serviceTwo.setSpec("1124");
        visitTwo.addService(serviceTwo);
        human.addVisit(visitTwo);
        List<NewHuman> newHumans = new ArrayList<>();
        newHumans.add(human);

        errorChecker.checkForMoreThanOneVisit(newHumans);

        verify(listOfError, never()).addError(visitOne, "содержит более одного обращения");

    }

    @Test
    public void testCheckForMoreThanOneVisitWhenMKBSameAndKulsDifferent() {
        NewHuman human = getCorrectHuman();

        NewVisit visitOne = new NewVisit();
        visitOne.setSn(12);
        NewService serviceOne = getServiceByTabnAndMkb("111", "O20.0", "B01.001");
        serviceOne.setSpec("1124");
        visitOne.addService(serviceOne);
        human.addVisit(visitOne);

        NewVisit visitTwo = new NewVisit();
        visitTwo.setSn(13);
        NewService serviceTwo = getServiceByTabnAndMkb("111", "O20.0", "B01.002");
        serviceTwo.setSpec("1124");
        visitTwo.addService(serviceTwo);
        human.addVisit(visitTwo);
        List<NewHuman> newHumans = new ArrayList<>();
        newHumans.add(human);

        errorChecker.checkForMoreThanOneVisit(newHumans);

        verify(listOfError).addError(visitOne, "(307) содержит более одного обращения");

    }

    @Test
    public void testIncorrectOkatoForStrangers() {
        NewVisit visit = new NewVisit();
        visit.setOkatoOms("03000");
        visit.setPlOgrn(ErrorChecker.TFOMS_OGRN);
        errorChecker.setSmo("9007");

        errorChecker.checkForCorrectOkatoForStrangers(Arrays.asList(visit));

        verify(listOfError).addError(visit, "краевой в счете для инокраевых");
    }

    @Test
    public void testCheckForIncorrectPolisNumberForTemporalyPolisWhenItIsIncorrect() {
        NewVisit visit = new NewVisit();
        visit.setSpv(2);
        visit.setSpn("231");
        visit.setSps("");

        errorChecker.checkForIncorrectPolisNumber(Arrays.asList(visit));

        verify(listOfError).addError(visit, "некорректно заполнен номер полиса");
    }

    @Test
    public void testCheckForIncorrectPolisNumberForPermanentWhenItIsIncorrect() {
        NewVisit visit = new NewVisit();
        visit.setSpv(3);
        visit.setSpn("0420887000398");
        visit.setSps("");

        errorChecker.checkForIncorrectPolisNumber(Arrays.asList(visit));

        verify(listOfError).addError(visit, "некорректно заполнен номер полиса");
    }

    @Test
    public void testCheckForRedundantService() {

        Uslugi307 uslugi307 = new Uslugi307();
        uslugi307.setDoctor("акушер-гинеколог");
        uslugi307.setObrashenie("B01.001.019");
        uslugi307.setUslugi(Arrays.asList("B01.001.001", "B01.001.002", "B01.001.003", "B01.001.004",
                "B01.001.010", "B01.001.011", "B01.001.016", "B01.001.017"));
        when(uslugi307List.getUslugi()).thenReturn(Arrays.asList(uslugi307));

        NewHuman human = getCorrectHuman();
        NewVisit visit = new NewVisit();
        human.addVisit(visit);
        NewService serviceOne = new NewService();
        visit.addService(serviceOne);
        NewService serviceTwo = new NewService();
        visit.addService(serviceTwo);
        serviceOne.setKusl("B01.001.001");
        serviceOne.setMkbх("Z01.4");
        serviceTwo.setKusl("B01.001.019");
        serviceTwo.setMkbх("Z01.4");

        errorChecker.checkForRedundantService(Arrays.asList(human));

        verify(listOfError).addError(visit, "содержит лишнее обращение");
    }

    @Test
    public void testCheckForMissedService() {

        Uslugi307 uslugi307 = new Uslugi307();
        uslugi307.setDoctor("акушер-гинеколог");
        uslugi307.setObrashenie("B01.001.019");
        uslugi307.setUslugi(Arrays.asList("B01.001.001", "B01.001.002", "B01.001.003", "B01.001.004",
                "B01.001.010", "B01.001.011", "B01.001.016", "B01.001.017"));
        when(uslugi307List.getUslugi()).thenReturn(Arrays.asList(uslugi307));

        NewHuman human = getCorrectHuman();
        NewVisit visit = new NewVisit();
        human.addVisit(visit);
        NewService serviceOne = new NewService();
        serviceOne.setMkbх("Z00.0");
        visit.addService(serviceOne);
        NewService serviceTwo = new NewService();
        visit.addService(serviceTwo);
        NewService serviceThree = new NewService();
        visit.addService(serviceThree);
        serviceOne.setKusl("B01.001.001");
        serviceTwo.setKusl("B01.001.001");
        serviceThree.setKusl("B01.001.001");

        errorChecker.checkForMissedService(Arrays.asList(human));

        verify(listOfError).addError(visit, "отсутствует обращение к врач-акушер-гинеколог");
    }

    @Test
    public void testCheckForIncorrectOkato() {
        NewVisit visit = new NewVisit();
        visit.setPlOgrn(ErrorChecker.TFOMS_OGRN);
        visit.setOkatoOms("");
        errorChecker.checkForIncorrectOkato(Arrays.asList(visit));

        verify(listOfError).addError(visit, "не указана территория для инокраевого");
    }

    @Test
    public void testCheckForIncorrectDocument() {
        NewHuman human = getCorrectHuman();
        human.setcDoc(1);
        human.setnDoc("");
        human.setsDoc("");
        errorChecker.checkForIncorrectDocument(Arrays.asList(human));

        verify(listOfError).addError(human, "неправильно заполнена серия или номер документа");
    }

    @Test
    public void testCheckForIncorrectVisitResult() {
        NewVisit visit = new NewVisit();
        visit.setIshob("301");
        NewService service = new NewService();
        visit.addService(service);
        service.setMkbх("Z01.4");

        errorChecker.checkForIncorrectIshob(Collections.singletonList(visit));

        verify(listOfError).addError(visit, "неправильный исход обращения");
    }

    @Test
    public void testCheckForRedundantVisitForPregnantWomen() {

        Uslugi307 uslugi307 = new Uslugi307();
        uslugi307.setDoctor("акушер-гинеколог");
        uslugi307.setObrashenie("B01.001.019");
        uslugi307.setUslugi(Arrays.asList("B01.001.001", "B01.001.002", "B01.001.003", "B01.001.004",
                "B01.001.010", "B01.001.011", "B01.001.016", "B01.001.017"));
        when(uslugi307List.getUslugi()).thenReturn(Arrays.asList(uslugi307));

        NewHuman human = getCorrectHuman();
        NewVisit visit = new NewVisit();
        human.addVisit(visit);
        NewService serviceOne = new NewService();
        visit.addService(serviceOne);
        NewService serviceTwo = new NewService();
        visit.addService(serviceTwo);
        NewService serviceThree = new NewService();
        visit.addService(serviceThree);
        serviceOne.setKusl("B01.001.001");
        serviceTwo.setKusl("B01.001.001");
        serviceThree.setKusl("B01.001.019");
        serviceOne.setMkbх("Z34.0");
        serviceTwo.setMkbх("Z34.0");
        serviceThree.setMkbх("Z34.0");

        errorChecker.checkForRedundantService(Arrays.asList(human));

        verify(listOfError).addError(visit, "содержит лишнее обращение");
    }

    @Test
    public void testCheckIncorrectMKBWhenItIsCorrect() {
        when(mkb.contains(anyString())).thenReturn(false);
        when(mkb.contains("Z99.9")).thenReturn(true);

        NewService service = new NewService();
        service.setMkbх("Z99.9");

        errorChecker.checkForInсorrectMKB(Arrays.asList(service));

        verify(listOfError, never()).addError(any(NewService.class), anyString());
    }

    @Test
    public void testCheckIncorrectMKBWhenItIsNotCorrect() {
        when(mkb.contains(anyString())).thenReturn(false);

        NewService service = new NewService();
        service.setMkbх("Z99.9");

        errorChecker.checkForInсorrectMKB(Collections.singletonList(service));

        verify(listOfError).addError(service, "неправильный МКБ");
    }


    @Test
    public void testCheckForMoreThanOneVisitWhenMKBIsSameAndKuslIsProfilactic() {
        NewHuman human = getCorrectHuman();

        NewVisit visitOne = new NewVisit();
        visitOne.setSn(12);
        NewService serviceOne = getServiceByTabnAndMkb("111", "Z00.0", "B04.001");
        serviceOne.setSpec("1124");
        visitOne.addService(serviceOne);
        human.addVisit(visitOne);

        NewVisit visitTwo = new NewVisit();
        visitTwo.setSn(13);
        NewService serviceTwo = getServiceByTabnAndMkb("111", "Z00.0", "B04.001");
        serviceTwo.setSpec("1124");
        visitTwo.addService(serviceTwo);
        human.addVisit(visitTwo);

        List<NewHuman> newHumans = new ArrayList<>();
        newHumans.add(human);

        errorChecker.checkForMoreThanOneVisit(newHumans);

        verify(listOfError, never()).addError(visitOne, "содержит более одного обращения");
    }

    @Test
    public void testCheckReduandOGRNWhenItIsIncorrect() {
        NewVisit visitOne = new NewVisit();

        errorChecker.setSmo("1207");
        visitOne.setPlOgrn(ErrorChecker.TFOMS_OGRN);
        errorChecker.checkReduandOGRN(Collections.singletonList(visitOne));

        errorChecker.setSmo("1507");
        visitOne.setPlOgrn(ErrorChecker.TFOMS_OGRN);
        errorChecker.checkReduandOGRN(Collections.singletonList(visitOne));

        errorChecker.setSmo("1807");
        visitOne.setPlOgrn(ErrorChecker.TFOMS_OGRN);
        errorChecker.checkReduandOGRN(Collections.singletonList(visitOne));

        errorChecker.setSmo("4407");
        visitOne.setPlOgrn(ErrorChecker.TFOMS_OGRN);
        errorChecker.checkReduandOGRN(Collections.singletonList(visitOne));

        errorChecker.setSmo("9007");
        visitOne.setPlOgrn(ErrorChecker.ALPHA_OGRN);
        errorChecker.checkReduandOGRN(Collections.singletonList(visitOne));

        verify(listOfError, times(5)).addError(visitOne, "неправильный ОГРН");
    }

    @Test
    public void testCheckRedundantORGNWhenItIsCorrect() {
        NewVisit visit = new NewVisit();
        errorChecker.setSmo("1207");
        visit.setPlOgrn(ErrorChecker.ALPHA_OGRN);
        errorChecker.checkReduandOGRN(Collections.singletonList(visit));

        verify(listOfError, never()).addError(visit, "неправильный ОГРН");
    }

    @Test
    public void testCheckPolisTypeWhenItIsNotOk() {
        NewVisit visit = new NewVisit();
        visit.setSpv(0);

        errorChecker.checkForIncorrectPolisType(Collections.singletonList(visit));

        verify(listOfError).addError(visit, "не указан тип полиса");
    }


    private NewService getServiceByTabnAndMkb(String tabn, String mkbx, String kusl) {
        NewService service = new NewService();
        service.setKusl(kusl);
        service.setMkbх(mkbx);
        service.setDocTabn(tabn);
        return service;
    }

    @Test
    public void testCheckForError347WhenVisitIsCorrect(){
        NewVisit visit = new NewVisit();
        NewService ksg = new NewService();
        NewService operation = new NewService();
        ksg.setKusl("G10.2516.179");
        ksg.setMkbх("I87.0");
        operation.setKusl("A06.12.036");
        operation.setMkbх("I87.0");
        visit.addService(ksg);
        visit.addService(operation);

        errorChecker.checkForIncorrectSpr69(Collections.singletonList(visit));

        verify(listOfError, never()).addError(visit, "КСГ не соответствует SPR69");
    }

    @Test
    public void testCheckForError347WhenVisitHasIncorrectMKB(){
        NewVisit visit = new NewVisit();
        NewService ksg = new NewService();
        NewService operation = new NewService();
        ksg.setKusl("G10.2516.179");
        ksg.setMkbх("I87.1");
        operation.setKusl("A06.12.036");
        operation.setMkbх("I87.1");
        visit.addService(ksg);
        visit.addService(operation);

        errorChecker.checkForIncorrectSpr69(Collections.singletonList(visit));

        verify(listOfError).addError(visit, "КСГ не соответствует SPR69");

    }
    @Test
    public void testCheckForError347WhenVisitIsCorrectWithoutOperationButWithMKB(){
        NewVisit visit = new NewVisit();
        NewService ksg = new NewService();
        ksg.setKusl("G10.2516.176");
        ksg.setMkbх("I80.0");
        visit.addService(ksg);

        errorChecker.checkForIncorrectSpr69(Collections.singletonList(visit));

        verify(listOfError,never()).addError(visit, "КСГ не соответствует SPR69");
    }
    @Test
    public void testCheckForError347WhenVisitIsIncorrectWithoutOperationAndWithIncorrectMKB(){
        NewVisit visit = new NewVisit();
        NewService ksg = new NewService();
        ksg.setKusl("G10.2516.176");
        ksg.setMkbх("I66.0");
        visit.addService(ksg);

        errorChecker.checkForIncorrectSpr69(Collections.singletonList(visit));

        verify(listOfError).addError(visit, "КСГ не соответствует SPR69");
    }
}