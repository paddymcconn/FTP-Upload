import org.apache.commons.net.ftp.*;
import java.io.*;
import java.net.*;

public class Upload
{
	public static void main(String[] args)
	{
		// ** create an ftp connection **
		// get an ftpClient object
		FTPClient ftpClient = new FTPClient();

		try
		{
			// pass directory path on server to connect
			ftpClient.connect("ftp.box.com");
			// pass user name and password
			boolean login = ftpClient.login("emailAddress", "Password");
            // hashing these would be best but for this silly ftp, its enough
			ftpClient.enterLocalPassiveMode();// important
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			
			if (login)
			{
				System.out.println("Connection established...");
				System.out.println("Status: " + ftpClient.getStatus());
				
				// UPLOAD TESTING
				
				File firstLocalFile = new File("C:/test.txt");
				 
	            String firstRemoteFile = "test.txt";
	            InputStream inputStream = new FileInputStream(firstLocalFile);
	            // input the file with input stream.
	            System.out.println("Start uploading first file");
	            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
	            inputStream.close();
	            
	            if (done)
	            {
	                System.out.println("The first file is uploaded successfully.");
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