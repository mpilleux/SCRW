package cl.alma.scrw.ui.processes;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;


import cl.alma.scrw.diagram.DiagramView;
import cl.alma.scrw.ui.forms.UserFormView;

import com.github.peholmst.mvp4vaadin.navigation.ControllablePresenter;

/**
 * This class is the presenter of the ProcessView.
 * 
 * This class allows to start a new instance, suspend, activate, show diagram of a process definition
 * 
 * MODIFIED BY Mauricio Pilleux
 *
 */
public class ProcessPresenter extends ControllablePresenter<ProcessView> 
{

	private static final long serialVersionUID = 3328656287757828734L;

	private static Logger log = Logger.getLogger(ProcessPresenter.class
		.getName());

	public ProcessPresenter(ProcessView view) {
		super(view);
	}

	@Override
	public void init()
	{
		getView().setProcessDefinitions( getAllProcessDefinitions() );
		getView().setSuspendedProcessDefinitions( getAllSuspendedProcessDefinitions() );
	}

	/**
	 * Starts a new instance of processDefintion if possible and display success message. else
	 * display error message.
	 * @param processDefiniton = process definition to start a new instance
	 */
	public void startNewInstance(ProcessDefinition processDefinition) 
	{
		try 
		{
			if ( processDefinitionHasForm(processDefinition ) )
			{
				openFormForProcessDefinition(processDefinition);
			}
			else 
			{
				getRuntimeService().startProcessInstanceById(
						processDefinition.getId());
				getView().showProcessStartSuccess( processDefinition );
			}
		} 
		catch ( RuntimeException e ) 
		{
			log.log(Level.SEVERE, "Could not start process instance", e);
			getView().showProcessStartFailure(processDefinition);
		}
	}
	
	/**
	 * Suspend processDefintion if possible and display message.
	 * if not, displays a error message.
	 * @param processDefinition
	 */
	public void suspendProcess( ProcessDefinition processDefinition )
	{
		try 
		{
			getRepositoryService().suspendProcessDefinitionById( 
					processDefinition.getId() );
			
			/*cancel
			 * getRepositoryService().suspendProcessDefinitionById( 
					processDefinition.getId(), true, null );*/
			
			getView().showProcessSuspendSuccess(processDefinition);
			this.init();
			
		} 
		catch (RuntimeException e) 
		{
			log.log(Level.SEVERE, "Could not suspend process", e);
			getView().showProcessSuspendFailure(processDefinition);
		}
	}
	
	public void deployProcess( String fileName, String path )
	{
		RepositoryService repositoryService = ProcessEngines
				.getDefaultProcessEngine().getRepositoryService();
		
		repositoryService
		.createDeployment()
		.addClasspathResource(
				path+"/"+fileName)
		.deploy();
	}
	
	/**
	 * displays the DiagramView by passing the processDefinition id to the view.
	 * @param processDefinition process definition whose diagram will be shown.
	 */
	public void gotoDiagram( ProcessDefinition processDefinition )
	{
		String procId = null;
		if( processDefinition != null )
			procId = processDefinition.getId();
		if( procId != null )
		{
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put( DiagramView.KEY_PROCESS_DEFINITION_ID, procId );
			getViewController().goToView( DiagramView.VIEW_ID, params );
		}
	}
	
	/**
	 * activates processDefintion if possible.
	 * shows a message on the window on success, else display error.
	 * @param processDefinition = process definition to be activated.
	 */
	public void activateProcess( ProcessDefinition processDefinition )
	{
		try 
		{
			getRepositoryService().activateProcessDefinitionById( 
					processDefinition.getId() );
			
			getView().showProcessActivatedSuccess( processDefinition );
			this.init();
			
		} 
		catch ( RuntimeException e ) 
		{
			log.log(Level.SEVERE, "Could not activate process", e);
			getView().showProcessActivatedFailure( processDefinition );
		}
	}

	/**
	 * @param processDef = process definition.
	 * @return true if processDef has a form.
	 */
	public boolean processDefinitionHasForm( ProcessDefinition processDef ) 
	{
		return getFormService().getStartFormData(processDef.getId())
				.getFormKey() != null;
	}

	/**
	 * gets the processDef form key and displays calls the associated form from
	 * the UserFormView.
	 * @param processDef = process defintion whose form will be called.
	 */
	public void openFormForProcessDefinition(ProcessDefinition processDef) 
	{
		String formKey = getFormKey(processDef);
		if (formKey != null)
		{
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put(UserFormView.KEY_FORM_KEY, formKey);
			params.put(UserFormView.KEY_PROCESS_DEFINITION_ID,
					processDef.getId());
			getViewController().goToView( UserFormView.VIEW_ID, params );
		}
	}

	/**
	 * gets the processDef form key. This form key allows to call a form from the 
	 * formService.
	 * @param processDef = process definition whose form key will be returned.
	 * @return processDef's form key.
	 */
	private String getFormKey( ProcessDefinition processDef )
	{
		return getFormService().getStartFormData( processDef.getId() )
				.getFormKey();
	}

	/**
	 * gets a list of active process defintions from the activiti engine.
	 * @return the list of active process defintions.
	 */
	private List<ProcessDefinition> getAllProcessDefinitions()
	{
		ProcessDefinitionQuery query = getRepositoryService()
				.createProcessDefinitionQuery().active();
		return query.orderByProcessDefinitionName().asc().list();
	}
	
	/**
	 * gets a list of suspended process definitions (non activate processes) from 
	 * the activiti engine.
	 * @return the list of suspended process definitions.
	 */
	private List<ProcessDefinition> getAllSuspendedProcessDefinitions()
	{
		ProcessDefinitionQuery query = getRepositoryService()
				.createProcessDefinitionQuery().suspended();
		return query.orderByProcessDefinitionName().asc().list();
	}

	private RepositoryService getRepositoryService()
	{
		return ProcessEngines.getDefaultProcessEngine().getRepositoryService();
	}

	private RuntimeService getRuntimeService() 
	{
		return ProcessEngines.getDefaultProcessEngine().getRuntimeService();
	}

	private FormService getFormService() 
	{
		return ProcessEngines.getDefaultProcessEngine().getFormService();
	}

}
