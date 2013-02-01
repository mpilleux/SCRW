package cl.alma.scrw.ui.tasks;

import org.activiti.engine.task.Task;

/**
 * This interface shows the necessary methods that MyTasksViewImpl will implement, and MyTasksPresenter will call.  
 *
 */
public interface MyTasksView extends TasksView {

	String VIEW_ID = "myTasks";

	/**
	 * Displays a message on the window that the task was completed succesully.
	 * @param task = task that was completed sucessfully.
	 */
	void showTaskCompletedSuccess(Task task);

	/**
	 * Displays a message on the window that the task to be completed failed.
	 * @param task = task that failed on completion.
	 */
	void showTaskCompletedFailure(Task task);
}
