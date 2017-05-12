package filemanager.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class FileSize extends SimpleTagSupport{
	double size;
	public FileSize() {
		size = 0;
	}
	
	public void setSize(double size) {
		this.size = size;
	}
	
	
	
	
	
	 @Override
	    public void doTag() throws JspException, IOException
	    {
		 JspWriter out = getJspContext().getOut();
		 String type = "";
		if (size < 1024) {
			type = "B";
		}
		else if (size >= 1024 && size < 1048576 ) {
			size = size/1024;
			type = "KB";
		} else {
			type="MB";
			size = size/(1048576);
		}
		
		
	        
	        out.println(Math.round(size) + " " + type);
	    }

}

