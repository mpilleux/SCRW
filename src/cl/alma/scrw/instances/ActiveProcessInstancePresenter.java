package cl.alma.scrw.instances;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;

import com.github.peholmst.mvp4vaadin.navigation.ControllablePresenter;
import com.github.peholmst.mvp4vaadin.navigation.ControllableView;
import com.github.peholmst.mvp4vaadin.navigation.Direction;
import com.github.peholmst.mvp4vaadin.navigation.ViewController;
/**
 * This class is the presenter of the ActiveProcessInstanceView.
 * 
 * This class allows to initialize the active process instance list
 * and allows to navigate to the ProcessStatusView.
 * @author Mauricio Pilleux
 *
 */
public class ActiveProcessInstancePresenter extends ControllablePresenter<ActiveProcessInstanceView> {


	private static final long serialVersionUID = 1494482378867394394L;

	public ActiveProcessInstancePresenter(ActiveProcessInstanceView view) 
	{
		super(view);
	}

	@Override
	/**
	 * initialices the ActiveProcessInstanceView
	 */
	public void init() 
	{
		getView().setProcessInstances(getAllUnFinishedProcessInstances());
	}
	
	@Override
	/**
	 * everytime a user sees the ActiveProcessInstanceView, updates the list.
	 */
	protected void viewShown(ViewController viewController,
			Map<String, Object> userData, ControllableView oldView,
			Direction direction) 
	{
		updateProcessList();
	}
	
	private void updateProcessList()
	{
		getView().setProcessInstances(getAllUnFinishedProcessInstances());
	}
	
	/**
	 * @return a list of all active process instances
	 */
	private List<HistoricProcessInstance> getAllUnFinishedProcessInstances() 
	{
		List<ProcessInstance> activeProcessInstance = getRuntimeService()
				.createProcessInstanceQuery().active().list();
		
		Set<String> activeProcessInstanceIdList = new HashSet<String>();
		
		for( ProcessInstance proc : activeProcessInstance )
			activeProcessInstanceIdList.add( proc.getId() );
		
		if( activeProcessInstanceIdList.size() < 1 )//no finished process instance
			return new ArrayList<HistoricProcessInstance>();
		
		return getHistoryService().createHistoricProcessInstanceQuery().unfinished().processInstanceIds( activeProcessInstanceIdList ).list();
	}
	
	private RuntimeService getRuntimeService() 
	{
		return ProcessEngines.getDefaultProcessEngine().getRuntimeService();
	}
	
	private HistoryService getHistoryService() 
	{
		return ProcessEngines.getDefaultProcessEngine().getHistoryService();
	}
	
	/**
	 * sets the current view as a new ProcessStatusView by passing the selected historicProcessInstance 
	 * @param historicProcessInstance = processInstance passed to the report.
	 */
	public void setHistBrowser( HistoricProcessInstance historicProcessInstance ) 
	{
		String histProcId = null;
		if( historicProcessInstance != null )
			histProcId = historicProcessInstance.getId();
		if( histProcId != null )
		{
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put( ProcessStatusView.KEY_HISTORY_PROCCESS_INSTANCE_ID, histProcId );
			getViewController().goToView( ProcessStatusView.VIEW_ID, params );
		}
	}

}
