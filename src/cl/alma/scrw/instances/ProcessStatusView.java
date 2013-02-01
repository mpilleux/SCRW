package cl.alma.scrw.instances;

import org.activiti.engine.history.HistoricProcessInstance;

import com.github.peholmst.mvp4vaadin.navigation.ControllableView;

/**
 * This interface shows the necessary methods thatProcessStatusViewImpl will implement, and ProcessStatusPresenter will call.  
 * @author Mauricio Pilleux
 *
 */
public interface ProcessStatusView extends ControllableView 
{

	String VIEW_ID = "activeInstancesStatus";
	
	String KEY_HISTORY_PROCCESS_INSTANCE_ID = "historyProcessInstanceId";

	/**
	 * clears all data from the window.
	 */
	void hideData();
	
	/**
	 * sets the historicProcessInstance whose data will be shown. 
	 * @param historicProcessInstance = historic process instance whose data will be shown.
	 */
	void setHistoricProcessInstance( HistoricProcessInstance historicProcessInstance );

}
