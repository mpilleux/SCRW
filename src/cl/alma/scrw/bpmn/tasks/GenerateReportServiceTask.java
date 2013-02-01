package cl.alma.scrw.bpmn.tasks;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.history.HistoricProcessInstance;

import cl.alma.scrw.pdfexport.PdfFromComponent;
import cl.alma.scrw.reports.ReportPopulator;

import com.vaadin.Application;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * This class intends to create a process final report and store it in the web server.
 * 
 * This class uses the same class that creates a web final report.
 * @author Mauricio Pilleux
 *
 */
public class GenerateReportServiceTask extends Application implements JavaDelegate {
	
	private static final long serialVersionUID = -1086057698514664675L;
	private VerticalLayout layout;
	
	private final boolean NODELETE = false;
	
	@Override
	public void execute( DelegateExecution execution )
	{
		Logger log = Logger.getLogger(SystemCheckInconsistenciesServiceTask.class
				.getName());
		
		this.init();
    	String procInstanceId = execution.getProcessInstanceId();
    	
    	populateLayout( procInstanceId );
    	
		PdfFromComponent factory = new PdfFromComponent();
    	factory.setTitulo( "Final Report");
    	factory.setFileName( "Title_proc" );
		
		//create pdf
        factory.export( layout, NODELETE );
        
        File reportFile = factory.getTempFile();
        log.log(Level.INFO, "Report file stored at: "+ reportFile.getAbsolutePath() );
        
        execution.setVariable( "finalReportPath", reportFile.getAbsolutePath() );
                
	}
	
	private void populateLayout( String procInstanceId )
	{
		HistoricProcessInstance historicProcessInstance = getHistoryService()
    			.createHistoricProcessInstanceQuery().processInstanceId( procInstanceId )
    			.singleResult();
    	
    	
    	ReportPopulator reportPopulator = new ReportPopulator( historicProcessInstance );
    	
    	TextArea request = reportPopulator.getRequest();
		TextField title = reportPopulator.getTitle();
		Table antennaTable = reportPopulator.getAntennaTable();
		
		VerticalLayout taskDisplay = reportPopulator.populateTaskDisplay( request, antennaTable, title, true );
		VerticalLayout userDisplay = reportPopulator.populateUserDisplay( request, antennaTable, title, false );
		VerticalLayout taskDefinitionDisplay = reportPopulator.populateTaskDefinitionDisplay( request, antennaTable, title, false );
		
		this.layout.addComponent( taskDisplay );
		this.layout.addComponent( userDisplay );
		this.layout.addComponent( taskDefinitionDisplay );
		
	}

	@Override
	public void init() {
		// First create the main window.
        final Window main = new Window ("Report creation");
        setMainWindow(main);
         
        layout = new VerticalLayout();
        
        // Add some content to the window.
        main.addComponent( layout );
		
	}
	
	private HistoryService getHistoryService() 
	{
		return ProcessEngines.getDefaultProcessEngine().getHistoryService();
	}
	
	
}
