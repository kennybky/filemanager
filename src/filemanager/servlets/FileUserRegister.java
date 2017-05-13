package filemanager.servlets;

import java.io.IOException;

import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import filemanager.models.User;


@WebServlet("/FileUserRegister")
public class FileUserRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileUserRegister() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session  = request.getSession();
		session.invalidate();
		request.getRequestDispatcher("FileRegisterUser.jsp").forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			HttpSession session = request.getSession();
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String fName = request.getParameter("fName");
		String lName = request.getParameter("lName");
		
		
		if (username == null || password == null || password =="" || fName == null || lName == null ||
				username == "" || password =="" || fName == "" || lName == "") {
			request.setAttribute("message", "You must complete all fields");
			request.getRequestDispatcher("FileRegisterUser.jsp").forward(request, response);
		} else {
			
			Connection c = null;
			try {
				
				String url = "jdbc:mysql://localhost/cs3220stu45";
	            String uname = "cs3220stu45";
	            String pass = "root";
	            c = DriverManager.getConnection( url, uname, pass );
	            
	            String checkSql = "select username from users where username = '" + username + "'";
	            PreparedStatement pstmt = c.prepareStatement(checkSql);
	            ResultSet rs = pstmt.executeQuery(checkSql);
	            
	            if(rs.next()) {
					/*request.setAttribute("message", "Username already exists");
					request.getRequestDispatcher("FileRegisterUser.jsp").forward(request, response);*/
	            	response.getWriter().print("Username already exists!");
	    			response.setStatus(400);
				} else {
	            
	            String sql = "insert into users (username, password, fname, lname) values (?, ?, ?, ?)";
	            PreparedStatement preparedStatement = c.prepareStatement(sql);
	            preparedStatement.setString(1, username);
	            preparedStatement.setString(2, password);
	            preparedStatement.setString(3, fName);
	            preparedStatement.setString(4, lName);
	            preparedStatement.executeUpdate();
				
	            String selectSQL = "select id, username, password, fname, lname from users where username ='" + username + "' and password ='"+ password + "'";
	    		PreparedStatement statement = c.prepareStatement(selectSQL);
	    		
	    		ResultSet rs2 = statement.executeQuery(selectSQL );
	    		
	    		if (rs2.next()) {
	    		 User user = new User(rs2.getInt("id"), rs2.getString("username"), rs2.getString("password"), rs2.getString("fname"),
	    				rs2.getString("lname"));
	    		 c.close();
	    		 session.setAttribute("user", user);
	 			response.sendRedirect("MyFileManager");
	    		} else {
	    			request.setAttribute("message", "oops something went wrong");
	    			request.getRequestDispatcher( "ErrorMessage.jsp" )
	 	           .forward( request, response );
	    		}
			
				
		}
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
