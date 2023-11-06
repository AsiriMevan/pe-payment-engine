package lk.dialog.pe.scheduler.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter@Setter
public class QueryPaymentsSummaryRequest {
    private String contractNo;
    private String receiptNo;
    private Date receiptFromDate;
    private Date receiptToDate;
    private String receiptBranch;
    private String branchCounter;
    private String chequeBankCode;
    private String chequeBankBranchCode;
    private String chequeNo;
    private String user;

}