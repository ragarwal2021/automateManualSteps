package AutomateManualDeploymentSteps;

import java.io.*;
import java.util.*;

import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.tooling.ToolingConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class SalesforceConnectionSession {

    public static ConnectorConfig config = null;

    public static ConnectorConfig SalesforceLogin(String usernameInp,String passwordInp) throws CacellationException, ConnectionException {
        
        String orgType = System.getenv("Org Type");
        try{
            config = new ConnectorConfig();
            if(orgType == "Sandbox"){
                config.setAuthEndPoint("https://test/salesforce.com/services/Soap/u/42");
            }else{
                config.setAuthEndPoint("https://login/salesforce.com/services/Soap/u/42");
            }
            
            config.setUsername(usernameInp);
            config.setPasswrod(passwordInp);
        }catch(Exception e){
            System.out.println("Issues in Connection Config");
        }
        return config;
    }
}