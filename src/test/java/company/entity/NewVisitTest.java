package company.entity;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Created by Necros on 14.07.2015.
 */
public class NewVisitTest {

    @Test
    public void testEquals() throws Exception {
        NewVisit visit1 = new NewVisit();
        visit1.setCodeMo("123");
        visit1.setDatn(new Date(2015, 01, 01));
        visit1.setDato(new Date(2015, 01, 01));
        visit1.setDatps(new Date(2015, 01, 01));
        visit1.setDatrp(new Date(2015, 01, 01));
        visit1.setDats(new Date(2015, 01, 01));
        visit1.setDvozvrat(new Date(2015, 01, 01));
        visit1.setFamp("123");
        visit1.setId(1l);
        visit1.setImp("123");
        visit1.setIshl("123");
        visit1.setIshob("123");
        visit1.setKsmo("123");
        visit1.setMp("123");
        visit1.setNaprMo("123");
        visit1.setNaprN("123");
        visit1.setNovor("123");
        visit1.setNs(1d);
        visit1.setOkatoOms("123");
        visit1.setOtp("123");
        visit1.setPlOgrn("123");
        visit1.setPolp("123");
        visit1.setPv("123");
        visit1.setqG("123");
        visit1.setSn(1d);
        visit1.setSpn("123");
        visit1.setSps("123");
        visit1.setSpv(1d);
        visit1.setStatP("123");
        visit1.setSummaI(1d);
        visit1.setVs("123");

        NewVisit visit2 = new NewVisit();
        visit2.setCodeMo("123");
        visit2.setDatn(new Date(2015, 01, 01));
        visit2.setDato(new Date(2015, 01, 01));
        visit2.setDatps(new Date(2015, 01, 01));
        visit2.setDatrp(new Date(2015, 01, 01));
        visit2.setDats(new Date(2015, 01, 01));
        visit2.setDvozvrat(new Date(2015, 01, 01));
        visit2.setFamp("123");
        visit2.setId(1l);
        visit2.setImp("123");
        visit2.setIshl("123");
        visit2.setIshob("123");
        visit2.setKsmo("123");
        visit2.setMp("123");
        visit2.setNaprMo("123");
        visit2.setNaprN("123");
        visit2.setNovor("123");
        visit2.setNs(1d);
        visit2.setOkatoOms("123");
        visit2.setOtp("123");
        visit2.setPlOgrn("123");
        visit2.setPolp("123");
        visit2.setPv("123");
        visit2.setqG("123");
        visit2.setSn(1d);
        visit2.setSpn("123");
        visit2.setSps("123");
        visit2.setSpv(1d);
        visit2.setStatP("123");
        visit2.setSummaI(1d);
        visit2.setVs("123");
        Assert.assertEquals(visit1,visit2);
    }
}