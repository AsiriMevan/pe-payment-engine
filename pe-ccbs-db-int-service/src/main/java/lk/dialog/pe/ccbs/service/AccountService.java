package lk.dialog.pe.ccbs.service;

import java.util.List;

import lk.dialog.pe.ccbs.dto.Account;
import lk.dialog.pe.common.exception.DCPEException;

public interface AccountService {

	public List<Account>  getAccontNoByInvoiceNo(String invoiceNo, String traceId) throws DCPEException ;

	public List<Account>  getDBNAccountNoByInvoiceNo(String invoiceNo, String traceId) throws DCPEException ;

}
