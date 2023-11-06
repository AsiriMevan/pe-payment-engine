package lk.dialog.pe.ccbs.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lk.dialog.pe.ccbs.dto.AccountRef;
import lk.dialog.pe.ccbs.dto.HotlineRemarkRequest;
import lk.dialog.pe.ccbs.dto.Remark;
import lk.dialog.pe.ccbs.dto.RemarkInfo;
import lk.dialog.pe.ccbs.repository.QueryExecuterRepository;
import lk.dialog.pe.ccbs.service.GetCcbsWifiRemarksService;
import lk.dialog.pe.ccbs.util.Constants;
import lk.dialog.pe.ccbs.util.DCPEUtil;
import lk.dialog.pe.ccbs.util.SQLQueryEnum;
import lk.dialog.pe.ccbs.util.Util;
import lk.dialog.pe.common.exception.DCPEException;

@Service
public class GetCcbsWifiRemarksServiceImpl implements GetCcbsWifiRemarksService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GetCcbsWifiRemarksServiceImpl.class);
	
	@Autowired
	private QueryExecuterRepository queryExecuterRepository;
	
	@Autowired
	@Qualifier("queryMap")
	private  Map<String, String>  map;	
	
	@Override
	public List<RemarkInfo> getCcbsWifiRemarks(HotlineRemarkRequest hotlineRemarkRequest, String traceId) throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("GetCcbsWifiRemarksRequest : traceId={}|HotlineRemarkRequest={}",traceId,hotlineRemarkRequest);
				
		List<AccountRef> accounts = hotlineRemarkRequest.getAccounts();
		List<String> wifiContractList = new ArrayList<>();
		
		for (AccountRef accountRef : accounts) {			
			wifiContractList.add(accountRef.getContractNo());					
		}		
		
		List<RemarkInfo> remarkInfoList = null; 
		
		remarkInfoList = getCcbsWifiRemark(wifiContractList, traceId);		
						
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		
		LOGGER.info("getHotLineRemarksResponse : traceId={}|timeTaken={}",traceId,timeTaken);
		return remarkInfoList;
	}
	
	public List<RemarkInfo> getCcbsWifiRemark(List<String> contractWifiList, String traceId) throws DCPEException {
		Instant start = Instant.now();
		String request = DCPEUtil.convertToString(contractWifiList);
		LOGGER.info("getCcbsWifiRemarkRequest : traceId={}|contractWifiList={}",traceId, request);

		String contractQueryStr;
		List<RemarkInfo> remarkInfoList = new ArrayList<>();
		List<Map<String, Object>> rows;
		StringBuilder contractQueryStrBuilder = new StringBuilder(map.get(SQLQueryEnum.SQL_CCBS_WIFI_REMARKS.getValue()));

		for (int i = 0; i < contractWifiList.size(); i++) {
			contractQueryStrBuilder.append("'" + contractWifiList.get(i) + "',");
		}
		
		if (!contractWifiList.isEmpty()) {
			contractQueryStr = contractQueryStrBuilder.deleteCharAt(contractQueryStrBuilder.length() - 1).append(") ORDER BY CONTRACT_ID").toString();
			rows = queryExecuterRepository.getDataByQuery(contractQueryStr, traceId);
			
			
			for (Map<String, Object> row : rows) {
				RemarkInfo remarkInfo = new RemarkInfo();
				remarkInfo.setContractNo(((java.math.BigDecimal) row.get(Constants.STR_CONTRACT_ID)).toString());
				String note = (String) row.get("NOTE");
				if (note != null) {
					note = note.replaceAll("[^\\p{ASCII}]", "").replaceAll("\\p{Cntrl}", "");

					if (remarkInfoList.contains(remarkInfo)) {
						int position = remarkInfoList.indexOf(remarkInfo);
						remarkInfo = remarkInfoList.get(position);
						Remark remark = new Remark();
						remark.setRemark(note);
						remark.setCreatedDate(Util.timeStampToCalenderTimeModiify((java.sql.Timestamp) row.get(Constants.STR_CREATED_DATE)));
						remark.setCreatedUser((String) row.get(Constants.STR_CREATED_USER));
						remarkInfo.addRemark(remark);
					} else {
						List<Remark> remarkList = new ArrayList<>();
						Remark remark = new Remark();
						remark.setRemark(note);
						remark.setCreatedDate(Util.timeStampToCalenderTimeModiify((java.sql.Timestamp) row.get(Constants.STR_CREATED_DATE)));
						remark.setCreatedUser((String) row.get(Constants.STR_CREATED_USER));
						remarkList.add(remark);
						remarkInfo.setRemarks(remarkList);
						remarkInfoList.add(remarkInfo);
					}

				}
			}
		}
		Long  timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(remarkInfoList);
		LOGGER.info("getCcbsWifiRemarkResponse : traceId={}|timeTaken={}|remarkInfoList={}",traceId, timeTaken, responseString);
		return remarkInfoList;
	}

	public List<RemarkInfo> processRemarks(List<Map<String, Object>> rows,String traceId) {
		Instant start = Instant.now();
		String requestString = DCPEUtil.convertToString(rows);
		LOGGER.info("processRemarksRequest : traceId={}|rows={}",traceId, requestString);

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
					remark.setCreatedDate(Util.timeStampToCalenderTimeModiify((java.sql.Timestamp) row.get(Constants.STR_CREATED_DATE)));
					remark.setCreatedUser((String) row.get(Constants.STR_CREATED_USER));
					remarkInfo.addRemark(remark);
				} else {
					List<Remark> remarkList = new ArrayList<>();
					Remark remark = new Remark();
					remark.setRemark(note);
					remark.setCreatedDate(Util.timeStampToCalenderTimeModiify((java.sql.Timestamp) row.get(Constants.STR_CREATED_DATE)));
					remark.setCreatedUser((String) row.get(Constants.STR_CREATED_USER));
					remarkList.add(remark);
					remarkInfo.setRemarks(remarkList);
					remarkInfoList.add(remarkInfo);
				}
			}
		}
		Long  timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("processRemarksRequest : traceId={}|timeTaken={}|rows={}",traceId, timeTaken, requestString);
		return remarkInfoList;
	}

}
