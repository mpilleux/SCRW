package cl.alma.scrw.diagram;

import org.activiti.engine.repository.ProcessDefinition;

import com.github.peholmst.mvp4vaadin.navigation.ControllableView;

/**
 * This interface shows the necessary methods that DiagramViewImpl will implement, and DiagramPresenter will call.  
 * @author Mauricio Pilleux
 *
 */
public interface DiagramView extends ControllableView {

	String VIEW_ID = "processDiagram";
	
	String KEY_PROCESS_DEFINITION_ID = "dProcessDefinition";
	
	/**
	 * clears all data from the window.
	 */
	void hideData();
	
	/**
	 * sets the processDefinition whose data will be shown.
	 * @param processDefinition = process definition whose data will be shown
	 */
	void setProcessDefinition( ProcessDefinition processDefinition );

}
