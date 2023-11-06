package lk.dialog.pe.customer.info.api;

import lk.dialog.pe.customer.info.testutil.PayloadPath;
import lk.dialog.pe.customer.info.testutil.RequestPayloadUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestQueryProfileController {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getCCBSProfile() throws Exception {
//        String payload = RequestPayloadUtil.getJsonString(PayloadPath.GET_CCBS_PROFILE);
//            mvc.perform(MockMvcRequestBuilders
//                            .put("/pe/ccbsprofile")
//                            .content(payload)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(1))
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.profiles").isNotEmpty())
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.profiles[0].custRef").value("197736405023"));

    }

}
