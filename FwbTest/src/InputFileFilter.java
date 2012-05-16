import java.io.*;
import javax.swing.filechooser.FileFilter;
 

public class InputFileFilter extends FileFilter 
{
	
	private boolean save;
	
	public InputFileFilter(boolean save)
	{
		this.save = save;
	}
	
	public boolean isFileApproved(File f)
	{
		String ext = Utils.getExtension(f);
		if(ext == null)
		{
			return false;
		}
		if(!save)
		{
			if(ext.equals(Utils.output))
			{
				return true;
			}
		}
		if(ext.equals(Utils.input)) 
        {
        	return true;
        }
		
		return false;
	}
	
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
        	if(!save)
        	{
        		if(extension.equals(Utils.output))
        		{
        			return true;
        		}
        	}
            if(extension.equals(Utils.input)) 
            {
            	return true;
            }
        }
 
        return false;
    }
 
    @Override
    public String getDescription() 
    {
    	if(save)
    	{
    		return "Input files (.in)";
    	}
        return "Input and ouput files (.in & .fwbo)";
    }
}