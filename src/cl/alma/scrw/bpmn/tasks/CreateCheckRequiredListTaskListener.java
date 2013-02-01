package cl.alma.scrw.bpmn.tasks;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
/**
 * This class intends to execute the necessary steps after the Setup Finished Page is completed.
 * 
 * This includes adding a variable that represents the work of the user must be checked.
 * The variable name is checkRequired_${username} where ${username} is the corresponding user name.
 * 
 * @author Mauricio Pilleux 
 *
 */
public class CreateCheckRequiredListTaskListener implements TaskListener{
	
	@Override
	public void notify( DelegateTask execution ) 
	{	
		execution.setVariable("checkRequired_"+execution.getVariable("assignee"),execution.getVariable("checkRequired"));	
	}

	
}
