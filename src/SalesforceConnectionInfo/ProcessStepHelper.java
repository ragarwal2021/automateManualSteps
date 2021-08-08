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

import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class ProcessStepHelper{

    //Get Client Id and Client Secret from Connected App
    public static boolean getClientIdAndSecret(MetadataConnection Metadatacon) throws ConnectionException, IOException{
        try {
            ReadResult readResult = Metadatacon.readMetadata("ConnectedApp", new String[] {"Liberty_Cloud_Forge" });
            Metadata[] mdInfo = readResult.getRecords();
            System.out.println("Number of component info returned: " + mdInfo.length);
            for (Metadata md : mdInfo) {
                if (md != null) {
                    ConnectedApp obj = (ConnectedApp) md;
                    ConnectedAppOauthConfig connAppOAuthConfig = obj.getOauthConfig();

                    System.out.println("Connected App Label >>" + obj.getLabel());
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
            String permissionSetName = "System_Admin";
            GetUserInfoResult userInfo = orgConnection.getUserInfo();

            //Get list of user which has GRS Developer Profile.
            String queryStr = "Select Id,Username,Profile.Name FROM USER WHERE isActive = 'TRUE' "
                                + "AND Profile.Name = 'GRS Developer' "
                                + "AND ID != '"+userInfo.getUserId()+"'";

            QueryResult qrResult = orgConnection.query(queryStr);
            SObject[] activeGRSDevUserList = qr.getRecords();

            //Get List of User which has alrady System admin permission set assigned
            queryStr = "Select ID, AssigneeId,PermissionSetId FROM PermissionSetAssignment WHERE PermissionSet.Name = '"+permissionSetName+"'";
            QueryResult qrResult2 = orgConnection.query(queryStr);
            SObject[] permissionSetAssignments = qr.getRecords();
            String PermissionSetId = permissionSetAssignments[0].PermissionSetId;
            
            //Get System Administrator Profile ID
            queryStr = "Select ID FROM Profile WHERE Name = 'System Administrator' LIMIT 1";
            QueryResult qrResult3 = orgConnection.query(queryStr);
            SObject[] systemAdminProfile = qr.getRecords();
            String sysAdminProfielId = systemAdminProfile[0].Id;

            ArrayList<SObject> usersToUpdate = new ArrayList<SObject>();
            ArrayList<SObject> psaSystemAdminList = new ArrayList<SObject>();

            Set<String> assigneeIdSet = new HashSet();

            for(SObject psa : permissionSetAssignments){
                assigneeIdSet.add(psa.AssigneeId);
            }

            for(SObject thisUser: activeGRSDevUserList){
                thisUser.ProfileId = sysAdminProfielId;
                usersToUpdate.add(thisUser);
                //Add Sys Admin Profile only if permission set is not already assigned
                if(!assigneeIdSet.contains(thisUser.Id)){
                    SObject psAssign = new SObject();
                    psAssign.setType("PermissionSetAssignment");
                    psAssign.setField("AssigneeId", thisUser.Id);
                    psAssign.setField("PermissionSetId", PermissionSetId);
                    psaSystemAdminList.add(psAssign);
                }
            }
            
            SObject[] updateUserData = usersToUpdate.toArray(new SObject[0]);
            SaveResult[] saveUserResult = orgConnection.update(updateUserData);

            SObject[] createPSA = psaSystemAdminList.toArray(new SObject[0]);
            SaveResult[] saveUserResult = orgConnection.create(createPSA);

        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

}