/* FTP Upload for Data migration:
 * need both this and FTPUtilsTest files
 * 
 * need to know -> 
 * 				string server = "ftp.box.com", this will not change
 * 				String user = "email address of the uploader", will change for migration
 * 				String pass = "password for box account"
 * 				String LocalDirPath = "this points to where the DL files have been exported to, eg c drive or where ever its saved"
 * 				Path path = "points to the csv that will be used to lookup and check if the file we are looking for exists in the local directory"
 * 				We are working off of the following box structure : Salesforce/programmes || projects/ named in csv and local file to be uploaded/ items
 * 				remoteDirPAth will change depending on the length of the file name, if its short its a program and adds that to the path and proceeds to upload it to this place on box
 * 				the commons-net-3.6.jar is needed for the ftp upload, it needs to be included in the project as follows:
 * 										- right click the project and select properties
 *  									- click the tab Libraries and select "add external jars"
 *  									- navigate to where the commons jar is saved and select it.
 *  The jar file can be found at the following location:					
 *  http://commons.apache.org/proper/commons-net/download_net.cgi?Preferred=http%3A%2F%2Fftp.heanet.ie%2Fmirrors%2Fwww.apache.org%2Fdist%2F
 *  if you get a "java.net.SocketException: Connection reset by peer: socket write error"
 *  and the code terminates, this means there is a subfile directory within the directory.
 *  just upload this manually via drag and drop and remove everything before this folder on the excel sheet.
 *  start process again.
 * */



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
 
import org.apache.commons.net.ftp.FTPClient;
 
public class FTPUploadDirectory extends FTPUtilTest
{
    public static void main(String[] args)
    {
    	
        /***Connect to box server and create a client connection***/
        String server = "ftp.box.com";
        int port = 21;
        String user = "EmailAddress;
        String pass = "Password";
         
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("UTF-8");
        try
        {
            // setting a bigger buffer size to help with mass transfer
            ftpClient.setBufferSize(1024000);
            // connect and login to the server
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
             
            // use local passive mode to pass firewall
            ftpClient.enterLocalPassiveMode();
             
           if(ftpClient.isConnected())
           {
        	   System.out.println("Connected");
    	   }
             
            // look up with csv
            Path path = Paths.get("C:\\ExcelTest.csv");
            String localDirPath = "C:\\path\\to\\wherever";
            // path + folder local take name off of csv and add it to get the appropriate folder.
             
            __main: // a goto to allow us to exit the main loop if we are to dis-connect from the server
            try
            {
                String remoteDirPath = "/Salesforce/";
                //need to make this filled by csv + box path
                
                List<String> lines = Files.readAllLines(path);
                for (String line : lines)
                {
                    remoteDirPath = "/Salesforce/";
                    String[] details = line.split(",",4);
                     
                    /***removing the commas to concatenate the string in one array column***/
                    if(getCommas(line) == 1)
                    {
                        details[0]+= "," + details[1];
                    }
                    if(getCommas(line) == 2)
                    {
                        details[0]+= "," + details[1];
                        details[1]= details[2];
                    }
                    else if (getCommas(line) == 3)
                    {
                        details[0]+= "," + details[1] +","+ details[2];
                        details[1] = details[3];
                    }
                    else if (getCommas(line) == 4)
                    {
                        details[0]+= ","+details[1]+","+details[2]+","+details[3];
                        details[1] = details[4];    
                    }
                     
                    /***for blank 2nd column entries, add null as will not read in otherwise if we need it for identifying the project uniquely***/
                     
                    if(getCommas(line)>0 && details[1].equals(""))
                    // to remove the empty strings and replace with null to remove array out of index error
                    {
                        details[1] = null;
                    }
                    String ref =details[0];
                    details[0] = details[0].toString();
                    int IsAFolder;
                    ref = details[0];
                    File local = new File(localDirPath+ref);

                    IsAFolder = ftpClient.cwd(remoteDirPath+ref); 
                    // changes to box folder name, returns 250 if exists 
                    
                    // we check to see if this is stored locally 
                    if(local.exists())
                    {
                        System.out.println("file exists locally, path is safe and correct.");
                        localDirPath = localDirPath+ref;
                    }
                    else
                    {
                        System.out.println("The file path : " + localDirPath+ref+ " either does not point to the folder properly or the folder does not exist.");
                        break __main;
                        // Disconnects and goes to __main outside of try to disconnect 
                    }
                    
                    /***check if it is a folder on box in the current directory and add the files here***/
                    if(IsAFolder == 250) 
                    {   
                        if(ref.length()<=9)
                        // only programs have a reference code less than or equal to 9 max.
                        {
                            remoteDirPath+="Programmes/";
                            // now pointed to the programs folder
                        }
                        else
                        // reference code is longer than 8, meaning it is a project
                        {
                            remoteDirPath+="Projects/";
                            System.out.println(remoteDirPath);
                        }
                        remoteDirPath+=ref;
                        // add the extension to the path for the folder
                        FTPUtilTest.uploadDirectory(ftpClient, remoteDirPath, localDirPath, "");
                        // here we call ftputils as we have found the location we want to upload to 
                        
                        remoteDirPath = "/Salesforce/";
                        localDirPath = "C:\\";
                        // reset the extension to the original
                    }
                    
                    /***create a folder in the directory and upload its files to this location***/
                    else
                    {
                        boolean madeDir = false;
                        if(ref.length()<=9)
                        // only programs have a reference code less than or equal to 9.
                        {
                            remoteDirPath+="Programmes/"+ref;
                            madeDir = ftpClient.makeDirectory(remoteDirPath);
                            // make the folder at this path location
                        }
                        else
                        // reference code is longer than 9, meaning it is a project
                        {
                            remoteDirPath+="Projects/"+ref;
                            madeDir = ftpClient.makeDirectory(remoteDirPath);
                            // make the folder
                        }
                        //check to see if the directory was made successfully  
                        if (madeDir)
                        {
                            System.out.println("made the folder : "+ref+" inside the path "+remoteDirPath);
                        }
                        FTPUtilTest.uploadDirectory(ftpClient, remoteDirPath, localDirPath, "");
                        remoteDirPath = ("/salesforce/");
                        localDirPath = "C:\\";
                        // change path back to original after calling method upload
					}
				 }
            }//end try inner
            catch (IOException e)
            {
                System.out.println(e);
            }// catch reading csv
            
            // log out and disconnect from the server
            ftpClient.logout();
            ftpClient.disconnect();
  
            System.out.println("Disconnected");
        }
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
    }// end main
     
    /***for getting the occurrences of commas ***/
    
    public static int getCommas(String st)
    {
        int count = 0;
        for(int i =0; i<st.length(); i++)
        {
            if (st.charAt(i) == ',')
            {
                count ++;
            }
        }
        return count;
    }
}// end class