package filemanager.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.Statement;

import filemanager.models.File;
import filemanager.models.User;


@WebServlet("/CreateFolder")
public class CreateFolder extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
       

	
    public CreateFolder() {
        super();
       
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

    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		 User user = (User) session.getAttribute( "user");
		 Integer parent = 0;
			String key = request.getParameter("key");
			
			if (key !=null)
			parent = Integer.parseInt(key);
		 File file = getFile(parent);
	       
	       if (user == null) {
	    	   response.sendRedirect("FileManagerLogin");
	       } 
	       else if (parent == 0) {
	    	   request.getRequestDispatcher("CreateForm.jsp").forward(request, response);
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
		request.getRequestDispatcher("CreateForm.jsp").forward(request, response);
	       }
	}


	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute( "user");
		 
		
		Integer parent = 0;
		String key = request.getParameter("key");
		
		if (key !=null)
		parent = Integer.parseInt(key);
		
		
		
		String name = request.getParameter("name");
		
		
		Connection c = null;
		try {
			String url = "jdbc:mysql://localhost/cs3220stu45";
            String username = "cs3220stu45";
            String password = "root";

            c = DriverManager.getConnection( url, username, password );
            String selectSQL = "insert into files (name, parent_id, is_folder, type, date, size, user_id)"
            		+ " values (?, ?, true, 'folder', now(), null, ? )";
            PreparedStatement preparedStatement = c.prepareStatement(selectSQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString( 1, name);
            if (parent == 0) {
           preparedStatement.setNull( 2, parent);
            } else {
            preparedStatement.setInt( 2, parent);
            }
            preparedStatement.setInt( 3, user.getId());
            
            preparedStatement.executeUpdate();
            ResultSet r =  preparedStatement.getGeneratedKeys();
            int returnKey = 0;
            if (r.next()) {
            	returnKey = r.getInt(1);
            }
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyy hh:mma");
            Date date = new Date();
           String time = format.format(date);
            
   
            response.getWriter().print("" +returnKey + "&" + time);
            
            
            c.close();
		//response.sendRedirect("MyFileManager?key="+ parent);
		} catch(Exception e) {
			throw new ServletException(e);
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
