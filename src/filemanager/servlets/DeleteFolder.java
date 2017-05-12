package filemanager.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import filemanager.models.File;
import filemanager.models.User;
import java.sql.*;

@WebServlet("/DeleteFolder")
public class DeleteFolder extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteFolder() {
        super();
        // TODO Auto-generated constructor stub
    }

    private File getFile(Integer id) throws ServletException {
    	File f = null;
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
		
		
		
		
		File deleteFolder = getFile(parent);
		
		
	       
		 
	       if (user == null) {
	    	   response.sendRedirect("FileManagerLogin");
	       } else if(deleteFolder == null || deleteFolder.getUser() != user.getId()) {
	    	   request.setAttribute("message", "Invalid operation. You have no authorizarion for this transaction. "
						+ "Access denied.");
				request.getRequestDispatcher("ErrorMessage.jsp").forward(request, response);
	       }
	       
	       else {
		
		Integer grand = deleteFolder.getParent();
		
		List<File> allFiles = getFiles();
		
		recursiveDelete(allFiles, parent, deleteFolder.isFolder());
		
		if (grand == null) 
			response.sendRedirect("MyFileManager");
		else
		    	response.sendRedirect("MyFileManager?key="+ grand);
	       }
	}
    
    private void recursiveDelete(List<File> files, Integer parent, Boolean isFolder) throws ServletException {
    	
    	for (File f : files) {
    		if (f.getParent() == parent) {
    			recursiveDelete(files, f.getId(), f.isFolder());
    		}
    	}
    	if (isFolder == false) {
			boolean success = deleteFile(parent+"");
        	if (success == false)
   			 throw new ServletException("Error Path " + getServletContext().getRealPath("/WEB-INF/files/"+ parent + " not found"));
		} 
    	deleteFolder(parent);
	}

	private void deleteFolder(Integer id) throws ServletException {
		Connection c = null;
		try {
			String url = "jdbc:mysql://localhost/cs3220stu45";
            String username = "cs3220stu45";
            String password = "jl.*q!oW";

            c = DriverManager.getConnection( url, username, password );
            String deleteSQL = "delete from files where id = ?";
            PreparedStatement ps = c.prepareStatement(deleteSQL);
            ps.setInt(1, id);
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

	private boolean deleteFile(String name) {
    	String fileDir = getServletContext().getRealPath("/WEB-INF/files/"+ name);
    	
    	java.io.File file = new java.io.File(fileDir);
		boolean success = file.delete();
		  return success;
    }
	
	private List<File> getFiles() throws ServletException{
		List<File> children = new ArrayList<File>();
		Connection c = null;
		try {
			String url = "jdbc:mysql://localhost/cs3220stu45";
            String username = "cs3220stu45";
            String password = "jl.*q!oW";

            c = DriverManager.getConnection( url, username, password );
            String selectSQL = "select * from files";
            PreparedStatement ps = c.prepareStatement(selectSQL);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
            	children.add(new File(rs.getInt("id"), rs.getString("name"), rs.getInt("parent_id"),
	            		  rs.getBoolean("is_folder"), rs.getString("type"), rs.getTimestamp("date"), 
	            		  rs.getLong("size"), rs.getInt("user_id")));
            }
		}
            catch(SQLException e) {
            	throw new ServletException(e.getMessage());
            }
            finally {
            	try
                {
                    if( c != null ) c.close();
                }
                catch( SQLException e )
                {
                    throw new ServletException( e );
            }
            }
		return children;
	}
	
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	

}
