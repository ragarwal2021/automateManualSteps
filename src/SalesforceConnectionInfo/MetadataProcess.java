package AutomateManualDeploymentSteps;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.opencsv.CSVReader;
import com.sforce.soap.metadata.ApexCodeUnitStatus;
import com.sforce.soap.metadata.ApexTrigger;
import com.sforce.soap.metadata.CustomLabel;
import com.sforce.soap.metadata.Metadata;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.metadata.Profile;
import com.sforce.soap.metadata.ProfileLoginIpRange;
import com.sforce.soap.metadata.ReadResult;
import com.sforce.soap.metadata.RemoteSiteSetting;
import com.sforce.soap.metadata.ConnectedApp;
import com.sforce.soap.metadata.ConnectedAppOauthConfig;
import com.sforce.soap.metadata.ConnectedAppAttribute;
import com.sforce.soap.metadata.SaveResult;

import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class MetadataProcess{

    //Get Client Id and Client Secret from Connected App
    public static boolean getClientIdAndSecret(MetadataConnection Metadatacon) throws ConnectionException, IOException{
        try {
            ReadResult readResult = Metadatacon.readMetadata("ConnectedApp", new String[] {"Liberty_Cloud_Forge" });
            Metadata[] mdInfo = readResult.getRecords();
            System.out.println("Number of component info returned: " + mdInfo.length);
            for (Metadata md : mdInfo) {
                if (md != null) {
                    ConnectedApp obj = (ConnectedApp) md;
                    System.out.println("Connected App Label " + obj.getLabel());
                    
                } else {
                    System.out.println("Empty metadata.");
                }
            }
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}