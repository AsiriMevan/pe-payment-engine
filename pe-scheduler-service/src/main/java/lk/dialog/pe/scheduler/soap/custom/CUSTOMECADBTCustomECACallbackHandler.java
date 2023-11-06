package lk.dialog.pe.scheduler.soap.custom;

/**
 * CUSTOMECADBTCustomECACallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */


import lk.dialog.pe.scheduler.soap.custom.CUSTOMECADBTCustomECAStub;

/**
     *  CUSTOMECADBTCustomECACallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class CUSTOMECADBTCustomECACallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public CUSTOMECADBTCustomECACallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public CUSTOMECADBTCustomECACallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for dbtQuerySettledAndUnSettledInvoices method
            * override this method for handling normal response from dbtQuerySettledAndUnSettledInvoices operation
            */
           public void receiveResultdbtQuerySettledAndUnSettledInvoices(
                    CUSTOMECADBTCustomECAStub.DbtQuerySettledAndUnSettledInvoicesOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQuerySettledAndUnSettledInvoices operation
           */
            public void receiveErrordbtQuerySettledAndUnSettledInvoices(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtModifyCommitmentInstance method
            * override this method for handling normal response from dbtModifyCommitmentInstance operation
            */
           public void receiveResultdbtModifyCommitmentInstance(
                    CUSTOMECADBTCustomECAStub.DbtModifyCommitmentInstanceOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtModifyCommitmentInstance operation
           */
            public void receiveErrordbtModifyCommitmentInstance(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtCreateSubscription method
            * override this method for handling normal response from dbtCreateSubscription operation
            */
           public void receiveResultdbtCreateSubscription(
                    CUSTOMECADBTCustomECAStub.DbtCreateSubscriptionOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtCreateSubscription operation
           */
            public void receiveErrordbtCreateSubscription(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryEnterpriseRentalData method
            * override this method for handling normal response from dbtQueryEnterpriseRentalData operation
            */
           public void receiveResultdbtQueryEnterpriseRentalData(
                    CUSTOMECADBTCustomECAStub.DbtQueryEnterpriseRentalDataOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryEnterpriseRentalData operation
           */
            public void receiveErrordbtQueryEnterpriseRentalData(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtCreateModifyCommitmentMembers method
            * override this method for handling normal response from dbtCreateModifyCommitmentMembers operation
            */
           public void receiveResultdbtCreateModifyCommitmentMembers(
                    CUSTOMECADBTCustomECAStub.DbtCreateModifyCommitmentMembersOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtCreateModifyCommitmentMembers operation
           */
            public void receiveErrordbtCreateModifyCommitmentMembers(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtCreatePhysicalPaymentAndAccountPaymentsNewCrDr method
            * override this method for handling normal response from dbtCreatePhysicalPaymentAndAccountPaymentsNewCrDr operation
            */
           public void receiveResultdbtCreatePhysicalPaymentAndAccountPaymentsNewCrDr(
                    CUSTOMECADBTCustomECAStub.DbtCreatePhysicalPaymentAndAccountPaymentsNewCrDrOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtCreatePhysicalPaymentAndAccountPaymentsNewCrDr operation
           */
            public void receiveErrordbtCreatePhysicalPaymentAndAccountPaymentsNewCrDr(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtSetCustChildToNewRoot method
            * override this method for handling normal response from dbtSetCustChildToNewRoot operation
            */
           public void receiveResultdbtSetCustChildToNewRoot(
                    CUSTOMECADBTCustomECAStub.DbtSetCustChildToNewRootOutput result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtSetCustChildToNewRoot operation
           */
            public void receiveErrordbtSetCustChildToNewRoot(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryStarPointDetails method
            * override this method for handling normal response from dbtQueryStarPointDetails operation
            */
           public void receiveResultdbtQueryStarPointDetails(
                    CUSTOMECADBTCustomECAStub.DbtQueryStarPointDetailsOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryStarPointDetails operation
           */
            public void receiveErrordbtQueryStarPointDetails(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryAllCommitmentPolicies method
            * override this method for handling normal response from dbtQueryAllCommitmentPolicies operation
            */
           public void receiveResultdbtQueryAllCommitmentPolicies(
                    CUSTOMECADBTCustomECAStub.DbtQueryAllCommitmentPoliciesOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryAllCommitmentPolicies operation
           */
            public void receiveErrordbtQueryAllCommitmentPolicies(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtRealizeCheque method
            * override this method for handling normal response from dbtRealizeCheque operation
            */
           public void receiveResultdbtRealizeCheque(
                    CUSTOMECADBTCustomECAStub.DbtRealizeChequeOutput result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtRealizeCheque operation
           */
            public void receiveErrordbtRealizeCheque(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtCreatePhysicalPaymentAndAccountPaymentsNew method
            * override this method for handling normal response from dbtCreatePhysicalPaymentAndAccountPaymentsNew operation
            */
           public void receiveResultdbtCreatePhysicalPaymentAndAccountPaymentsNew(
                    CUSTOMECADBTCustomECAStub.DbtCreatePhysicalPaymentAndAccountPaymentsNewOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtCreatePhysicalPaymentAndAccountPaymentsNew operation
           */
            public void receiveErrordbtCreatePhysicalPaymentAndAccountPaymentsNew(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtCreateBillChangeRequest method
            * override this method for handling normal response from dbtCreateBillChangeRequest operation
            */
           public void receiveResultdbtCreateBillChangeRequest(
                    CUSTOMECADBTCustomECAStub.DbtCreateBillChangeRequestOutput result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtCreateBillChangeRequest operation
           */
            public void receiveErrordbtCreateBillChangeRequest(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtCancelPhysicalPayment method
            * override this method for handling normal response from dbtCancelPhysicalPayment operation
            */
           public void receiveResultdbtCancelPhysicalPayment(
                    CUSTOMECADBTCustomECAStub.DbtCancelPhysicalPaymentOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtCancelPhysicalPayment operation
           */
            public void receiveErrordbtCancelPhysicalPayment(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtCancelBillChangeRequest method
            * override this method for handling normal response from dbtCancelBillChangeRequest operation
            */
           public void receiveResultdbtCancelBillChangeRequest(
                    CUSTOMECADBTCustomECAStub.DbtCancelBillChangeRequestOutput result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtCancelBillChangeRequest operation
           */
            public void receiveErrordbtCancelBillChangeRequest(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtCreatePhysicalPaymentAndAccountPayments method
            * override this method for handling normal response from dbtCreatePhysicalPaymentAndAccountPayments operation
            */
           public void receiveResultdbtCreatePhysicalPaymentAndAccountPayments(
                    CUSTOMECADBTCustomECAStub.DbtCreatePhysicalPaymentAndAccountPaymentsOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtCreatePhysicalPaymentAndAccountPayments operation
           */
            public void receiveErrordbtCreatePhysicalPaymentAndAccountPayments(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtProductTerminateAndActivate method
            * override this method for handling normal response from dbtProductTerminateAndActivate operation
            */
           public void receiveResultdbtProductTerminateAndActivate(
                    CUSTOMECADBTCustomECAStub.DbtProductTerminateAndActivateOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtProductTerminateAndActivate operation
           */
            public void receiveErrordbtProductTerminateAndActivate(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryAllPaymentDetails method
            * override this method for handling normal response from dbtQueryAllPaymentDetails operation
            */
           public void receiveResultdbtQueryAllPaymentDetails(
                    CUSTOMECADBTCustomECAStub.DbtQueryAllPaymentDetailsOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryAllPaymentDetails operation
           */
            public void receiveErrordbtQueryAllPaymentDetails(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryCustomerAttributehistory method
            * override this method for handling normal response from dbtQueryCustomerAttributehistory operation
            */
           public void receiveResultdbtQueryCustomerAttributehistory(
                    CUSTOMECADBTCustomECAStub.DbtQueryCustomerAttributehistoryOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryCustomerAttributehistory operation
           */
            public void receiveErrordbtQueryCustomerAttributehistory(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtManageEnterpriseRental method
            * override this method for handling normal response from dbtManageEnterpriseRental operation
            */
           public void receiveResultdbtManageEnterpriseRental(
                    CUSTOMECADBTCustomECAStub.DbtManageEnterpriseRentalOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtManageEnterpriseRental operation
           */
            public void receiveErrordbtManageEnterpriseRental(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQuerySMSExclusions method
            * override this method for handling normal response from dbtQuerySMSExclusions operation
            */
           public void receiveResultdbtQuerySMSExclusions(
                    CUSTOMECADBTCustomECAStub.DbtQuerySMSExclusionsOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQuerySMSExclusions operation
           */
            public void receiveErrordbtQuerySMSExclusions(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryNexusPointDetails method
            * override this method for handling normal response from dbtQueryNexusPointDetails operation
            */
           public void receiveResultdbtQueryNexusPointDetails(
                    CUSTOMECADBTCustomECAStub.DbtQueryNexusPointDetailsOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryNexusPointDetails operation
           */
            public void receiveErrordbtQueryNexusPointDetails(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryAllFinancialTransForAccount method
            * override this method for handling normal response from dbtQueryAllFinancialTransForAccount operation
            */
           public void receiveResultdbtQueryAllFinancialTransForAccount(
                    CUSTOMECADBTCustomECAStub.DbtQueryAllFinancialTransForAccountOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryAllFinancialTransForAccount operation
           */
            public void receiveErrordbtQueryAllFinancialTransForAccount(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtModifySubscription method
            * override this method for handling normal response from dbtModifySubscription operation
            */
           public void receiveResultdbtModifySubscription(
                    CUSTOMECADBTCustomECAStub.DbtModifySubscriptionOutput result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtModifySubscription operation
           */
            public void receiveErrordbtModifySubscription(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtProcessDunningRequests method
            * override this method for handling normal response from dbtProcessDunningRequests operation
            */
           public void receiveResultdbtProcessDunningRequests(
                    CUSTOMECADBTCustomECAStub.DbtProcessDunningRequestsOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtProcessDunningRequests operation
           */
            public void receiveErrordbtProcessDunningRequests(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryAllPaymentDetailsNew method
            * override this method for handling normal response from dbtQueryAllPaymentDetailsNew operation
            */
           public void receiveResultdbtQueryAllPaymentDetailsNew(
                    CUSTOMECADBTCustomECAStub.DbtQueryAllPaymentDetailsNewOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryAllPaymentDetailsNew operation
           */
            public void receiveErrordbtQueryAllPaymentDetailsNew(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryEbillSubscriptions method
            * override this method for handling normal response from dbtQueryEbillSubscriptions operation
            */
           public void receiveResultdbtQueryEbillSubscriptions(
                    CUSTOMECADBTCustomECAStub.DbtQueryEbillSubscriptionsOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryEbillSubscriptions operation
           */
            public void receiveErrordbtQueryEbillSubscriptions(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtModifyCxType method
            * override this method for handling normal response from dbtModifyCxType operation
            */
           public void receiveResultdbtModifyCxType(
                    CUSTOMECADBTCustomECAStub.DbtModifyCxTypeOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtModifyCxType operation
           */
            public void receiveErrordbtModifyCxType(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryFeatureCommitmentRentalData method
            * override this method for handling normal response from dbtQueryFeatureCommitmentRentalData operation
            */
           public void receiveResultdbtQueryFeatureCommitmentRentalData(
                    CUSTOMECADBTCustomECAStub.DbtQueryFeatureCommitmentRentalDataOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryFeatureCommitmentRentalData operation
           */
            public void receiveErrordbtQueryFeatureCommitmentRentalData(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryCommPolicyDetailsForCustomer method
            * override this method for handling normal response from dbtQueryCommPolicyDetailsForCustomer operation
            */
           public void receiveResultdbtQueryCommPolicyDetailsForCustomer(
                    CUSTOMECADBTCustomECAStub.DbtQueryCommPolicyDetailsForCustomerOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryCommPolicyDetailsForCustomer operation
           */
            public void receiveErrordbtQueryCommPolicyDetailsForCustomer(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryAllBillChangeRequests method
            * override this method for handling normal response from dbtQueryAllBillChangeRequests operation
            */
           public void receiveResultdbtQueryAllBillChangeRequests(
                    CUSTOMECADBTCustomECAStub.DbtQueryAllBillChangeRequestsOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryAllBillChangeRequests operation
           */
            public void receiveErrordbtQueryAllBillChangeRequests(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryAllFinancialTransForCustomer method
            * override this method for handling normal response from dbtQueryAllFinancialTransForCustomer operation
            */
           public void receiveResultdbtQueryAllFinancialTransForCustomer(
                    CUSTOMECADBTCustomECAStub.DbtQueryAllFinancialTransForCustomerOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryAllFinancialTransForCustomer operation
           */
            public void receiveErrordbtQueryAllFinancialTransForCustomer(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtCreateModifyPaymentExtension method
            * override this method for handling normal response from dbtCreateModifyPaymentExtension operation
            */
           public void receiveResultdbtCreateModifyPaymentExtension(
                    CUSTOMECADBTCustomECAStub.DbtCreateModifyPaymentExtensionOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtCreateModifyPaymentExtension operation
           */
            public void receiveErrordbtCreateModifyPaymentExtension(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtPostChequeAndForcefullRealization method
            * override this method for handling normal response from dbtPostChequeAndForcefullRealization operation
            */
           public void receiveResultdbtPostChequeAndForcefullRealization(
                    CUSTOMECADBTCustomECAStub.DbtPostChequeAndForcefullRealizationOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtPostChequeAndForcefullRealization operation
           */
            public void receiveErrordbtPostChequeAndForcefullRealization(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryAllGracePeriodCreditType method
            * override this method for handling normal response from dbtQueryAllGracePeriodCreditType operation
            */
           public void receiveResultdbtQueryAllGracePeriodCreditType(
                    CUSTOMECADBTCustomECAStub.DbtQueryAllGracePeriodCreditTypeOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryAllGracePeriodCreditType operation
           */
            public void receiveErrordbtQueryAllGracePeriodCreditType(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtAdoptParent method
            * override this method for handling normal response from dbtAdoptParent operation
            */
           public void receiveResultdbtAdoptParent(
                    CUSTOMECADBTCustomECAStub.DbtAdoptParentOutput result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtAdoptParent operation
           */
            public void receiveErrordbtAdoptParent(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryBalanceForCustomer method
            * override this method for handling normal response from dbtQueryBalanceForCustomer operation
            */
           public void receiveResultdbtQueryBalanceForCustomer(
                    CUSTOMECADBTCustomECAStub.DbtQueryBalanceForCustomerOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryBalanceForCustomer operation
           */
            public void receiveErrordbtQueryBalanceForCustomer(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryAllDeposits method
            * override this method for handling normal response from dbtQueryAllDeposits operation
            */
           public void receiveResultdbtQueryAllDeposits(
                    CUSTOMECADBTCustomECAStub.DbtQueryAllDepositsOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryAllDeposits operation
           */
            public void receiveErrordbtQueryAllDeposits(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryStarPointSummary method
            * override this method for handling normal response from dbtQueryStarPointSummary operation
            */
           public void receiveResultdbtQueryStarPointSummary(
                    CUSTOMECADBTCustomECAStub.DbtQueryStarPointSummaryOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryStarPointSummary operation
           */
            public void receiveErrordbtQueryStarPointSummary(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtPerformDunningActions method
            * override this method for handling normal response from dbtPerformDunningActions operation
            */
           public void receiveResultdbtPerformDunningActions(
                    CUSTOMECADBTCustomECAStub.DbtPerformDunningActionsOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtPerformDunningActions operation
           */
            public void receiveErrordbtPerformDunningActions(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryFlySmileDetails method
            * override this method for handling normal response from dbtQueryFlySmileDetails operation
            */
           public void receiveResultdbtQueryFlySmileDetails(
                    CUSTOMECADBTCustomECAStub.DbtQueryFlySmileDetailsOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryFlySmileDetails operation
           */
            public void receiveErrordbtQueryFlySmileDetails(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryUnrealizedCheques method
            * override this method for handling normal response from dbtQueryUnrealizedCheques operation
            */
           public void receiveResultdbtQueryUnrealizedCheques(
                    CUSTOMECADBTCustomECAStub.DbtQueryUnrealizedChequesOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryUnrealizedCheques operation
           */
            public void receiveErrordbtQueryUnrealizedCheques(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryUnrealizedChequesNew method
            * override this method for handling normal response from dbtQueryUnrealizedChequesNew operation
            */
           public void receiveResultdbtQueryUnrealizedChequesNew(
                    CUSTOMECADBTCustomECAStub.DbtQueryUnrealizedChequesNewOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryUnrealizedChequesNew operation
           */
            public void receiveErrordbtQueryUnrealizedChequesNew(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtManageFeatureCommitmentRental method
            * override this method for handling normal response from dbtManageFeatureCommitmentRental operation
            */
           public void receiveResultdbtManageFeatureCommitmentRental(
                    CUSTOMECADBTCustomECAStub.DbtManageFeatureCommitmentRentalOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtManageFeatureCommitmentRental operation
           */
            public void receiveErrordbtManageFeatureCommitmentRental(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryCustomerGradingHistory method
            * override this method for handling normal response from dbtQueryCustomerGradingHistory operation
            */
           public void receiveResultdbtQueryCustomerGradingHistory(
                    CUSTOMECADBTCustomECAStub.DbtQueryCustomerGradingHistoryOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryCustomerGradingHistory operation
           */
            public void receiveErrordbtQueryCustomerGradingHistory(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryEbillTransactions method
            * override this method for handling normal response from dbtQueryEbillTransactions operation
            */
           public void receiveResultdbtQueryEbillTransactions(
                    CUSTOMECADBTCustomECAStub.DbtQueryEbillTransactionsOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryEbillTransactions operation
           */
            public void receiveErrordbtQueryEbillTransactions(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtPostChequeInSuspendStatus method
            * override this method for handling normal response from dbtPostChequeInSuspendStatus operation
            */
           public void receiveResultdbtPostChequeInSuspendStatus(
                    CUSTOMECADBTCustomECAStub.DbtPostChequeInSuspendStatusOutput result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtPostChequeInSuspendStatus operation
           */
            public void receiveErrordbtPostChequeInSuspendStatus(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtDepositSetOff method
            * override this method for handling normal response from dbtDepositSetOff operation
            */
           public void receiveResultdbtDepositSetOff(
                    CUSTOMECADBTCustomECAStub.DbtDepositSetOffOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtDepositSetOff operation
           */
            public void receiveErrordbtDepositSetOff(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryBalanceForAccount method
            * override this method for handling normal response from dbtQueryBalanceForAccount operation
            */
           public void receiveResultdbtQueryBalanceForAccount(
                    CUSTOMECADBTCustomECAStub.DbtQueryBalanceForAccountOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryBalanceForAccount operation
           */
            public void receiveErrordbtQueryBalanceForAccount(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryAccountAttributehistory method
            * override this method for handling normal response from dbtQueryAccountAttributehistory operation
            */
           public void receiveResultdbtQueryAccountAttributehistory(
                    CUSTOMECADBTCustomECAStub.DbtQueryAccountAttributehistoryOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryAccountAttributehistory operation
           */
            public void receiveErrordbtQueryAccountAttributehistory(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtSMSExclusions method
            * override this method for handling normal response from dbtSMSExclusions operation
            */
           public void receiveResultdbtSMSExclusions(
                    CUSTOMECADBTCustomECAStub.DbtSMSExclusionsOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtSMSExclusions operation
           */
            public void receiveErrordbtSMSExclusions(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtSubscriptionMovement method
            * override this method for handling normal response from dbtSubscriptionMovement operation
            */
           public void receiveResultdbtSubscriptionMovement(
                    CUSTOMECADBTCustomECAStub.DbtSubscriptionMovementOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtSubscriptionMovement operation
           */
            public void receiveErrordbtSubscriptionMovement(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQueryPaymentExtension method
            * override this method for handling normal response from dbtQueryPaymentExtension operation
            */
           public void receiveResultdbtQueryPaymentExtension(
                    CUSTOMECADBTCustomECAStub.DbtQueryPaymentExtensionOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQueryPaymentExtension operation
           */
            public void receiveErrordbtQueryPaymentExtension(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtRealizeChequeNew method
            * override this method for handling normal response from dbtRealizeChequeNew operation
            */
           public void receiveResultdbtRealizeChequeNew(
                    CUSTOMECADBTCustomECAStub.DbtRealizeChequeNewOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtRealizeChequeNew operation
           */
            public void receiveErrordbtRealizeChequeNew(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtCreateDeposit method
            * override this method for handling normal response from dbtCreateDeposit operation
            */
           public void receiveResultdbtCreateDeposit(
                    CUSTOMECADBTCustomECAStub.DbtCreateDepositOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtCreateDeposit operation
           */
            public void receiveErrordbtCreateDeposit(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtCreateOneTimeCharge method
            * override this method for handling normal response from dbtCreateOneTimeCharge operation
            */
           public void receiveResultdbtCreateOneTimeCharge(
                    CUSTOMECADBTCustomECAStub.DbtCreateOneTimeChargeOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtCreateOneTimeCharge operation
           */
            public void receiveErrordbtCreateOneTimeCharge(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtModifyCxTypeDBN method
            * override this method for handling normal response from dbtModifyCxTypeDBN operation
            */
           public void receiveResultdbtModifyCxTypeDBN(
                    CUSTOMECADBTCustomECAStub.DbtModifyCxTypeDBNOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtModifyCxTypeDBN operation
           */
            public void receiveErrordbtModifyCxTypeDBN(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtAssociateCommitmentToCustomer method
            * override this method for handling normal response from dbtAssociateCommitmentToCustomer operation
            */
           public void receiveResultdbtAssociateCommitmentToCustomer(
                    CUSTOMECADBTCustomECAStub.DbtAssociateCommitmentToCustomerOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtAssociateCommitmentToCustomer operation
           */
            public void receiveErrordbtAssociateCommitmentToCustomer(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbtQuerySMSNotifications method
            * override this method for handling normal response from dbtQuerySMSNotifications operation
            */
           public void receiveResultdbtQuerySMSNotifications(
                    CUSTOMECADBTCustomECAStub.DbtQuerySMSNotificationsOutputE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbtQuerySMSNotifications operation
           */
            public void receiveErrordbtQuerySMSNotifications(Exception e) {
            }
                


    }
    