package AutomateManualDeploymentSteps;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

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
import com.sforce.soap.partner.GetUserInfoResult;
import com.sforce.soap.partner.sobject.SObject;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class ProcessStepHelper{

    //Get Client Id and Client Secret from Connected App
    public static boolean getClientIdAndSecret(MetadataConnection Metadatacon) throws ConnectionException, IOException{
        try {
            ReadResult readResult = Metadatacon.readMetadata("ConnectedApp", new String[] {"Liberty_Cloud_Forge" });
            Metadata[] mdInfo = readResult.getRecords();
            for (Metadata md : mdInfo) {
                if (md != null) {
                    ConnectedApp obj = (ConnectedApp) md;
                    ConnectedAppOauthConfig connAppOAuthConfig = obj.getOauthConfig();

                    System.out.println("Connected App Consumer Key >> " + connAppOAuthConfig.getConsumerKey());
                    System.out.println("Connected App Consumer Secret >> " + connAppOAuthConfig.getConsumerSecret());
                } else {
                    System.out.println("Empty metadata.");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    //Get Client Id and Client Secret from Connected App
    public static boolean assignSystemAdminProfileAndPermissionSet(PartnerConnection orgConnection) throws ConnectionException, IOException{
        try {
            GetUserInfoResult userInfo = orgConnection.getUserInfo();

            //Get list of user which has GRS Developer Profile.
            String queryStr = "Select Id,Username,Profile.Name FROM USER WHERE isActive = TRUE "
                                + "AND Profile.Name = 'GRS Developer' "
                                + "AND Id != '"+userInfo.getUserId()+"'";

            QueryResult qrResult = orgConnection.query(queryStr);
            SObject[] activeGRSDevUserList = qrResult.getRecords();

            //Get System_Admin Permission Set Id
            queryStr = "Select Id, Name FROM PermissionSet WHERE Name = 'System_Admin'";
            QueryResult qrResult4 = orgConnection.query(queryStr);
            SObject[] permissionSet = qrResult4.getRecords();
            String permissionSetId  = "";

            Set<String> assigneeIdSet = new HashSet();

            if(permissionSet.length > 0){
                System.out.println("permissionSet--> " + permissionSet[0].getField("Id"));
                System.out.println("permissionSet--> " + permissionSet[0].getField("Name"));
                permissionSetId = (String)permissionSet[0].getField("Id");
                
                //Get List of User which has alrady System admin permission set assigned
                queryStr = "Select Id, AssigneeId,PermissionSetId FROM PermissionSetAssignment WHERE PermissionSetId = '"+permissionSetId+"'";
                QueryResult qrResult2 = orgConnection.query(queryStr);
                SObject[] permissionSetAssignments = qrResult2.getRecords();
                
                for(SObject psa : permissionSetAssignments){
                    assigneeIdSet.add((String)psa.getField("AssigneeId"));
                }
            
            }
            
            //Get System Administrator Profile ID
            queryStr = "Select Id FROM Profile WHERE Name = 'System Administrator' LIMIT 1";
            QueryResult qrResult3 = orgConnection.query(queryStr);
            SObject[] systemAdminProfile = qrResult3.getRecords();
            String sysAdminProfielId = (String)systemAdminProfile[0].getField("Id");

            ArrayList<SObject> usersToUpdate = new ArrayList<SObject>();
            ArrayList<SObject> psaSystemAdminList = new ArrayList<SObject>();

            

            for(SObject thisUser: activeGRSDevUserList){
                SObject newUser = new SObject();
                newUser.setType("User");
                newUser.setField("ProfileId",sysAdminProfielId);
                newUser.setField("Id",(String)thisUser.getField("Id"));
                usersToUpdate.add(newUser);
                //Add Sys Admin Profile only if permission set is not already assigned
                if(permissionSetId != "" && !assigneeIdSet.contains((String)thisUser.getField("Id"))){
                    SObject psAssign = new SObject();
                    psAssign.setType("PermissionSetAssignment");
                    psAssign.setField("AssigneeId", (String)thisUser.getField("Id"));
                    psAssign.setField("PermissionSetId", permissionSetId);
                    psaSystemAdminList.add(psAssign);
                }
            }
            
            SObject[] updateUserData = usersToUpdate.toArray(new SObject[0]);
            com.sforce.soap.partner.SaveResult[] saveUserResult = orgConnection.update(updateUserData);

            SObject[] createPSA = psaSystemAdminList.toArray(new SObject[0]);
            com.sforce.soap.partner.SaveResult[] savePSAResult = orgConnection.create(createPSA);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

}