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
@Table(name="GROUPS") // this table renaming is a workaround. Leaving it as it is cause JPA not creating this table

public class Group implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String groupName;

	@ManyToMany(mappedBy="groups")
	private List<User> users;

	public Group() {
		super();
	}
	public Group(String groupName) {
		this.groupName = groupName;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}
