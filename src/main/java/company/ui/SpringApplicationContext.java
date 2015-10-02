package company.ui;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by Necros on 01.04.2015.
 */
@org.springframework.stereotype.Service
public class SpringApplicationContext implements ApplicationContextAware {
    private static ApplicationContext CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CONTEXT = applicationContext;
    }

    public static Object getBean(String beanName){
        return CONTEXT.getBean(beanName);
    }
}
