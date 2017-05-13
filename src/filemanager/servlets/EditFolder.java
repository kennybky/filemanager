package filemanager.servlets;

import java.io.IOException;
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

import filemanager.models.File;
import filemanager.models.User;


@WebServlet("/EditFolder")
public class EditFolder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public EditFolder() {
        super();
        // TODO Auto-generated constructor stub
    }

	
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		 User user = (User) session.getAttribute( "user");
	       
	       if (user == null) {
	    	   response.sendRedirect("FileManagerLogin");
	       }
	       
	       else {
		
		
		Integer parent = 0;
		String key = request.getParameter("key");
		
		if (key !=null)
		parent = Integer.parseInt(key);
		
		File file = getFile(parent);
		
		if (file.getUser() != user.getId()) {
			request.setAttribute("message", "Invalid operation. You have no authorizarion for this transaction. "
					+ "Access denied.");
			request.getRequestDispatcher("ErrorMessage.jsp").forward(request, response);
		} else {
		
		String value = file.getName();
		
		request.setAttribute("value", value);
		request.getRequestDispatcher("EditForm.jsp").forward(request, response);
		}
	      }
	}
	
	private File getFile(Integer id) throws ServletException {
    	File f = null;
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
            	f = new File(rs.getInt("id"), rs.getString("name"), rs.getInt("parent_id"),
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
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		

		Integer parent = 0;
		String key = request.getParameter("key");
		
		if (key !=null)
		parent = Integer.parseInt(key);
		
		String name = request.getParameter("name");
		File editFile = getFile(parent);
		
		Integer grand = editFile.getParent();
		Connection c = null;
		try {
			String url = "jdbc:mysql://localhost/cs3220stu45";
            String username = "cs3220stu45";
            String password = "root";

            c = DriverManager.getConnection( url, username, password );
            String changeSQL = "update files set name = ? where id = ?";
            PreparedStatement ps = c.prepareStatement(changeSQL);
            ps.setString(1, name);
            ps.setInt(2, parent);
            
             ps.executeUpdate();
            
            
            

    		if (grand == null)
    		response.sendRedirect("MyFileManager");
    		else
    			response.sendRedirect("MyFileManager?key=" + grand);
		} catch(SQLException e) {
			throw new ServletException(e.getMessage());
		} finally {
			try
            {
                if( c != null ) c.close();
            }
            catch( SQLException e )
            {
                throw new ServletException( e.getMessage() );
		}
			
		}
		
		
		
		
	}

}
