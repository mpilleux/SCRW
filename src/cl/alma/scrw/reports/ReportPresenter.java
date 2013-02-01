package cl.alma.scrw.reports;

import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;

import com.github.peholmst.mvp4vaadin.navigation.ControllablePresenter;
import com.github.peholmst.mvp4vaadin.navigation.ControllableView;
import com.github.peholmst.mvp4vaadin.navigation.Direction;
import com.github.peholmst.mvp4vaadin.navigation.ViewController;

/**
 * This class is the presenter of the ReportView.
 * 
 * This class allows to initialize the active process instance
 * and allows update the ReportView.
 * @author Mauricio Pilleux
 *
 */
public class ReportPresenter extends ControllablePresenter<ReportView> {


	private static final long serialVersionUID = 4817979597967150335L;

	public ReportPresenter(ReportView view) {
		super(view);
	}
	
	@Override
	/**
	 * everytime the user sees the HistoryDataView, updates the data.
	 */
	protected void viewShown(ViewController viewController,
			Map<String, Object> userData, ControllableView oldView,
			Direction direction) {
		
		if (userData != null) {
			String histProcId = (String) userData.get( ReportView.KEY_HISTORY_PROCCESS_INSTANCE_ID );
			showHistory( histProcId );
		} else {
			getView().hideData();
		}
	}
	
	/**
	 * sets the historicProcessInstance in the view whose id correspond to histProcId
	 * @param histProcId = id of processInstance to be set.
	 */
	private void showHistory( String histProcId )
	{
		HistoricProcessInstance historicProcessInstance = getHistoryService()
				.createHistoricProcessInstanceQuery()
				.processInstanceId( histProcId ).singleResult(); 
		
		getView().setHistoricProcessInstance( historicProcessInstance );
		
	}
	
	private HistoryService getHistoryService() 
	{
		return ProcessEngines.getDefaultProcessEngine().getHistoryService();
	}

}
