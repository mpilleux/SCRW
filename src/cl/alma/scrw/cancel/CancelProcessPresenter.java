package cl.alma.scrw.cancel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;

import com.github.peholmst.mvp4vaadin.navigation.ControllablePresenter;
import com.github.peholmst.mvp4vaadin.navigation.ControllableView;
import com.github.peholmst.mvp4vaadin.navigation.Direction;
import com.github.peholmst.mvp4vaadin.navigation.ViewController;
/**
 * This class is the presenter of the CancelProcessView.
 * 
 * This class allows to initialize the active process instance list
 * and allows to cancel a given process Instance.
 * @author Mauricio Pilleux
 *
 */
public class CancelProcessPresenter extends ControllablePresenter<CancelProcessView> {


	private static final long serialVersionUID = 1494482378867394394L;

	public CancelProcessPresenter(CancelProcessView view) {
		super(view);
	}

	@Override
	/**
	 * Initializes the CancelProcessView by passing a list of active process instances
	 */
	public void init() {
		getView().setProcessInstances(getAllUnFinishedProcessInstances());
	}
	
	@Override
	/**
	 * updates the active process instance list every time a user opens the view.
	 */
	protected void viewShown(ViewController viewController,
			Map<String, Object> userData, ControllableView oldView,
			Direction direction) {
		updateProcessList();
	}
	
	/**
	 * updates the active process instance list.
	 */
	private void updateProcessList()
	{
		getView().setProcessInstances(getAllUnFinishedProcessInstances());
	}	
	
	/**
	 * querys for all active process instances
	 * @return a list of all active process instances
	 */
	private List<HistoricProcessInstance> getAllUnFinishedProcessInstances() {
		List<ProcessInstance> CancelProcess = getRuntimeService()
				.createProcessInstanceQuery().active().list();
		
		Set<String> CancelProcessIdList = new HashSet<String>();
		
		for( ProcessInstance proc : CancelProcess )
			CancelProcessIdList.add( proc.getId() );
		
		if( CancelProcessIdList.size() < 1 )
			return new ArrayList<HistoricProcessInstance>();
		
		return getHistoryService().createHistoricProcessInstanceQuery().unfinished().processInstanceIds( CancelProcessIdList ).list();
	}
	
	private RuntimeService getRuntimeService() {
		return ProcessEngines.getDefaultProcessEngine().getRuntimeService();
	}
	
	private HistoryService getHistoryService() {
		return ProcessEngines.getDefaultProcessEngine().getHistoryService();
	}
	
	/**
	 * Cancels every execution of historicProcessInstance
	 * @param historicProcessInstance = the process instance to be cancelled
	 */
	public void cancelProcess( HistoricProcessInstance historicProcessInstance )
	{
		List<Execution> executionList = getRuntimeService()
				.createExecutionQuery()
				.signalEventSubscriptionName( "Cancel Process" )
				.processDefinitionId( historicProcessInstance.getProcessDefinitionId() )
				.list();
		
		for( Execution execution : executionList )
			getRuntimeService().signalEventReceived( "Cancel Process", execution.getId() );
	}

}
