package entities;

import entities.User;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Group
 *
 */
@Entity
@NamedQuery(name="Group.findAll", query="SELECT g FROM Group g")

public class Group implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String groupname;

	//bi-directional many-to-many association to User
	@ManyToMany(mappedBy="groups")
	private List<User> users;

	public Group() {
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	public String getGroupName() {
		return this.groupname;
	}

	public void setGroupName(String groupname) {
		this.groupname = groupname;
	}

}
