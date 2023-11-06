package lk.dialog.pe.scheduler.config;

import lk.dialog.pe.scheduler.config.prop.SmsConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ConfigLoaderTest {

    @Autowired
    SmsConfig smsConfig;

    @Test
    public void checkSingleParam(){
        String  val = smsConfig.getModuleId();
        System.out.println(val);
    }
    @Test
    public void smsListCount(){
        Assert.assertEquals(smsConfig.getMessages().size(),10);
    }

}
