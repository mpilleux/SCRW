package cl.alma.scrw.ui.processes;

import java.io.File;
import java.util.List;

import org.activiti.engine.repository.ProcessDefinition;

import cl.alma.scrw.ui.util.AbstractDemoView;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;

/**
/**
 * This class represents the view implementation of a process view.
 * 
 * This class allows to start new instances on active process definitions, suspend active process definitons,
 * 
 * see activate process diagram, and activate suspended process defintions.
 * 
 * This class implements ProcessView, and the corresponding presenter (controller) is ProcessPresenter.
 * 
 * modified by Mauricio Pilleux
 *
 */
public class ProcessViewImpl extends
		AbstractDemoView<ProcessView, ProcessPresenter> implements
		ProcessView 
		{

	private static final long serialVersionUID = 8609961536309155685L;

	private final String pathToBPM = "/cl/alma/scrw/bpmn";
	
	private Table processTable;
	
	private Table suspendedProcessTable;
	
	private Table deployProcessTable;
	
	private TabSheet tabs;

	private BeanItemContainer<ProcessDefinition> dataSource;
	
	private BeanItemContainer<ProcessDefinition> dataSourceSuspended;

	public ProcessViewImpl() 
	{
		super(true);
	}

	@Override
	public String getDisplayName() 
	{
		return "Process Browser";
	}

	@Override
	public String getDescription() 
	{
		return "Browse processes and start new instances";
	}

	@Override
	protected ProcessPresenter createPresenter() 
	{
		return new ProcessPresenter(this);
	}

	@Override
	/**
	 * Initializes the view. 
	 */
	protected void initView() 
	{
		super.initView();
		
		Button refresh = new Button("Refresh");
		refresh.addStyleName(Reindeer.BUTTON_SMALL);
		refresh.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				getPresenter().init();
			}
		});
		
		getViewLayout().addComponent( refresh );
		
		tabs = new TabSheet();
		tabs.setSizeFull();
		tabs.addStyleName(Reindeer.TABSHEET_MINIMAL);

		getViewLayout().addComponent( tabs );
		getViewLayout().setExpandRatio(tabs, 1.0F);
		
		
		processTable = new Table();
		dataSource = new BeanItemContainer<ProcessDefinition>(
				ProcessDefinition.class);
		processTable.setContainerDataSource(dataSource);
		processTable.setVisibleColumns(new String[] { "name", "key", "version",
				"resourceName", "category" });
		processTable.setSizeFull();
		processTable.addGeneratedColumn("name", createNameColumnGenerator());
		
		
		suspendedProcessTable = new Table();
		dataSourceSuspended = new BeanItemContainer<ProcessDefinition>(
				ProcessDefinition.class);
		suspendedProcessTable.setContainerDataSource(dataSourceSuspended);
		suspendedProcessTable.setVisibleColumns(new String[] { "name", "key", "version",
				"resourceName", "category" });
		suspendedProcessTable.setSizeFull();
		suspendedProcessTable.addGeneratedColumn("name", createNameActivateColumnGenerator());
		
		deployProcessTable = new Table();
		deployProcessTable.addContainerProperty("Process", String.class,  null);
		
		final File folder = new File(Thread.currentThread(
			    ).getContextClassLoader(
			    ).getResource(
			    '/'+pathToBPM.replace(
			    '.', '/')).getFile() );
		 for (final File fileEntry : folder.listFiles()) {
		        if ( ! fileEntry.isDirectory() && fileEntry.getName().endsWith(".bpmn") ) {
		        	deployProcessTable.addItem(new Object[] { fileEntry.getName() }, 
		        			fileEntry.getName() );
		        }
		    }
		 deployProcessTable.addGeneratedColumn( "Process", createDeployColumnGenerator( pathToBPM ) );
		
		tabs.addTab( processTable, "Active Procesess", null );
		tabs.addTab(suspendedProcessTable, "Suspended Preocesses", null );
		tabs.addTab(deployProcessTable, "Deploy process", null );
		
	}

	@SuppressWarnings("serial")
	/**
	 * Adds a new start process definition popup for each element in the column
	 * This popup allows to start a new instance o suspend an active process..
	 * @return the column with the new start process definition popup.
	 */
	private ColumnGenerator createNameColumnGenerator() 
	{
		return new ColumnGenerator() 
		{

			@Override
			public Component generateCell(Table source, Object itemId,
					Object columnId)
			{
				ProcessDefinition processDefinition = (ProcessDefinition) itemId;
				PopupView popupView = createProcessDefinitionPopup(processDefinition);
				return popupView;
			}
		};
	}
	
	@SuppressWarnings("serial")
	/**
	 * Adds a new start process definition popup for each element in the column
	 * This popup allows to start a new instance o suspend an active process..
	 * @return the column with the new start process definition popup.
	 */
	private ColumnGenerator createDeployColumnGenerator( final String path ) 
	{
		return new ColumnGenerator() 
		{

			@Override
			public Component generateCell(Table source, Object itemId,
					Object columnId)
			{
				String fileName = (String) itemId;
				PopupView popupView = createProcessDeploymentPopup( fileName, path );
				return popupView;
			}
		};
	}
	
	@SuppressWarnings("serial")
	/**
	 * Adds a new activation process definition popup for each element in the column
	 * This popup allows to activate a suspended process.
	 * @return the column with the new activation process definition popup.
	 */
	private ColumnGenerator createNameActivateColumnGenerator() 
	{
		return new ColumnGenerator() 
		{
			@Override
			public Component generateCell(Table source, Object itemId,
					Object columnId) 
			{
				ProcessDefinition processDefinition = (ProcessDefinition) itemId;
				PopupView popupView = createActivateProcessDefinitionPopup(processDefinition);
				return popupView;
			}
		};
	}

	@SuppressWarnings("serial")
	/**
	 * Creates a new start process definition popup.
	 * This popup starts a new instance of the processDefinition when clicked. 
	 * This popup allows also to suspend the processDefinition.
	 * This popup allows to see the process diagram.
	 * @param processDefinition = processDefintion to be started or suspended
	 * @return the new start process definition popup
	 */
	private PopupView createProcessDefinitionPopup(
			final ProcessDefinition processDefinition) {
		final VerticalLayout layout = new VerticalLayout();
		final PopupView popup = new PopupView(processDefinition.getName(),
				layout);

		layout.setSizeUndefined();
		layout.setMargin(true);
		layout.setSpacing(true);
		Label header = new Label(String.format(
				"What would you like to do with <b>%s</b>?",
				processDefinition.getName()));
		header.setContentMode(Label.CONTENT_XHTML);
		layout.addComponent(header);

		Button startNewInstanceButton = new Button("Start a new instance");
		startNewInstanceButton.addStyleName(Reindeer.BUTTON_SMALL);
		startNewInstanceButton.addListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				getPresenter().startNewInstance( processDefinition );
				popup.setPopupVisible(false);
			}
		});
		
		Button suspendProcessButton = new Button("Suspend");
		suspendProcessButton.addStyleName(Reindeer.BUTTON_SMALL);
		suspendProcessButton.addListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				getPresenter().suspendProcess( processDefinition );
				popup.setPopupVisible(false);
			}
		});
		
		Button diagramButton = new Button("view diagram");
		diagramButton.addStyleName(Reindeer.BUTTON_SMALL);
		diagramButton.addListener(new Button.ClickListener() 
		{

			@Override
			public void buttonClick(ClickEvent event) 
			{
				getPresenter().gotoDiagram( processDefinition );
				popup.setPopupVisible(false);
			}
		});
		layout.addComponent( startNewInstanceButton );
		layout.addComponent( diagramButton );
		layout.addComponent( suspendProcessButton );
		
		return popup;
	}
	
	@SuppressWarnings("serial")
	/**
	 * Creates a new activation process definition popup.
	 * This popup activates the processDefinition when clicked. 
	 * @param processDefinition = processDefintion to be activated
	 * @return the new activation process definition popup
	 */
	private PopupView createActivateProcessDefinitionPopup(
			final ProcessDefinition processDefinition) 
	{
		final VerticalLayout layout = new VerticalLayout();
		final PopupView popup = new PopupView(processDefinition.getName(),
				layout);

		layout.setSizeUndefined();
		layout.setMargin(true);
		layout.setSpacing(true);
		Label header = new Label(String.format(
				"What would you like to do with <b>%s</b>?",
				processDefinition.getName()));
		header.setContentMode(Label.CONTENT_XHTML);
		layout.addComponent(header);

		Button activateButton = new Button("Activate process");
		activateButton.addStyleName(Reindeer.BUTTON_SMALL);
		activateButton.addListener(new Button.ClickListener() 
		{

			@Override
			public void buttonClick(ClickEvent event) 
			{
				getPresenter().activateProcess( processDefinition );
				popup.setPopupVisible(false);
			}
		});
		
		
		layout.addComponent( activateButton );
		
		return popup;
	}
	
	@SuppressWarnings("serial")
	/**
	 * Creates a new process deployment popup.
	 * This popup deploys the filename when clicked. 
	 * @param processDefinition = processDefintion to be activated
	 * @return the new activation process definition popup
	 */
	private PopupView createProcessDeploymentPopup(
			final String fileName, final String path ) 
	{
		final VerticalLayout layout = new VerticalLayout();
		final PopupView popup = new PopupView(fileName,
				layout);

		layout.setSizeUndefined();
		layout.setMargin(true);
		layout.setSpacing(true);
		Label header = new Label(String.format(
				"What would you like to do with <b>%s</b>?",
				fileName));
		header.setContentMode(Label.CONTENT_XHTML);
		layout.addComponent(header);

		Button deployButton = new Button("Deploy process");
		deployButton.addStyleName(Reindeer.BUTTON_SMALL);
		deployButton.addListener(new Button.ClickListener() 
		{

			@Override
			public void buttonClick(ClickEvent event) 
			{
				getPresenter().deployProcess( fileName, path );
				getPresenter().init();
				popup.setPopupVisible(false);
			}
		});
		
		
		layout.addComponent( deployButton );
		
		return popup;
	}

	@Override
	/**
	 * Sets the list of process definitions.
	 * This list of processes will be shown by the view.
	 * The ProcessPresented is in charge of stting this list.
	 * @param definitions = List of process definitions to be set in the view.
	 */
	public void setProcessDefinitions(List<ProcessDefinition> definitions) 
	{
		dataSource.removeAllItems();
		dataSource.addAll(definitions);
	}
	
	@Override
	/**
	 * Sets the list of suspended process definitions.
	 * This list will be set in one of the tabs in the view.
	 * The ProcessPresented is in charge of stting this list.
	 * @param definitions
	 */
	public void setSuspendedProcessDefinitions(List<ProcessDefinition> definitions) 
	{
		dataSourceSuspended.removeAllItems();
		dataSourceSuspended.addAll(definitions);
	}

	@Override
	/**
	 * Display a message to indicate process was started successfully. 
	 * @param process = process that started successfully.
	 */
	public void showProcessStartSuccess(ProcessDefinition process) 
	{
		getViewLayout().getWindow().showNotification(
				String.format("%s started successfully", process.getName()),
				Notification.TYPE_HUMANIZED_MESSAGE);
	}

	@Override
	/**
	 * Display a message to indicate process encountered an error when trying to deploy. 
	 * @param process = process that started unsuccessfully.
	 */
	public void showProcessStartFailure(ProcessDefinition process) 
	{
		getViewLayout()
				.getWindow()
				.showNotification(
						String.format(
								"Could not start %s. Please check the logs for more information.",
								process.getName()),
						Notification.TYPE_ERROR_MESSAGE);
	}
	
	/**
	 * Display a message to indicate that the suspension of a process was successful.
	 * @param process = suspended process.
	 */
	public void showProcessSuspendSuccess(ProcessDefinition process)
	{
		getViewLayout().getWindow().showNotification(
				String.format("%s suspended successfully", process.getName()),
				Notification.TYPE_HUMANIZED_MESSAGE);
	}
	
	/**
	 * Display a message to indicate that the suspension of a process was unsuccessful.
	 * @param process = non suspended process.
	 */
	public void showProcessSuspendFailure(ProcessDefinition process) 
	{
		getViewLayout()
				.getWindow()
				.showNotification(
						String.format(
								"Could not suspend %s. Please check the logs for more information.",
								process.getName()),
						Notification.TYPE_ERROR_MESSAGE);
	}
	
	/**
	 * Display a message to indicate that the activation of a process was successful.
	 * @param process = activated process.
	 */
	public void showProcessActivatedSuccess(ProcessDefinition process)
	{
		getViewLayout().getWindow().showNotification(
				String.format("%s activated successfully", process.getName()),
				Notification.TYPE_HUMANIZED_MESSAGE);
	}
	
	/**
	 * Display a message to indicate that the activation of a process was unsuccessful.
	 * @param process = non activated process.
	 */
	public void showProcessActivatedFailure(ProcessDefinition process) 
	{
		getViewLayout()
				.getWindow()
				.showNotification(
						String.format(
								"Could not activate %s. Please check the logs for more information.",
								process.getName()),
						Notification.TYPE_ERROR_MESSAGE);
	}
}
