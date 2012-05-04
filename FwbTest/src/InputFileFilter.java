import java.io.*;
import javax.swing.filechooser.FileFilter;
 

public class InputFileFilter extends FileFilter 
{
 
	@Override
    public boolean accept(File f) 
    {
        if (f.isDirectory()) 
        {
            return true;
        }
 
        String extension = Utils.getExtension(f);
        if(extension != null) 
        {
            if(extension.equals(Utils.output) || extension.equals(Utils.input)) 
            {
            	return true;
            }
            else 
            {
                return false;
            }
        }
 
        return false;
    }
 
    @Override
    public String getDescription() {
        return "Input and ouput files (.fwbi & .fwbo)";
    }
}