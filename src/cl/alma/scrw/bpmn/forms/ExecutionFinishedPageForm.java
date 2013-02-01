package cl.alma.scrw.bpmn.forms;

import java.util.Map;


import cl.alma.scrw.ui.util.AbstractUserTaskForm;

import com.vaadin.data.Validator;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.TextArea;

/**
 * This class creates the basic layout used in the Execution Finished Page Task.
 * This task represents that the corresponding user acknowledges that he has done his work.
 * 
 * This includes creating the necessary fields, populating the fields that show some process variables,
 * adding the necessary vallidators to the fields, and copying the data with the correct name to the process.
 * 
 * @author Mauricio Pilleux
 *
 */
public class ExecutionFinishedPageForm extends AbstractUserTaskForm {

	private static final long serialVersionUID = -6355792599279114870L;

	public static final String FORM_KEY = "executionFinishedPageForm";

	private CheckBox ackEFinishedPage;

	private TextArea request;
	
	private TextArea comment;
	
	@Override
	public String getDisplayName() 
	{
		return "Execution Finished Page";
	}
	
	public String getDescription()
	{
		return "This page is used to let know the system the the changes are done.";
	}

	@Override
	public String getFormKey() 
	{
		return FORM_KEY;
	}

	@Override
	public void copyFormProperties(Map<String, String> destination)
	{
		//maybe not necessary
		destination.put("ackEFinishedPage", (String) ""+ackEFinishedPage.getValue());
		destination.put("ackEFinishedPageComment", (String) ""+comment.getValue());
	}

	@Override
	protected void populateFormField(String propertyId, String propertyValue) {
		if( propertyValue == null )
			return;
		if (propertyId.equals("request")) {
			request.setValue(propertyValue);
			request.setReadOnly( true );
		}
	}

	@Override
	protected void init() {
		request = new TextArea( "Request" );
		request.setColumns( 35 );
		request.setRows( 15 );
		addComponent( request );
		
		comment = new TextArea( "Comment" );
		comment.setColumns( 35 );
		addComponent( comment );
		
		ackEFinishedPage = new CheckBox( "Acknowledge Execution is Finished" );
		ackEFinishedPage.setRequired(true);
		addComponent( ackEFinishedPage );
		
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
		                    "You must check the checkbox.");
		            } 
		        }
		    }
		};
		
		ackEFinishedPage.addValidator(checkBoxValidator);
	}



}
