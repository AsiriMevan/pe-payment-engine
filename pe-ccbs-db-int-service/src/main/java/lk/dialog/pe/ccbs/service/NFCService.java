package lk.dialog.pe.ccbs.service;

import java.util.List;

import lk.dialog.pe.ccbs.dto.Account;
import lk.dialog.pe.common.exception.DCPEException;

public interface NFCService {

	public String getNfcEmailByContract(String contractNo, String traceId) throws DCPEException;

	public List<Account> getNfcWifiAccountNoByInvoiceNo(String invoiceNo, String traceId) throws DCPEException;

}
