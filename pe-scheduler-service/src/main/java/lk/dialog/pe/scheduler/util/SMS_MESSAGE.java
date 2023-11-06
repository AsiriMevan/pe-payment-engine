package lk.dialog.pe.scheduler.util;

public enum SMS_MESSAGE {
     ACTIVE_GSM_CX_VALID_PAY(0),
     INACTIVE_GSM_CX_SUFFICIENT_PAY(1),
     INACTIVE_GSM_CX_IN_SUFFICIENT_PAY(2),
     UNSUCCESSFUL_GSM_PAY(3),

     ACTIVE_DTV_CX_VALID_PAY(4),
     INACTIVE_DTV_CX_SUFFICIENT_PAY(5),
     INACTIVE_DTV_CX_IN_SUFFICIENT_PAY(6),

     ACTIVE_DBN_CX_VALID_PAY(7),
     INACTIVE_DBN_CX_SUFFICIENT_PAY(8),
     INACTIVE_DBN_CX_IN_SUFFICIENT_PAY(9),
     INACTIVE_VOLTE_CX_IN_SUFFICIENT_PAY(10);

     private int value ;

     SMS_MESSAGE(int value){
          this.value = value;
     }

     public int getValue() {
          return value;
     }
}
