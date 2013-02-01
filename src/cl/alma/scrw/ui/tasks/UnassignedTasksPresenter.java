package cl.alma.scrw.ui.tasks;

import java.util.ArrayList;
import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;

import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import cl.alma.scrw.bpmn.session.UserData;

import com.vaadin.Application;

/**
 * This class implements the functionalities of unassigned tasks.
 * 
 * This are tasks that the current user is able to assign to himself because they are
 * 
 * assigned to one of his groups.
 *
 */
public class UnassignedTasksPresenter extends
		TasksPresenter<UnassignedTasksView> 
{

	private static final long serialVersionUID = -7000703160639810245L;

	//private static Logger log = Logger.getLogger(UnassignedTasksPresenter.class
	//		.getName());

	public UnassignedTasksPresenter(UnassignedTasksView view,
			Application application) 
	{
		super(view, application);
	}

	/**
	 * The current user claims task if possible.
	 * And update task list is called.
	 * @param task = task to be claimed
	 */
	public void assignTaskToCurrentUser( Task task )
	{
		String currentUserId = getIdOfCurrentUser();

		//log.log(Level.INFO, "Assigning task {1} to user {2}", new Object[] {
		//		task.getId(), currentUserId });
		try 
		{
			getTaskService().claim( task.getId(), currentUserId) ;
			updateTaskList();
			getView().showTaskAssignmentSuccess(task);
		}
		catch (RuntimeException e) 
		{
			//log.log(Level.SEVERE, "Could not assign task to user", e);
			getView().showTaskAssignmentFailure(task);
		}
	}

	/**
	 * @deprecated
	 */
	public void assignTaskToOtherUser(Task task) {
		// TODO Implement me!
	}

	@Override
	protected List<Task> queryForTasksToShow() 
	{
		//String currentUser = getIdOfCurrentUser();
		List<String> groups = this.getGroups();
		if( groups.size() == 0 )
			return new ArrayList<Task>();
		
		TaskQuery query = getTaskService().createTaskQuery();
		query.taskUnassigned().active().taskCandidateGroupIn( groups )
			.orderByTaskPriority().desc().orderByDueDate().desc();
		//query.taskUnassigned().active().taskCandidateUser( currentUser )
			//	.orderByTaskPriority().desc().orderByDueDate().desc();
		return query.list();
	}
	
	/**
	 * gets the current user groups
	 * @return a list with the current user groups
	 */
	private List<String> getGroups()
	{
		return ((UserData)getApplication().getUser()).getGroups();
	}
}
