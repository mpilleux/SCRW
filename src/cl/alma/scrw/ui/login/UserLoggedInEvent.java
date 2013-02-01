package cl.alma.scrw.ui.login;

import com.github.peholmst.mvp4vaadin.View;
import com.github.peholmst.mvp4vaadin.ViewEvent;

/**
 * this class implements the functionality of a UserLoggedIn Event
 * and stores necessary session data.
 *
 */
public class UserLoggedInEvent extends ViewEvent {

	private static final long serialVersionUID = 938345850041617561L;

	private final String username;

	public UserLoggedInEvent(View source, String username) 
	{
		super(source);
		this.username = username;
	}
    
	/**
	 * gets the userName associatted with the log in.
	 * @return the login user name.
	 */
	public String getUsername() {
		return username;
	}

}
