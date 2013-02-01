package cl.alma.scrw.ui.main;

import com.github.peholmst.mvp4vaadin.View;

/**
 * This interface shows the necessary methods that MainViewImpl will implement, and MainPresenter will call.  
 *
 */
public interface MainView extends View {

	/**
	 * Sets the number of Unassigned Tasks that can be assigned by the current user.
	 * This are tasks assigned to a group.
	 * @param taskCount = number of unassigned tasks to set. 
	 */
	void setNumberOfUnassignedTasks( long taskCount );

	/**
	 * Sets the number of Tasks assigned to the current user.
	 * This are tasks assigned to the user.
	 * @param taskCount = number of tasks to set. 
	 */
	void setNumberOfMyTasks( long taskCount );

	/**
	 * Sets the name of the current user.
	 * @param username = name of the current user. 
	 */
	void setNameOfCurrentUser(String username);

}
