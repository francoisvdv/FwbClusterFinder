import java.io.File;

public class Utils 
{
    public final static String input = "in";
    public final static String output = "fwbout";

    /**
     * Get the extension of a file.
     * @param f the file
     * @return the extension
     */
    public static String getExtension(File f) 
    {	
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
 
        if(i > 0 &&  i < s.length() - 1) 
        {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}