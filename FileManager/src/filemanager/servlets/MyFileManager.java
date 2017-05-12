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





@WebServlet("/MyFileManager")
public class MyFileManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public MyFileManager() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		

        try
        {
            Class.forName( "com.mysql.jdbc.Driver" );
        }
        catch( ClassNotFoundException e )
        {
            throw new ServletException( e );
        }
		
		String path = "/images";
		
		List<String> lib = new ArrayList<String>(); 
		
		String fileDir = getServletContext().getRealPath( path);
		
		 java.io.File file = new java.io.File(fileDir);
		 
		java.io. File[] listOfFiles = file.listFiles();
		
		for (java.io.File img : listOfFiles) {
			String[] name = img.getName().split("\\.");
			lib.add(name[0]);
		}
		getServletContext().setAttribute("imgLib", lib);	
		
		
		
	}
	
	
	private File[] getAttributes(File parentFile, File grand, LinkedList<File> ancestors, Integer id)
	throws ServletException{
		
		List<File> files = new ArrayList<File>();
		Connection c = null;
		try {
			
			String url = "jdbc:mysql://localhost/cs3220stu45";
            String username = "cs3220stu45";
            String password = "jl.*q!oW";

            c = DriverManager.getConnection( url, username, password );
            String selectSQL = "select * from files";
            PreparedStatement preparedStatement = c.prepareStatement(selectSQL);
            
            ResultSet rs = preparedStatement.executeQuery(selectSQL );
            
            while( rs.next() ){
	               files.add(new File(rs.getInt("id"), rs.getString("name"), rs.getInt("parent_id"),
	            		  rs.getBoolean("is_folder"), rs.getString("type"), rs.getTimestamp("date"), 
	            		  rs.getLong("size"), rs.getInt("user_id")));
	            }
            
            parentFile = getFile(files, id);
           
            if (parentFile !=null) {
         	   grand = getFile(files, parentFile.getParent());
            }
           
        Integer fileList = null;

   		if (grand !=null){
   			fileList = grand.getParent();
   			ancestors.addFirst(grand);
   		}
   		
   		while (fileList !=null) {
   			File nextFile = getFile(files, fileList);
   			ancestors.addFirst(nextFile);
   			if (nextFile!=null)
   			fileList = nextFile.getParent();
   			else fileList = null;
   		}
           
            
            
		} catch(SQLException e) {
			throw new ServletException();
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
		File[] f = new File[2];
		f[0] = parentFile;
		f[1] = grand;
		return f;
	}
	
	private File getFile(List<File> files, Integer id) {
		
		for (File f : files)
			if (id.equals(f.getId())) return f;
		
		return null;
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<File> files = new ArrayList<File>();
		HttpSession session = request.getSession();
		 User user = (User) session.getAttribute( "user");
		 Integer parent = 0;
		String key = request.getParameter("key");
		if (key !=null)
			parent = Integer.parseInt(key);	
		
		 File parentFile = null;
         File grand = null;
         LinkedList<File> ancestors = new LinkedList<File>();
         
        File[] f =  getAttributes(parentFile, grand, ancestors, parent);
        
        parentFile = f[0];
        grand = f[1];
         
        
		
		 
		if (user == null) {
	    	   response.sendRedirect("FileManagerLogin");
	       } else if (parentFile!=null && parentFile.getUser() != user.getId() ) {
	    	   request.setAttribute("message", "You are not allowed to view this file");
	    	   request.getRequestDispatcher( "ErrorMessage.jsp" )
	           .forward( request, response );
	    	   
	       } else {
		
		Connection c = null;
		try {
			
			String url = "jdbc:mysql://localhost/cs3220stu45";
            String username = "cs3220stu45";
            String password = "jl.*q!oW";

            c = DriverManager.getConnection( url, username, password );
            String selectSQL;
            PreparedStatement preparedStatement;
            
            if (parent == 0) {
            selectSQL = "select f.id, f.name, f.parent_id, f.is_folder, f.type, f.date, f.size, "
            		+ "f.user_id from files f where "
            		+ "f.user_id =" + user.getId() +" and f.parent_id is null order by f.is_folder desc";
            preparedStatement = c.prepareStatement(selectSQL);
            
            } else {
            	 selectSQL = "select f.id, f.name, f.is_folder, f.parent_id, f.type, f.date, f.size,"
            	 		+ " f.user_id from files f where "
                 		+ "f.user_id =" + user.getId()  +  " and f.parent_id =" + parent + " order by f.is_folder desc";
            	 preparedStatement = c.prepareStatement(selectSQL);
                }
            
            ResultSet rs = preparedStatement.executeQuery(selectSQL );
            
            while( rs.next() ){
	               files.add(new File(rs.getInt("id"), rs.getString("name"), rs.getInt("parent_id"),
	            		  rs.getBoolean("is_folder"), rs.getString("type"), rs.getTimestamp("date"), 
	            		  rs.getLong("size"), rs.getInt("user_id")));
	            }
            
           
            
            
            
            
            c.close();
			
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
			
		
		
		request.setAttribute("grand", grand);
		request.setAttribute("parent", parent);
		request.setAttribute("parentFile", parentFile);
		request.setAttribute("ancestors", ancestors);
		request.setAttribute("Files", files);
		
		request.getRequestDispatcher( "DisplayFiles.jsp" )
        .forward( request, response );
		
	       }
	       
	
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
