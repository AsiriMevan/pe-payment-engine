
/**
 * ECAPaymentCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package lk.dialog.pe.scheduler.soap.payment;

import lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub;

/**
     *  ECAPaymentCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class ECAPaymentCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public ECAPaymentCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public ECAPaymentCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for createAccountPaymentRecharge_1 method
            * override this method for handling normal response from createAccountPaymentRecharge_1 operation
            */
           public void receiveResultcreateAccountPaymentRecharge_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.CreateAccountPaymentRecharge_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createAccountPaymentRecharge_1 operation
           */
            public void receiveErrorcreateAccountPaymentRecharge_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createAccountPaymentRecharge_2 method
            * override this method for handling normal response from createAccountPaymentRecharge_2 operation
            */
           public void receiveResultcreateAccountPaymentRecharge_2(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.CreateAccountPaymentRecharge_2Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createAccountPaymentRecharge_2 operation
           */
            public void receiveErrorcreateAccountPaymentRecharge_2(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for modifyAccountBudgetPaymentPlan_1 method
            * override this method for handling normal response from modifyAccountBudgetPaymentPlan_1 operation
            */
           public void receiveResultmodifyAccountBudgetPaymentPlan_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ModifyAccountBudgetPaymentPlan_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from modifyAccountBudgetPaymentPlan_1 operation
           */
            public void receiveErrormodifyAccountBudgetPaymentPlan_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllocationsForBill_2 method
            * override this method for handling normal response from queryAllocationsForBill_2 operation
            */
           public void receiveResultqueryAllocationsForBill_2(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllocationsForBill_2Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllocationsForBill_2 operation
           */
            public void receiveErrorqueryAllocationsForBill_2(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for modifyAccountBudgetPaymentPlan_2 method
            * override this method for handling normal response from modifyAccountBudgetPaymentPlan_2 operation
            */
           public void receiveResultmodifyAccountBudgetPaymentPlan_2(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ModifyAccountBudgetPaymentPlan_2Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from modifyAccountBudgetPaymentPlan_2 operation
           */
            public void receiveErrormodifyAccountBudgetPaymentPlan_2(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for readAllPaymentMethodDataForICO_5_1 method
            * override this method for handling normal response from readAllPaymentMethodDataForICO_5_1 operation
            */
           public void receiveResultreadAllPaymentMethodDataForICO_5_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ReadAllPaymentMethodDataForICO_5_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from readAllPaymentMethodDataForICO_5_1 operation
           */
            public void receiveErrorreadAllPaymentMethodDataForICO_5_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for readPaymentOrigin_5_1 method
            * override this method for handling normal response from readPaymentOrigin_5_1 operation
            */
           public void receiveResultreadPaymentOrigin_5_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ReadPaymentOrigin_5_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from readPaymentOrigin_5_1 operation
           */
            public void receiveErrorreadPaymentOrigin_5_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for readPaymentMethod_5_1 method
            * override this method for handling normal response from readPaymentMethod_5_1 operation
            */
           public void receiveResultreadPaymentMethod_5_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ReadPaymentMethod_5_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from readPaymentMethod_5_1 operation
           */
            public void receiveErrorreadPaymentMethod_5_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllocationsForBill_1 method
            * override this method for handling normal response from queryAllocationsForBill_1 operation
            */
           public void receiveResultqueryAllocationsForBill_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllocationsForBill_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllocationsForBill_1 operation
           */
            public void receiveErrorqueryAllocationsForBill_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for modifyPhysicalPayment_1 method
            * override this method for handling normal response from modifyPhysicalPayment_1 operation
            */
           public void receiveResultmodifyPhysicalPayment_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ModifyPhysicalPayment_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from modifyPhysicalPayment_1 operation
           */
            public void receiveErrormodifyPhysicalPayment_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for readAccountPaymentRequests_5_1 method
            * override this method for handling normal response from readAccountPaymentRequests_5_1 operation
            */
           public void receiveResultreadAccountPaymentRequests_5_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ReadAccountPaymentRequests_5_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from readAccountPaymentRequests_5_1 operation
           */
            public void receiveErrorreadAccountPaymentRequests_5_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for startAccountBudgetPaymentPlan_1 method
            * override this method for handling normal response from startAccountBudgetPaymentPlan_1 operation
            */
           public void receiveResultstartAccountBudgetPaymentPlan_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.StartAccountBudgetPaymentPlan_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from startAccountBudgetPaymentPlan_1 operation
           */
            public void receiveErrorstartAccountBudgetPaymentPlan_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllProductInstancesUnderSubAccountBPP_1 method
            * override this method for handling normal response from queryAllProductInstancesUnderSubAccountBPP_1 operation
            */
           public void receiveResultqueryAllProductInstancesUnderSubAccountBPP_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllProductInstancesUnderSubAccountBPP_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllProductInstancesUnderSubAccountBPP_1 operation
           */
            public void receiveErrorqueryAllProductInstancesUnderSubAccountBPP_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for startAccountBudgetPaymentPlan_3 method
            * override this method for handling normal response from startAccountBudgetPaymentPlan_3 operation
            */
           public void receiveResultstartAccountBudgetPaymentPlan_3(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.StartAccountBudgetPaymentPlan_3Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from startAccountBudgetPaymentPlan_3 operation
           */
            public void receiveErrorstartAccountBudgetPaymentPlan_3(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for startAccountBudgetPaymentPlan_2 method
            * override this method for handling normal response from startAccountBudgetPaymentPlan_2 operation
            */
           public void receiveResultstartAccountBudgetPaymentPlan_2(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.StartAccountBudgetPaymentPlan_2Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from startAccountBudgetPaymentPlan_2 operation
           */
            public void receiveErrorstartAccountBudgetPaymentPlan_2(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllBudgetPaymentPlans_2 method
            * override this method for handling normal response from queryAllBudgetPaymentPlans_2 operation
            */
           public void receiveResultqueryAllBudgetPaymentPlans_2(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllBudgetPaymentPlans_2Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllBudgetPaymentPlans_2 operation
           */
            public void receiveErrorqueryAllBudgetPaymentPlans_2(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllBudgetPaymentPlans_1 method
            * override this method for handling normal response from queryAllBudgetPaymentPlans_1 operation
            */
           public void receiveResultqueryAllBudgetPaymentPlans_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllBudgetPaymentPlans_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllBudgetPaymentPlans_1 operation
           */
            public void receiveErrorqueryAllBudgetPaymentPlans_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryPaymentRechargesForAccount_1 method
            * override this method for handling normal response from queryPaymentRechargesForAccount_1 operation
            */
           public void receiveResultqueryPaymentRechargesForAccount_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryPaymentRechargesForAccount_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryPaymentRechargesForAccount_1 operation
           */
            public void receiveErrorqueryPaymentRechargesForAccount_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryPaymentRechargesForAccount_2 method
            * override this method for handling normal response from queryPaymentRechargesForAccount_2 operation
            */
           public void receiveResultqueryPaymentRechargesForAccount_2(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryPaymentRechargesForAccount_2Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryPaymentRechargesForAccount_2 operation
           */
            public void receiveErrorqueryPaymentRechargesForAccount_2(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createPhysicalPayment_2 method
            * override this method for handling normal response from createPhysicalPayment_2 operation
            */
           public void receiveResultcreatePhysicalPayment_2(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.CreatePhysicalPayment_2Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createPhysicalPayment_2 operation
           */
            public void receiveErrorcreatePhysicalPayment_2(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllocationsForAccountPayment_1 method
            * override this method for handling normal response from queryAllocationsForAccountPayment_1 operation
            */
           public void receiveResultqueryAllocationsForAccountPayment_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllocationsForAccountPayment_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllocationsForAccountPayment_1 operation
           */
            public void receiveErrorqueryAllocationsForAccountPayment_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAccountBudgetPaymentPlan_1 method
            * override this method for handling normal response from queryAccountBudgetPaymentPlan_1 operation
            */
           public void receiveResultqueryAccountBudgetPaymentPlan_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAccountBudgetPaymentPlan_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAccountBudgetPaymentPlan_1 operation
           */
            public void receiveErrorqueryAccountBudgetPaymentPlan_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllocationsForAccountPayment_2 method
            * override this method for handling normal response from queryAllocationsForAccountPayment_2 operation
            */
           public void receiveResultqueryAllocationsForAccountPayment_2(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllocationsForAccountPayment_2Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllocationsForAccountPayment_2 operation
           */
            public void receiveErrorqueryAllocationsForAccountPayment_2(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createPhysicalPayment_1 method
            * override this method for handling normal response from createPhysicalPayment_1 operation
            */
           public void receiveResultcreatePhysicalPayment_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.CreatePhysicalPayment_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createPhysicalPayment_1 operation
           */
            public void receiveErrorcreatePhysicalPayment_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for cancelAllocation_1 method
            * override this method for handling normal response from cancelAllocation_1 operation
            */
           public void receiveResultcancelAllocation_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.CancelAllocation_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from cancelAllocation_1 operation
           */
            public void receiveErrorcancelAllocation_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryPaymentRequest_1 method
            * override this method for handling normal response from queryPaymentRequest_1 operation
            */
           public void receiveResultqueryPaymentRequest_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryPaymentRequest_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryPaymentRequest_1 operation
           */
            public void receiveErrorqueryPaymentRequest_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAccountBudgetPaymentPlan_2 method
            * override this method for handling normal response from queryAccountBudgetPaymentPlan_2 operation
            */
           public void receiveResultqueryAccountBudgetPaymentPlan_2(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAccountBudgetPaymentPlan_2Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAccountBudgetPaymentPlan_2 operation
           */
            public void receiveErrorqueryAccountBudgetPaymentPlan_2(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllAccountPayments_3 method
            * override this method for handling normal response from queryAllAccountPayments_3 operation
            */
           public void receiveResultqueryAllAccountPayments_3(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllAccountPayments_3Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllAccountPayments_3 operation
           */
            public void receiveErrorqueryAllAccountPayments_3(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllAccountPayments_2 method
            * override this method for handling normal response from queryAllAccountPayments_2 operation
            */
           public void receiveResultqueryAllAccountPayments_2(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllAccountPayments_2Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllAccountPayments_2 operation
           */
            public void receiveErrorqueryAllAccountPayments_2(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllAccountPayments_5 method
            * override this method for handling normal response from queryAllAccountPayments_5 operation
            */
           public void receiveResultqueryAllAccountPayments_5(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllAccountPayments_5Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllAccountPayments_5 operation
           */
            public void receiveErrorqueryAllAccountPayments_5(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllAccountPayments_4 method
            * override this method for handling normal response from queryAllAccountPayments_4 operation
            */
           public void receiveResultqueryAllAccountPayments_4(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllAccountPayments_4Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllAccountPayments_4 operation
           */
            public void receiveErrorqueryAllAccountPayments_4(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllAccountPayments_1 method
            * override this method for handling normal response from queryAllAccountPayments_1 operation
            */
           public void receiveResultqueryAllAccountPayments_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllAccountPayments_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllAccountPayments_1 operation
           */
            public void receiveErrorqueryAllAccountPayments_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for cancelPaymentRequest_1 method
            * override this method for handling normal response from cancelPaymentRequest_1 operation
            */
           public void receiveResultcancelPaymentRequest_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.CancelPaymentRequest_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from cancelPaymentRequest_1 operation
           */
            public void receiveErrorcancelPaymentRequest_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getPayDateAndAmountForAccountBudgetPaymentPlan_1 method
            * override this method for handling normal response from getPayDateAndAmountForAccountBudgetPaymentPlan_1 operation
            */
           public void receiveResultgetPayDateAndAmountForAccountBudgetPaymentPlan_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.GetPayDateAndAmountForAccountBudgetPaymentPlan_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getPayDateAndAmountForAccountBudgetPaymentPlan_1 operation
           */
            public void receiveErrorgetPayDateAndAmountForAccountBudgetPaymentPlan_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for readAllPaymentMethods_5_1 method
            * override this method for handling normal response from readAllPaymentMethods_5_1 operation
            */
           public void receiveResultreadAllPaymentMethods_5_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ReadAllPaymentMethods_5_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from readAllPaymentMethods_5_1 operation
           */
            public void receiveErrorreadAllPaymentMethods_5_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAvailableBudgetPaymentPlansForAccount_1 method
            * override this method for handling normal response from queryAvailableBudgetPaymentPlansForAccount_1 operation
            */
           public void receiveResultqueryAvailableBudgetPaymentPlansForAccount_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAvailableBudgetPaymentPlansForAccount_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAvailableBudgetPaymentPlansForAccount_1 operation
           */
            public void receiveErrorqueryAvailableBudgetPaymentPlansForAccount_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllAccountPaymentsForPhysicalPayment_1 method
            * override this method for handling normal response from queryAllAccountPaymentsForPhysicalPayment_1 operation
            */
           public void receiveResultqueryAllAccountPaymentsForPhysicalPayment_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllAccountPaymentsForPhysicalPayment_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllAccountPaymentsForPhysicalPayment_1 operation
           */
            public void receiveErrorqueryAllAccountPaymentsForPhysicalPayment_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllAccountPaymentsForPhysicalPayment_2 method
            * override this method for handling normal response from queryAllAccountPaymentsForPhysicalPayment_2 operation
            */
           public void receiveResultqueryAllAccountPaymentsForPhysicalPayment_2(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllAccountPaymentsForPhysicalPayment_2Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllAccountPaymentsForPhysicalPayment_2 operation
           */
            public void receiveErrorqueryAllAccountPaymentsForPhysicalPayment_2(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for cancelAccountPayment_1 method
            * override this method for handling normal response from cancelAccountPayment_1 operation
            */
           public void receiveResultcancelAccountPayment_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.CancelAccountPayment_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from cancelAccountPayment_1 operation
           */
            public void receiveErrorcancelAccountPayment_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createPhysicalPaymentAndAccountPaymentsSuper_1 method
            * override this method for handling normal response from createPhysicalPaymentAndAccountPaymentsSuper_1 operation
            */
           public void receiveResultcreatePhysicalPaymentAndAccountPaymentsSuper_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.CreatePhysicalPaymentAndAccountPaymentsSuper_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createPhysicalPaymentAndAccountPaymentsSuper_1 operation
           */
            public void receiveErrorcreatePhysicalPaymentAndAccountPaymentsSuper_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createPhysicalPaymentAndAccountPaymentsSuper_2 method
            * override this method for handling normal response from createPhysicalPaymentAndAccountPaymentsSuper_2 operation
            */
           public void receiveResultcreatePhysicalPaymentAndAccountPaymentsSuper_2(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.CreatePhysicalPaymentAndAccountPaymentsSuper_2Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createPhysicalPaymentAndAccountPaymentsSuper_2 operation
           */
            public void receiveErrorcreatePhysicalPaymentAndAccountPaymentsSuper_2(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for endAccountBudgetPaymentPlan_3 method
            * override this method for handling normal response from endAccountBudgetPaymentPlan_3 operation
            */
           public void receiveResultendAccountBudgetPaymentPlan_3(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.EndAccountBudgetPaymentPlan_3Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from endAccountBudgetPaymentPlan_3 operation
           */
            public void receiveErrorendAccountBudgetPaymentPlan_3(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for endAccountBudgetPaymentPlan_2 method
            * override this method for handling normal response from endAccountBudgetPaymentPlan_2 operation
            */
           public void receiveResultendAccountBudgetPaymentPlan_2(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.EndAccountBudgetPaymentPlan_2Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from endAccountBudgetPaymentPlan_2 operation
           */
            public void receiveErrorendAccountBudgetPaymentPlan_2(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deletePaymentAllocation_1 method
            * override this method for handling normal response from deletePaymentAllocation_1 operation
            */
           public void receiveResultdeletePaymentAllocation_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.DeletePaymentAllocation_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deletePaymentAllocation_1 operation
           */
            public void receiveErrordeletePaymentAllocation_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for endAccountBudgetPaymentPlan_1 method
            * override this method for handling normal response from endAccountBudgetPaymentPlan_1 operation
            */
           public void receiveResultendAccountBudgetPaymentPlan_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.EndAccountBudgetPaymentPlan_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from endAccountBudgetPaymentPlan_1 operation
           */
            public void receiveErrorendAccountBudgetPaymentPlan_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for modifyAccountBudgetPlanEndDate_1 method
            * override this method for handling normal response from modifyAccountBudgetPlanEndDate_1 operation
            */
           public void receiveResultmodifyAccountBudgetPlanEndDate_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ModifyAccountBudgetPlanEndDate_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from modifyAccountBudgetPlanEndDate_1 operation
           */
            public void receiveErrormodifyAccountBudgetPlanEndDate_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addPaymentMandateToAccount_5_1 method
            * override this method for handling normal response from addPaymentMandateToAccount_5_1 operation
            */
           public void receiveResultaddPaymentMandateToAccount_5_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.AddPaymentMandateToAccount_5_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addPaymentMandateToAccount_5_1 operation
           */
            public void receiveErroraddPaymentMandateToAccount_5_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for modifyPaymentMandate_1 method
            * override this method for handling normal response from modifyPaymentMandate_1 operation
            */
           public void receiveResultmodifyPaymentMandate_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ModifyPaymentMandate_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from modifyPaymentMandate_1 operation
           */
            public void receiveErrormodifyPaymentMandate_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllPhysicalPayments_3 method
            * override this method for handling normal response from queryAllPhysicalPayments_3 operation
            */
           public void receiveResultqueryAllPhysicalPayments_3(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllPhysicalPayments_3Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllPhysicalPayments_3 operation
           */
            public void receiveErrorqueryAllPhysicalPayments_3(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllPhysicalPayments_2 method
            * override this method for handling normal response from queryAllPhysicalPayments_2 operation
            */
           public void receiveResultqueryAllPhysicalPayments_2(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllPhysicalPayments_2Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllPhysicalPayments_2 operation
           */
            public void receiveErrorqueryAllPhysicalPayments_2(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for modifyPaymentAllocation_1 method
            * override this method for handling normal response from modifyPaymentAllocation_1 operation
            */
           public void receiveResultmodifyPaymentAllocation_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ModifyPaymentAllocation_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from modifyPaymentAllocation_1 operation
           */
            public void receiveErrormodifyPaymentAllocation_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for resubmitPaymentRequest_1 method
            * override this method for handling normal response from resubmitPaymentRequest_1 operation
            */
           public void receiveResultresubmitPaymentRequest_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ResubmitPaymentRequest_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from resubmitPaymentRequest_1 operation
           */
            public void receiveErrorresubmitPaymentRequest_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createPaymentRequest_1 method
            * override this method for handling normal response from createPaymentRequest_1 operation
            */
           public void receiveResultcreatePaymentRequest_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.CreatePaymentRequest_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createPaymentRequest_1 operation
           */
            public void receiveErrorcreatePaymentRequest_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllocationReceivableTargetsForOpenItem_1 method
            * override this method for handling normal response from queryAllocationReceivableTargetsForOpenItem_1 operation
            */
           public void receiveResultqueryAllocationReceivableTargetsForOpenItem_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllocationReceivableTargetsForOpenItem_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllocationReceivableTargetsForOpenItem_1 operation
           */
            public void receiveErrorqueryAllocationReceivableTargetsForOpenItem_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createReconciledCash_1 method
            * override this method for handling normal response from createReconciledCash_1 operation
            */
           public void receiveResultcreateReconciledCash_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.CreateReconciledCash_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createReconciledCash_1 operation
           */
            public void receiveErrorcreateReconciledCash_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllocationReceivableTargetsForOpenItem_2 method
            * override this method for handling normal response from queryAllocationReceivableTargetsForOpenItem_2 operation
            */
           public void receiveResultqueryAllocationReceivableTargetsForOpenItem_2(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllocationReceivableTargetsForOpenItem_2Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllocationReceivableTargetsForOpenItem_2 operation
           */
            public void receiveErrorqueryAllocationReceivableTargetsForOpenItem_2(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for failPhysicalPayment_1 method
            * override this method for handling normal response from failPhysicalPayment_1 operation
            */
           public void receiveResultfailPhysicalPayment_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.FailPhysicalPayment_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from failPhysicalPayment_1 operation
           */
            public void receiveErrorfailPhysicalPayment_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createPaymentRequest_2 method
            * override this method for handling normal response from createPaymentRequest_2 operation
            */
           public void receiveResultcreatePaymentRequest_2(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.CreatePaymentRequest_2Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createPaymentRequest_2 operation
           */
            public void receiveErrorcreatePaymentRequest_2(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllSubscriptionsUnderSubAccountBPP_1 method
            * override this method for handling normal response from queryAllSubscriptionsUnderSubAccountBPP_1 operation
            */
           public void receiveResultqueryAllSubscriptionsUnderSubAccountBPP_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllSubscriptionsUnderSubAccountBPP_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllSubscriptionsUnderSubAccountBPP_1 operation
           */
            public void receiveErrorqueryAllSubscriptionsUnderSubAccountBPP_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteReconciledCash_1 method
            * override this method for handling normal response from deleteReconciledCash_1 operation
            */
           public void receiveResultdeleteReconciledCash_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.DeleteReconciledCash_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteReconciledCash_1 operation
           */
            public void receiveErrordeleteReconciledCash_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAccountPayment_2 method
            * override this method for handling normal response from queryAccountPayment_2 operation
            */
           public void receiveResultqueryAccountPayment_2(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAccountPayment_2Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAccountPayment_2 operation
           */
            public void receiveErrorqueryAccountPayment_2(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAccountPayment_3 method
            * override this method for handling normal response from queryAccountPayment_3 operation
            */
           public void receiveResultqueryAccountPayment_3(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAccountPayment_3Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAccountPayment_3 operation
           */
            public void receiveErrorqueryAccountPayment_3(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAccountPayment_4 method
            * override this method for handling normal response from queryAccountPayment_4 operation
            */
           public void receiveResultqueryAccountPayment_4(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAccountPayment_4Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAccountPayment_4 operation
           */
            public void receiveErrorqueryAccountPayment_4(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAccountPayment_1 method
            * override this method for handling normal response from queryAccountPayment_1 operation
            */
           public void receiveResultqueryAccountPayment_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAccountPayment_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAccountPayment_1 operation
           */
            public void receiveErrorqueryAccountPayment_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getRevisedCollectionDateForAGivenPeriod_1 method
            * override this method for handling normal response from getRevisedCollectionDateForAGivenPeriod_1 operation
            */
           public void receiveResultgetRevisedCollectionDateForAGivenPeriod_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.GetRevisedCollectionDateForAGivenPeriod_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getRevisedCollectionDateForAGivenPeriod_1 operation
           */
            public void receiveErrorgetRevisedCollectionDateForAGivenPeriod_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllPhysicalPayments_1 method
            * override this method for handling normal response from queryAllPhysicalPayments_1 operation
            */
           public void receiveResultqueryAllPhysicalPayments_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllPhysicalPayments_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllPhysicalPayments_1 operation
           */
            public void receiveErrorqueryAllPhysicalPayments_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for readPaymentMandate_5_1 method
            * override this method for handling normal response from readPaymentMandate_5_1 operation
            */
           public void receiveResultreadPaymentMandate_5_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ReadPaymentMandate_5_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from readPaymentMandate_5_1 operation
           */
            public void receiveErrorreadPaymentMandate_5_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for cancelPhysicalPayment_1 method
            * override this method for handling normal response from cancelPhysicalPayment_1 operation
            */
           public void receiveResultcancelPhysicalPayment_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.CancelPhysicalPayment_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from cancelPhysicalPayment_1 operation
           */
            public void receiveErrorcancelPhysicalPayment_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for readAccountPayments_5_1 method
            * override this method for handling normal response from readAccountPayments_5_1 operation
            */
           public void receiveResultreadAccountPayments_5_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ReadAccountPayments_5_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from readAccountPayments_5_1 operation
           */
            public void receiveErrorreadAccountPayments_5_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for readPaymentMethod_6 method
            * override this method for handling normal response from readPaymentMethod_6 operation
            */
           public void receiveResultreadPaymentMethod_6(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ReadPaymentMethod_6Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from readPaymentMethod_6 operation
           */
            public void receiveErrorreadPaymentMethod_6(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for cancelExpectedBudgetPayment_1 method
            * override this method for handling normal response from cancelExpectedBudgetPayment_1 operation
            */
           public void receiveResultcancelExpectedBudgetPayment_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.CancelExpectedBudgetPayment_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from cancelExpectedBudgetPayment_1 operation
           */
            public void receiveErrorcancelExpectedBudgetPayment_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for modifyAccountPayment_1 method
            * override this method for handling normal response from modifyAccountPayment_1 operation
            */
           public void receiveResultmodifyAccountPayment_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ModifyAccountPayment_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from modifyAccountPayment_1 operation
           */
            public void receiveErrormodifyAccountPayment_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for modifyExpectedBudgetPayment_1 method
            * override this method for handling normal response from modifyExpectedBudgetPayment_1 operation
            */
           public void receiveResultmodifyExpectedBudgetPayment_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ModifyExpectedBudgetPayment_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from modifyExpectedBudgetPayment_1 operation
           */
            public void receiveErrormodifyExpectedBudgetPayment_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for reprocessPaymentRecharge_1 method
            * override this method for handling normal response from reprocessPaymentRecharge_1 operation
            */
           public void receiveResultreprocessPaymentRecharge_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ReprocessPaymentRecharge_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from reprocessPaymentRecharge_1 operation
           */
            public void receiveErrorreprocessPaymentRecharge_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for autoAllocatePayment_1 method
            * override this method for handling normal response from autoAllocatePayment_1 operation
            */
           public void receiveResultautoAllocatePayment_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.AutoAllocatePayment_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from autoAllocatePayment_1 operation
           */
            public void receiveErrorautoAllocatePayment_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllocationInvoiceTargetsForOpenItem_1 method
            * override this method for handling normal response from queryAllocationInvoiceTargetsForOpenItem_1 operation
            */
           public void receiveResultqueryAllocationInvoiceTargetsForOpenItem_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllocationInvoiceTargetsForOpenItem_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllocationInvoiceTargetsForOpenItem_1 operation
           */
            public void receiveErrorqueryAllocationInvoiceTargetsForOpenItem_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryExpectedBudgetPaymentsForAccount_1 method
            * override this method for handling normal response from queryExpectedBudgetPaymentsForAccount_1 operation
            */
           public void receiveResultqueryExpectedBudgetPaymentsForAccount_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryExpectedBudgetPaymentsForAccount_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryExpectedBudgetPaymentsForAccount_1 operation
           */
            public void receiveErrorqueryExpectedBudgetPaymentsForAccount_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for collectionsWriteOff_1 method
            * override this method for handling normal response from collectionsWriteOff_1 operation
            */
           public void receiveResultcollectionsWriteOff_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.CollectionsWriteOff_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from collectionsWriteOff_1 operation
           */
            public void receiveErrorcollectionsWriteOff_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for reinstateExpectedBudgetPayment_1 method
            * override this method for handling normal response from reinstateExpectedBudgetPayment_1 operation
            */
           public void receiveResultreinstateExpectedBudgetPayment_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ReinstateExpectedBudgetPayment_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from reinstateExpectedBudgetPayment_1 operation
           */
            public void receiveErrorreinstateExpectedBudgetPayment_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for readAllPaymentOrigins_5_1 method
            * override this method for handling normal response from readAllPaymentOrigins_5_1 operation
            */
           public void receiveResultreadAllPaymentOrigins_5_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ReadAllPaymentOrigins_5_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from readAllPaymentOrigins_5_1 operation
           */
            public void receiveErrorreadAllPaymentOrigins_5_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for querySingleBudgetPaymentPlan_1 method
            * override this method for handling normal response from querySingleBudgetPaymentPlan_1 operation
            */
           public void receiveResultquerySingleBudgetPaymentPlan_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QuerySingleBudgetPaymentPlan_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from querySingleBudgetPaymentPlan_1 operation
           */
            public void receiveErrorquerySingleBudgetPaymentPlan_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for querySingleBudgetPaymentPlan_2 method
            * override this method for handling normal response from querySingleBudgetPaymentPlan_2 operation
            */
           public void receiveResultquerySingleBudgetPaymentPlan_2(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QuerySingleBudgetPaymentPlan_2Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from querySingleBudgetPaymentPlan_2 operation
           */
            public void receiveErrorquerySingleBudgetPaymentPlan_2(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createPaymentAllocation_1 method
            * override this method for handling normal response from createPaymentAllocation_1 operation
            */
           public void receiveResultcreatePaymentAllocation_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.CreatePaymentAllocation_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createPaymentAllocation_1 operation
           */
            public void receiveErrorcreatePaymentAllocation_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createAccountPayment_3 method
            * override this method for handling normal response from createAccountPayment_3 operation
            */
           public void receiveResultcreateAccountPayment_3(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.CreateAccountPayment_3Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createAccountPayment_3 operation
           */
            public void receiveErrorcreateAccountPayment_3(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createAccountPayment_2 method
            * override this method for handling normal response from createAccountPayment_2 operation
            */
           public void receiveResultcreateAccountPayment_2(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.CreateAccountPayment_2Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createAccountPayment_2 operation
           */
            public void receiveErrorcreateAccountPayment_2(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllocationTargetsForBalanceForward_1 method
            * override this method for handling normal response from queryAllocationTargetsForBalanceForward_1 operation
            */
           public void receiveResultqueryAllocationTargetsForBalanceForward_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllocationTargetsForBalanceForward_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllocationTargetsForBalanceForward_1 operation
           */
            public void receiveErrorqueryAllocationTargetsForBalanceForward_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createAccountPayment_4 method
            * override this method for handling normal response from createAccountPayment_4 operation
            */
           public void receiveResultcreateAccountPayment_4(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.CreateAccountPayment_4Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createAccountPayment_4 operation
           */
            public void receiveErrorcreateAccountPayment_4(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllocationTargetsForBalanceForward_2 method
            * override this method for handling normal response from queryAllocationTargetsForBalanceForward_2 operation
            */
           public void receiveResultqueryAllocationTargetsForBalanceForward_2(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllocationTargetsForBalanceForward_2Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllocationTargetsForBalanceForward_2 operation
           */
            public void receiveErrorqueryAllocationTargetsForBalanceForward_2(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for representPaymentRequest_1 method
            * override this method for handling normal response from representPaymentRequest_1 operation
            */
           public void receiveResultrepresentPaymentRequest_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.RepresentPaymentRequest_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from representPaymentRequest_1 operation
           */
            public void receiveErrorrepresentPaymentRequest_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryBudgetPaymentPlanForAccount_1 method
            * override this method for handling normal response from queryBudgetPaymentPlanForAccount_1 operation
            */
           public void receiveResultqueryBudgetPaymentPlanForAccount_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryBudgetPaymentPlanForAccount_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryBudgetPaymentPlanForAccount_1 operation
           */
            public void receiveErrorqueryBudgetPaymentPlanForAccount_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addBudgetBillingPlanToAccountProductAndSubscriptionSuper_1 method
            * override this method for handling normal response from addBudgetBillingPlanToAccountProductAndSubscriptionSuper_1 operation
            */
           public void receiveResultaddBudgetBillingPlanToAccountProductAndSubscriptionSuper_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.AddBudgetBillingPlanToAccountProductAndSubscriptionSuper_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addBudgetBillingPlanToAccountProductAndSubscriptionSuper_1 operation
           */
            public void receiveErroraddBudgetBillingPlanToAccountProductAndSubscriptionSuper_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createAccountPayment_1 method
            * override this method for handling normal response from createAccountPayment_1 operation
            */
           public void receiveResultcreateAccountPayment_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.CreateAccountPayment_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createAccountPayment_1 operation
           */
            public void receiveErrorcreateAccountPayment_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for modifyPaymentRequest_1 method
            * override this method for handling normal response from modifyPaymentRequest_1 operation
            */
           public void receiveResultmodifyPaymentRequest_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ModifyPaymentRequest_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from modifyPaymentRequest_1 operation
           */
            public void receiveErrormodifyPaymentRequest_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for readAccountPaymentMandatesForPaymentMethods_5_1 method
            * override this method for handling normal response from readAccountPaymentMandatesForPaymentMethods_5_1 operation
            */
           public void receiveResultreadAccountPaymentMandatesForPaymentMethods_5_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ReadAccountPaymentMandatesForPaymentMethods_5_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from readAccountPaymentMandatesForPaymentMethods_5_1 operation
           */
            public void receiveErrorreadAccountPaymentMandatesForPaymentMethods_5_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for modifyReconciledCash_1 method
            * override this method for handling normal response from modifyReconciledCash_1 operation
            */
           public void receiveResultmodifyReconciledCash_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ModifyReconciledCash_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from modifyReconciledCash_1 operation
           */
            public void receiveErrormodifyReconciledCash_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for readAccountPayment_5_1 method
            * override this method for handling normal response from readAccountPayment_5_1 operation
            */
           public void receiveResultreadAccountPayment_5_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ReadAccountPayment_5_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from readAccountPayment_5_1 operation
           */
            public void receiveErrorreadAccountPayment_5_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllAccountPaymentsForDiscountName_1 method
            * override this method for handling normal response from queryAllAccountPaymentsForDiscountName_1 operation
            */
           public void receiveResultqueryAllAccountPaymentsForDiscountName_1(
                    lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.QueryAllAccountPaymentsForDiscountName_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllAccountPaymentsForDiscountName_1 operation
           */
            public void receiveErrorqueryAllAccountPaymentsForDiscountName_1(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllPaymentRequestForAccount_1 method
            * override this method for handling normal response from queryAllPaymentRequestForAccount_1 operation
            */
           public void receiveResultqueryAllPaymentRequestForAccount_1(
                    ECAPaymentStub.QueryAllPaymentRequestForAccount_1Output result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllPaymentRequestForAccount_1 operation
           */
            public void receiveErrorqueryAllPaymentRequestForAccount_1(Exception e) {
            }
                


    }
    