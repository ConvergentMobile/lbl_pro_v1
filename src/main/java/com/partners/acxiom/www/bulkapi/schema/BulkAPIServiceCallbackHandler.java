
/**
 * BulkAPIServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:24:30 EDT)
 */

    package com.partners.acxiom.www.bulkapi.schema;

    /**
     *  BulkAPIServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class BulkAPIServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public BulkAPIServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public BulkAPIServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for UploadFile method
            * override this method for handling normal response from UploadFile operation
            */
           public void receiveResultUploadFile(
                    com.partners.acxiom.www.bulkapi.schema.BulkAPIServiceStub.UploadFileResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from UploadFile operation
           */
            public void receiveErrorUploadFile(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ClientIdListIdLatestXRef method
            * override this method for handling normal response from ClientIdListIdLatestXRef operation
            */
           public void receiveResultClientIdListIdLatestXRef(
                    com.partners.acxiom.www.bulkapi.schema.BulkAPIServiceStub.ClientIdListIdLatestXRefResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ClientIdListIdLatestXRef operation
           */
            public void receiveErrorClientIdListIdLatestXRef(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ProcessingStatus method
            * override this method for handling normal response from ProcessingStatus operation
            */
           public void receiveResultProcessingStatus(
                    com.partners.acxiom.www.bulkapi.schema.BulkAPIServiceStub.ProcessingStatusResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ProcessingStatus operation
           */
            public void receiveErrorProcessingStatus(java.lang.Exception e) {
            }
                


    }
    