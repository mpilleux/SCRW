package cl.alma.scrw.ui.main;

import com.github.peholmst.mvp4vaadin.View;
import com.github.peholmst.mvp4vaadin.ViewEvent;

/**
 * this class implements the functionality of a UserLoggedOut Event.
 *
 */
public class UserLoggedOutEvent extends ViewEvent {

	private static final long serialVersionUID = -4491077705486724571L;

	public UserLoggedOutEvent( View source ) {
		super(source);
	}

}
