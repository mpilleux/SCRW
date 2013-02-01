package cl.alma.scrw.reports;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.history.HistoricProcessInstance;

import cl.alma.scrw.pdfexport.PdfFromComponent;
import cl.alma.scrw.ui.util.AbstractDemoView;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * This view intends to generate process reports.
 * 
 * A process report consist of a user report, a complete task report based on creation history.
 * and a task report based on unique task definitionKey.
 * 
 * All of this reports also contains a list of the antennas used in the process, 
 * the process request text, and the process comment. 
 * 
 * @author Mauricio Pilleux
 *
 */
public class ReportViewImpl extends
		AbstractDemoView<ReportView, ReportPresenter> implements
		ReportView {

	private static final long serialVersionUID = 7031376660983110484L;

	private TabSheet tabs;
	
	private TextArea[] request;
	
	private TextField[] title;
	
	private Table[] antennaTable;
	
	private VerticalLayout userDisplay;
	
	private VerticalLayout taskDisplay;
	
	private VerticalLayout taskDefinitionDisplay;
	
	private Panel display;
	
	private HistoricProcessInstance historicProcessInstance;
		
	private String tabTaskRName = "Task Report";
	
	private String tabUserRName = "User Report";
	
	private String tabUniqueTaskRName = "Unique Task Report";
	
	private VerticalLayout reportHeader;
	
	private ReportPopulator reportPopulator;

	public ReportViewImpl( ) 
	{
		init();
	}

	@Override
	public String getDisplayName() {
		return "Process report Browser";
	}

	@Override
	public String getDescription() {
		return "Generate process report.";
	}

	@Override
	/**
	 * initial view.
	 */
	protected void initView() {
		super.initView();

		display = new Panel();
		
		display.setSizeFull();
		display.addStyleName(Reindeer.PANEL_LIGHT);
		display.setScrollable( true );
		display.setSizeFull();
		getViewLayout().addComponent( display );
		getViewLayout().setExpandRatio( display, 1.0F);
		
		reportHeader = new VerticalLayout();
		display.addComponent( reportHeader );
		
		tabs = new TabSheet();
		tabs.setSizeFull();
		tabs.addStyleName(Reindeer.TABSHEET_MINIMAL);

		display.addComponent( tabs );
		
		//populates the tables with data
		updateControls();
	}
	
	/**
	 * populates the report view.
	 * The process report view consist of a user report, a complete task report based on creation history.
	 * and a task report based on unique task definitionKey.
	 * 
	 * All of this reports also contain a list of the antennas used in the process, 
	 * the process request text, and the process comment
	 */
	private void populate() 
	{
		/*userDisplay = new VerticalLayout();
		userDisplay.setMargin( true );
		userDisplay.setSpacing( true );
		
		taskDisplay = new VerticalLayout();
		taskDisplay.setMargin( true );
		taskDisplay.setSpacing( true );
		
		taskDefinitionDisplay = new VerticalLayout();
		taskDefinitionDisplay.setMargin( true );
		taskDefinitionDisplay.setSpacing( true );*/
		
		this.reportPopulator = new ReportPopulator( this.historicProcessInstance );
		
		this.request = new TextArea[3];
		this.title = new TextField[3];
		this.antennaTable = new Table[3];
		
		//HistoricVariableInstanceQuery variableQuery = getVariableQuery();
		
		/*List<String> antennaList = new ArrayList<String>();
		if( variableQuery != null && variableQuery.variableName( "antennaList" ).singleResult() != null )
			antennaList = ( List<String> ) variableQuery.variableName( "antennaList" ).singleResult().getValue();
		
		List<String> newAntennaList = new ArrayList<String>();
		if( variableQuery != null && variableQuery.variableName( "newAntennaList" ).singleResult() != null )
			newAntennaList = ( List<String> ) variableQuery.variableName( "newAntennaList" ).singleResult().getValue();*/
		
		
		for( int i = 0; i < request.length; i++ )
		{
			this.request[i] = this.reportPopulator.getRequest();
			this.title[i] = this.reportPopulator.getTitle();
			this.antennaTable[i] = this.reportPopulator.getAntennaTable();
			/*
			this.request[i].setColumns( 35 );
			this.request[i].setRows( 15 );
			
			if( variableQuery != null && variableQuery.variableName( "request" ).singleResult() != null )
				this.request[i].setValue( variableQuery.variableName( "request" ).singleResult().getValue() );
			this.request[i].setReadOnly( true );
			
			if( variableQuery != null && variableQuery.variableName( "comment" ).singleResult() != null )
				this.comment[i].setValue( variableQuery.variableName( "comment" ).singleResult().getValue() );
			this.comment[i].setReadOnly( true );
			
			this.antennaTable[i].addContainerProperty("Antenna", String.class,  null);
			this.antennaTable[i].addContainerProperty("New?", String.class,  null);
			this.antennaTable[i].setPageLength( antennaList.size() );*/
		}
		
		/*int count = 1;
		//populates antenna tables
		for( String antenna : antennaList )
		{
			if( newAntennaList.contains( antenna ) )
				for( int i = 0; i < request.length; i++ )
					antennaTable[i].addItem(new Object[] {
					    antenna, "yes" }, new Integer( count ) );
			else
				for( int i = 0; i < request.length; i++ )
					antennaTable[i].addItem(new Object[] {
					    antenna, "no" }, new Integer( count ) );
			count++;			
		}*/
		
		/*
		//populates userDisplay
		populateUserDisplay();
		
		//populates taskDisplay
		populateTaskDisplay();
		
		//populates taskDefinitionDisplay
		populateTaskDefinitionDisplay();*/
		
		this.userDisplay = this.reportPopulator.populateUserDisplay( request[0], antennaTable[0], title[0], true );
		this.taskDisplay = this.reportPopulator.populateTaskDisplay( request[1], antennaTable[1], title[1], true );
		this.taskDefinitionDisplay = this.reportPopulator.populateTaskDefinitionDisplay( request[2], antennaTable[2], title[2], true );
		
		taskDisplay.addComponent( generatePDFButton( taskDisplay, tabTaskRName, "Download this report page" ) );
		userDisplay.addComponent( generatePDFButton( userDisplay, tabUserRName, "Download this report page" ) );
		taskDefinitionDisplay.addComponent( generatePDFButton( taskDefinitionDisplay, tabUniqueTaskRName, "Download this report page" ) );
       
		List<Component> pdfAllComponent = new ArrayList<Component>();
		pdfAllComponent.add( taskDisplay );
		pdfAllComponent.add( userDisplay );
		pdfAllComponent.add( taskDefinitionDisplay );
		
		this.reportHeader.addComponent( generatePDFButton( pdfAllComponent, "Final Report", "Download report" ) );
		
		//add everything to the window
		tabs.addTab( taskDisplay, tabTaskRName, null );
		tabs.addTab( userDisplay, tabUserRName, null );
		tabs.addTab( taskDefinitionDisplay, tabUniqueTaskRName, null );

	}
	
	/**
	 * Creates a new PDF export button
	 * This button will export in pdf the componentList. The document title is title
	 * @param componentList = component list to be exported
	 * @param title = document title
	 * @param buttonText = The text displayed by the button
	 * @return the new PDF export button
	 */
	private Button generatePDFButton( final List<Component> componentList, final String title, String buttonText )
	{
		Button b = new Button( buttonText );
		
        b.addListener(new com.vaadin.ui.Button.ClickListener() 
        {
			private static final long serialVersionUID = 1L;

				@Override
                public void buttonClick( com.vaadin.ui.Button.ClickEvent event )
				{
                	PdfFromComponent factory = new PdfFromComponent();
                	factory.setTitulo( title );
                	factory.setFileName( "Title_proc"+historicProcessInstance.getId() );
                	//factory.setTempFile( "Title_proc"+historicProcessInstance.getId() );
                    factory.export( componentList );
                }
        });
        
        return b;
	}
	
	/**
	 * Creates a new PDF export button
	 * This button will export in pdf the component. The document title is title
	 * @param componen = component to be exported
	 * @param title = document title
	 * @param buttonText = The text displayed by the button
	 * @return the new PDF export button
	 */
	private Button generatePDFButton( Component component, String title, String buttonText )
	{
		List<Component> comp = new ArrayList<Component>();
		comp.add( component );
		return generatePDFButton( comp, title, buttonText );
	}

	@Override
	/**
	 * clears all data from the window.
	 */
	public void hideData()
	{
		this.historicProcessInstance = null;
		updateControls();
	}

	@Override
	/**
	 * sets the historicProcessInstance whose data will be shown. 
	 * @param historicProcessInstance = historic process instance whose data will be shown.
	 */
	public void setHistoricProcessInstance( HistoricProcessInstance historicProcessInstance ) 
	{
		this.historicProcessInstance = historicProcessInstance;
		updateControls();
	}
	
	@Override
	protected ReportPresenter createPresenter() 
	{
		return new ReportPresenter(this);
	}
	
	/**
	 * refreshes the window every time someone opens it.
	 */
	private void updateControls()
	{
		this.updateHeaderLabel();
		this.tabs.removeAllComponents();
		this.reportHeader.removeAllComponents();
		if( this.historicProcessInstance != null )
		{
			populate();
		}
	}		
}
