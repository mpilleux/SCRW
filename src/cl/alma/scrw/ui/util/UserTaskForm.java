package cl.alma.scrw.ui.util;

import java.util.Map;

import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;

import com.vaadin.ui.Component;

/**
 * This class intends to implement the functionalities of the user task form.
 * 
 * This includes the ability to submit data to the activiti engine, get all the information necessary,
 * 
 * validate, etc.
 *
 */
public interface UserTaskForm extends java.io.Serializable {

	enum Type {
		START_FORM, TASK_FORM
	}

	Type getFormType();

	String getProcessDefinitionId();

	String getTaskId();

	String getDisplayName();

	String getDescription();

	String getFormKey();
	
	boolean isValid();
	
	public void setError( String txt );
	
	String validate();

	void populateForm(StartFormData formData, String processDefinitionId);

	void populateForm(TaskFormData formData, String taskId);

	Map<String, String> getFormProperties();

	Component getFormComponent();
}
