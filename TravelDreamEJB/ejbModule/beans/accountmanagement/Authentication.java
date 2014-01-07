package beans.accountmanagement;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 * Session Bean implementation class authentication
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class Authentication {

    /**
     * Default constructor. 
     */
    public Authentication() {
        // TODO Auto-generated constructor stub
    }

}
