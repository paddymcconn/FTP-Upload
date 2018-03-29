import java.io.File;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class FileDirectorySearching
{
	public static void main (String args[])
	{
		FTPClient ftpClient = new FTPClient();
		// pass directory path on server to connect

		try
		{
			// pass directory path on server to connect
			ftpClient.connect("ftp.box.com");
			// pass user name and password
			ftpClient.enterLocalPassiveMode();// important
			boolean login = ftpClient.login("EmailAddress", "Password");
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			// connect to box
			File file = new File ("C:/data migration");
			findDirectory(file);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
	private static boolean findDirectory(File parentDirectory)
	{
		try
		{	
			System.out.println(parentDirectory.getPath());
		    File[] files = parentDirectory.listFiles();
		    for (File file : files) {
		        if (!file.isFile()) {

		            if (file.getName().equals("hello")) {

		                System.out.println("hello Folder found : ");
		                System.out.println("hello folder path : " + file.getAbsolutePath());
		                System.out.println("Parent path :  " + file.getParent());
		                return true;
		            } else if (file.isDirectory()) {
		                return findDirectory(file);
		            }
		        }
		    }
		    System.out.println("hello Folder not found : ");
		    return false;
			
		
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		
	    System.out.println("Folder not found : ");
	    return false;
	}
}
