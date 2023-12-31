package lk.dialog.pe.scheduler.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ReconnectServiceTest {

    @Autowired
    ReconnectionService reconnectionService;

    @Test
    public void success(){
        reconnectionService.reconnect("B693130",23);
    }
}
