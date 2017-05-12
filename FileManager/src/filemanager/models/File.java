package filemanager.models;


import java.sql.Timestamp;



public class File {
	Integer id;
    String name;
    Integer parent;
    String type;
    Timestamp date;
    Long size;
    boolean folder;
    Integer user;
    
    public File(Integer id, String name, Integer parent, boolean isFolder, String type, Timestamp date, Long size, Integer user) {
    	this.id = id;
    	this.name = name;
    	this.parent = parent;
    	this.folder = isFolder;
    	this.date = date;
    	this.size = size;
    	this.type = type;
    	this.user = user;
    }

	public Integer getId() {
		return id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public Long getSize() {
		return size;
	}


	public boolean isFolder() {
		return folder;
	}

	public Integer getUser() {
		return user;
	}

	
 
}
