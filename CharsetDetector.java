import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class CharsetDetector
{
    public Charset detectCharset(File f, String[] charsets)
    {
        Charset charset = null;

        for (String charsetName : charsets)
        {
            charset = detectCharset(f, Charset.forName(charsetName));
            if (charset != null)
            {
                break;
            }
        }

        return charset;
    }

    private Charset detectCharset(File f, Charset charset)
    {
        try
        {
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(f));

            CharsetDecoder decoder = charset.newDecoder();
            decoder.reset();

            byte[] buffer = new byte[512];
            boolean identified = false;
            
            while ((input.read(buffer) != -1) && (!identified))
            {
                identified = identify(buffer, decoder);
            }

            input.close();

            if (identified)
            {
            	System.out.println(charset);
            	return charset;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private boolean identify(byte[] bytes, CharsetDecoder decoder)
    {
        try
        {
            decoder.decode(ByteBuffer.wrap(bytes));
        }
        catch (CharacterCodingException e)
        {
            return false;
        }
        return true;
    }

    public static void main(String[] args)
    {
        File f = new File("C:\\Users\\patrick.mcconnell\\Desktop\\box migration\\asfw\\â€“â€”â€�â€˜â€™.txt");

        String[] charsetsToBeTested = {"UTF-8", "ISO-8859-1"};

        CharsetDetector cd = new CharsetDetector();
        Charset charset = cd.detectCharset(f, charsetsToBeTested);

        if (charset != null)
        {
            try
            {
            	InputStreamReader reader = new InputStreamReader(new FileInputStream(f), charset);
                int c = 0;
                while ((c = reader.read()) != -1)
                {
                    System.out.print((char)c+"  ");
                }
                reader.close();
            }
            catch (FileNotFoundException fnfe)
            {
                fnfe.printStackTrace();
            }
            catch(IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
        else
        {
            System.out.println("Unrecognized charset.");
        }
    }
}