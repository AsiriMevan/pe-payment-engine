package lk.dialog.pe.customer.info.service;

import java.util.List;

import lk.dialog.pe.common.domain.CRMProfileRequest;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.customer.info.domain.Account;
import lk.dialog.pe.customer.info.domain.CCBSProfileRequest;
import lk.dialog.pe.customer.info.domain.Profile;
import lk.dialog.pe.credit.cam.dto.FixedNumberDTO;

public interface PersistanceIntegrationService {

	public List<Account> findAccountNoByInvoiceNo(String invoiceNumber, String traceId) throws DCPEException;

	public List<Account> findDBNAccountNoByInvoiceNo(String invoiceNo, String traceId) throws DCPEException;

	public List<Account> findCcbsWifiAccountNoByInvoiceNo(String invoiceNo, String traceId) throws DCPEException;

	public List<Profile> findVolteProfilesById(CRMProfileRequest jsonReq, String traceId) throws DCPEException;

	public List<Profile> findProfilesById(CRMProfileRequest jsonReq, String traceId) throws DCPEException;

	public List<Profile> findProfilesByInvoiceNo(String invoiceNo, String traceId) throws DCPEException;

	public String nfcEmailbyContract(String accountNo, String traceId) throws DCPEException;
	
	public List<Profile> findProfilesByAccount(CCBSProfileRequest ccbsProfileRequest, String traceId) throws DCPEException;

	public List<Profile> findProfilesByBulkAccount(CCBSProfileRequest ccbsProfileRequest, String traceId) throws DCPEException;

	FixedNumberDTO validateCcbsAvailablityForContract(String contractNo, String traceId);

	FixedNumberDTO validateCcbsAvailablityForConnRef(String mobileNo, String traceId);
}
