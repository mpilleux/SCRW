package cl.alma.scrw.format;

import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

/**
 * This class intends to format a table column that contains a time in miliseconds
 * the format is HH:MM:SS
 * @author Mauricio Pilleux
 *
 */
public class TimeColumnGenerator implements ColumnGenerator{

	private static final long serialVersionUID = -5983613956303540075L;

	@Override
	public Component generateCell(Table source, Object itemId, Object columnId) {
		
		Property prop = source.getItem(itemId).getItemProperty(columnId);
		
		if( prop != null && !(prop+"").equals("null") && prop.getType().equals( Long.class ) && prop.getValue() != null  )
		{
			long milis = (Long)prop.getValue();
			return this.format( milis );
		}
		return new Label( "" );
	}
	
	/**
	 * formats a long in miliseconds to a Label in hh:mm:ss
	 * @param milis = number in miliseconds to be formatted.
	 * @return the formated numeber.
	 */
	public Component format( long milis )
	{
		long hours = milis/(1000*60*60);
		milis = milis - hours*(1000*60*60);
		long minutes = milis/(1000*60);
		milis = milis - minutes*(1000*60);
		long seconds = milis/1000; 
		
		String time = "";
		if( hours < 10 )
			time+="0"+hours+":";
		else
			time+=hours+":";
		
		if( minutes < 10 )
			time+="0"+minutes+":";
		else
			time+=minutes+":";
		
		if( seconds < 10 )
			time+="0"+seconds;
		else
			time+=seconds;
		
		return new Label( time );
	}
	
}
