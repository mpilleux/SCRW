package cl.alma.scrw.bpmn.tasks;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
/**
 * This class intends to execute the necessary steps after the Setup Finished Page is completed.
 * 
 * This includes adding two variables, one that represents is check of the work is required after the execution,
 * and a new assignee variable that represents to whom the future tasks will be delegated.
 * 
 * The check required variable name is checkRequired_${username} where ${username} is the corresponding user name.
 * The new assignee variable name is newAssignee_${username} where ${username} is the corresponding user name. 
 * 
 * @author Mauricio Pilleux 
 *
 */
public class SetUpFinishedTaskListener implements TaskListener{
	
	@Override
	public void notify( DelegateTask execution ) 
	{		
		execution.setVariable( "checkRequired_"+execution.getVariable("assignee"), execution.getVariable("checkRequired") );
		if( execution.getVariable( "newAssignee") != null )
			execution.setVariable( "newAssignee_"+execution.getVariable("assignee"), execution.getVariable("newAssignee") );
		else
			execution.setVariable( "newAssignee_"+execution.getVariable("assignee"), "" );
	}

	
}
