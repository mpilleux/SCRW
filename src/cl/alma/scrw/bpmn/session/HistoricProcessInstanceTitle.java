package cl.alma.scrw.bpmn.session;

import java.util.Date;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.HistoricVariableInstanceQuery;

public class HistoricProcessInstanceTitle implements HistoricProcessInstance 
{

	private HistoricProcessInstance historicProcessInstance;
	
	private String hTitle = "";
		
	private String varName;
	
	
	public HistoricProcessInstanceTitle( HistoricProcessInstance historicProcessInstance, String varName )
	{
		this.historicProcessInstance = historicProcessInstance;
		this.varName = varName;
		this.obtainTitle();
	}
	
	private void obtainTitle()
	{
		HistoricVariableInstance historicVariableInstance = getHistoricVariableInstanceQuery()
				 .processInstanceId( historicProcessInstance.getId() )
				 .variableName( this.varName )
				 .singleResult();
		 
		 String procTitle = "";
		 if( historicVariableInstance != null )
			 procTitle = (String) historicVariableInstance.getValue();
		 
		 this.setTitle( procTitle );
	}
	
	public void setTitle( String title )
	{
		this.hTitle = title;
	}
	
	public String getTitle()
	{
		return this.hTitle==null?"":this.hTitle;
	}
	
	/**
	 * @return Historic Variable Instance Query query
	 */
	private HistoricVariableInstanceQuery getHistoricVariableInstanceQuery()
	{
		return  getHistoryService().createHistoricVariableInstanceQuery();
	}
		
	private HistoryService getHistoryService() 
	{
		return ProcessEngines.getDefaultProcessEngine().getHistoryService();
	}
	
	@Override
	public String getBusinessKey() {
		
		return this.historicProcessInstance.getBusinessKey();
	}

	@Override
	public String getDeleteReason() {
		
		return this.historicProcessInstance.getDeleteReason();
	}

	@Override
	public Long getDurationInMillis() {
		
		return this.historicProcessInstance.getDurationInMillis();
	}

	@Override
	@Deprecated
	public String getEndActivityId() {
		
		return this.historicProcessInstance.getEndActivityId();
	}

	@Override
	public Date getEndTime() {
		
		return this.historicProcessInstance.getEndTime();
	}

	@Override
	public String getId() {
		
		return this.historicProcessInstance.getId();
	}

	@Override
	public String getProcessDefinitionId() {
		
		return this.historicProcessInstance.getProcessDefinitionId();
	}

	@Override
	public String getStartActivityId() {
		
		return this.historicProcessInstance.getStartActivityId();
	}

	@Override
	public Date getStartTime() {
		
		return this.historicProcessInstance.getStartTime();
	}

	@Override
	public String getStartUserId() {
		
		return this.historicProcessInstance.getStartUserId();
	}

	@Override
	public String getSuperProcessInstanceId() {
		
		return this.historicProcessInstance.getSuperProcessInstanceId();
	}

}
