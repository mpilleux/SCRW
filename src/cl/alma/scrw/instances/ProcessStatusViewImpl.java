package cl.alma.scrw.instances;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.task.Task;
import org.activiti.explorer.Constants;
import org.activiti.explorer.ui.util.InputStreamStreamSource;
import org.activiti.engine.impl.RepositoryServiceImpl;

import cl.alma.scrw.format.TimeColumnGenerator;
import cl.alma.scrw.ui.util.AbstractDemoView;

import com.vaadin.Application;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.StreamResource.StreamSource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Reindeer;

/**
 * This class represents the view implementation of a ProcessStatus View.
 * 
 * This class shows tables which contains all possible data stored in the database of 
 * an active processInstance and its current state diagram.
 * 
 * This class implements ProcessStatusView, and the corresponding presenter (controller) is ProcessStatusPresenter.
 * 
 * @author Mauricio Pilleux
 *
 */
public class ProcessStatusViewImpl extends
		AbstractDemoView<ProcessStatusView, ProcessStatusPresenter> implements
		ProcessStatusView 
		{

	private static final long serialVersionUID = 8630129746309155685L;

	private Table histTaskTable;
	
	private Table taskTable;
	
	private Table variableTable;
	
	private Table detailTable;
	
	private Table activityTable;
	
	private TabSheet tabs;
	 
	private Application application;
	
	private VerticalLayout processImageContainer;
	
	HistoricProcessInstance historicProcessInstance;

	public ProcessStatusViewImpl( Application app ) 
	{
		this.application = app;
		init();
	}

	@Override
	public String getDisplayName() 
	{
		return "Process Status Browser";
	}

	@Override
	public String getDescription() 
	{
		return "Browse process status";
	}

	@Override
	/**
	 * initial view.
	 */
	protected void initView() {
		super.initView();

		Button refresh = new Button("Refresh");
		refresh.addStyleName(Reindeer.BUTTON_SMALL);
		refresh.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				getPresenter().init();
			}
		});
		
		getViewLayout().addComponent( refresh );
		
		tabs = new TabSheet();
		tabs.setSizeFull();
		tabs.addStyleName(Reindeer.TABSHEET_MINIMAL);

		getViewLayout().addComponent( tabs );
		getViewLayout().setExpandRatio(tabs, 1.0F);

		
		this.histTaskTable = new Table();
		this.variableTable = new Table();
		this.detailTable = new Table();
		this.activityTable = new Table();
		this.taskTable = new Table();
		
		processImageContainer = new VerticalLayout();
		updateControls();
	}
		
	/**
	 * initialices every table and tabs
	 */
	private void initTables()
	{
		variableTable = new Table();
		variableTable.setSizeFull();
		variableTable.setImmediate( true );
		
		histTaskTable = new Table();
		histTaskTable.setSizeFull();
		histTaskTable.setImmediate( true );
		
		detailTable = new Table();
		detailTable.setSizeFull();
		detailTable.setImmediate( true );

		activityTable = new Table();
		activityTable.setSizeFull();
		activityTable.setImmediate( true );
		
		taskTable = new Table();
		taskTable.setSizeFull();
		taskTable.setImmediate( true );

		tabs.addTab( histTaskTable, "Tasks", null );
		tabs.addTab( variableTable, "Variables", null );
		tabs.addTab( activityTable, "Activites", null );
		tabs.addTab( detailTable, "Details", null );
		
		processImageContainer = new VerticalLayout();
		this.initImage();
		tabs.addTab( processImageContainer, "Diagram", null );
		
		tabs.addTab( taskTable, "Open Tasks", null );
		
		populateTables();
	}
	
	/**
	 * Creates the current processDefinition diagram and add it to the image container.
	 * This method uses activiti's ProcessDiagramGenerator.generateDiagram.
	 * this methods can be found at the activiti explorer source code.
	 */
	protected void initImage()
	{
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) getRepositoryService())
				.getDeployedProcessDefinition( this.historicProcessInstance.getProcessDefinitionId() );
			
		StreamResource imageResource = null;
	    if(processDefinitionEntity.getDiagramResourceName() != null) 
	    {
		    	
		    final InputStream definitionImageStream = ProcessDiagramGenerator.generateDiagram(processDefinitionEntity,
					"png", 
					getRuntimeService().getActiveActivityIds( this.historicProcessInstance.getId() ) );
		    
		    StreamSource streamSource = new InputStreamStreamSource( definitionImageStream );
		      
		     
		     String imageExtension = extractImageExtension(processDefinitionEntity.getDiagramResourceName());
		     String fileName = processDefinitionEntity.getId() + UUID.randomUUID() + "." + imageExtension;
		      
		     imageResource = new StreamResource( streamSource, fileName, this.application );
	     
	    }
	    
		Embedded embedded = new Embedded(null, imageResource);
	    embedded.setType(Embedded.TYPE_IMAGE);
	    embedded.setSizeUndefined();
		
		
		Panel imagePanel = new Panel();
	    imagePanel.addStyleName(Reindeer.PANEL_LIGHT);
	    imagePanel.setWidth(100, Sizeable.UNITS_PERCENTAGE);
	    imagePanel.setHeight(400, Sizeable.UNITS_PIXELS);
	    HorizontalLayout panelLayout = new HorizontalLayout();
	    panelLayout.setSizeUndefined();
	    imagePanel.setContent(panelLayout);
	    imagePanel.addComponent(embedded);
	    
	    processImageContainer.addComponent(imagePanel);
	}
	
	/**
	 * gets the image extension of diagramResourseName
	 * @param diagramResourceName = diagramResourceName whose image extension will be obtained.
	 * @return the diagramResourceName image extension
	 */
	protected String extractImageExtension(String diagramResourceName) 
	{
	    String[] parts = diagramResourceName.split(".");
	    if(parts.length > 1) {
	      return parts[parts.length - 1];
	    }
	    return Constants.DEFAULT_DIAGRAM_IMAGE_EXTENSION;
	}

	/**
	 * populates the variableTable, the histTaskTable, the detailTable, and the activityTable, and taskTable.
	 */
	private void populateTables() 
	{
		populateVariablesTable();
		populateHistTasksTable();
		populateDetailTable();
		populateActivityTable();
		populateOpenTasksTable();
	}
	
	/**
	 * populates the taskTable
	 * 
	 * this table shows the task in which the process is currently available.
	 */
	private void populateOpenTasksTable()
	{

		List<Task> taskList = getTaskService().createTaskQuery().processInstanceId( this.historicProcessInstance.getId() ).list();
		
		BeanItemContainer<Task> dataSource = new BeanItemContainer<Task>(
				Task.class, taskList);
		
		taskTable.setContainerDataSource( dataSource );
		taskTable.setVisibleColumns(new String[] { "id", "name", "description", "assignee",
				"delegationState", "processDefinitionId", "processInstanceId" });
	}
	
	/**
	 * populates the variableTable
	 * 
	 * this includes all variables created in the process.
	 */
	private void populateVariablesTable()
	{
		List<HistoricVariableInstance> allVariables = getHistoryService().createHistoricVariableInstanceQuery()
		  .processInstanceId( this.historicProcessInstance.getId() )
		  .orderByVariableName().desc()
		  .list();
		
		BeanItemContainer<HistoricVariableInstance> dataSource = new BeanItemContainer<HistoricVariableInstance>(
				HistoricVariableInstance.class, allVariables);
		
		variableTable.setContainerDataSource( dataSource );
		variableTable.setVisibleColumns(new String[] { "id", "variableTypeName", "variableName",
				"value" });
	}
	
	/**
	 * populates the taskTable.
	 * tasks include only user tasks.
	 */
	private void populateHistTasksTable()
	{
		List<HistoricTaskInstance> allTasks = getHistoryService().createHistoricTaskInstanceQuery()
				.processInstanceId( this.historicProcessInstance.getId() )
				.orderByHistoricTaskInstanceDuration().desc()
				.list();
		
		BeanItemContainer<HistoricTaskInstance> dataSource = new BeanItemContainer<HistoricTaskInstance>(
				HistoricTaskInstance.class, allTasks);
		
		histTaskTable.setContainerDataSource( dataSource );
		histTaskTable.setVisibleColumns(new String[] { "id", "taskDefinitionKey", "name", "startTime",
				"endTime", "durationInMillis", "assignee" });
		histTaskTable.addGeneratedColumn("durationInMillis", new TimeColumnGenerator() );
	}
	
	/**
	 * populates the detalTable.
	 */
	private void populateDetailTable()
	{
		List<HistoricDetail> allDetails = getHistoryService().createHistoricDetailQuery()
				.processInstanceId( this.historicProcessInstance.getId() )
				.orderByTime().desc()
				.list();
		
		BeanItemContainer<HistoricDetail> dataSource = new BeanItemContainer<HistoricDetail>(
				HistoricDetail.class, allDetails);
		
		detailTable.setContainerDataSource( dataSource );
		detailTable.setVisibleColumns(new String[] { "id", "time","taskId", 
				"processInstanceId", "executionId", "activityInstanceId" });
	}
	
	/**
	 * populates the data in the activityTable.
	 * an activity includes user tasks and service tasks
	 */
	private void populateActivityTable()
	{
		List<HistoricActivityInstance> allActivities = getHistoryService().createHistoricActivityInstanceQuery()
				.processInstanceId( this.historicProcessInstance.getId() )
				.orderByHistoricActivityInstanceEndTime().desc()
				.list();
		
		BeanItemContainer<HistoricActivityInstance> dataSource = new BeanItemContainer<HistoricActivityInstance>(
				HistoricActivityInstance.class, allActivities);
		
		activityTable.setContainerDataSource( dataSource );
		activityTable.setVisibleColumns(new String[] { "id", "activityId", "activityName", "taskId",
				"assignee", "startTime","endTime", "durationInMillis", 
				"executionId", "processDefinitionId", "processInstanceId" });
		activityTable.addGeneratedColumn("durationInMillis", new TimeColumnGenerator() );
	}
	
	private HistoryService getHistoryService() 
	{
		return ProcessEngines.getDefaultProcessEngine().getHistoryService();
	}

	@Override
	/**
	 * clears all data from the window.
	 */
	public void hideData() 
	{
		this.historicProcessInstance = null;
		updateControls();
	}

	@Override
	/**
	 * sets the historicProcessInstance whose data will be shown. 
	 * @param historicProcessInstance = historic process instance whose data will be shown.
	 */
	public void setHistoricProcessInstance( HistoricProcessInstance historicProcessInstance ) 
	{
		this.historicProcessInstance = historicProcessInstance;
		updateControls();
	}
	
	@Override
	protected ProcessStatusPresenter createPresenter() 
	{
		return new ProcessStatusPresenter(this);
	}
	
	/**
	 * updates the data to be shown in this view.
	 */
	private void updateControls()
	{
		this.updateHeaderLabel();
		this.tabs.removeAllComponents();
		if( this.historicProcessInstance != null )
		{
			initTables();
		}
	}
	
	private RepositoryService getRepositoryService() 
	{
		return ProcessEngines.getDefaultProcessEngine().getRepositoryService();
	}
	
	private TaskService getTaskService() 
	{
		return ProcessEngines.getDefaultProcessEngine().getTaskService();
	}
	
	private RuntimeService getRuntimeService() 
	{
		return ProcessEngines.getDefaultProcessEngine().getRuntimeService();
	}
	
}
