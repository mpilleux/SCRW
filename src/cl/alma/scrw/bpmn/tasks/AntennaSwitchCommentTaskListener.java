package cl.alma.scrw.bpmn.tasks;
 
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
/**
 * This class intends to execute the necessary steps after the Execution of ${antenna} Finished Page is completed.
 * 
 * This includes adding a comment variable that represents the work on the new Antenna created.
 * The variable name is newAntenna_${antenna}_PageComment where ${antenna} is the corresponding antenna name.
 * 
 * @author Mauricio Pilleux 
 *
 */
public class AntennaSwitchCommentTaskListener implements TaskListener{
	
	@Override
	public void notify( DelegateTask execution ) 
	{
		execution.setVariable("newAntenna_"+execution.getVariable("antenna")+"_PageComment",execution.getVariable("ackEFinishedPageComment"));
	}

	
}
