package AutomateManualDeploymentSteps;

import java.awt.List;
import java.io.*;
import java.lang.*;
import java.util.*;

import javax.print.DocFlavor.String;

import com.sforce.soap.partner.Connector;
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
import sailpoint.connector.salesforce.webservices.partner.PicklistEntry;
import com.sforce.soap.metadata,MetadataConnection;

public class AutomateManualDeploymentSteps{
    public static coid public static void main(String[] args) throws ConnectionException, Cancellationexception, IOException {
         boolean saleforceOrgConnected = false;

         String username = System.getenv("Username");
         String password = System.getenv("Password");
        //Connecting to Salesforce Org
        ConnectorConfig salesforceOrgConfig = SalesforceConnectionSession.SalesforceLogin(username,password);
        PartnerConnection orgConnection = null;
        try{
            orgConnection = Connector.newConneciton(salesforceOrgConfig);
            GetUserInfoResult orgUserInfo = orgConnection.getUserInfo();
            saleforceOrgConnected = true;

            System.out.println(">>>Logged in successfully in Salesforce Org as User --" + orgUserInfo.getUserName());
        }
    }
}
