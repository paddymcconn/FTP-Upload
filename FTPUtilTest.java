/* this file does the actual uploading for the ftp
 * it deals with files inside of folders recursively,
 * 
 * */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.net.ftp.FTPClient;
public class FTPUtilTest
{
	public static void uploadDirectory(FTPClient ftpClient, String remoteDirPath, String localParentDir,String remoteParentDir) throws IOException
	{

		System.out.println("LISTING directory: " + localParentDir);
	//	ftpClient.setControlEncoding("UTF-8");

		File localDir = new File(localParentDir);
		File[] subFiles = localDir.listFiles();
		if (subFiles != null && subFiles.length > 0)
		{
			for (File item : subFiles)
			{
				String remoteFilePath = remoteDirPath + remoteParentDir + "/" + item.getName();
				if (remoteParentDir.equals(""))
				{
					remoteFilePath = remoteDirPath + "/" + item.getName();
				}
				if (item.isFile())
				{
					// upload the file
					String localFilePath = item.getAbsolutePath();
					System.out.println("About to upload the file: " + localFilePath);
					boolean uploaded = uploadSingleFile(ftpClient, localFilePath, remoteFilePath);
					if (uploaded)
					{
						System.out.println("UPLOADED a file to: " + remoteFilePath);
					}
					else
					{
						System.out.println("COULD NOT upload the file: " + localFilePath);
					}
				}
				else
				{
					// create directory
					boolean created = ftpClient.makeDirectory(remoteFilePath);
					// if the end file is a folder it creates and recursivly looks in here to either create others or upload files and backtrack
					if (created)
					{
						System.out.println("CREATED the directory: " + remoteFilePath);
					} else
					{
						System.out.println("COULD NOT create the directory: " + remoteFilePath);
					}
					// upload the sub directory
					String parent = remoteParentDir + "/" + item.getName();
					System.out.println(parent + "  " + item);
					if (remoteParentDir.equals(""))
					{
						parent = item.getName();
					}
					localParentDir = item.getAbsolutePath();
					uploadDirectory(ftpClient, remoteDirPath, localParentDir, parent);
				}
			}
		}
	}
	public static boolean uploadSingleFile(FTPClient ftpClient,String localFilePath, String remoteFilePath) throws IOException
	{
		File localFile = new File(localFilePath);
		InputStream inputStream = new FileInputStream(localFile);
		try
		{
			return ftpClient.storeFile(remoteFilePath, inputStream);
		}
		finally
		{
			inputStream.close();
		}
	}
}