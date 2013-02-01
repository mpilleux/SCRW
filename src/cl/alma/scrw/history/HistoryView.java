package cl.alma.scrw.history;

import java.util.List;

import org.activiti.engine.history.HistoricProcessInstance;

import com.github.peholmst.mvp4vaadin.navigation.ControllableView;

/**
 * This interface shows the necessary methods that HistoryViewImpl will implement, and HistoryPresenter will call.  
 * @author Mauricio Pilleux
 *
 */
public interface HistoryView extends ControllableView {

	String VIEW_ID = "history";
	
	/**
	 * Sets a list of finished process instances to the view.
	 * @param list = finished process instance list to be set.
	 */
	void setProcessInstances(List<HistoricProcessInstance> list);

}
