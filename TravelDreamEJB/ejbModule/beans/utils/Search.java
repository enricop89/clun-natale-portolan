package beans.utils;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import entities.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class Search
 */
@Stateless
@LocalBean
public class Search {
	@PersistenceContext
    private EntityManager entityManager;
	
	public User findUserByEmail(String email) {
    	return entityManager.find(User.class, email);
    }
}
