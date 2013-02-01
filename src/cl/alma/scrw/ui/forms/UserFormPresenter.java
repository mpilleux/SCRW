package cl.alma.scrw.ui.forms;

import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;


import cl.alma.scrw.ui.util.UserTaskForm;
import cl.alma.scrw.ui.util.UserTaskFormContainer;

import com.github.peholmst.mvp4vaadin.navigation.ControllablePresenter;
import com.github.peholmst.mvp4vaadin.navigation.ControllableView;
import com.github.peholmst.mvp4vaadin.navigation.Direction;
import com.github.peholmst.mvp4vaadin.navigation.ViewController;

/**
 * This class is the presenter of the UserFormView.
 * 
 * This class allows to initialize the current form (start event form or task form) in the view
 * and allows submit form data to the activiti engine.
 *
 */
public class UserFormPresenter extends ControllablePresenter<UserFormView> {

	private static final long serialVersionUID = 6378990821642431252L;

	private final UserTaskFormContainer userTaskFormContainer;

	public UserFormPresenter(UserFormView view,
			UserTaskFormContainer userTaskFormContainer) 
	{
		super(view);
		this.userTaskFormContainer = userTaskFormContainer;
	}

	@Override
	/**
	 * everytime the user sees the UserFormView, update the data.
	 */
	protected void viewShown(ViewController viewController,
			Map<String, Object> userData, ControllableView oldView,
			Direction direction) 
	{
		if (userData != null) 
		{
			String formKey = (String) userData.get(UserFormView.KEY_FORM_KEY);
			if (userData.containsKey(UserFormView.KEY_TASK_ID)) 
			{
				String taskId = (String) userData.get(UserFormView.KEY_TASK_ID);
				showTaskForm(formKey, taskId);
			} 
			else if (userData
					.containsKey(UserFormView.KEY_PROCESS_DEFINITION_ID)) 
			{
				String processDefId = (String) userData
						.get(UserFormView.KEY_PROCESS_DEFINITION_ID);
				showProcessForm(formKey, processDefId);
			} 
			else 
			{
				getView().hideForm();
			}
		} 
		else 
		{
			getView().hideForm();
		}
	}

	/**
	 * Submits the form if it is valid
	 * if the form is not valid, then it show the errors.
	 * @param form = form to be submitted
	 */
	public void submitForm(UserTaskForm form) {
		
		String formValidation = form.validate();
		if( formValidation.length() > 0 )//invalid form
		{
			form.setError( formValidation );
			getView().setForm( form );
			return;
		}
					
		if (form.getFormType().equals(UserTaskForm.Type.START_FORM)) {
			getFormService().submitStartFormData(form.getProcessDefinitionId(),
					form.getFormProperties());
		} else if (form.getFormType().equals(UserTaskForm.Type.TASK_FORM)) {
			getFormService().submitTaskFormData(form.getTaskId(),
					form.getFormProperties());
		}
		getViewController().goBack();
	}

	/**
	 * Sets the form in the view associated to taskId, whose key is formKey
	 * @param formKey = formKey associated to the form to set.
	 * @param taskId = taskId corresponding to the form to be set.
	 */
	private void showTaskForm( String formKey, String taskId ) 
	{
		UserTaskForm form = userTaskFormContainer.getForm( formKey );
		TaskFormData formData = getFormService().getTaskFormData( taskId );
		form.populateForm( formData, taskId );
		getView().setForm( form );
	}

	/**
	 * Sets the start event form in the view associated to processDefinition, whose key is formKey
	 * @param formKey = formKey associated to the form to set.
	 * @param processDefinitionId = process definition id corresponding to the start event form to be set.
	 */
	private void showProcessForm( String formKey, String processDefinitionId ) 
	{
		UserTaskForm form = userTaskFormContainer.getForm( formKey );
		StartFormData formData = getFormService().getStartFormData(
				processDefinitionId );
		form.populateForm( formData, processDefinitionId );
		getView().setForm( form );
	}

	private FormService getFormService() 
	{
		return ProcessEngines.getDefaultProcessEngine().getFormService();
	}
}
