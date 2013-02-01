package cl.alma.scrw.history;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;

import cl.alma.scrw.format.TimeColumnGenerator;
import cl.alma.scrw.ui.util.AbstractDemoView;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.Reindeer;
/**
 * This class represents the view implementation of a HistoryData View.
 * 
 * This class shows tables which contains all possible data stored in the database of a finished processInstance.
 * 
 * This class implements HistoryDataView, and the corresponding presenter (controller) is HistoryDataPresenter.
 * 
 * @author Mauricio Pilleux
 *
 */
public class HistoryDataViewImpl extends
		AbstractDemoView<HistoryDataView, HistoryDataPresenter> implements
		HistoryDataView 
		{

	private static final long serialVersionUID = 8630161578302584685L;

	private Table taskTable;
	
	private Table variableTable;
	
	private Table detailTable;
	
	private Table activityTable;
	
	private TabSheet tabs;
	
	HistoricProcessInstance historicProcessInstance;

	public HistoryDataViewImpl() {
		init();
	}

	@Override
	public String getDisplayName() {
		return "History Data Browser";
	}

	@Override
	public String getDescription() {
		return "Browse processes data";
	}

	@Override
	/**
	 * initial view.
	 */
	protected void initView() {
		super.initView();

		tabs = new TabSheet();
		tabs.setSizeFull();
		tabs.addStyleName(Reindeer.TABSHEET_MINIMAL);

		getViewLayout().addComponent( tabs );
		getViewLayout().setExpandRatio(tabs, 1.0F);
		
		this.taskTable = new Table();
		this.variableTable = new Table();
		this.detailTable = new Table();
		this.activityTable = new Table();
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
		
		taskTable = new Table();
		taskTable.setSizeFull();
		taskTable.setImmediate( true );
		
		detailTable = new Table();
		detailTable.setSizeFull();
		detailTable.setImmediate( true );

		activityTable = new Table();
		activityTable.setSizeFull();
		activityTable.setImmediate( true );

		tabs.addTab( taskTable, "Tasks", null );
		tabs.addTab( variableTable, "Variables", null );
		tabs.addTab( activityTable, "Activites", null );
		tabs.addTab( detailTable, "Details", null );
		populateTables();
	}

	/**
	 * populates the variableTable, the taskTable, the detailTable, and the activityTable.
	 */
	private void populateTables() 
	{
		populateVariablesTable();
		populateTasksTable();
		populateDetailTable();
		populateActivityTable();
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
	private void populateTasksTable()
	{
		List<HistoricTaskInstance> allTasks = getHistoryService().createHistoricTaskInstanceQuery()
				.processInstanceId( this.historicProcessInstance.getId() )
				.finished()
				.orderByHistoricTaskInstanceDuration().desc()
				.list();
		
		BeanItemContainer<HistoricTaskInstance> dataSource = new BeanItemContainer<HistoricTaskInstance>(
				HistoricTaskInstance.class, allTasks);
		
		taskTable.setContainerDataSource( dataSource );
		taskTable.setVisibleColumns(new String[] { "id", "taskDefinitionKey", "name", "startTime",
				"endTime", "durationInMillis", "assignee" });
		taskTable.addGeneratedColumn("durationInMillis", new TimeColumnGenerator() );
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
				.finished()
				.orderByHistoricActivityInstanceEndTime().desc()
				.list();
		
		BeanItemContainer<HistoricActivityInstance> dataSource = new BeanItemContainer<HistoricActivityInstance>(
				HistoricActivityInstance.class, allActivities);
		
		activityTable.setContainerDataSource( dataSource );
		activityTable.setVisibleColumns(new String[] { "id", "activityId", "activityName",
				"assignee", "startTime","endTime", "durationInMillis", 
				"taskId", "executionId", "processDefinitionId", "processInstanceId" });
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
	protected HistoryDataPresenter createPresenter() 
	{
		return new HistoryDataPresenter(this);
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

	
}
