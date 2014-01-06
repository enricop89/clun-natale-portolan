package entities;

import entities.Group;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: User
 *
 */
@Entity
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")

public class User implements Serializable {	
	private static final long serialVersionUID = 1L;

	@Id
	private String username;
	
	private String password;
	
	private String email;
	
	private String name;
	
	private String surname;

	//bi-directional many-to-many association to Group
	@ManyToMany
	@JoinTable(
		name="USER_GROUP"
		, joinColumns={
			@JoinColumn(name="USERNAME", referencedColumnName="USERNAME")
			}
		, inverseJoinColumns={
			@JoinColumn(name="GROUPNAME", referencedColumnName="GROUPNAME")
			}
		)
	private List<Group> groups;

	public User() {
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
		return username;
	}

	public void setUserName(String username) {
		this.username = username;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
}
