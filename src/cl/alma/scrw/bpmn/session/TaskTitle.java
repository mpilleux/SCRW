package cl.alma.scrw.bpmn.session;

import java.util.Date;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.HistoricVariableInstanceQuery;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;


public class TaskTitle implements Task
{
	private Task task;
	private String taskTitle = "";
	private String varName;
	
	public TaskTitle( Task task2, String varName )
	{
		this.task = task2;
		this.varName = varName;
		this.obtainTitle();
	}
	
	private void obtainTitle()
	{
		String procInstanceId = getTaskQuery()
				 .taskId( task.getId() )
				 .singleResult()
				 .getProcessInstanceId();
		
		 HistoricVariableInstance historicVariableInstance = getHistoricVariableInstanceQuery()
				 .processInstanceId( procInstanceId )
				 .variableName( this.varName )
				 .singleResult();
		 
		 String procTitle = "";
		 if( historicVariableInstance != null )
			 procTitle = (String) historicVariableInstance.getValue();
		 
		 this.setTitle( procTitle );
	}
	
	public void setTitle( String title )
	{
		this.taskTitle = title;
	}
	
	public String getTitle()
	{
		return this.taskTitle==null?"":this.taskTitle;
	}


	/**
	 * @return task query
	 */
	private TaskQuery getTaskQuery()
	{
		return  getTaskService().createTaskQuery();
	}
	
	/**
	 * @return Historic Variable Instance Query query
	 */
	private HistoricVariableInstanceQuery getHistoricVariableInstanceQuery()
	{
		return  getHistoryService().createHistoricVariableInstanceQuery();
	}
	
	
	private TaskService getTaskService() 
	{
		return ProcessEngines.getDefaultProcessEngine().getTaskService();
	}
	
	private HistoryService getHistoryService() 
	{
		return ProcessEngines.getDefaultProcessEngine().getHistoryService();
	}

	@Override
	public void delegate(String arg0) {
		this.task.delegate(arg0);
		
	}



	@Override
	public String getAssignee() {
		
		return this.task.getAssignee();
	}



	@Override
	public Date getCreateTime() {
		
		return this.task.getCreateTime();
	}



	@Override
	public DelegationState getDelegationState() {
		
		return this.task.getDelegationState();
	}



	@Override
	public String getDescription() {
		
		return this.task.getDescription();
	}



	@Override
	public Date getDueDate() {
		
		return this.task.getDueDate();
	}



	@Override
	public String getExecutionId() {
		
		return this.task.getExecutionId();
	}



	@Override
	public String getId() {
		
		return this.task.getId();
	}



	@Override
	public String getName() {
		
		return this.task.getName();
	}



	@Override
	public String getOwner() {
		
		return this.task.getOwner();
	}



	@Override
	public String getParentTaskId() {
		
		return this.task.getParentTaskId();
	}



	@Override
	public int getPriority() {
		
		return this.task.getPriority();
	}



	@Override
	public String getProcessDefinitionId() {
		
		return this.task.getProcessDefinitionId();
	}



	@Override
	public String getProcessInstanceId() {
		
		return this.task.getProcessInstanceId();
	}



	@Override
	public String getTaskDefinitionKey() {
		
		return this.task.getTaskDefinitionKey();
	}



	@Override
	public boolean isSuspended() {
		
		return this.task.isSuspended();
	}



	@Override
	public void setAssignee(String arg0) {
		this.task.setAssignee(arg0);
		
	}



	@Override
	public void setDelegationState(DelegationState arg0) {
		this.task.setDelegationState(arg0);
		
	}



	@Override
	public void setDescription(String arg0) {
		
		this.task.setDescription(arg0);
	}



	@Override
	public void setDueDate(Date arg0) {
		this.task.setDueDate(arg0);
		
	}



	@Override
	public void setName(String arg0) {
		
		this.task.setName(arg0);
	}



	@Override
	public void setOwner(String arg0) {
		
		this.task.setOwner(arg0);
	}



	@Override
	public void setParentTaskId(String arg0) {
		
		this.task.setParentTaskId(arg0);
	}



	@Override
	public void setPriority(int arg0) {
		
		this.task.setPriority(arg0);
	}
}
