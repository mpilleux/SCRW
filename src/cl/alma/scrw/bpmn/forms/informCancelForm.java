package cl.alma.scrw.bpmn.forms;

import java.util.Map;


import cl.alma.scrw.ui.util.AbstractUserTaskForm;

import com.vaadin.ui.TextArea;

/**
 * This class creates the basic layout used in the Specify cancelation reason Task.
 * This task intends that the corresponding user give a comment about the cancelation reason.
 * 
 * This includes creating the necessary fields, populating the fields that show some process variables,
 * adding the necessary vallidators to the fields, and copying the data with the correct name to the process.
 * 
 * @author Mauricio Pilleux
 *
 */
public class informCancelForm extends AbstractUserTaskForm {

	private static final long serialVersionUID = -6355792599279114870L;

	public static final String FORM_KEY = "informCancel";
	
	private TextArea request;
	
	private TextArea comment;

	@Override
	public String getDisplayName() {
		return "Cancel reason";
	}

	@Override
	public String getFormKey() {
		return FORM_KEY;
	}

	@Override
	public void copyFormProperties(Map<String, String> destination) {
		//maybe not necessary
		destination.put("cancelComment", (String) ""+comment.getValue());
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

	}



}
