package cl.alma.scrw.bpmn.forms;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;


import cl.alma.scrw.ui.util.AbstractUserTaskForm;

import com.vaadin.data.Validator;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.TextArea;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class creates the basic layout used in the Review Page Task.
 * This task intends to show the error messages that occurred during runtime and makes that user acknowledge that he has fixed the errors. 
 * 
 * This includes creating the necessary fields, populating the fields that show some process variables,
 * adding the necessary vallidators to the fields, and copying the data with the correct name to the process.
 * 
 * @author Mauricio Pilleux
 *
 */
public class ReviewPageForm extends AbstractUserTaskForm {

	private static final long serialVersionUID = -6355792599279114870L;

	public static final String FORM_KEY = "reviewPageForm";

	private TextArea error;
	
	private TextArea request;

	private CheckBox ackReviewDone;

	@Override
	public String getDisplayName() 
	{
		return "Review Page";
	}
	
	public String getDescription()
	{
		return "This page is only generated if inconsistencies are found.\n" +
				"The coordinator should resolve the inconsistencies (calling the responsibles) before proceeding.";
	}

	@Override
	public String getFormKey() 
	{
		return FORM_KEY;
	}

	@Override
	public void copyFormProperties( Map<String, String> destination )
	{
		//maybe not necessary
		destination.put("ackReviewDone", (String) ""+ackReviewDone.getValue());
	}

	@Override
	protected void populateFormField( String propertyId, String propertyValue )
	{
		
		if( propertyValue == null )
			return;
		
		if ( propertyId.equals("incFound") ) 
		{
			error.setReadOnly( false );
			error.setValue( error.getValue() + readXmlError( propertyValue ) );
			error.setReadOnly( true );
			return;
		}
		else if ( propertyId.equals("request") ) 
		{
			request.setValue(propertyValue);
			request.setReadOnly( true );
			return;
		}
		else if ( propertyId.equals("blockWS") )
		{
			error.setReadOnly( false );
			error.setValue( error.getValue() + readXmlError( propertyValue ) );
			error.setReadOnly( true );
			return;
		}
		else if ( propertyId.equals("unblockAfterCheck") )
		{
			error.setReadOnly( false );
			error.setValue( error.getValue() + readXmlError( propertyValue ) );
			error.setReadOnly( true );
			return;
		}
		else if ( propertyId.equals("changesWS") )
		{
			error.setReadOnly( false );
			error.setValue( error.getValue() + readXmlError( propertyValue ) );
			error.setReadOnly( true );
			return;
		}
		else if ( propertyId.equals("applyWS") )
		{
			error.setReadOnly( false );
			error.setValue( error.getValue() + readXmlError( propertyValue ) );
			error.setReadOnly( true );
			return;
		}
		else if ( propertyId.equals("unblockFinishCancel") )
		{
			error.setReadOnly( false );
			error.setValue( error.getValue() + readXmlError( propertyValue ) );
			error.setReadOnly( true );
			return;
		}
	}
	
	/**
	 * Reads the webService Error
	 * @param error = error to be read
	 * @return the formatted error.
	 * @see http://www.java2s.com/Code/Java/XML/ParseanXMLstringUsingDOMandaStringReader.htm
	 */
	private String readXmlError( String xmlRecords )
	{
		String res = "";
		    DocumentBuilder db = null;
			try {
				db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				
			    InputSource is = new InputSource();
			    is.setCharacterStream(new StringReader(xmlRecords));
	
			    Document doc;
				
					doc = db.parse(is);
				
			    NodeList nodes = doc.getElementsByTagName("error");
	
			    for (int i = 0; i < nodes.getLength(); i++) 
			    {
			      Element element = (Element) nodes.item( i );			     			      
			      res += getCharacterDataFromElement( element )+"\n";
			    }
			    return res;
			} 
			catch (ParserConfigurationException e) 
			{
				return "";
			}
			catch (SAXException e) 
			{
				return "";
			}
			catch (IOException e) 
			{
				
				return "";
			}
		    
	}
	
	public static String getCharacterDataFromElement(Element e) {
	    Node child = e.getFirstChild();
	    if (child instanceof CharacterData) {
	      CharacterData cd = (CharacterData) child;
	      return cd.getData();
	    }
	    return "";
	  }

	@Override
	protected void init() {
		request = new TextArea( "Request" );
		request.setColumns( 35 );
		request.setRows( 15 );
		addComponent( request );
		
		error = new TextArea( "Error" );
		error.setValue("");
		error.setColumns( 35 );
		error.setRows( 15 );
		addComponent( error );

		ackReviewDone = new CheckBox( "Reload" );
		ackReviewDone.setRequired(true);
		addComponent( ackReviewDone );
		
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
		
		ackReviewDone.addValidator(checkBoxValidator);
	}



}
