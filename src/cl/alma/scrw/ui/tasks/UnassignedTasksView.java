package cl.alma.scrw.ui.tasks;

import org.activiti.engine.task.Task;

/**
 * This interface implements the methods necessary by the UnassignedTasksViewImpl and UnassignedTasksPresenter. 
 *
 */
public interface UnassignedTasksView extends TasksView {

	String VIEW_ID = "unassignedTasks";

	/**
	 * Display message that task was assigned to himself successfully. 
	 * @param task = task whose assign successfull message is shown.
	 */
	void showTaskAssignmentSuccess( Task task );

	/**
	 * Display message that task was assigned to himself failed. 
	 * @param task = task whose assign failure message is shown.
	 */
	void showTaskAssignmentFailure( Task task );
}
