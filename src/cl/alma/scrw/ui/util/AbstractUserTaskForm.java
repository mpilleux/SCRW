package cl.alma.scrw.ui.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
//import java.util.logging.Level;
//import java.util.logging.Logger;

import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;

import com.vaadin.data.Validatable;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * This abstract class implements the functionalities of a UserTaskForm.
 * 
 * some of the functionlities are:
 * 	get form information
 *  validate form
 *  display form
 *  etc
 *
 */
public abstract class AbstractUserTaskForm extends VerticalLayout implements
		UserTaskForm 
		{

	//private static Logger log = Logger.getLogger(AbstractUserTaskForm.class
		//	.getName());

	private static final long serialVersionUID = -1578037829262126003L;

	private String processDefinitionId;

	private String taskId;
	
	private Label errMsg;
	

	public AbstractUserTaskForm() 
	{
		this.errMsg = new Label();
		this.addComponent( this.errMsg );
		init();
	}

	protected abstract void init();
	
	
	@Override
	public Type getFormType() 
	{
		if (taskId != null) {
			return Type.TASK_FORM;
		} else if (processDefinitionId != null) {
			return Type.START_FORM;
		} else {
			throw new IllegalStateException(
					"No taskId nor processDefinitionId has been specified");
		}
	}

	@Override
	public String getProcessDefinitionId() 
	{
		return processDefinitionId;
	}

	@Override
	public String getTaskId()
	{
		return taskId;
	}

	@Override
	public void populateForm( StartFormData formData, String processDefinitionId ) 
	{
		this.processDefinitionId = processDefinitionId;
		populateFormFields(formData);
	}

	@Override
	public void populateForm( TaskFormData formData, String taskId ) 
	{
		this.taskId = taskId;
		populateFormFields(formData);
	}

	/**
	 * this method populates the current form fields with the corresponding activiti formData.
	 * @param formData = Activiti FormData to set
	 */
	protected void populateFormFields( FormData formData ) 
	{
		for ( FormProperty property : formData.getFormProperties() ) 
		{
			String propertyId = property.getId();
			String propertyValue = property.getValue();
			populateFormField(propertyId, propertyValue);
		}
	}

	@Override
	/**
	 * gets the current form properties
	 * @return a map containing the form properties <propertyName, propertyValue>
	 */
	public Map<String, String> getFormProperties() 
	{
		//log.log(Level.INFO, "Constructing form property map for {1}",
		//		getFormKey());
		HashMap<String, String> map = new HashMap<String, String>();
		copyFormProperties(map);
		return map;
	}
	
	/**
	 * Validates the current form
	 * @return true if the form is valid, else returns false.
	 */
	public boolean isValid()
	{		
		//Traverse all form objects and adds them in stack.
		Stack<Component> stack = new Stack<Component>();
		stack.push(this);
		while ( ! stack.isEmpty() ) {
		    Component c = stack.pop();
		    
		    //if c is ComponentContainer, traverse all of its components and add them to the stack.
		    if ( c instanceof ComponentContainer )
		        for ( Iterator<Component> i = ((ComponentContainer) c).getComponentIterator(); i.hasNext(); )
		            stack.add( i.next() );
		    
		    //only validate the object if it is validatable.
		    if( ! ( c instanceof Validatable ) )
				continue;
			Validatable t =  (Validatable)c;
			
			if( ! t.isValid() )
				return false;
		}
		return true;
	}
	
	/**
	 * Assumption: the form is not valid.
	 * this method looks for the first invalid element 
	 * @return invalid element error message
	 */
	public String validate()
	{
		String msg = "";
		
		Stack<Component> stack = new Stack<Component>();
		stack.push(this);
		while ( ! stack.isEmpty() ) {
		    Component c = stack.pop();
		    
		    //if c is ComponentContainer, traverse all of its components and add them to the stack.
		    if ( c instanceof ComponentContainer )
		        for ( Iterator<Component> i = ((ComponentContainer) c).getComponentIterator(); i.hasNext(); )
		            stack.add( i.next() );

		    //only validate the object if it is validatable.
		    if( ! ( c instanceof Validatable ) )
				continue;
			Validatable t =  (Validatable)c;
			
			try{
				t.validate();
			}
			catch(InvalidValueException e)
			{
				msg = e.getMessage();
				break;
			}
		}
		return msg;
	}
	
	/**
	 * this method sets the form error message
	 * @param txt = error message
	 */
	@SuppressWarnings("deprecation")
	public void setError( String txt )
	{
		this.errMsg.setValue( txt );
		this.errMsg.setStyle(Reindeer.LABEL_H2);
	}
	
	protected abstract void copyFormProperties(Map<String, String> destination);

	protected abstract void populateFormField(String propertyId,
			String propertyValue);

	@Override
	public Component getFormComponent() {
		return this;
	}

}
