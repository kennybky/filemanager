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

import filemanager.models.User;

/**
 * Servlet implementation class DeleteShared
 */
@WebServlet("/DeleteShared")
public class DeleteShared extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteShared() {
        super();
        // TODO Auto-generated constructor stub
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
            String selectSQL = "select user_id from shared where id = ?";
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
			request.setAttribute("message", "You cant delete this file");
			request.getRequestDispatcher("ErrorMessage.jsp").forward(request, response);
		 } else {
			 Connection c = null;
				try {
					String url = "jdbc:mysql://localhost/cs3220stu45";
		            String username = "cs3220stu45";
		            String password = "jl.*q!oW";

		            c = DriverManager.getConnection( url, username, password );
		            String deleteSQL = "delete from shared where id = ?";
		            PreparedStatement ps = c.prepareStatement(deleteSQL);
		            ps.setInt(1, file);
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
				response.sendRedirect("SharedFolder");
			 
		 }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
