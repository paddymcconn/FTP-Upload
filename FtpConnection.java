import java.nio.file.*;
import java.util.*;
import org.apache.commons.net.ftp.*;
import org.apache.*;
import java.io.*;
import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
public class FtpConnection
{
	public static void main(String[] args)
	{
		Path path = Paths.get("C:\\Programme_List.csv");
		FTPClient ftpClient = new FTPClient();
		
		try
		{
			// pass directory path on server to connect
			ftpClient.connect("ftp.box.com");
			// pass user name and password
			ftpClient.enterLocalPassiveMode();// important
			boolean login = ftpClient.login("EmailAddress", "Password");
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			// connect to box
			File dir = new File("/test");
		      File[] files = dir.listFiles();
		      FileFilter fileFilter = new FileFilter()
		      {
		         public boolean accept(File file)
		         {
		            return file.isDirectory();
		         }
		      };
		      files = dir.listFiles(fileFilter);
		      System.out.println(files.length);
		      
		      if (files.length == 0)
		      {
		         System.out.println("Either dir does not exist or is not a directory");
		      }
		      else
		      {
		         for (int i = 0; i< files.length; i++)
		         {
		            File filename = files[i];
		            System.out.println(filename.toString());
		         }
			}
		
			boolean logout = ftpClient.logout();
			
			if (logout)
			{
				System.out.println("Connection close...");
			}
			// logging out
		}
		catch (Exception e)
		{
			System.out.println(e);
		} // catch ftp connection
	}// main
}// class