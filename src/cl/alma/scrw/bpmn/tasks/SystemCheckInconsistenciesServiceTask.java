package cl.alma.scrw.bpmn.tasks;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class SystemCheckInconsistenciesServiceTask implements JavaDelegate{
	
	@Override
	public void execute( DelegateExecution execution )
	{
		Logger log = Logger.getLogger(SystemCheckInconsistenciesServiceTask.class
				.getName());
		
		execution.setVariable("incFound", false);
		log.log(Level.INFO, "I am checking for inconsistencies");
	}
}
