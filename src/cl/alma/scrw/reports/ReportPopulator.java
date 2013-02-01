package cl.alma.scrw.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstanceQuery;

import cl.alma.scrw.format.TimeColumnGenerator;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * This class intends to be the center of information regarding the report.
 * 
 * This class is used by the report view impl, and to generate the pdf reports emailed at the end of the process.
 * 
 * @author Mauricio Pilleux
 *
 */
public class ReportPopulator 
{
	private HistoricProcessInstance historicProcessInstance;
	
	private List<String> actorList;
	
	private List<String> taskDefinitionKeyList;//list of all task definition
	
	private Map<String, String> taskInstanceTaskDefinition;
	
	public ReportPopulator( HistoricProcessInstance historicProcessInstance )
	{
		this.historicProcessInstance = historicProcessInstance;
		this.setActivityList();
	}
	
	public List<Component> getCompleteReportLayout()
	{
		List<Component> componentList = new ArrayList<Component>();
		
		
		componentList.add( this.populateUserDisplay( this.getRequest(), this.getAntennaTable(), this.getTitle(), true ) );
		componentList.add( this.populateTaskDisplay() );
		componentList.add( this.populateTaskDefinitionDisplay() );
		
		return componentList;
	}
	
	public VerticalLayout populateUserDisplay( )
	{
		return this.populateUserDisplay( null, null, null, false );
	}
	
	public VerticalLayout populateTaskDisplay( )
	{
		return this.populateTaskDisplay( null, null, null, false );
	}
	
	public VerticalLayout populateTaskDefinitionDisplay( )
	{
		return this.populateTaskDefinitionDisplay( null, null, null, false );
	}
	
	/**
	 * @return a text area with the value of the request variable in the activiti engine.
	 */
	public TextArea getRequest()
	{
		TextArea request = new TextArea("Request");
		
		
		request.setColumns( 35 );
		request.setRows( 15 );
		
		HistoricVariableInstanceQuery variableQuery = getVariableQuery();
		
		if( variableQuery != null && variableQuery.variableName( "request" ).singleResult() != null )
			request.setValue( variableQuery.variableName( "request" ).singleResult().getValue() );
		request.setReadOnly( true );
		
		return request;
	}
	
