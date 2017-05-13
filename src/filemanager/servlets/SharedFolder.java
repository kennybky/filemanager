package filemanager.servlets;

import java.io.IOException;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;

import java.util.LinkedList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import filemanager.models.*;

/**
 * Servlet implementation class SharedFolder
 */
@WebServlet("/SharedFolder")
public class SharedFolder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SharedFolder() {
        super();
        // TODO Auto-generated constructor stub
    }

   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<SharedFile> files = new ArrayList<SharedFile>();
		HttpSession session = request.getSession();
		 User user = (User) session.getAttribute( "user");
		
         
		 
		if (user == null) {
	    	   response.sendRedirect("FileManagerLogin");
	       } else {
		
		Connection c = null;
		try {
			String url = "jdbc:mysql://localhost/cs3220stu45";
            String username = "cs3220stu45";
            String password = "root";

            c = DriverManager.getConnection( url, username, password );
            String selectSQL;
            PreparedStatement pstmt;
            
            selectSQL = "select s.id, s.file_id, s.user_id, s.date, f.name, f.type,  f.size, concat_ws(' ', u.fname, u.lname) as ownername "
            		+ "from files f join shared s on f.id = file_id "
            		+ "join users u on f.user_id = u.id where s.user_id = ?";
            
            pstmt = c.prepareStatement(selectSQL);
            pstmt.setInt(1, user.getId());
            ResultSet rs = pstmt.executeQuery();
            
            
            while( rs.next() ){
	               files.add(new SharedFile(rs.getInt("id"), rs.getInt("file_id"), rs.getInt("user_id"), rs.getTimestamp("date"), rs.getString("name"),
	            		 rs.getString("type"), rs.getLong("size"), rs.getString("ownername")));
	            }
            
            
            
			
			
		} catch(SQLException e) {
			 throw new ServletException( e );
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
		
		request.setAttribute("Files", files);
		
		request.getRequestDispatcher( "Shared.jsp" )
        .forward( request, response );
		
	       }
		
		
		
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
