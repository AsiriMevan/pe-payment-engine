package lk.dialog.pe.ccbs.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lk.dialog.pe.ccbs.dto.DcsRemarkRequest;
import lk.dialog.pe.ccbs.dto.HotlineRemarkRequest;
import lk.dialog.pe.ccbs.dto.Remark;
import lk.dialog.pe.ccbs.dto.RemarkInfo;
import lk.dialog.pe.ccbs.repository.QueryExecuterRepository;
import lk.dialog.pe.ccbs.service.GetHotLineRemarksService;
import lk.dialog.pe.ccbs.util.Constants;
import lk.dialog.pe.ccbs.util.SQLQueryEnum;
import lk.dialog.pe.ccbs.util.Util;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GetHotLineRemarksServiceImpl implements GetHotLineRemarksService {

  private static final Logger LOGGER = LoggerFactory.getLogger(GetHotLineRemarksServiceImpl.class);

  @Autowired
  private QueryExecuterRepository queryExecuterRepository;

  @Autowired
  @Qualifier("queryMap")
  private Map<String, String> map;

  @Override
  public List<RemarkInfo> getHotLineRemarks(HotlineRemarkRequest hotlineRemarkRequest,
      String traceId) throws DCPEException {
    Instant start = Instant.now();
    //todo - why bellow is commented?
    //	LOGGER.info("getHotlineRemarksRequest : traceId={}|contractList={}",traceId, contractList);
    String contractQueryStr;
    List<RemarkInfo> remarkInfoList = new ArrayList<>();

    List<Map<String, Object>> rows;
    StringBuilder contractQueryStrBuilder = new StringBuilder(
        map.get(SQLQueryEnum.SQL_HOTLINE_REMARKS.getValue()));

    //todo - there could be an error if empty account list passed
    hotlineRemarkRequest.getAccounts().stream().forEach(accountRef -> {
      contractQueryStrBuilder.append("'" + accountRef.getContractNo() + "',");

    });

    contractQueryStr = contractQueryStrBuilder.deleteCharAt(contractQueryStrBuilder.length() - 1)
        .append(") ORDER BY CONTRACT_ID").toString();
    rows = queryExecuterRepository.getDataByQuery(contractQueryStr, traceId);
    remarkInfoList = processRemarks(rows, traceId);

    Long timeTaken = DCPEUtil.getTimeTaken(start);
    //todo - response is not logged
    //String responseString = DCPEUtil.convertToString(remarkInfoList);
    LOGGER.info("getHotlineRemarksRequest : traceId={}|timeTaken={}", traceId, timeTaken);
    return remarkInfoList;

  }

  @Override
  public List<RemarkInfo> getDcsRemarks(DcsRemarkRequest dcsRemarkRequest,
                                        String traceId) throws DCPEException {
    LOGGER.info("getDcsRemarks Request : traceId=[{}] | dcsRemarkRequest=[{}]", traceId, dcsRemarkRequest);
    Instant start = Instant.now();

    String contractQueryStr;
    String contractDcsQueryStr;
    String finalSql;
    boolean isContractVisit = false;
    List<RemarkInfo> remarkInfoList;

    List<Map<String, Object>> rows;
    StringBuilder contractQueryStrBuilder = new StringBuilder(map.get(SQLQueryEnum.SQL_HOTLINE_REMARKS.getValue()));
    StringBuilder contractDcsQueryStrBuilder = new StringBuilder(map.get(SQLQueryEnum.SQL_DCS_REMARKS.getValue()));

    final List<String> contractList = dcsRemarkRequest.getVolteContractList();

    /* traverse through contract list */
      for (String s : contractList) {
          if (s.matches(Constants.INTEGER_VALIDATOR)) {
              contractQueryStrBuilder.append("'").append(s).append("',");
              isContractVisit = true;
          }
          contractDcsQueryStrBuilder.append("'").append(s).append("',");
      }

    if (isContractVisit) {
      contractQueryStr = contractQueryStrBuilder.deleteCharAt(contractQueryStrBuilder.length() - 1).append(")").toString();
      contractDcsQueryStr = contractDcsQueryStrBuilder.deleteCharAt(contractDcsQueryStrBuilder.length() - 1).append(") ORDER BY CONTRACT_ID").toString();
      finalSql = contractQueryStr + " UNION ALL " + contractDcsQueryStr;
      rows = queryExecuterRepository.getDataByQuery(finalSql, traceId);
      remarkInfoList = processRemarks(rows, traceId);

    } else {
      contractDcsQueryStr = contractDcsQueryStrBuilder.deleteCharAt(contractDcsQueryStrBuilder.length() - 1).append(") ORDER BY CONTRACT_ID").toString();
      rows = queryExecuterRepository.getDataByQuery(contractDcsQueryStr, traceId);
      remarkInfoList = processRemarks(rows, traceId);
    }

    Long timeTaken = DCPEUtil.getTimeTaken(start);
    final String remarkInfoListString = DCPEUtil.convertToString(remarkInfoList);
    LOGGER.info("getDcsRemarks Request : traceId=[{}] | remarkInfoListString=[{}] | timeTaken=[{}]", traceId, remarkInfoListString, timeTaken);
    return remarkInfoList;

  }

  public List<RemarkInfo> processRemarks(List<Map<String, Object>> rows, String traceId) {
    Instant start = Instant.now();
    String requestString = DCPEUtil.convertToString(rows);
    //todo - why bellow is commented?
//		LOGGER.info("processRemarksRequest : traceId={}|rows={}",traceId, requestString);

    List<RemarkInfo> remarkInfoList = new ArrayList<>();
    for (Map<String, Object> row : rows) {
      RemarkInfo remarkInfo = new RemarkInfo();
      remarkInfo.setContractNo(row.get(Constants.STR_CONTRACT_ID).toString());

      String note = (String) row.get("NOTE");
      if (note != null) {
        note = note.replaceAll("[^\\p{ASCII}]", "").replaceAll("\\p{Cntrl}", "");

        if (remarkInfoList.contains(remarkInfo)) {
          int position = remarkInfoList.indexOf(remarkInfo);
          remarkInfo = remarkInfoList.get(position);
          Remark remark = new Remark();
          remark.setRemark(note);
          remark.setCreatedDate(Util.timeStampToCalenderTimeModiify(
              (java.sql.Timestamp) row.get(Constants.STR_CREATED_DATE)));
          remark.setCreatedUser((String) row.get(Constants.STR_CREATED_USER));
          remarkInfo.addRemark(remark);
        } else {
          List<Remark> remarkList = new ArrayList<>();
          Remark remark = new Remark();
          remark.setRemark(note);
          remark.setCreatedDate(Util.timeStampToCalenderTimeModiify(
              (java.sql.Timestamp) row.get(Constants.STR_CREATED_DATE)));
          remark.setCreatedUser((String) row.get(Constants.STR_CREATED_USER));
          remarkList.add(remark);
          remarkInfo.setRemarks(remarkList);
          remarkInfoList.add(remarkInfo);
        }
      }
    }
    Long timeTaken = DCPEUtil.getTimeTaken(start);
    //todo - why bellow is commented?
    //String responseString = DCPEUtil.convertToString(remarkInfoList);
    //LOGGER.info("processRemarksResponse : traceId={}|timeTaken={}|rows={}", traceId, timeTaken, responseString);
    return remarkInfoList;
  }

}
