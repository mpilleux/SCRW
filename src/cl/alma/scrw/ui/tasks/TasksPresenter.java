package cl.alma.scrw.ui.tasks;

import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

import cl.alma.scrw.bpmn.session.UserData;

import com.github.peholmst.mvp4vaadin.navigation.ControllablePresenter;
import com.github.peholmst.mvp4vaadin.navigation.ControllableView;
import com.github.peholmst.mvp4vaadin.navigation.Direction;
import com.github.peholmst.mvp4vaadin.navigation.ViewController;
import com.vaadin.Application;

/**
 * This class intends to implement the necessary functionalities that all task presenters should have.
 * 
 * Such as; query for tasks to display, and refresh the views.
 *
 * @param <V> task view
 */
public abstract class TasksPresenter<V extends TasksView> extends
		ControllablePresenter<V>
{

	private static final long serialVersionUID = -7145661950408374225L;

	private final Application application;

	public TasksPresenter(V view, Application application) 
	{
		super(view);
		this.application = application;
	}

	protected final Application getApplication()
	{
		return application;
	}

	protected abstract List<Task> queryForTasksToShow();

	@Override
	protected void viewShown(ViewController viewController,
			Map<String, Object> userData, ControllableView oldView,
			Direction direction) 
	{
		updateTaskList();
	}

	/**
	 * updates task list.
	 */
	protected void updateTaskList() 
	{
		List<Task> tasksToShow = queryForTasksToShow();
		getView().setTasks( tasksToShow );
	}

	/**
	 * updates the task list.
	 */
	public void refreshTasks()
	{
		updateTaskList();
	}

	/**
	 * gets the id of the current user.
	 * @return the id of the logged in user.
	 */
	protected String getIdOfCurrentUser()
	{
		return  ( (UserData)application.getUser() ).getUsername();
	}

	protected TaskService getTaskService() 
	{
		return ProcessEngines.getDefaultProcessEngine().getTaskService();
	}
}
