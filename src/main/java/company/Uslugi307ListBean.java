package company;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Necros on 06.06.2015.
 */
@Configuration
public class Uslugi307ListBean {
    @Bean
    public Uslugi307List uslugi307() throws Exception {
        Reader reader = new FileReader(getClass().getClassLoader().getResource("Uslugi307.xml").getFile());
        Serializer serializer = new Persister();
        return serializer.read(Uslugi307List.class, reader);
    }

    @Bean
    public List<Spr69Value> spr69List() throws Exception {
        List<Spr69Value> list = new ArrayList<>();
        return list;
    }
}
