package filemanager.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import filemanager.models.User;

@WebServlet("/Download")
public class Download extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public Download()
    {
        super();
    }
    
    private filemanager.models.File getFile(Integer id) throws ServletException {
    	filemanager.models.File f = null;
		Connection c = null;
		try {
			String url = "jdbc:mysql://localhost/cs3220stu45";
            String username = "cs3220stu45";
            String password = "root";

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
    
    private boolean getAllowed(Integer id, User user) throws ServletException {
    	boolean allowed = false;
    	int sharedUser = 0;
		Connection c = null;
		try {
			String url = "jdbc:mysql://localhost/cs3220stu45";
            String username = "cs3220stu45";
            String password = "root";

            c = DriverManager.getConnection( url, username, password );
            String selectSQL = "select user_id from shared where id = ?";
            PreparedStatement ps = c.prepareStatement(selectSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery(); 
            
            if(rs.next()) {
            	sharedUser = rs.getInt("user_id");
            }
            c.close();
            
            if (sharedUser == user.getId()) {
            	allowed = true;
            }
            
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
		return allowed;
	}

    protected void doGet( HttpServletRequest request,
        HttpServletResponse response ) throws ServletException, IOException
    {
    	HttpSession session = request.getSession();
		 User user = (User) session.getAttribute( "user");
		 String key = request.getParameter("key");
		 Integer parent = Integer.parseInt(key);
		 String shared = request.getParameter("shared");
		 Integer sharedId = 0;
		 boolean sharedUser = false;
		 
		 if (user == null) {
	    	   response.sendRedirect("FileManagerLogin");
	    	   
	       }
		 
			if (shared != null) {
				sharedId = Integer.parseInt(shared);	
				sharedUser = getAllowed(sharedId, user);
			}
		 
		
		 
		
	       
		 filemanager.models.File theFile = getFile(parent);
		 
	       
	        if((theFile == null || theFile.getUser() != user.getId()) && (sharedUser == false)) {
	    	   request.setAttribute("message", "Invalid operation. You have no authorizarion for this transaction. "
						+ "Access denied.");
				request.getRequestDispatcher("ErrorMessage.jsp").forward(request, response);
	       }
	       
	       
	       else {
    	
    	
        // Get the path to the file and create a java.io.File object
    	
    	String fpath = "/WEB-INF/files/" + key;
        String path = getServletContext()
            .getRealPath( fpath);
        File file = new File(path);
        
        String type = request.getParameter("type");
        String name = request.getParameter("name");

        // Set the response headers. File.length() returns the size of the file
        // as a long, which we need to convert to a String.
        response.setContentType( type );
        response.setHeader( "Content-Length", "" + file.length() );
        response.setHeader( "Content-Disposition",
            "inline; filename=" + name  );

        // Binary files need to read/written in bytes.
        FileInputStream in = new FileInputStream( file );
        OutputStream out = response.getOutputStream();
        byte buffer[] = new byte[2048];
        int bytesRead;
        while( (bytesRead = in.read( buffer )) > 0 )
            out.write( buffer, 0, bytesRead );
        in.close();
	       }
    }

	

}