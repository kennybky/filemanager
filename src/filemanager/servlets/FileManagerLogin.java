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


@WebServlet("/FileManagerLogin")
public class FileManagerLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    public FileManagerLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    private User getUser(String name, String pass) throws ServletException{
		User u = null;
		Connection c = null;
		try {
			
			String url = "jdbc:mysql://localhost/cs3220stu45";
            String username = "cs3220stu45";
            String password = "root";

            c = DriverManager.getConnection( url, username, password );
		String selectSQL = "select id, username, password, fname, lname from users where username ='" + name + "' and password ='"+ pass + "'";
		PreparedStatement preparedStatement = c.prepareStatement(selectSQL);
		
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		
		if (rs.next()) {
		u = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("fname"),
				rs.getString("lname"));
		}
		
		}
		catch(SQLException e) {
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
		
			return u;
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("FileManagerLogin.jsp").forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String name = request.getParameter("username");
		String password = request.getParameter("password");
		
		User user = getUser(name, password);
		
		if(user == null) {
			request.setAttribute("message", "Invalid Username or password");
			request.getRequestDispatcher("FileManagerLogin.jsp").forward(request, response);
		} else {
			session.setAttribute("user", user);
			response.sendRedirect("MyFileManager");
		}
	}

}
