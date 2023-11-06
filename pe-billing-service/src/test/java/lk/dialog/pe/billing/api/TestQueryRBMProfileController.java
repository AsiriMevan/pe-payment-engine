package lk.dialog.pe.billing.api;

import lk.dialog.pe.billing.testutil.PayloadPath;
import lk.dialog.pe.billing.testutil.RequestPayloadUtil;
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
public class TestQueryRBMProfileController {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getRBMProfile() throws Exception {
        String payload = RequestPayloadUtil.getJsonString(PayloadPath.QUERY_RBM_PROFILE);
            mvc.perform(MockMvcRequestBuilders
                            .put("/pe/rbmprofile")
                            .content(payload)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(1));
			/*
			 * .andExpect(MockMvcResultMatchers.jsonPath("$.accounts").isNotEmpty())
			 * .andExpect(MockMvcResultMatchers.jsonPath("$.accounts[0].connRef").value(
			 * "742953079"))
			 * .andExpect(MockMvcResultMatchers.jsonPath("$.accounts[0].contractNo").value(
			 * "32929792"));
			 */

    }

}
