package cl.alma.scrw.ui.forms;


import cl.alma.scrw.ui.util.UserTaskForm;

import com.github.peholmst.mvp4vaadin.navigation.ControllableView;

/**
 * This interface shows the necessary methods that UserFormViewImpl will implement, and UserFormPresenter will call.  
 *
 */
public interface UserFormView extends ControllableView 
{

	String VIEW_ID = "userForm";

	String KEY_FORM_KEY = "formKey";

	String KEY_TASK_ID = "taskId";

	String KEY_PROCESS_DEFINITION_ID = "processDefinitionId";

	/**
	 * sets the form whose data will be shown. 
	 * @param form = form whose data will be shown.
	 */
	void setForm( UserTaskForm form );

	/**
	 * clears all data from the window (removes current form).
	 */
	void hideForm();
}
