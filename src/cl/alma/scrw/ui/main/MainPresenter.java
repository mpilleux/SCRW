package cl.alma.scrw.ui.main;

import java.util.List;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.TaskQuery;


import cl.alma.scrw.bpmn.session.UserData;
import cl.alma.scrw.ui.tasks.MyTasksView;
import cl.alma.scrw.ui.tasks.UnassignedTasksView;

import com.github.peholmst.mvp4vaadin.Presenter;
import com.github.peholmst.mvp4vaadin.navigation.DefaultViewController;
import com.github.peholmst.mvp4vaadin.navigation.ViewController;
import com.github.peholmst.mvp4vaadin.navigation.ViewProvider;
import com.vaadin.Application;

/**
 * This class is the presenter of MainView.
 * 
 * This class allows to define the current application's controls.
 *
 */
public class MainPresenter extends Presenter<MainView> 
{

	private static final long serialVersionUID = 1767368195413660867L;

	private final Application application;

	private ViewController viewController;

	public MainPresenter(MainView view, Application application,
			ViewProvider viewProvider) 
	{
		super(view);
		this.application = application;
		viewController = new DefaultViewController();
		viewController.setViewProvider(viewProvider);
	}

	@Override
	/**
	 * Initialices the application.
	 */
	public void init() 
	{
		refreshTaskCounters();
		String currentUser = getNameOfCurrentUser();
		getView().setNameOfCurrentUser(currentUser);
	}

	public ViewController getViewController() 
	{
		return viewController;
	}

	/**
	 * Log out of the application.
	 * This method fires a UserLoggedOutEvent.
	 */
	public void logout() 
	{
		fireViewEvent(new UserLoggedOutEvent(getView()));
	}

	/**
	 * updates both task counter.
	 * The task counters are: unassignedTasks and assignedTasks.
	 */
	public void refreshTaskCounters() 
	{
		getView().setNumberOfMyTasks( getNumberOfTasksAssignedToCurrentUser() );
		getView().setNumberOfUnassignedTasks( getNumberOfUnassignedTasks() );
	}

	/**
	 * @return the current user's number of unassigned tasks (This are tasks
	 * that are assigned to any of the user's groups.
	 */
	private long getNumberOfUnassignedTasks() 
	{
		List<String> groups = this.getGroups();
		if( groups.size() == 0 )
			return 0;
		
		TaskQuery query = getTaskService().createTaskQuery();
		query.taskUnassigned().active().taskCandidateGroupIn( groups );
		return query.count();
	}
	
	/**
	 * gets the current user groups
	 * @return a list with the current user groups
	 */
	private List<String> getGroups()
	{
		return ((UserData)application.getUser()).getGroups();
	}

	/**
	 * @return the number of tasks assigned to the current user.
	 */
	private long getNumberOfTasksAssignedToCurrentUser() 
	{
		String currentUser = getIdOfCurrentUser();
		TaskQuery query = getTaskService().createTaskQuery();
		return query.taskAssignee(currentUser).active().count();
	}

	/**
	 * Displays current user's unassigned tasks.This are tasks
	 * that are assigned to any of the user's groups.
	 */
	public void showUnassignedTasks() 
	{
		getViewController().goToView( UnassignedTasksView.VIEW_ID );
	}

	/**
	 * Displays current user's tasks.
	 */
	public void showMyTasks() 
	{
		getViewController().goToView( MyTasksView.VIEW_ID );
	}

	/**
	 * @return formatted id of current user.
	 */
	private String getNameOfCurrentUser() 
	{
		return String.format( "%s", getIdOfCurrentUser() );
	}

	/**
	 * @return the id of the current user (username)
	 */
	private String getIdOfCurrentUser() 
	{
		return  ((UserData)application.getUser()).getUsername();
	}

	private TaskService getTaskService() 
	{
		return ProcessEngines.getDefaultProcessEngine().getTaskService();
	}

}
