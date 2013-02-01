package cl.alma.scrw.bpmn.forms;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Map;

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


import cl.alma.scrw.ui.util.AbstractUserTaskForm;

import com.vaadin.data.Validator;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Table;

/**
 * This class creates the basic layout used in the Software Configuration Change Page Task.
 * This task intends to show a software user what changes needs to be done, and acknowledge when the changes are done.
 * 
 * This includes creating the necessary fields, populating the fields that show some process variables,
 * adding the necessary vallidators to the fields, and copying the data with the correct name to the process.
 * 
 * @author Mauricio Pilleux
 *
 */
public class SoftConfForm extends AbstractUserTaskForm 
{

	private static final long serialVersionUID = -6355792599279114870L;

	public static final String FORM_KEY = "softConfForm";

	private CheckBox softConf;
	
	private Table changes;

	@Override
	public String getDisplayName() 
	{
		return "Software Configuration Change";
	}
	
	public String getDescription()
	{
		return "This page shows a summary of all the configuration changes to be done.";
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
		destination.put("ackSoftConf", (String) ""+softConf.getValue());
	}
	
	/**
	 * Reads the webService Error
	 * @param error = error to be read
	 * @return the formatted error.
	 * @see http://www.java2s.com/Code/Java/XML/ParseanXMLstringUsingDOMandaStringReader.htm
	 */
	private ArrayList<String> readXmlError( String xmlRecords )
	{
		ArrayList<String> res = new ArrayList<String>();
		    DocumentBuilder db = null;
			try {
				db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				
			    InputSource is = new InputSource();
			    is.setCharacterStream(new StringReader(xmlRecords));
	
			    Document doc;
				
					doc = db.parse(is);
				
			    NodeList nodes = doc.getElementsByTagName("change");
	
			    for (int i = 0; i < nodes.getLength(); i++) 
			    {
			      Element element = (Element) nodes.item( i );			     			      
			      res.add( getCharacterDataFromElement( element ) );
			    }
			    
			    return res;
			} 
			catch (ParserConfigurationException e) 
			{
				return new ArrayList<String>();
			}
			catch (SAXException e) 
			{
				return new ArrayList<String>();
			}
			catch (IOException e) 
			{
				
				return new ArrayList<String>();
			}
		    
	}
	
	public static String getCharacterDataFromElement(Element e)
	{
	    Node child = e.getFirstChild();
	    if (child instanceof CharacterData) {
	      CharacterData cd = (CharacterData) child;
	      return cd.getData();
	    }
	    return "";
	  }
	
	
	

	@Override
	protected void populateFormField(String propertyId, String propertyValue) 
	{
		if( propertyValue == null )
			return;
		if ( propertyId.equals("changesWS") )
		{
			ArrayList<String> ch = readXmlError( propertyValue );
			changes.setPageLength( ch .size() );
			int index = 0;
			for( String change : ch )
			{
				
				changes.addItem(new Object[] {
					    change }, new Integer( index ) );
				index++;
			}
			
		}
	}

	@Override
	protected void init() 
	{
		changes = new Table( "Changes" );
		changes.addContainerProperty("Change", String.class,  null);
		addComponent( changes );

		softConf = new CheckBox( "Accept software configuration" );
		softConf.setRequired(true);
		addComponent( softConf );
		
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
		
		softConf.addValidator(checkBoxValidator);
	}



}
