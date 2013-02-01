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
 * This class creates the basic layout used in the Setup Finished Page Task.
 * This task represents that the corresponding user acknowledges that he has done the corresponding changes in the wiki.
 * 
 * This includes creating the necessary fields, populating the fields that show some process variables,
 * adding the necessary vallidators to the fields, and copying the data with the correct name to the process.
 * 
 * @author Mauricio Pilleux
 *
 */
public class SetUpFinishedPageForm extends AbstractUserTaskForm {

	private static final long serialVersionUID = -6355792599279114870L;

	public static final String FORM_KEY = "setUpFinishedPageForm";

	private TextArea request;

	private CheckBox ackFinishPage;
	
	private CheckBox checkRequired;
	
	private Select newAssignee;

	@Override
	public String getDisplayName() 
	{
		return "Set Up Finished Page";
	}

	public String getDescription()
	{
		return "In this page, the responsible will inform that the Configuration Page was updated.\n" +
				"The responsible in this page, can also indicate if a check after the system restart should be done.";
	}
	
	@Override
	public String getFormKey() 
	{
		return FORM_KEY;
	}

	@Override
	public void copyFormProperties(Map<String, String> destination) 
	{
		destination.put( "ackFinishPage", (String) "" + ackFinishPage.getValue() );
		destination.put( "checkRequired", (String) "" + checkRequired.getValue() );
		destination.put( "newAssignee", (String) "" + newAssignee.getValue() );
	}

	@Override
	protected void populateFormField(String propertyId, String propertyValue)
	{
		if( propertyValue == null )
			return;
		if ( propertyId.equals("request") ) 
		{
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
		addComponent( request );

		newAssignee = new Select("Assign task to another user");
		newAssignee.setFilteringMode( AbstractSelect.Filtering.FILTERINGMODE_CONTAINS );
		
		Collection<String> userList = Authentication.getUsers();
		for( String user : userList )
			newAssignee.addItem( user );
		
		addComponent( newAssignee );
		
		
		ackFinishPage = new CheckBox( "Acknowledge that the configuration page was updated" );
		ackFinishPage.setRequired( true );
		addComponent( ackFinishPage );
		
		checkRequired = new CheckBox( "Is check required after system restart?" );
		addComponent( checkRequired );
		
		Validator checkBoxValidator = new Validator() {

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
		
		ackFinishPage.addValidator(checkBoxValidator);
		
		
		
		
		
	}



}
