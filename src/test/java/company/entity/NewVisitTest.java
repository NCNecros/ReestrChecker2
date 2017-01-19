package company.entity;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;


/**
 * Created by Necros on 14.07.2015.
 */
public class NewVisitTest {

    @Test
    public void testEquals() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 1, 1);
        NewVisit visit1 = new NewVisit();
        visit1.setCodeMo("123");
        visit1.setDatn(calendar.getTime());
        visit1.setDato(calendar.getTime());
        visit1.setDatps(calendar.getTime());
        visit1.setDatrp(calendar.getTime());
        visit1.setDats(calendar.getTime());
        visit1.setDvozvrat(calendar.getTime());
        visit1.setFamp("123");
        visit1.setId(1);
        visit1.setImp("123");
        visit1.setIshl("123");
        visit1.setIshob("123");
        visit1.setKsmo("123");
        visit1.setMp("123");
        visit1.setNaprMo("123");
        visit1.setNaprN("123");
        visit1.setNovor("123");
        visit1.setNs(1);
        visit1.setOkatoOms("123");
        visit1.setOtp("123");
        visit1.setPlOgrn("123");
        visit1.setPolp("123");
        visit1.setPv("123");
        visit1.setqG("123");
        visit1.setSn(1);
        visit1.setSpn("123");
        visit1.setSps("123");
        visit1.setSpv(1);
        visit1.setSummaI(1d);
        visit1.setVs("123");

        NewVisit visit2 = new NewVisit();
        visit2.setCodeMo("123");
        visit2.setDatn(calendar.getTime());
        visit2.setDato(calendar.getTime());
        visit2.setDatps(calendar.getTime());
        visit2.setDatrp(calendar.getTime());
        visit2.setDats(calendar.getTime());
        visit2.setDvozvrat(calendar.getTime());
        visit2.setFamp("123");
        visit2.setId(1);
        visit2.setImp("123");
        visit2.setIshl("123");
        visit2.setIshob("123");
        visit2.setKsmo("123");
        visit2.setMp("123");
        visit2.setNaprMo("123");
        visit2.setNaprN("123");
        visit2.setNovor("123");
        visit2.setNs(1);
        visit2.setOkatoOms("123");
        visit2.setOtp("123");
        visit2.setPlOgrn("123");
        visit2.setPolp("123");
        visit2.setPv("123");
        visit2.setqG("123");
        visit2.setSn(1);
        visit2.setSpn("123");
        visit2.setSps("123");
        visit2.setSpv(1);
        visit2.setSummaI(1d);
        visit2.setVs("123");
        Assert.assertEquals(visit1,visit2);
        visit2 = null;
        Assert.assertNotEquals(visit1, visit2);
    }

    @Test
    public void testContainsKusl() {
        NewService service1 = new NewService();
        NewService service2 = new NewService();

        service1.setKusl("A06.30.001");
        service2.setKusl("A06.30.002");

        NewVisit visit = new NewVisit();
        visit.addService(service1);
        visit.addService(service2);

        Assert.assertTrue(visit.containsKusl("A06.30.001"));
        Assert.assertFalse(visit.containsKusl("A06.30.003"));

    }

    @Test
    public void testGetReadableDatN() {
        NewVisit visit = new NewVisit();
        visit.setDatn(new LocalDate(2015, 2, 1).toDate());
        Assert.assertEquals(visit.getReadableDatN(), "01.02.2015");
    }
}