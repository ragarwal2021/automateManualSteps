package AutomateManualDeploymentSteps;

import java.awt.List;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.concurrent.CancellationException;

//import javax.print.DocFlavor.String;

import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.GetUserInfoResult;
import com.sforce.soap.partner.DeleteResult;
import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.Error;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.FieldType;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.transport.SoapConnection;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.metadata.ConnectedApp;
import com.sforce.soap.metadata.ConnectedAppOauthConfig;

public class AutomateManualDeploymentSteps{
    public static void main(String[] args) throws ConnectionException, CancellationException, IOException {
         boolean saleforceOrgConnected = false;

         String username = System.getenv("Username");
         String password = System.getenv("Password");
         password += "pCQB6amhBcqkmHcmRBDOCbXqv";
        //Connecting to Salesforce Org
        ConnectorConfig salesforceOrgConfig = SalesforceConnectionSession.SalesforceLogin(username,password);
        PartnerConnection orgConnection = null;
        
            //System.out.println("salesforceOrgConfig--> " + salesforceOrgConfig);
            orgConnection = Connector.newConnection(salesforceOrgConfig);
            //System.out.println("orgConnection established--> " + orgConnection);
            GetUserInfoResult orgUserInfo = orgConnection.getUserInfo();
            //System.out.println("orgUserInfo--> " + orgUserInfo);
            saleforceOrgConnected = true;

            System.out.println(">>>Logged in successfully in Salesforce Org as User --" + orgUserInfo.getUserName());
        
            MetadataConnection Metadatacon = null;
            Metadatacon = SalesforceConnectionSession.MetaSaleesforceLogin(orgConnection,username,password);
            //All steps processed at this level
        processSteps(orgConnection, salesforceOrgConfig,Metadatacon);

    }

    public static void processSteps(PartnerConnection orgConnection, ConnectorConfig salesforceOrgConfig, MetadataConnection Metadatacon){


        //Process Step 1 : Get Client Id and Client Secret from Salesforce Org.
        System.out.println(">> Processing Step 1 : Get Client Id and Client Secret from Salesforce Org.");
        try{
            //ConnectedApp connectedAppVar = new ConnectedApp();
            Boolean result = ProcessStepHelper.getClientIdAndSecret(Metadatacon);
            System.out.println(">> Step 1 successfuly processed: ");
        }catch(Exception e){
            System.out.println(">> Error in processing step 1: "+ e.getMessage());
        }

        //Process Step 2 : Move Developers into System Admin Profile and Permission Set.
        System.out.println(">> Processing Step 2 : Moving Developers into System Admin Profile and Permission Set.");
        try{
            //ConnectedApp connectedAppVar = new ConnectedApp();
            Boolean result1 = ProcessStepHelper.assignSystemAdminProfileAndPermissionSet(orgConnection);
            System.out.println(">> Step 2 successfuly processed: ");
        }catch(Exception e){
            System.out.println(">> Error in processing step 2: "+ e.getMessage());
        }

    }
}
