package cl.alma.scrw.history;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;

import cl.alma.scrw.reports.ReportView;

import com.github.peholmst.mvp4vaadin.navigation.ControllablePresenter;
import com.github.peholmst.mvp4vaadin.navigation.ControllableView;
import com.github.peholmst.mvp4vaadin.navigation.Direction;
import com.github.peholmst.mvp4vaadin.navigation.ViewController;

/**
 * This class is the presenter of the HistoryView.
 * 
 * This class allows to initialize the finished process instance list
 * and allows to navigate to the reportView and historyDataView.
 * @author Mauricio Pilleux
 *
 */
public class HistoryPresenter extends ControllablePresenter<HistoryView> 
{

	private static final long serialVersionUID = 3328656147757828734L;

	public HistoryPresenter(HistoryView view) 
	{
		super(view);
	}

	@Override
	public void init() 
	{
		getView().setProcessInstances( getAllFinishedProcessInstances() );
	}
	
	@Override
	/**
	 * updates in the view the list of process instances every time the user opens this view.
	 */
	protected void viewShown(ViewController viewController,
			Map<String, Object> userData, ControllableView oldView,
			Direction direction) 
	{
		updateProcessList();
	}
	
	/**
	 * updates the list of all finished process instances in the view.
	 */
	private void updateProcessList()
	{
		getView().setProcessInstances( getAllFinishedProcessInstances() );
	}	
	
	/**
	 * @return list of all finished process instances
	 */
	private List<HistoricProcessInstance> getAllFinishedProcessInstances() 
	{
		return getHistoryService().createHistoricProcessInstanceQuery().finished().list();
	}

	/**
	 * sets the current view as a new HistoricDataView by passing the selected historicProcessInstance 
	 * @param historicProcessInstance = processInstance passed to the historic data view.
	 */
	public void setHistBrowser( HistoricProcessInstance historicProcessInstance ) {
		String histProcId = null;
		if( historicProcessInstance != null )
			histProcId = historicProcessInstance.getId();
		if( histProcId != null )
		{
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put( HistoryDataView.KEY_HISTORY_PROCCESS_INSTANCE_ID, histProcId );
			getViewController().goToView( HistoryDataView.VIEW_ID, params );
		}
	}
	/**
	 * sets the current view as a new ReportView by passing the selected historicProcessInstance 
	 * @param historicProcessInstance = processInstance passed to the report.
	 */
	public void setReportBrowser( HistoricProcessInstance historicProcessInstance ) {
		String histProcId = null;
		if( historicProcessInstance != null )
			histProcId = historicProcessInstance.getId();
		if( histProcId != null )
		{
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put( ReportView.KEY_HISTORY_PROCCESS_INSTANCE_ID, histProcId );
			getViewController().goToView( ReportView.VIEW_ID, params );
		}
	}
	
	private HistoryService getHistoryService() 
	{
		return ProcessEngines.getDefaultProcessEngine().getHistoryService();
	}

}
