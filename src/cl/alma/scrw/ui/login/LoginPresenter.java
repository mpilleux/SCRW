package cl.alma.scrw.ui.login;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;

import com.github.peholmst.mvp4vaadin.Presenter;

/**
 * This class is the presenter of a LoginView.
 * 
 * This class manages the application's login authentication using OpenLDAP.
 *
 */
public class LoginPresenter extends Presenter<LoginView> 
{

	private static final long serialVersionUID = -2879324628084421626L;

	public LoginPresenter(LoginView view) 
	{
		super(view);
	}

	/**
	 * Attemps to login to the LDAP server.
	 * 
	 * if username and password are correct, the application will authenticate the user in the activiti engine, and throw a user logged in event.
	 * @param username = user name to be authenticated
	 * @param password = password associated to username 
	 */
	public void attemptLogin(String username, String password) 
	{		
		Logger log = Logger.getLogger( this.getClass()
				.getName());
		log.log(Level.INFO, "attempting to log in " + username);
		if( Authentication.authenticate( username, password, "ldap://ldapste01.osf.alma.cl", "dc=alma,dc=info" ) )
		{
			log.log(Level.INFO, "log in success: " + username);
			getIdentityService().setAuthenticatedUserId( username );
			fireViewEvent( new UserLoggedInEvent( getView(), username ) );
		}
		else
		{
			log.log(Level.INFO, "attemp to log in failed " + username);
			getView().clearForm();
			getView().showLoginFailed();
		}
	}

	private IdentityService getIdentityService() 
	{
		return ProcessEngines.getDefaultProcessEngine().getIdentityService();
	}
}
