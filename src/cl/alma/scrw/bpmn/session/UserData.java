package cl.alma.scrw.bpmn.session;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is intended to store necessary data for session purposes.
 * This data includes username, name, lastname, and groups thaat the user belongs to. 
 * @author Mauricio Pilleux
 *
 */
public class UserData {
	
	private String username;
	
	private List<String> groups;
	
	private String name;
	
	private String lastName;
	
	public UserData()
	{
		this.username = "";
		
		this.groups = new ArrayList<String>();
	}
	
	public UserData( String username, String group )
	{
		 this.username = username;
		 
		 this.groups = new ArrayList<String>();
		 this.groups.add( group );
	}
	
	public UserData( String username, List<String> groups )
	{
		 this.username = username;
		 
		 this.groups = groups;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public List<String> getGroups()
	{
		return this.groups;
	}
	
	public void setUsername( String username )
	{
		this.username = username;
	}
	
	public void setGroups( List<String> groups )
	{
		this.groups = groups;
	}
	
	public void addGroup( String group )
	{
		this.groups.add( group );
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
	
}
