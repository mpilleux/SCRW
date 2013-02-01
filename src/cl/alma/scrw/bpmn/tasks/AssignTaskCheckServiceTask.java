package cl.alma.scrw.bpmn.tasks;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import cl.alma.scrw.ui.login.Authentication;

/**
 * This class intends to create the necessary variables to create the tasks.
 * 
 * This variables include a list of Actors and their mails (newAssigneeList, newAssigneeMailList), 
 * and a list of users that will check the work after execution and their mails (checkRequiredList, and checkRequiredMailList) 
 * 
 * @author Mauricio Pilleux
 *
 */
public class AssignTaskCheckServiceTask implements JavaDelegate{
	
	@Override
	@SuppressWarnings("unchecked")
	public void execute( DelegateExecution execution )
	{		
		List<String> assigneeList = (List<String>) execution.getVariable("assigneeList");
		List<String> assigneeMailList = (List<String>) execution.getVariable("assigneeMailList");
		String coordinatorMail = (String)execution.getVariable("coordinatorEmail");
		String softMail = (String)execution.getVariable("softwareEmail");
		
		List<String> checkRequiredList = new ArrayList<String>();
		List<String> newAssigneeList = new ArrayList<String>();
		List<String> newAssigneeMailList = new ArrayList<String>();
		List<String> checkRequiredMailList = new ArrayList<String>();
		List<String> fullMailList = assigneeMailList;
		
		for( String assignee : assigneeList )
		{
			String newAssignee = (String)execution.getVariable( "newAssignee_"+assignee );
			String mail = "";
			if( newAssignee == null || newAssignee.equals("") || newAssignee.equals("null") )
			{
				if( !newAssigneeList.contains( assignee ) )
				{
					newAssigneeList.add( assignee );
					mail = Authentication.getMail( assignee );
				}
			}
			else
			{
				if( !newAssigneeList.contains( newAssignee ) )
				{
					newAssigneeList.add( newAssignee );
					mail = Authentication.getMail( newAssignee );
				}
			}
			if( mail.length() > 0 )
			{
				newAssigneeMailList.add( mail );
				if( ! fullMailList.contains( mail ) )
					fullMailList.add( mail );
			}
			
			boolean checkRequired = (Boolean) execution.getVariable( "checkRequired_"+assignee );
			if( checkRequired )
			{
				String chkMail = "";
				if( !checkRequiredList.contains( assignee ) )
				{
					checkRequiredList.add( assignee );
					chkMail = Authentication.getMail( assignee );
				}
				if( chkMail.length() > 0 )
				{
					checkRequiredMailList.add( chkMail );
					if( ! fullMailList.contains( mail ) )
						fullMailList.add( chkMail );
				}
			}
		}
		
		if( ! fullMailList.contains( coordinatorMail ) )
			fullMailList.add( coordinatorMail );
		
		if( ! fullMailList.contains( softMail ) )
			fullMailList.add( softMail );
		
		
		execution.setVariable( "newAssigneeList", newAssigneeList );
		execution.setVariable( "newAssigneeMailList", newAssigneeMailList );
		execution.setVariable( "checkRequiredList", checkRequiredList );
		execution.setVariable( "checkRequiredMailList", checkRequiredMailList );
		
		execution.setVariable( "chkReq", checkRequiredList.size() );
		
		execution.setVariable( "fullMailList", fullMailList );
		
		
		
	}
}
