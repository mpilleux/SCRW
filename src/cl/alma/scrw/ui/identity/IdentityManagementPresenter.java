package cl.alma.scrw.ui.identity;

import com.github.peholmst.mvp4vaadin.navigation.ControllablePresenter;

/**
 * DEPRECATED
 * 
 * This class is not used because LDAP controls the identity management in activiti. 
 *
 */
public class IdentityManagementPresenter extends
		ControllablePresenter<IdentityManagementView> {

	private static final long serialVersionUID = -6991937643818589631L;

	public IdentityManagementPresenter(IdentityManagementView view) {
		super(view);
	}

}
