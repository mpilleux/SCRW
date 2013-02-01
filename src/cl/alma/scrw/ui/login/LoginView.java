package cl.alma.scrw.ui.login;

import com.github.peholmst.mvp4vaadin.View;

/**
 * This interface shows the necessary methods that LoginViewImpl will implement, and LoginPresenter will call.  
 *
 */
public interface LoginView extends View 
{
	
	/**
	 * Displays a login failed message in the current window.
	 */
	void showLoginFailed();

	/**
	 * Removes all user data from the login form.
	 */
	void clearForm();
}
