package filemanager.models;

import java.sql.Timestamp;

public class SharedFile {

	Integer id;
	Integer file;
	Integer user;
	Timestamp date;
	String name;
	String type;
	Long size;
	String ownername;

	public SharedFile(Integer id, Integer file, Integer user, Timestamp date, String name, String type, Long size,
			String ownername) {
		super();
		this.id = id;
		this.file = file;
		this.user = user;
		this.date = date;
		this.name = name;
		this.type = type;
		this.size = size;
		this.ownername = ownername;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFile() {
		return file;
	}

	public void setFile(Integer file) {
		this.file = file;
	}

	public Integer getUser() {
		return user;
	}

	public void setUser(Integer user) {
		this.user = user;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getOwnername() {
		return ownername;
	}

	public void setOwnername(String ownername) {
		this.ownername = ownername;
	}

}
