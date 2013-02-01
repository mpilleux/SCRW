package cl.alma.scrw.bpmn.tasks;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
/**
 * This class intends to execute the necessary steps after the Execution of Finished Page is completed.
 * 
 * This includes adding a comment variable that represents the work done by the user.
 * The variable name is ackEFinishedPageComment_${username} where ${username} is the corresponding username name.
 * 
 * @author Mauricio Pilleux 
 *
 */
public class ExecuteJobCommentTaskListener implements TaskListener{
	
	@Override
	public void notify( DelegateTask execution ) 
	{
		execution.setVariable("ackEFinishedPageComment_"+execution.getVariable("assignee"),execution.getVariable("ackEFinishedPageComment"));
	}

	
}
