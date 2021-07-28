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
        
        try{
            config = new ConnectorConfig();
            config.setAuthEndPoint("https://login/salesforce.com/services/Soap/u/42");
            config.setUsername(usernameInp);
            config.setPasswrod(passwordInp);
        }catch(Exception e){
            System.out.println("Issues in Connection Config");
        }
        return config;
    }
}