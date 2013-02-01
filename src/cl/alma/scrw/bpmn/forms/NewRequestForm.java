package cl.alma.scrw.bpmn.forms;

import java.util.Collection;
import java.util.List;
import java.util.Map;


import cl.alma.scrw.bpmn.forms.validators.AntennaValidator;
import cl.alma.scrw.ui.login.Authentication;
import cl.alma.scrw.ui.util.AbstractUserTaskForm;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
/**
 * This class creates the basic layout used in the Start Event.
 * This task intends that the corresponding user creates a new request which consists of a text, a title, a list of actors, and a list of antennas.
 * 
 * This includes creating the necessary fields, populating the fields that show some process variables,
 * adding the necessary vallidators to the fields, and copying the data with the correct name to the process.
 * 
 * @author Mauricio Pilleux
 *
 */
public class NewRequestForm extends AbstractUserTaskForm {

	private static final long serialVersionUID = -6355792599279114870L;

	public static final String FORM_KEY = "newRequestForm";//must have the form key assosiated to the .bpmn process task

	private TextArea request;
	
	private final int countAntennaVERTEX = 25;
	
	private final int countAntennaAEM = 25;
	
	private final int countAntennaMELCO12M = 4;
	
	private final int countAntennaMELCO7M = 12;
	
	private CheckBox[][] antennaVERTEX;
	
	private CheckBox[][] antennaAEM;
	
	private CheckBox[][] antennaMELCO12M;
	
	private CheckBox[][] antennaMELCO7M;
	
	private Table antennaVERTEXTable;
	
	private Table antennaAEMTable;
	
	private Table antennaMELCO12MTable;
	
	private Table antennaMELCO7MTable;
	
	private TwinColSelect actorsSelect;
	
	private TextField actorSelectFilter;
	
	private TextField title;
	
	private final int titleLength = 200;
	
	private VerticalLayout formContainer;

	private GridLayout requestContainer;
	
	private GridLayout antennasContainer;
	
	private List<String> userList;
	
		
	public String getDescription()
	{
		return "This page should be used by the coordinator to create a request.";
	}
	
	@Override
	protected void init() 
	{	
		antennaVERTEX = new CheckBox[countAntennaVERTEX][2];
		antennaAEM = new CheckBox[countAntennaAEM][2];
		antennaMELCO12M = new CheckBox[countAntennaMELCO12M][2];
		antennaMELCO7M = new CheckBox[countAntennaMELCO7M][2];
		
		formContainer = new VerticalLayout();
		
		requestContainer = new GridLayout( 2,2 );
		antennasContainer = new GridLayout( 4,1 );
		requestContainer.setSpacing(true);
		requestContainer.setMargin( false, true, false, true );
		
		formContainer.setMargin( false, true, false, true );
		formContainer.setSpacing( true );
		formContainer.addComponent( requestContainer );
		formContainer.addComponent(  antennasContainer );
		
		this.addComponent( formContainer );
		this.formContainer.setWidth(Sizeable.SIZE_UNDEFINED, 0);
		this.setExpandRatio( formContainer, 1.0F );
		
		title = new TextField( "Title" );
		title.setRequired( true );
		title.setRequiredError( "Title is missing" );
		title.setImmediate( true );
		title.setMaxLength( titleLength );
		//title.setMaxLength( titleLength );
		title.setWidth( "100%" );
		requestContainer.addComponent( title );
		
		actorsSelect = new TwinColSelect();
		actorsSelect.setRequired( true );
		actorsSelect.setRequiredError("Actors are missing");
		actorsSelect.setLeftColumnCaption("Actors List");
		actorsSelect.setRightColumnCaption("Selected Actors");
		
		userList = Authentication.getUsers();
		for( String user : userList )
			actorsSelect.addItem( user );
		actorsSelect.setRows( 10 );
		
		actorSelectFilter = new TextField("Filter Actors");
		actorSelectFilter.addListener(new TextChangeListener() {
			private static final long serialVersionUID = -2895846903879464655L;

			@SuppressWarnings("unchecked")
			public void textChange(TextChangeEvent event) {
		        String text = event.getText();
		        Collection<String> values = (Collection<String>) actorsSelect.getValue();
		        actorsSelect.removeAllItems();
		        for( String user : userList )
		        	if( user.contains( text ) )
		        		actorsSelect.addItem( user );
		        
		        for( String user : values )
		        	actorsSelect.addItem( user );
		        
		        actorsSelect.setValue( values );
		    }
		});
		actorSelectFilter.setTextChangeEventMode( TextChangeEventMode.LAZY );
		
		VerticalLayout vl = new VerticalLayout();
		vl.addComponent( actorSelectFilter );
		vl.addComponent( actorsSelect );
		requestContainer.addComponent( vl, 1, 1 );
		
		initAntennaList();
		antennasContainer.setSpacing(true);
		antennasContainer.setMargin( false, true, false, true );
		
		request = new TextArea( "Request" );
		request.setRequired( true );
		request.setRequiredError("Request is missing");
		request.setColumns( 35 );
		request.setRows( 15 );
		requestContainer.addComponent( request, 0, 1 );		
		
		antennasContainer.addComponent( antennaVERTEXTable );
		antennasContainer.addComponent( antennaAEMTable );
		antennasContainer.addComponent( antennaMELCO12MTable );
		antennasContainer.addComponent( antennaMELCO7MTable );
	}
	