	/**
	 * @return the process title.
	 */
	public TextField getTitle()
	{
		TextField title = new TextField("Title");
		title.setWidth( "100%" );
		
		HistoricVariableInstanceQuery variableQuery = getVariableQuery();
		
		
		if( variableQuery != null && variableQuery.variableName( "requestTitle" ).singleResult() != null )
			title.setValue( variableQuery.variableName( "requestTitle" ).singleResult().getValue() );
		title.setReadOnly( true );
		
		return title;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * @return the antenna table associated to processInstance
	 */
	public Table getAntennaTable()
	{
		Table antennaTable = new Table("Antennas Involved");
		
		HistoricVariableInstanceQuery variableQuery = getVariableQuery();
		
		List<String> antennaList = new ArrayList<String>();
		if( variableQuery != null && variableQuery.variableName( "antennaList" ).singleResult() != null )
			antennaList = ( List<String> ) variableQuery.variableName( "antennaList" ).singleResult().getValue();
		
		List<String> newAntennaList = new ArrayList<String>();
		if( variableQuery != null && variableQuery.variableName( "newAntennaList" ).singleResult() != null )
			newAntennaList = ( List<String> ) variableQuery.variableName( "newAntennaList" ).singleResult().getValue();
		
		antennaTable.addContainerProperty("Antenna", String.class,  null);
		antennaTable.addContainerProperty("New?", String.class,  null);
		antennaTable.setPageLength( antennaList.size() );
		
		int count = 1;
		//populates antenna tables
		for( String antenna : antennaList )
		{
			if( newAntennaList.contains( antenna ) )
				antennaTable.addItem(new Object[] {
					    antenna, "yes" }, new Integer( count ) );
			else
				antennaTable.addItem(new Object[] {
					    antenna, "no" }, new Integer( count ) );
			count++;			
		}
		
		return antennaTable;
	}
	
	/**
	 * Generates the necessary tables to create a user report
	 * based on the tasks achieved by the user.
	 */
	public VerticalLayout populateUserDisplay( Component request, Component antennaTable, Component title, boolean showTopDisplay )
	{
		if( this.historicProcessInstance == null )
			return new VerticalLayout();
		
		VerticalLayout userDisplay = new VerticalLayout();
		userDisplay.setMargin( true );
		userDisplay.setSpacing( true );
		
		if( showTopDisplay )
		{
			HorizontalLayout topDisplay = new HorizontalLayout();
			userDisplay.addComponent( title );
			topDisplay.addComponent( request );
			topDisplay.addComponent( antennaTable );
			userDisplay.addComponent( topDisplay );
		}
		
		
		for( String user : this.actorList )
		{
			//get the user tasks
			List<HistoricTaskInstance> taskInstanceList;
			HistoricTaskInstanceQuery taskQuery = getTaskQuery();
			
			if( getTaskQuery() == null )
				taskInstanceList = new ArrayList<HistoricTaskInstance>();
			else
				taskInstanceList = taskQuery
						.taskAssignee( user ).orderByHistoricTaskInstanceEndTime().asc().list();
			
			//creates table structure
			Table userTasksTable = new Table( "Username: "+user );
			userTasksTable.setSelectable( true );
			userTasksTable.addStyleName("components-inside");
			userTasksTable.addContainerProperty("ID", Integer.class,  null);
			userTasksTable.addContainerProperty("Task Name", String.class,  null);
			userTasksTable.addContainerProperty("Start Time", String.class,  null);
			userTasksTable.addContainerProperty("End Time", String.class,  null);
			userTasksTable.addContainerProperty("Duration", Label.class,  null);
			userTasksTable.addContainerProperty("Comment", TextArea.class,  "");
			userTasksTable.setPageLength( taskInstanceList.size() );
			userTasksTable.setWidth("100%");
			
			//populate table
			for( HistoricTaskInstance historicTaskInstance : taskInstanceList )
			{
				String user_comment = getUserComment( this.taskInstanceTaskDefinition.get( historicTaskInstance.getId() ), user ,historicTaskInstance.getName() );
				TextArea txtComment = new TextArea();
				txtComment.setValue( user_comment );
				txtComment.setReadOnly( true );
				
				Label lblTaskName = new Label();
				lblTaskName.setValue( historicTaskInstance.getName() );
				
				Label lblStartTime = new Label();
				lblStartTime.setValue( historicTaskInstance.getStartTime() );
				
				Label lblEndTime = new Label();
				lblEndTime.setValue( historicTaskInstance.getEndTime() );
				TimeColumnGenerator tc = new TimeColumnGenerator();
				userTasksTable.addItem(new Object[] {
						historicTaskInstance.getId(), lblTaskName,
						lblStartTime, lblEndTime,
						tc.format( historicTaskInstance.getDurationInMillis() ), txtComment
						}, new Integer( historicTaskInstance.getId() ) );
			}
			
			userDisplay.addComponent( userTasksTable );
		}
		return userDisplay;
	}
	
	/**
	 * Generates the necessary tables to create a task report
	 * based on the tasks's Historic Instance. 
	 */
	public VerticalLayout populateTaskDisplay( Component request, Component antennaTable, Component title, boolean showTopDisplay )
	{
		if( this.historicProcessInstance == null )
			return new VerticalLayout();
		
		VerticalLayout taskDisplay = new VerticalLayout();
		taskDisplay.setMargin( true );
		taskDisplay.setSpacing( true );
		
		if( showTopDisplay )
		{
			HorizontalLayout topDisplay = new HorizontalLayout();
			taskDisplay.addComponent( title );
			topDisplay.addComponent( request );
			topDisplay.addComponent( antennaTable );
			taskDisplay.addComponent( topDisplay );
		}
		
		//get the tasks ordered by start time.		
		List<HistoricTaskInstance> taskInstanceList;
		HistoricTaskInstanceQuery taskQuery = getTaskQuery();
		
		if( getTaskQuery() == null )
			taskInstanceList = new ArrayList<HistoricTaskInstance>();
		else
			taskInstanceList = taskQuery
					.orderByHistoricActivityInstanceStartTime().asc().list();
		
		//creates table structure
		Table activityTable = new Table( "Tasks" );
		activityTable.setSelectable( true );
		activityTable.addStyleName("components-inside");
		activityTable.addContainerProperty("ID", Integer.class,  null);
		activityTable.addContainerProperty("Task Name", String.class,  null);
		activityTable.addContainerProperty("Assignee", String.class,  null);
		activityTable.addContainerProperty("Start Time", Label.class,  null);
		activityTable.addContainerProperty("End Time", Label.class,  null);
		activityTable.addContainerProperty("Duration", Label.class,  null);
		activityTable.addContainerProperty("Comment", TextArea.class,  "");
		activityTable.setPageLength( taskInstanceList.size() );
		activityTable.setWidth("100%");
		
		for( HistoricTaskInstance historicTaskInstance : taskInstanceList )
		{
			String taskDefinitionId = this.taskInstanceTaskDefinition.get( historicTaskInstance.getId() );
			String user = historicTaskInstance.getAssignee();
			String taskName = historicTaskInstance.getName();
			String user_comment = getUserComment( taskDefinitionId, user ,taskName );
			TextArea txtComment = new TextArea();
			txtComment.setValue( user_comment );
			txtComment.setReadOnly( true );
			
			Label lblTaskName = new Label();
			lblTaskName.setValue( taskName );
			
			Label lblStartTime = new Label();
			lblStartTime.setValue( historicTaskInstance.getStartTime() );
			
			Label lblEndTime = new Label();
			lblEndTime.setValue( historicTaskInstance.getEndTime() );
			TimeColumnGenerator tc = new TimeColumnGenerator();
			activityTable.addItem(new Object[] {
					historicTaskInstance.getId(), lblTaskName.getValue(), historicTaskInstance.getAssignee(),
					lblStartTime, lblEndTime,
					tc.format( historicTaskInstance.getDurationInMillis() ), txtComment
					}, new Integer( historicTaskInstance.getId() ) );
		}
		
		taskDisplay.addComponent( activityTable );
		
		return taskDisplay;
	}
	
	/**
	 * Generates the necessary tables to create a task report
	 * based on the tasks's DefinitionKey.  
	 */
	public VerticalLayout populateTaskDefinitionDisplay( Component request, Component antennaTable, Component title, boolean showTopDisplay )
	{
		if( this.historicProcessInstance == null )
			return new VerticalLayout();
		
		VerticalLayout taskDefinitionDisplay = new VerticalLayout();
		taskDefinitionDisplay.setMargin( true );
		taskDefinitionDisplay.setSpacing( true );
		
		if( showTopDisplay )
		{
			HorizontalLayout topDisplay = new HorizontalLayout();
			taskDefinitionDisplay.addComponent( title );
			topDisplay.addComponent( request );
			topDisplay.addComponent( antennaTable );
			taskDefinitionDisplay.addComponent( topDisplay );
		}
		
		for( String taskDefinitionKey : this.taskDefinitionKeyList )
		{
			//get the tasks ordered by start time associated to the taskDefinitionKey.
			List<HistoricTaskInstance> taskInstanceList;
			HistoricTaskInstanceQuery taskQuery = getTaskQuery();
			
			if( getTaskQuery() == null )
				taskInstanceList = new ArrayList<HistoricTaskInstance>();
			else
				taskInstanceList = taskQuery
						.taskDefinitionKey( taskDefinitionKey )
						.orderByHistoricActivityInstanceStartTime().asc().list();
			
			//creates table structure
			Table taskDefinitionTable = new Table( taskDefinitionKey );
			taskDefinitionTable.setSelectable( true );
			taskDefinitionTable.addStyleName("components-inside");
			taskDefinitionTable.addContainerProperty("ID", Integer.class,  null);
			taskDefinitionTable.addContainerProperty("Task Name", String.class,  null);
			taskDefinitionTable.addContainerProperty("Assignee", String.class,  null);
			taskDefinitionTable.addContainerProperty("Start Time", Label.class,  null);
			taskDefinitionTable.addContainerProperty("End Time", Label.class,  null);
			taskDefinitionTable.addContainerProperty("Duration", Label.class,  null);
			taskDefinitionTable.addContainerProperty("Comment", TextArea.class,  "");
			taskDefinitionTable.setPageLength( taskInstanceList.size() );
			taskDefinitionTable.setWidth("100%");
			
			for( HistoricTaskInstance historicTaskInstance : taskInstanceList )
			{
				String taskDefinitionId = this.taskInstanceTaskDefinition.get( historicTaskInstance.getId() );
				String user = historicTaskInstance.getAssignee();
				String taskName = historicTaskInstance.getName();
				String user_comment = getUserComment( taskDefinitionId, user ,taskName );
				TextArea txtComment = new TextArea();
				txtComment.setValue( user_comment );
				txtComment.setReadOnly( true );
				
				Label lblTaskName = new Label();
				lblTaskName.setValue( taskName );
				
				Label lblStartTime = new Label();
				lblStartTime.setValue( historicTaskInstance.getStartTime() );
				
				Label lblEndTime = new Label();
				lblEndTime.setValue( historicTaskInstance.getEndTime() );
				
				TimeColumnGenerator tc = new TimeColumnGenerator();
				taskDefinitionTable.addItem(new Object[] {
						historicTaskInstance.getId(), lblTaskName.getValue(), historicTaskInstance.getAssignee(),
						lblStartTime, lblEndTime,
						tc.format( historicTaskInstance.getDurationInMillis() ), txtComment
						}, new Integer( historicTaskInstance.getId() ) );
			}
			
			taskDefinitionDisplay.addComponent( taskDefinitionTable );
			 
		}
		return taskDefinitionDisplay;
	}
	
	
	/**
	 * Gets the user comment associated to a taskDefinitionId, a user, and taskName
	 * @param taskDefinitionId = unique task identifier that allows us to pinpint the user comment
	 * @param user = gets the comment of this user.
	 * @param taskName = when the task id is executionAntennaUserTask, the antennaName is associated with the variable name, so taskName 
	 * is needed to obtain this antenna name.
	 * @return the UserComment associated with taskDefinitionId, user and taskName.
	 */
	private String getUserComment( String taskDefinitionId, String user ,String taskName )
	{
		String user_comment = "";
		HistoricVariableInstanceQuery variableQuery = getVariableQuery();
		
		if( !isValidVariable( taskDefinitionId ) || !isValidVariable( user ) || !isValidVariable( taskName ) || variableQuery == null )
			return "";
		
		if( taskDefinitionId.equals("executionFinishedUserTask") )
			user_comment = getVariableComment( variableQuery, "ackEFinishedPageComment_" + user );
		else if( taskDefinitionId.equals("softwareAvailableUserTask") )
			user_comment = getVariableComment( variableQuery, "softwareAvailableComment" );
		else if( taskDefinitionId.equals("checkDoneUserTask") )
			user_comment = getVariableComment( variableQuery, "checkComment_" + user );
		else if( taskDefinitionId.equals("executionAntennaUserTask") )
		{
			String antennaName = taskName.substring(20, taskName.indexOf(" ", 20) );
			user_comment = getVariableComment( variableQuery, "newAntenna_" + antennaName + "_PageComment" );
		}
		else if ( taskDefinitionId.equals("cancelDescriptionUserTask") )
			user_comment = getVariableComment( variableQuery, "cancelComment" );
		
		System.out.println("taskDefinitionId: "+taskDefinitionId+" user: "+user+" taskName: "+taskName+" userComment: "+user_comment);
		
		return user_comment;
	}
	
	/**
	 * Gets variableName from variableQuery if variable exists
	 * @param variableQuery = Activiti Variable Query
	 * @param variableName = variable name to be retrieved
	 * @return the variable value
	 */
	private String getVariableComment( HistoricVariableInstanceQuery variableQuery, String variableName )
	{
		String varValue = "";
		if( variableQuery.variableName( variableName ).singleResult() != null )
			varValue = (String) variableQuery.variableName( variableName ).singleResult().getValue();
		
		return varValue;
	}
	
	/**
	 * tests to see is string variable is valid
	 * @param variable the variable to be tested
	 * @return if the variable is valid
	 */
	private boolean isValidVariable( String variable )
	{
		if( variable == null || variable.equals("null") )
			return false;
		
		return true;
	}
		
	private HistoryService getHistoryService() 
	{
		return ProcessEngines.getDefaultProcessEngine().getHistoryService();
	}
	
	
	/**
	 * @return list of activityes of historicProcessInstance
	 */
	private HistoricActivityInstanceQuery getActivityQuery()
	{
		if( this.historicProcessInstance == null )
			return null;
		return getHistoryService().createHistoricActivityInstanceQuery()
				.processInstanceId( this.historicProcessInstance.getId() );
	}
	
	/**
	 * sets the actorList, taskDefinitionKeyList, taskInstanceTaskDefinition
	 * the actorList is a list of all actors that participated in one of the activitie stored in activityList.
	 * the taskDefinitionKeyList is a List of all tasks definition keys.
	 * taskInstanceTaskDefinition associates the activiti task with its corresponding DefinitionKey
	 */
	private void setActivityList()
	{
		this.actorList = new ArrayList<String>();
		this.taskDefinitionKeyList= new ArrayList<String>();
		this.taskInstanceTaskDefinition = new HashMap<String, String>(); 
		
		HistoricActivityInstanceQuery activityQuery = getActivityQuery();
		List<HistoricActivityInstance> activityList;
		if( activityQuery == null )
			activityList = new ArrayList<HistoricActivityInstance>();
		else
			activityList = activityQuery
					.orderByHistoricActivityInstanceEndTime().asc()
					.list();
		
		HistoricTaskInstanceQuery taskQuery = getTaskQuery();
		List<HistoricTaskInstance> taskList;
		if( taskQuery == null )
			taskList = new ArrayList<HistoricTaskInstance>();
		else
			taskList = taskQuery
					.orderByHistoricActivityInstanceStartTime().asc()
					.list();
		
		
		for( HistoricActivityInstance hist : activityList )
		{
			if( hist.getAssignee() != null && ! hist.getAssignee().equals("null") && ! this.actorList.contains( hist.getAssignee() ) )
			{
				this.actorList.add( hist.getAssignee() );
			}
		}
		
		for( HistoricTaskInstance task : taskList )
		{
			this.taskInstanceTaskDefinition.put( task.getId(), task.getTaskDefinitionKey() );
			if( task.getTaskDefinitionKey() != null && ! this.taskDefinitionKeyList.contains( task.getTaskDefinitionKey() ) )
			{
				this.taskDefinitionKeyList.add( task.getTaskDefinitionKey() );
			}
		}
	}
	
	/**
	 * @return list of variables of historicProcessInstance
	 */
	private HistoricVariableInstanceQuery getVariableQuery()
	{
		if( this.historicProcessInstance == null )
			return null;
		return  getHistoryService().createHistoricVariableInstanceQuery()
				.processInstanceId( this.historicProcessInstance.getId() );
	}
	
	/**
	 * @return list of tasks of historicProcessInstance
	 */
	private HistoricTaskInstanceQuery getTaskQuery()
	{
		if( this.historicProcessInstance == null )
			return null;
		return  getHistoryService().createHistoricTaskInstanceQuery()
				.processInstanceId( this.historicProcessInstance.getId() );
	}
}
