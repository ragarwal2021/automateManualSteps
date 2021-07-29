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

public class AutomateManualDeploymentSteps{
    public static void main(String[] args) throws ConnectionException, CancellationException, IOException {
         boolean saleforceOrgConnected = false;

         String username = System.getenv("Username");
         String password = System.getenv("Password");
        //Connecting to Salesforce Org
        ConnectorConfig salesforceOrgConfig = SalesforceConnectionSession.SalesforceLogin(username,password);
        PartnerConnection orgConnection = null;
        try{
            System.out.println("salesforceOrgConfig--> " + salesforceOrgConfig);
            orgConnection = Connector.newConnection(salesforceOrgConfig);
            System.out.println("orgConnection established--> " + orgConnection);
            GetUserInfoResult orgUserInfo = orgConnection.getUserInfo();
            System.out.println("orgUserInfo--> " + orgUserInfo);
            saleforceOrgConnected = true;

            System.out.println(">>>Logged in successfully in Salesforce Org as User --" + orgUserInfo.getUserName());
        }catch(Exception e){
            System.out.println(">>>Issues in Salesforce Connection, Please check your credentials" + e.printStackTrace());
        }
    }
}
