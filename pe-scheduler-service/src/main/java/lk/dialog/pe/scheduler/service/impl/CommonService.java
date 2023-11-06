package lk.dialog.pe.scheduler.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonService {

    protected ObjectMapper objectMapper;

    protected String convertToString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            return null;
        }
    }

}
