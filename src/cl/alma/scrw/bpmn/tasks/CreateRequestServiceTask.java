package cl.alma.scrw.bpmn.tasks;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import cl.alma.scrw.ui.login.Authentication;

/**
 * This class intends to create a new request.
 * 
 * this class assumens that the variables "actors", "antennas", and "newAntennas" are correctly set.
 * newAntennas represents a list of the names of the new antennas added to the system separated by a comma. This variable can be empty.
 * antennas contain a list of the names of the antennas to be used by the workflow separated by a comma.
 * actors contains a list of the names of the actor assigned to do the systems changes separated by a comma.
 * 
 * This class is called by a serviceTask which calls the execute method.
 * 
 * @author Mauricio Pilleux
 *
 */
public class CreateRequestServiceTask implements JavaDelegate{
	
	@Override
	public void execute( DelegateExecution execution )
	{
		
		String softwareEmail = "mpilleuxg@gmail.com";
		String coordinatorEmail = "mpilleuxg@gmail.com";
		String webURL = "http://urania.osf.alma.cl:8081/SCRW";
		
		//obtain variables from process
		String participants = (String) execution.getVariable( "actors" );
		String antennas = (String) execution.getVariable( "antennas" );
		String newAntennas = (String) execution.getVariable( "newAntennas" );
	
		
		String[] participantsArray = participants.trim().split( "," );
		String[] antennasArray = antennas.trim().split( "," );
		String[] newAntennasArray = newAntennas.trim().split( "," );
		
		List<String> assigneeList = new ArrayList<String>();
		List<String> assigneeMailList = new ArrayList<String>();
		List<String> antennaList = new ArrayList<String>();
		List<String> newAntennaList = new ArrayList<String>();
		
		for ( String assignee : participantsArray )
			if( assignee.length() > 0 && ! assigneeList.contains( assignee ) )
			{
				assigneeList.add( assignee );
				String mail = Authentication.getMail( assignee );
				if( mail.length() > 0 && ! assigneeMailList.contains( mail ) )
					assigneeMailList.add( mail );
			}
		
		execution.setVariable( "assigneeList", assigneeList );
		execution.setVariable( "assigneeMailList", assigneeMailList );
		
		if( ! assigneeMailList.contains( softwareEmail ) )
			assigneeMailList.add( softwareEmail );
		
		if( ! assigneeMailList.contains( coordinatorEmail ) )
			assigneeMailList.add( coordinatorEmail );
		
		execution.setVariable( "fullMailList",  assigneeMailList );
		
		antennas = "|";
		for( String ant : antennasArray )
		{
			if( ant.length() > 0 && ! antennaList.contains( ant ) )
			{
				antennas+=ant+"|";
				antennaList.add( ant );
			}
		}
		
		//every new antenna must be in the antennaList
		for( String ant : newAntennasArray )
		{
			if( ant.length() > 0 && ! newAntennaList.contains( ant ) )
			{
				newAntennaList.add( ant );
			
				if( ! antennas.contains( "|"+ant+"|" ) && ! antennaList.contains( ant ) )
					antennaList.add( ant );
			}
		}		
		
		execution.setVariable( "newAntennaList", newAntennaList );
		execution.setVariable( "antennaList", antennaList );
		execution.setVariable( "countNewAntennaList", newAntennaList.size() );
		
		execution.setVariable( "softwareEmail", softwareEmail );
		execution.setVariable( "coordinatorEmail", coordinatorEmail );
		execution.setVariable( "webURL", webURL );
	}
}
