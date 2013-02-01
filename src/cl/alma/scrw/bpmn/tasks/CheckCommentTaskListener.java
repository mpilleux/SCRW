package cl.alma.scrw.bpmn.tasks;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
/**
 * This class intends to execute the necessary steps after the Check Done Page is completed.
 * 
 * This includes adding a comment variable that represents the comment made by the revision of the work made by the user.
 * The variable name is checkComment_${username} where ${username} is the corresponding user name.
 * 
 * @author Mauricio Pilleux 
 *
 */
public class CheckCommentTaskListener implements TaskListener{
	
	@Override
	public void notify( DelegateTask execution ) 
	{
		execution.setVariable("checkComment_"+execution.getVariable("assignee"),execution.getVariable("checkDoneComment"));
	}

	
}
