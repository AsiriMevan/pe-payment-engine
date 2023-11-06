package lk.dialog.pe.scheduler.util;

import lk.dialog.pe.common.util.ConnectionStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;


@SpringBootTest
@Slf4j
public class EnumTests {

@Test
    public void orEqualMatching(){
    ConnectionStatus conStat = ConnectionStatus.getConnectionStatus("C");

    assertTrue(conStat.orEqual(ConnectionStatus.D,ConnectionStatus.T,ConnectionStatus.C));
}

    @Test
    public void orEqualMisMatching(){
        ConnectionStatus conStat = ConnectionStatus.getConnectionStatus("C");
        assertFalse(conStat.orEqual(ConnectionStatus.D,ConnectionStatus.T,ConnectionStatus.NC));
    }

    @Test
     public void nullTest(){
        ConnectionStatus conStat =ConnectionStatus.getConnectionStatus("K");
        assertNull(conStat );
    }

    @Test
    public void toStringTest(){
        ConnectionStatus conStat =ConnectionStatus.getConnectionStatus("C");
        log.info("enum.name={} , enum.toString={}",conStat.name(),conStat.toString());

    }



}
