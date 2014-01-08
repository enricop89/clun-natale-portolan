package entities;

import entities.Group;
import beans.accountmanagement.UserDTO;

import java.io.Serializable;
import java.util.List;
import java.lang.String;

import javax.persistence.*;

import org.apache.commons.codec.digest.DigestUtils;

import beans.utils.AESencrp;

/**
 * Entity implementation class for Entity: User
 *
 */
@Entity
@Table(name="USERS")

public class User implements Serializable {	
	private static final long serialVersionUID = 1L;

	@Id
	private String email;
	
	private String userName;
	
	private String password;
	
	private String firstName;
	
	private String lastName;
	
	private String loginfo;

	@ManyToMany
	@JoinTable(
		name="USER_GROUP"
		, joinColumns={
			@JoinColumn(name="EMAIL", referencedColumnName="EMAIL")
			}
		, inverseJoinColumns={
			@JoinColumn(name="GROUPNAME", referencedColumnName="GROUPNAME")
			}
		)
	private List<Group> groups;

	public User() {
		super();
	}
	
	public User(UserDTO user){
        this.userName	  = user.getUserName(); 
        this.password     = DigestUtils.sha256Hex(user.getPassword() );
        this.email        = user.getEmail();
        this.firstName    = user.getFirstName();
        this.lastName     = user.getLastName();     
        try {
			this.loginfo  = AESencrp.encrypt(user.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Group> getGroups() {
		return this.groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getLoginfo() {
		return loginfo;
	}
	
	public void setLoginfo(String loginfo) {
		this.loginfo = loginfo;
	}
}
