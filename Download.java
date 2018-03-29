import org.apache.commons.net.ftp.*;
import java.io.*;
import java.net.*;

public class Download
{
	public static void main(String[] args)
	{
		FTPClient ftpClient = new FTPClient();

		try
		{
			// pass directory path on server to connect
			ftpClient.connect("ftp.dlptest.com");
			// pass user name and password
			boolean login = ftpClient.login("dlpuser@dlptest.com", "Password");  
			ftpClient.enterLocalPassiveMode();// important
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			
			if (login)
			{
				System.out.println("Connection established...");
				System.out.println("Status: " + ftpClient.getStatus());
				//download files testing
				try
				{
					String remoteFile1 = "test.txt";// file we want
		            File downloadFile1 = new File("C:/Users/patrick.mcconnell/Downloads/test.txt"); //where to put it
		            OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
		            boolean success = ftpClient.retrieveFile(remoteFile1, outputStream1);
		            outputStream1.close();
		            if (success)
		            {
		            	System.out.println("download successful");
		            }
				}
				catch(IOException e)
				{
					System.out.println(e);	
				}
				// logout the user, returned true if logout successfully
				boolean logout = ftpClient.logout();
			
				if (logout)
				{
					System.out.println("Connection close...");
				}
			}
			else
			{
				System.out.println("Connection fail...");
			}

		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ftpClient.disconnect();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}// main
}// class