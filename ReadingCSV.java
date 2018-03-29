import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ReadingCSV
{
	public static void main (String args[])
	{
		Path path = Paths.get("C:\\Users\\patrick.mcconnell\\Documents\\data migration\\extract.csv");
		try
		{
			List<String> lines = Files.readAllLines(path,StandardCharsets.ISO_8859_1);
			for (String line : lines)
			{
				// prints out all csv rows
				String[] details = line.split(",");
				for(int i = 0; i<details[0].length(); i++)
				{
					if(details[0].charAt(i) == 150)
					{
						details[0] = details[0].replace(details[0].charAt(i), '-');
					}
					else if(details[0].charAt(i) == (145))
					{
						details[0] = details[0].replace(details[0].charAt(i), '\'');
					}
					else if(details[0].charAt(i) == (146))
					{
						details[0] = details[0].replace(details[0].charAt(i), '\'');
					}
				}
				System.out.println(details[0]);				
			}
		}//end try inner
		catch (IOException e)
		{
			System.out.println(e);
		}// catch reading csv
	}
}
