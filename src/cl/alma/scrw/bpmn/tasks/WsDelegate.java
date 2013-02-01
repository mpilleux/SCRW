package cl.alma.scrw.bpmn.tasks;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 * This class intends to call a webService using a dynamic client factory.
 * The parameters are set in the process xml.
 * @author Mauricio Pilleux
 *
 */
public class WsDelegate implements JavaDelegate {
	 
    private Expression wsdl;
 
    private Expression operation;
 
    private Expression parameters;
 
    private Expression returnValue;
 
    private Expression interceptor;
  
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void execute(DelegateExecution execution) throws Exception {
        String wsdlString = (String) wsdl.getValue(execution);
        
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        ArrayList<String> params = new ArrayList<String>();
        
        try {
        	Client client = dcf.createClient(wsdlString);
            
    		 if ( parameters != null ) 
    		 {
    			 StringTokenizer st = new StringTokenizer( (String)parameters.getValue(execution), ",");
    			 while ( st.hasMoreTokens() ) 
    			 {
    				 params.add(st.nextToken().trim());
    			 }
    		 }
            if (interceptor != null && interceptor.getValue(execution) != null) 
            {
                client.getEndpoint().getOutInterceptors().add((Interceptor) interceptor.getValue(execution));
            }
            Object[] response;
	   		if( params.size() > 0 )
	   			 response = client.invoke( (String)operation.getValue(execution), params.toArray( new Object[0] ) );
	   		else
	   			 response = client.invoke( (String)operation.getValue(execution), "" );
            if (returnValue != null) 
            {
                String returnVariableName = (String) returnValue.getValue(execution);
                execution.setVariable( returnVariableName, response[0] );
            }
        }
        catch (Exception e) 
        {
            if ( returnValue != null ) 
            {
            	String error = "<error>Error calling " +
                			"the web service in activity "+execution.getCurrentActivityName()+". " +
                					"Check logs for more details</error>";
            	Logger log = Logger.getLogger( this.getClass()
        				.getName());
        		log.log(Level.INFO, "Error calling " +
                		"the web service in activity "+execution.getCurrentActivityName()+".\n" +
                				" Web Service: " + wsdlString+" " +
                						"Operation: " + (String)operation.getValue(execution) + " " +
                						"Parameters: " + params.toArray( new Object[0] ) +". " +
                								"Please make sure the web services are running and parameters are correct."+" " +
                										"Exception: "+e);
        		
                String returnVariableName = (String) returnValue.getValue(execution);
                String variableValue = (String)execution.getVariable( "returnVariableName" );
                if( variableValue == null || variableValue.equals("null") || variableValue.equals("") )
                	execution.setVariable( returnVariableName, "<errors>"+error+"</errors>" );
                else
                {
                	variableValue.replaceAll("<errors>", "");
                	variableValue.replaceAll("</errors>", "");
                	variableValue.concat( error );
                }
            }
        }
    }
  
}