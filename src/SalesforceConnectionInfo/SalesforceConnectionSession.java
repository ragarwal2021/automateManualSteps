package AutomateManualDeploymentSteps;

import java.io.IOException;
import java.util.concurrent.CancellationException;

import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.GetUserInfoResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.tooling.ToolingConnection;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class SalesforceConnectionSession {

    public static ConnectorConfig config = null;

    public static ConnectorConfig SalesforceLogin(String usernameInp,String passwordInp) throws CancellationException, ConnectionException {
        
        String orgType = System.getenv("Org Type");
        try{
            config = new ConnectorConfig();
            System.out.println("ogType--> " + orgType);
            if(orgType == "Sandbox"){
                config.setAuthEndpoint("https://test.salesforce.com/services/Soap/u/42");
            }else{
                config.setAuthEndpoint("https://login.salesforce.com/services/Soap/u/42");
            }
            
            config.setUsername(usernameInp);
            config.setPassword(passwordInp);
            System.out.println("usernameInp--> " + usernameInp);
            System.out.println("passwordInp--> " + passwordInp);
        }catch(Exception e){
            System.out.println("Issues in Connection Config");
        }
        return config;
    }

    public static MetadataConnection MetaSaleesforceLogin(PartnerConnection partnerConfig, String usernameInp, String passwordInp) throws ConnectionException {

        LoginResult lr = partnerConfig.login(usernameInp,passwordInp);
        ConnectorConfig metadataConfig = new ConnectorConfig();
        metadataConfig.setServiceEndpoint(lr.getMetadataServerUrl());
        metadataConfig.setSessionId(lr.getSessionId());
        metadataConfig.setManualLogin(false);
        MetadataConnection connection = com.sforce.soap.metadata.Connector.newConnection(metadataConfig);
        return connection;
    }

    public static ToolingConnection ToolingSaleesforceLogin(PartnerConnection partnerConfig, String usernameInp, String passwordInp) throws ConnectionException {

        LoginResult lr = partnerConfig.login(usernameInp,passwordInp);
        ConnectorConfig toolingConfig = new ConnectorConfig();
        System.out.println("lr.getMetadataServerUrl()--> " + lr.getMetadataServerUrl());
        System.out.println("lr.getMetadataServerUrl().replace--> " + lr.getMetadataServerUrl().replace("/u/","/T"));
        toolingConfig.setServiceEndpoint(lr.getMetadataServerUrl().replace("/u/","/T"));
        toolingConfig.setSessionId(lr.getSessionId());
        ToolingConnection connection = com.sforce.soap.tooling.Connector.newConnection(toolingConfig);
        return connection;
    }
}