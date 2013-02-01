package cl.alma.scrw.ui.tasks;

import java.util.HashMap;
import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;


import cl.alma.scrw.ui.forms.UserFormView;

import com.vaadin.Application;

/**
 * This class is the presenter of MyTaskView.
 * 
 * This class has the ability to display the tasks assigned to the user, open forms, and complete tasks.
 *
 */
public class MyTasksPresenter extends TasksPresenter<MyTasksView> 
{

	private static final long serialVersionUID = 4095041737006335740L;

	//private static Logger log = Logger.getLogger(MyTasksPresenter.class
	//		.getName());

	public MyTasksPresenter(MyTasksView view, Application application) 
	{
		super(view, application);
	}

	@Override
	protected List<Task> queryForTasksToShow() 
	{
		String currentUser = getIdOfCurrentUser();
		TaskQuery query = getTaskService().createTaskQuery();
		query.taskAssignee(currentUser).active().orderByTaskPriority().desc()
				.orderByDueDate().desc();
		return query.list();
	}

	/**
	 * when task has no form key, complete task.
	 * @param task = task to be completed.
	 */
	public void completeTask(Task task) 
	{
		//log.log(Level.INFO, "Completing task {1}", task.getId());
		try 
		{
			getTaskService().complete( task.getId() );
			updateTaskList();
			getView().showTaskCompletedSuccess( task );
		} 
		catch (RuntimeException e) 
		{
			//log.log(Level.SEVERE, "Could not complete task", e);
			getView().showTaskCompletedFailure(task);
		}
	}

	/**
	 * @param task = current task to query
	 * @return true if task has form key. 
	 */
	public boolean taskHasForm(Task task) 
	{
		return getFormService().getTaskFormData( task.getId() ).getFormKey() != null;
	}

	/**
	 * Calls the UserFormView passing the id of task, and the task's form key as parameters.
	 * @param task = task whose form will be opened
	 */
	public void openFormForTask( Task task )
	{
		String formKey = getFormKey( task );
		if (formKey != null) 
		{
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put(UserFormView.KEY_FORM_KEY, formKey);
			params.put(UserFormView.KEY_TASK_ID, task.getId());
			getViewController().goToView(UserFormView.VIEW_ID, params);
		}
	}

	/**
	 * gets the task form key.
	 * @param task = task whose form key will be obtained
	 * @return task's form key.
	 */
	private String getFormKey( Task task ) 
	{
		return getFormService().getTaskFormData( task.getId() ).getFormKey();
	}

	/**
	 * @deprecated
	 */
	public void delegateToOtherUser( Task task ) 
	{

	}

	private FormService getFormService() 
	{
		return ProcessEngines.getDefaultProcessEngine().getFormService();
	}
}
