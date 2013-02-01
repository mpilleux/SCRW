package cl.alma.scrw.ui.tasks;

import java.util.List;

import org.activiti.engine.task.Task;

import com.github.peholmst.mvp4vaadin.navigation.ControllableView;

/**
 * This interface shows the functionalities that all tasks should have.
 * 
 * Tasks can be assigned to a user, or they can belong to the user's group.
 *
 */
public interface TasksView extends ControllableView 
{
    
	/**
	 * sets the list of tasks to be shown.
	 * @param tasks = task list to be shown
	 */
	void setTasks(List<Task> tasks);

}
