package filemanager.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.*;
import filemanager.models.*;


@WebServlet("/Share")
public class Share extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Share() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer file = 0;
		String key = request.getParameter("key");
		
		if (key!=null) {
			file = Integer.parseInt(key);
		}
		
		User user = (User) session.getAttribute("user");
		
		 if (user == null) {
	    	   response.sendRedirect("FileManagerLogin"); 
	       } 
		 
		 boolean owner = checkOwner(file, user);
		 
		 if (owner == false) {
			request.setAttribute("message", "You cant share this file");
			request.getRequestDispatcher("ErrorMessage.jsp").forward(request, response);
		 } else {
		
		String uname = request.getParameter("username");
		
		Integer userId = getUser(uname);
		
		if (userId == null) {
			response.getWriter().print("No user found with that username");
			response.setStatus(400);
		} else {
		
		Connection c = null;
		try {
			String url = "jdbc:mysql://localhost/cs3220stu45";
            String username = "cs3220stu45";
            String password = "jl.*q!oW";

            c = DriverManager.getConnection( url, username, password );
            String insertSQL = "insert into shared (file_id, user_id, date) values ( ? , ? , now());";
            PreparedStatement ps = c.prepareStatement(insertSQL);
            ps.setInt(1, file);
            ps.setInt(2, userId);
            ps.executeUpdate(); 
			
			
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

	private Integer getUser(String uname) throws ServletException {
		Integer userId = null;
    	
		Connection c = null;
		try {
			String url = "jdbc:mysql://localhost/cs3220stu45";
            String username = "cs3220stu45";
            String password = "jl.*q!oW";

            c = DriverManager.getConnection( url, username, password );
            String selectSQL = "select id from users where username = ?";
            PreparedStatement ps = c.prepareStatement(selectSQL);
            ps.setString(1, uname);
            ResultSet rs = ps.executeQuery(); 
            
            if(rs.next()) {
            	userId = rs.getInt("id");
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
		return userId;
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private boolean checkOwner(Integer id, User user) throws ServletException {
    	int userId = 0;
    	boolean owner = false;
		Connection c = null;
		try {
			String url = "jdbc:mysql://localhost/cs3220stu45";
            String username = "cs3220stu45";
            String password = "jl.*q!oW";

            c = DriverManager.getConnection( url, username, password );
            String selectSQL = "select user_id from files where id = ?";
            PreparedStatement ps = c.prepareStatement(selectSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery(); 
            
            if(rs.next()) {
            	userId = rs.getInt("user_id");
            }
            
             if (userId == user.getId()) {
            	 owner = true;
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
		return owner;
	}

}
