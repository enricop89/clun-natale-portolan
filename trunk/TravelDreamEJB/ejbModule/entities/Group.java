package entities;

import entities.User;

import java.io.Serializable;
import java.util.List;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Group
 *
 */
@Entity
@Table(name="GROUPS") // this renaming is a workaround for JPA not creating this table

public class Group implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String groupname;

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
