package filemanager.servlets;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.mysql.jdbc.Statement;

import filemanager.models.User;

@WebServlet("/Upload")
public class Upload extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    public Upload()
    {
        super();
    }

    protected void doGet( HttpServletRequest request,
        HttpServletResponse response ) throws ServletException, IOException
    {
    	HttpSession session = request.getSession();
		 User user = (User) session.getAttribute( "user");
		 Integer parent = 0;
			String key = request.getParameter("key");
			
			if (key !=null)
			parent = Integer.parseInt(key);
		 filemanager.models.File file = getFile(parent);
	       
	       if (user == null) {
	    	   response.sendRedirect("FileManagerLogin");
	       } 
	       else if (parent == 0) {
	    	   request.getRequestDispatcher("UploadForm.jsp").forward(request, response);
	       }
	       else if(file == null) {
	    	   request.setAttribute("message", "Invalid file");
				request.getRequestDispatcher("ErrorMessage.jsp").forward(request, response);
	       }
	       else if(file.getUser() != user.getId()) {
	    	   request.setAttribute("message", "Invalid operation. You have no authorizarion for this transaction. "
						+ "Access denied.");
				request.getRequestDispatcher("ErrorMessage.jsp").forward(request, response);
	       }
	       else {
        request.getRequestDispatcher("UploadForm.jsp").forward(request, response);
	       }
    }
    
    private filemanager.models.File getFile(Integer id) throws ServletException {
    	filemanager.models.File f = null;
		Connection c = null;
		try {
			String url = "jdbc:mysql://localhost/cs3220stu45";
            String username = "cs3220stu45";
            String password = "jl.*q!oW";

            c = DriverManager.getConnection( url, username, password );
            String selectSQL = "select * from files where id = ?";
            PreparedStatement ps = c.prepareStatement(selectSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery(); 
            
            if(rs.next()) {
            	f = new filemanager.models.File(rs.getInt("id"), rs.getString("name"), rs.getInt("parent_id"),
	            		  rs.getBoolean("is_folder"), rs.getString("type"), rs.getTimestamp("date"), 
	            		  rs.getLong("size"), rs.getInt("user_id"));
            }
            c.close();
		} catch(SQLException e) {
			throw new ServletException();
		} finally {
			try
            {
                if( c != null ) c.close();
            }
            catch( SQLException e )
            {
                throw new ServletException( e );
		}
		}
		return f;
	}

    
    protected void doPost( HttpServletRequest request,
        HttpServletResponse response ) throws ServletException, IOException
    {
    	HttpSession session = request.getSession();
		 User user = (User) session.getAttribute( "user");
    	
    	
    	Integer parentId = Integer.parseInt(request.getParameter("key"));
    	
    	
    	
    	
        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Configure a repository (to ensure a secure temp location is used)
        ServletContext servletContext = this.getServletConfig()
            .getServletContext();
        File repository = (File) servletContext
            .getAttribute( "javax.servlet.context.tempdir" );
        factory.setRepository( repository );

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload( factory );

        
        // The directory we want to save the uploaded files to.
        String fileDir = getServletContext().getRealPath( "/WEB-INF/files" );
        // Parse the request
        try
        {
            List<FileItem> items = upload.parseRequest( request );
            for( FileItem item : items )
            {
                // If the item is not a form field - meaning it's an uploaded
                // file, we save it to the target dir
                if( !item.isFormField() )
                {
                    // item.getName() will return the full path of the uploaded
                    // file, e.g. "C:/My Documents/files/test.txt", but we only
                    // want the file name part, which is why we first create a
                    // File object, then use File.getName() to get the file
                    // name.
                     String fileName = (new File( item.getName() )).getName();
                     
                     Connection c = null;
             		try {
             			String url = "jdbc:mysql://localhost/cs3220stu45";
                         String username = "cs3220stu45";
                         String password = "jl.*q!oW";

                         c = DriverManager.getConnection( url, username, password );
                         String insertSQL = "insert into files (name, parent_id, is_folder, type, date, size, user_id)"
                         		+ " values (?, ?, false, ?, now(), ?, ? )";
                         PreparedStatement preparedStatement = c.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                         preparedStatement.setString( 1, fileName);
                         if (parentId == 0) {
                        preparedStatement.setNull( 2, parentId);
                         } else {
                         preparedStatement.setInt( 2, parentId);
                         }
                         preparedStatement.setString( 3, item.getContentType());
                         preparedStatement.setLong( 4, item.getSize());
                         preparedStatement.setInt( 5, user.getId());
                         
                         preparedStatement.executeUpdate();
                         
                        ResultSet r =  preparedStatement.getGeneratedKeys();
                        
                        r.next();
                        int id = r.getInt(1);
                        
                         
             		c.close();
                   
                    File file = new File( fileDir, id +"" );
                    item.write( file );
                    
                    
            		
             		} catch(SQLException e) {
             			throw new ServletException(e.getMessage());
             		} finally {
             			try
                        {
                            if( c != null ) c.close();
                        }
                        catch( SQLException e )
                        {
                            throw new ServletException( e );
            		}
             		}
                }
            }

        }
        catch( Exception e )
        {
            throw new IOException( e.getMessage() );
        }

        
        
        response.sendRedirect("MyFileManager?key=" + parentId);
    }
    
   

}