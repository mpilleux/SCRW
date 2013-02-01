package cl.alma.scrw.instances;

import java.util.List;

import org.activiti.engine.history.HistoricProcessInstance;

import com.github.peholmst.mvp4vaadin.navigation.ControllableView;
/**
 * This interface shows the necessary methods that ActiveProcessInstanceViewImpl will implement, and ActiveProcessInstancePresenter will call.  
 * @author Mauricio Pilleux
 *
 */
public interface ActiveProcessInstanceView extends ControllableView {

	String VIEW_ID = "activeInstances";
	
	/**
	 * Sets a list of active process instances to the view.
	 * @param list = active process instance list to be set.
	 */
	void setProcessInstances(List<HistoricProcessInstance> list);

}
