package cl.alma.scrw.ui.login;

import java.util.Hashtable;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import javax.naming.directory.*;

import cl.alma.scrw.bpmn.tasks.SystemCheckInconsistenciesServiceTask;

/**	
 * 
 * @author daniel
 *	This Class allows check the Authentication of an user on the OpenLDAP server
 *
 */
public class Authentication {
	
	/**
	 * static method that allow authenticate an user
	 * @param username of the user to authenticate
	 * @param password of the user to authenticate
	 * @param server ldap address of the server (e.g: ldap://ldapste01.sco.alma.cl)
	 * @param basedn of the ldap server (e.g: dc=alma,dc=info)
	 * @return this method returns true or false depending if the user could be authenticated or not
	 */
	
	static boolean authenticate(String username, String password, String server, String basedn) {
		Logger log = Logger.getLogger( Authentication.class
				.getName());
		
		try {
			log.log(Level.INFO, "trying to authenticate " + username);
			Properties props = new Properties();
			props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			props.put(Context.PROVIDER_URL, server);
			props.put(Context.REFERRAL, "ignore");
			props.put(Context.SECURITY_PRINCIPAL, dnFromUser(username, server, basedn));
			props.put(Context.SECURITY_CREDENTIALS, password);
			
			log.log(Level.INFO, "conecting to LDAP" );
			InitialDirContext context = new InitialDirContext(props);
			
			boolean result = context != null && ! context.equals( "" );
			
			if( result )
				return true;
			else
				return false;
		    }
		catch (NamingException e) {
			log.log(Level.INFO, "authentication failed");
			return false;
		}
	}

	private static String dnFromUser(String username, String server, String basedn) throws NamingException {
		Logger log = Logger.getLogger(SystemCheckInconsistenciesServiceTask.class
				.getName());
		try{
			log.log(Level.INFO, "trying at method dnFromUser");
			Properties props = new Properties();
		    props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		    props.put(Context.PROVIDER_URL, server);
		    props.put(Context.REFERRAL, "ignore");
		    
		    log.log(Level.INFO, "conecting to LDAP at method dnFromUser" );
		    InitialDirContext context = new InitialDirContext(props);
		    
		    SearchControls ctrls = new SearchControls();
		    ctrls.setReturningAttributes(new String[] { "givenName", "sn" });
		    ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		    
		    NamingEnumeration<SearchResult> answers = context.search(basedn, "(uid=" + username + ")", ctrls);
		    
		    SearchResult result = answers.next();
		    return result.getNameInNamespace();
	    }
	    catch(Exception e)
	    {
	    	log.log(Level.INFO, "method dnFromUser failed");
	    	return "";
	    }
	}
	
	/**
	 * ask LDAP for a list of users with mail.
	 * @return List of users from LDAP
	 */
	public static List<String> getUsers() {
		
		// QUERY (People that have an e-mail)
		String query = "(&(objectClass=person)(mail=*))";
		
		return getQuery( query, "uid" );
	}
	
	/**
	 * ask LDAP for username's email.
	 * @param username = the user whose mail will return this function
	 * @return username's email
	 */
	public static String getMail( String username ) 
	{
		String query = "(&(objectClass=person)(uid=" + username + "))";
		List<String> mailList = getQuery( query, "mail" );
		String mail = "";
		if( mailList.size() > 0 )
			mail = mailList.get(0);
		
		return mail;
	}

	/**
	 * ask LDAP if username belongs to group groupName
	 * @param username = name of the LDAP user which if going to be asked for
	 * @param groupName = name of the LDAP group where username is going to be asked for 
	 * @return true if username belongs to group groupName
	 */
	public static boolean groupContainsUser( String groupName, String username ) 
	{
		String query = "(&(objectClass=posixGroup)(cn="+ groupName +")(memberUid="+ username +"))";
		
		return getQuery( query, "memberUid" ).size() > 0 ;
		
	}
	
	/**
	 * ask LDAP for query.
	 * @param query = question asked to LDAP.
	 * @param attr = attribute that will be obtained from the LDAP query result.
	 * @return the LDAP result of query.
	 */
	private static List<String> getQuery( String query, String attr ) {
		
		try {
			String url = "ldap://ldapste01.osf.alma.cl/dc=alma,dc=info"; //URL to connect with LDAP in SCO
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, url);
			DirContext context = new InitialDirContext(env);
			
			SearchControls ctrl = new SearchControls();
	        ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
	        NamingEnumeration<SearchResult> enumeration = context.search("", query, ctrl);
	        
	        List<String> userList = new ArrayList<String>();
	        while (enumeration.hasMore()) {
	            SearchResult result = (SearchResult) enumeration.next();
	            Attributes attribs = result.getAttributes(); // Get attributes of Results
	            
	            /* QUERY RESULTS */
	            userList.add( ((BasicAttribute) attribs.get( attr ) ).get()+"" );
	            //System.out.print(((BasicAttribute) attribs.get("uid")).get() + ", ");
	            //System.out.print(((BasicAttribute) attribs.get("mail")).get() + "\n");
	        }
	        return userList;
	        
	        
		} catch (Exception e) {
			//e.printStackTrace();
			return new ArrayList<String>();
		}
	}
	
	
}