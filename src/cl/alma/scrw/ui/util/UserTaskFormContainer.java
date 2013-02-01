package cl.alma.scrw.ui.util;

import java.util.HashMap;
import java.util.Map;

/**
 * This class works as a helper to contain all the forms available in the application.
 * 
 * This class has the ability to call and store UserTaskForms
 *
 */
public class UserTaskFormContainer implements java.io.Serializable {

	private static final long serialVersionUID = -8090027061007059322L;

	private Map<String, Class<? extends UserTaskForm>> formMap = new HashMap<String, Class<? extends UserTaskForm>>();

	public boolean containsForm(String formKey)
	{
		return formMap.containsKey(formKey);
	}

	/**
	 * Gets the form associated with the formkey.
	 * @param formKey = forkey of the form to be obtained
	 * @return the form associated to formkey.
	 */
	public UserTaskForm getForm(String formKey)
	{
		Class<? extends UserTaskForm> formClass = formMap.get(formKey);
		if (formClass == null) {
			return null;
		} else {
			try {
				return formClass.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Registers a new form.
	 * @param formKey = form key associated to the form object.
	 * @param formClass = class that represents the form.
	 */
	public void registerForm(String formKey,
			Class<? extends UserTaskForm> formClass) 
	{
		formMap.put(formKey, formClass);
	}
}