	private void initAntennaList()
	{
		antennaVERTEXTable = new Table("VEXTEX antennas");
		antennaVERTEXTable.addContainerProperty("Antenna", String.class,  null);
		antennaVERTEXTable.addContainerProperty("Select",  CheckBox.class,  null);
		antennaVERTEXTable.addContainerProperty("New?",       CheckBox.class, null);
		antennaVERTEXTable.setPageLength( countAntennaVERTEX );

		antennaAEMTable = new Table("AEM antennas");
		antennaAEMTable.addContainerProperty("Antenna", String.class,  null);
		antennaAEMTable.addContainerProperty("Select",  CheckBox.class,  null);
		antennaAEMTable.addContainerProperty("New?",       CheckBox.class, null);
		antennaAEMTable.setPageLength( countAntennaAEM );
		
		antennaMELCO12MTable = new Table("MELCO12 antennas");
		antennaMELCO12MTable.addContainerProperty("Antenna", String.class,  null);
		antennaMELCO12MTable.addContainerProperty("Select",  CheckBox.class,  null);
		antennaMELCO12MTable.addContainerProperty("New?",       CheckBox.class, null);
		antennaMELCO12MTable.setPageLength( countAntennaMELCO12M );
		
		antennaMELCO7MTable = new Table("MELCO7 antennas");
		antennaMELCO7MTable.addContainerProperty("Antenna", String.class,  null);
		antennaMELCO7MTable.addContainerProperty("Select",  CheckBox.class,  null);
		antennaMELCO7MTable.addContainerProperty("New?",       CheckBox.class, null);
		antennaMELCO7MTable.setPageLength( countAntennaMELCO7M );		
		
		for( int i = 0; i < countAntennaVERTEX; i++ )
		{
			String num = getNumFormat( i );
			
			antennaVERTEX[i][0] = new CheckBox();
			antennaVERTEX[i][1] = new CheckBox();
			
			antennaVERTEXTable.addItem(new Object[] {
					"DV"+num, antennaVERTEX[i][0],antennaVERTEX[i][1]}, new Integer(i+1));
		}
		
		for( int i = 0; i < countAntennaAEM; i++ )
		{
			String num = getNumFormat( i + 40 );
				
			antennaAEM[i][0] = new CheckBox();
			antennaAEM[i][1] = new CheckBox();
			
			antennaAEMTable.addItem(new Object[] {
					"DA"+ num, antennaAEM[i][0],antennaAEM[i][1]}, new Integer( i + countAntennaVERTEX + 1 ) );
		}
		
		for( int i = 0; i < countAntennaMELCO12M; i++ )
		{
			String num = getNumFormat( i );
				
			antennaMELCO12M[i][0] = new CheckBox();
			antennaMELCO12M[i][1] = new CheckBox();
			
			antennaMELCO12MTable.addItem(new Object[] {
					"PM"+num, antennaMELCO12M[i][0],antennaMELCO12M[i][1]}, new Integer( i + countAntennaVERTEX + countAntennaAEM + 1 ) );
		}
		
		for( int i = 0; i < countAntennaMELCO7M; i++ )
		{
			String num = getNumFormat( i ); 
				
			antennaMELCO7M[i][0] = new CheckBox();
			antennaMELCO7M[i][1] = new CheckBox();
			
			antennaMELCO7MTable.addItem(new Object[] {
					"CM"+num, antennaMELCO7M[i][0],antennaMELCO7M[i][1]}, new Integer( i + countAntennaVERTEX + countAntennaMELCO12M + countAntennaAEM + 1 ) );
		}
	}
	
