package lk.dialog.pe.ccbs.service.impl;

import lk.dialog.pe.ccbs.dto.DcsProfile;
import lk.dialog.pe.ccbs.repository.QueryExecuterRepository;
import lk.dialog.pe.ccbs.service.DcsService;
import lk.dialog.pe.ccbs.util.Constants;
import lk.dialog.pe.ccbs.util.DCPEUtil;
import lk.dialog.pe.ccbs.util.SQLQueryEnum;
import lk.dialog.pe.common.exception.DCPEException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DcsServiceImpl implements DcsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DcsServiceImpl.class);

    @Autowired
    private QueryExecuterRepository queryExecuterRepository;

    @Autowired
    @Qualifier("queryMap")
    private  Map<String, String>  map;

    @Override
    public List<DcsProfile> getDcsMobileByContract(String contractId, String traceId) throws DCPEException {
        Instant start = Instant.now();
        LOGGER.info("getDcsMobileByContract Request : traceId=[{}] | contractId=[{}]", traceId, contractId);
        List<DcsProfile> dcsProfileList = getDcsMobileNoByContract(contractId, traceId);

        Long timeTaken = DCPEUtil.getTimeTaken(start);
        LOGGER.info("getDcsMobileByContract Response : traceId=[{}] | timeTaken=[{}] | dcsProfileList=[{}]",traceId,timeTaken, dcsProfileList);
        return dcsProfileList;
    }

    private List<DcsProfile> getDcsMobileNoByContract(String contractId, String traceId) throws DCPEException {
        Instant start = Instant.now();
        LOGGER.info("getDcsMobileNoByContract Request : traceId=[{}] | contractId=[{}]", traceId, contractId);

        List<Map<String, Object>> rows = queryExecuterRepository.getData(map.get(SQLQueryEnum.SQL_DCS_MOBILE_BY_CONTRACT.getValue()), new Object[] {contractId}, traceId);

        Long timeTaken = DCPEUtil.getTimeTaken(start);
        LOGGER.info("getDcsMobileNoByContract SQL Response : timeTaken=[{}] | traceId=[{}]", timeTaken, traceId);

        return processDcsRows(rows, traceId);
    }

    private List<DcsProfile> processDcsRows(List<Map<String, Object>> rows, String traceId) {
        LOGGER.info("processDcsRows Request : traceId=[{}] | rows=[{}]", traceId, rows);
        List<DcsProfile> dcsProfiles = new ArrayList<>();
        if (rows != null) {
            for (Map<String, Object> row : rows) {
                DcsProfile dcsProfile = new DcsProfile();
                String mainNumber = (String) row.get("MAIN_NUMBER");
                dcsProfile.setMainNumber(mainNumber != null ? mainNumber.toUpperCase() : null);
                dcsProfile.setContractId(row.get(Constants.STR_CONTRACT_ID).toString());
                dcsProfile.setMobileNumber(row.get(Constants.STR_MOBILE_NO).toString());
                dcsProfiles.add(dcsProfile);
            }
        }
        LOGGER.info("processDcsRows Response : traceId=[{}] | dcsProfiles=[{}]", traceId, dcsProfiles);
        return dcsProfiles;
    }
}
