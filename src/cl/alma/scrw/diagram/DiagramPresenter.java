package cl.alma.scrw.diagram;

import java.util.Map;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;

import com.github.peholmst.mvp4vaadin.navigation.ControllablePresenter;
import com.github.peholmst.mvp4vaadin.navigation.ControllableView;
import com.github.peholmst.mvp4vaadin.navigation.Direction;
import com.github.peholmst.mvp4vaadin.navigation.ViewController;

/**
 * This class is the presenter of DiagramView.
 * 
 * This class allows to pass the processDefinition to the view.
 * @author Mauricio Pilleux
 *
 */
public class DiagramPresenter extends ControllablePresenter<DiagramView> {

	
	private static final long serialVersionUID = 7285283126389356966L;

	public DiagramPresenter(DiagramView view) {
		super(view);
	}
	
	@Override
	/**
	 * gets the process definition id through session data
	 * and shows it on the view.
	 */
	protected void viewShown(ViewController viewController,
			Map<String, Object> userData, ControllableView oldView,
			Direction direction) 
	{
		
		if ( userData != null ) 
		{
			String procDefId = (String) userData.get( DiagramView.KEY_PROCESS_DEFINITION_ID );
			showDefinition( procDefId );
		} 
		else 
		{
			getView().hideData();
		}
	}
	/**
	 * sets the processDefinition object whose id correspond to procDefId into the view.
	 * @param procDefId = processDefinition id
	 */
	private void showDefinition( String procDefId )
	{		
		ProcessDefinition processDefinition = getRepositoryService()
				.createProcessDefinitionQuery()
				.processDefinitionId( procDefId ).singleResult();
		
		getView().setProcessDefinition( processDefinition );
		
	}
	
	private RepositoryService getRepositoryService() 
	{
		return ProcessEngines.getDefaultProcessEngine().getRepositoryService();
	}

}