	public boolean isValid()
	{		
		return this.request.isValid() &&
				this.title.isValid() &&
				this.actorsSelect.isValid() && 
				new AntennaValidator( antennaVERTEX, antennaAEM, antennaMELCO12M, antennaMELCO7M ).isValid();
	}
	
	public String validate()
	{		
		try{
			request.validate();
		}
		catch(InvalidValueException e)
		{
			return e.getMessage();
		}
		
		try{
			title.validate();
		}
		catch(InvalidValueException e)
		{
			return e.getMessage();
		}
		
		try{
			actorsSelect.validate();
		}
		catch(InvalidValueException e)
		{
			return e.getMessage();
		}
		
		try{
			new AntennaValidator( antennaVERTEX, antennaAEM, antennaMELCO12M, antennaMELCO7M ).validate(null);
		}
		catch(InvalidValueException e)
		{
			return e.getMessage();
		}
		
		return "";
	}
	
	
	/**
	 * Adds a cero ("0") in front of (num+1), if (num+1) is less than 10
	 * @param num = number to be formatted
	 * @return formatted number
	 */
	private String getNumFormat( int num )
	{
		if( (num+1) < 10 )
			return "0" + (num+1);
		else
			return "" + (num+1); 
	}
	
	@Override
	public String getDisplayName() {
		return "New Request";
	}

	@Override
	public String getFormKey() {
		return FORM_KEY;
	}

	@Override
	public void copyFormProperties(Map<String, String> destination) 
	{			
		destination.put("request",		(String) request.getValue());
		destination.put("actors", 		getActors() );
		destination.put("antennas", 	getAntennaColumn( 0 ) );
		destination.put("newAntennas", 	getAntennaColumn( 1 ) );
		destination.put("requestTitle", (String) title.getValue() );
	}
	
	/**
	 * formats the twincollums to obtain an valid actors list.
	 * 
	 * a valid actors list consists of a string of all actors concatenated by a comma.
	 * @return valid actors list.
	 */
	private String getActors()
	{
		//creates the actor String
		String actors = "";
		@SuppressWarnings("unchecked")
		Collection<String> values = (Collection<String>) actorsSelect.getValue();
		for( String user : values )
			actors += user+",";
		actors = actors.substring( 0, actors.length() -1 );
		
		return actors;
	}
	
	
	private String getAntennaColumn( int j )
	{
		if ( j != 1 && j != 0 )
			return "";
		String res = "";
		for( int i = 0; i < countAntennaVERTEX; i++ )
		{
			String num = getNumFormat( i );
			
			if( (Boolean) antennaVERTEX[i][j].getValue() )
				res+="DV"+num+",";
		}
		
		for( int i = 0; i < countAntennaAEM; i++ )
		{
			String num = getNumFormat( i + 40 );
				
			if( (Boolean) antennaAEM[i][j].getValue() )
				res+="DA"+num+",";
		}
		
		for( int i = 0; i < countAntennaMELCO12M; i++ )
		{
			String num = getNumFormat( i );
			
			if( (Boolean) antennaMELCO12M[i][j].getValue() )
				res+="PM"+num+",";
		}
		
		for( int i = 0; i < countAntennaMELCO7M; i++ )
		{
			String num = getNumFormat( i );
			
			if( (Boolean) antennaMELCO7M[i][j].getValue() )
				res+="CM"+num+",";
		}
		return res.length()>0?res:"";
	}
	

	@Override
	protected void populateFormField(String propertyId, String propertyValue) {
		if( propertyValue == null )
			return;
		
	}

}
