package cl.alma.scrw.bpmn.forms;

import java.util.Collection;
import java.util.Map;


import cl.alma.scrw.ui.login.Authentication;
import cl.alma.scrw.ui.util.AbstractUserTaskForm;

import com.vaadin.data.Validator;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextArea;

/**
 * This class creates the basic layout used in the Acknowledge Page Task.
 * This task represents that the corresponding user acknowledges that there are some tasks to be done.
 * The user can also assign a new user to the task.  
 * 
 * This includes creating the necessary fields, populating the fields that show some process variables,
 * adding the necessary vallidators to the fields, and copying the data with the correct name to the process.
 * 
 * @author Mauricio Pilleux
 *
 */
public class AcknowledgePageForm extends AbstractUserTaskForm {

	private static final long serialVersionUID = -6355792599279114870L;

	public static final String FORM_KEY = "acknowledgePageForm";

	private TextArea request;

	private CheckBox ackPage;
	
	private Select newAssignee;

	@Override
	public String getDisplayName() 
	{
		return "Acknowledge Page";
	}
	
	public String getDescription()
	{
		return "This page is to let know the system the work is in progress.\n " +
				"This means, after the user acknowledges this page, the wiki pages must be modified";
		
	}

	@Override
	public String getFormKey() 
	{
		return FORM_KEY;
	}

	@Override
	public void copyFormProperties(Map<String, String> destination) 
	{
		destination.put( "ackPage", (String) ""+ackPage.getValue() );
		if( newAssignee.getValue() != null && !newAssignee.getValue().equals("") && !newAssignee.getValue().equals("null")  )
			destination.put("assignee", (String) newAssignee.getValue() );
	}

	@Override
	protected void populateFormField(String propertyId, String propertyValue)
	{
		if( propertyValue == null )
			return;
		if (propertyId.equals("request")) {
			request.setValue(propertyValue);
			request.setReadOnly( true );
		}
	}
	
	@Override
	protected void init() 
	{
		request = new TextArea( "Request" );
		request.setColumns( 35 );
		request.setRows( 15 );
		addComponent(request);
		
		newAssignee = new Select("Assign task to another user");
		newAssignee.setFilteringMode( AbstractSelect.Filtering.FILTERINGMODE_CONTAINS );
		
		Collection<String> userList = Authentication.getUsers();
		for( String user : userList )
			newAssignee.addItem( user );
		addComponent( newAssignee );
		
		ackPage = new CheckBox( "Acknowledge the work is in progress" );
		ackPage.setRequired(true);
		addComponent( ackPage );
		
		Validator checkBoxValidator = new Validator() {

		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isValid(Object value) {
		        if (value == null ) {
		            return false;
		        }

		        return (Boolean) value;
		    }

		    // Upon failure, the validate() method throws an exception
		    // with an error message.
		    public void validate(Object value)
		                throws InvalidValueException {
		        if (!isValid(value)) {
		            if ( value != null ) {
		                throw new InvalidValueException(
		                    "You must mark the checkbox.");
		            } 
		        }
		    }
		};
		ackPage.addValidator(checkBoxValidator);
	}

}
