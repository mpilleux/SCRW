package cl.alma.scrw.cancel;

import java.util.List;

import org.activiti.engine.history.HistoricProcessInstance;

import com.github.peholmst.mvp4vaadin.navigation.ControllableView;
/**
 * This interface shows the necessary methods that CancelProcessViewImpl will implement, and CancelProcessPresenter will call.  
 * @author Mauricio Pilleux
 *
 */
public interface CancelProcessView extends ControllableView {

	String VIEW_ID = "cancelProcess";
	
	/**
	 * Sets a list of active process instances that can be cancelled.
	 * @param list = active process instance list to be set.
	 */
	void setProcessInstances(List<HistoricProcessInstance> list);
	
	/**
	 * Message to be show in a window when a process is cancelled successfully.
	 * @param historicProcessInstance = cancelled process.
	 */
	public void showProcessCanceled( HistoricProcessInstance historicProcessInstance );
}
