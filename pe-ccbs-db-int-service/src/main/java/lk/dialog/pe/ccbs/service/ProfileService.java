package lk.dialog.pe.ccbs.service;

import java.util.List;

import lk.dialog.pe.ccbs.dto.CRMProfileRequest;
import lk.dialog.pe.ccbs.dto.Profile;
import lk.dialog.pe.common.exception.DCPEException;

public interface ProfileService {

	public List<Profile>  getProfilesByInvoiceNo(String invoiceNo, String traceId)throws DCPEException ;

	public List<Profile>  getVolteProfilesById(CRMProfileRequest crmProfileRequest, String traceId) throws DCPEException ;

	public List<Profile>  getProfilesById(CRMProfileRequest crmProfileRequest, String traceId) throws DCPEException ;

	public List<Profile> getProfilesByAccount(List<String> mobileList, List<String> contractList, String traceId) throws DCPEException ;

	public List<Profile> getProfilesByBulkAccount(List<String> mobileList, List<String> contractList, String traceId) throws DCPEException ;

}
