package cl.alma.scrw.bpmn.forms.validators;

import com.vaadin.data.Validator;
import com.vaadin.ui.CheckBox;

/**
 * This class is created to validate the antenna combo Box.
 * 
 * A valid antenna Combo Box is when at least one antenna is selected.
 * Asumming that the request must at least work with one antenna. 
 * 
 * @author Mauricio Pilleux
 *
 */
public class AntennaValidator implements Validator{


	private static final long serialVersionUID = 5591380526474310138L;

	private CheckBox[][] antennaVERTEX;
	
	private CheckBox[][] antennaAEM;
	
	private CheckBox[][] antennaMELCO12M;
	
	private CheckBox[][] antennaMELCO7M;
	
	public AntennaValidator( CheckBox[][] antennaVERTEX, CheckBox[][] antennaAEM, CheckBox[][] antennaMELCO12M, CheckBox[][] antennaMELCO7M )
	{
		this.antennaVERTEX = antennaVERTEX;
		this.antennaAEM = antennaAEM;
		this.antennaMELCO12M = antennaMELCO12M;
		this.antennaMELCO7M = antennaMELCO7M;
	}
	
	@Override
	/**
	 * Validate the antenna combobox
	 * The antenna combobox is valid iff there is at least one antenna selected.
	 * throws invalidValueException if the antenna combobox is not valid.
	 */
	public void validate(Object value) throws InvalidValueException {
		
		if( ! this.isValid( value ) )
			throw new InvalidValueException(
                "You must choose at leat one antenna.");
		
	}
	
	/**
	 * calls the validate method
	 * @return true if the form is valid
	 */
	public boolean isValid()
	{
		return isValid( null );
	}

	@Override
	/**
	 * Validate the antenna combobox
	 * The antenna combobox is valid iff there is at least one antenna selected.
	 * return true if antenna combobox is valid, else return false
	 */
	public boolean isValid(Object value) {
		for( int i = 0; i < this.antennaVERTEX.length; i++ )
			if( (Boolean)antennaVERTEX[i][0].getValue() || (Boolean)antennaVERTEX[i][1].getValue() )		
				return true;		
		
		for( int i = 0; i < this.antennaAEM.length; i++ )
			if( (Boolean)antennaAEM[i][0].getValue() || (Boolean)antennaAEM[i][1].getValue() )
				return true;	
		
		for( int i = 0; i < this.antennaMELCO12M.length; i++ )
			if( (Boolean)antennaMELCO12M[i][0].getValue() || (Boolean)antennaMELCO12M[i][1].getValue() )
				return true;	
		
		for( int i = 0; i < this.antennaMELCO7M.length; i++ )
			if( (Boolean)antennaMELCO7M[i][0].getValue() || (Boolean)antennaMELCO7M[i][1].getValue() )
				return true;	
		
		return false;
	}

}
