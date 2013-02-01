package cl.alma.scrw.instances;

import java.util.List;

import org.activiti.engine.history.HistoricProcessInstance;

import cl.alma.scrw.bpmn.session.HistoricProcessInstanceTitle;
import cl.alma.scrw.ui.util.AbstractDemoView;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
/**
 * This class represents the view implementation of an active process instance view.
 * 
 * This class shows a list of active process instances and allows the ability to see the process status.
 * 
 * This class implements ctiveProcessInstanceView, and the corresponding presenter (controller) is ctiveProcessInstancePresenter.
 * 
 * @author Mauricio Pilleux
 *
 */
public class ActiveProcessInstanceViewImpl extends
		AbstractDemoView<ActiveProcessInstanceView, ActiveProcessInstancePresenter> implements
		ActiveProcessInstanceView {

	
	private static final long serialVersionUID = 8460252522943724956L;

	private Table processTable;

	private BeanItemContainer<HistoricProcessInstanceTitle> dataSource;

	public ActiveProcessInstanceViewImpl() 
	{
		super(true);
	}

	@Override
	public String getDisplayName()
	{
		return "Active Process Instances";
	}

	@Override
	public String getDescription() 
	{
		return "Browse processes that are currently active";
	}

	@Override
	protected ActiveProcessInstancePresenter createPresenter() 
	{
		return new ActiveProcessInstancePresenter(this);
	}

	@Override
	/**
	 * initializes the view. 
	 * shows a list of active processInstance.
	 */
	protected void initView() {
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
		
		processTable = new Table();
		dataSource = new BeanItemContainer<HistoricProcessInstanceTitle>(
				HistoricProcessInstanceTitle.class);
		processTable.setContainerDataSource(dataSource);
		processTable.setVisibleColumns(new String[] { "id", "processDefinitionId", "title", "startUserId", "startTime"});
		processTable.setSizeFull();
		processTable.addGeneratedColumn("id", createNameColumnGenerator() );
		getViewLayout().addComponent( refresh );
		getViewLayout().addComponent(processTable);
		getViewLayout().setExpandRatio(processTable, 1.0F);
	}

	@SuppressWarnings("serial")
	/**
	 * creates a historicProcessInstance popup for each element in the column
	 * @return a column whose elements have a historicProcesInstance popup.
	 */
	private ColumnGenerator createNameColumnGenerator() {
		return new ColumnGenerator() {

			@Override
			public Component generateCell(Table source, Object itemId,
					Object columnId) {
				HistoricProcessInstance historicProcessInstance = (HistoricProcessInstance) itemId;
				PopupView popupView = createHistoricProcessInstancePopup(historicProcessInstance);
				return popupView;
			}
		};
	}	

	@SuppressWarnings("serial")
	/**
	 * creates a popup that allows to see the process status of historicProcesInstance.
	 * @param historicProcessInstance = process instance whose status will be available 
	 * @return the created popup
	 */
	private PopupView createHistoricProcessInstancePopup(
			final HistoricProcessInstance historicProcessInstance) {
		final VerticalLayout layout = new VerticalLayout();
		final PopupView popup = new PopupView(historicProcessInstance.getId(),
				layout);

		layout.setSizeUndefined();
		layout.setMargin(true);
		layout.setSpacing(true);
		Label header = new Label(String.format(
				"What would you like to do with process: <b>%s</b>?",
				historicProcessInstance.getId()));
		header.setContentMode(Label.CONTENT_XHTML);
		layout.addComponent(header);

		Button startNewInstanceButton = new Button("See process status");
		startNewInstanceButton.addStyleName(Reindeer.BUTTON_SMALL);
		startNewInstanceButton.addListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				getPresenter().setHistBrowser(historicProcessInstance);
				popup.setPopupVisible(false);
			}
		});
		layout.addComponent(startNewInstanceButton);

		return popup;
	}

	@Override
	/**
	 * Sets a list of active process instances to the view.
	 * @param list = active process instance list to be set.
	 */
	public void setProcessInstances(List<HistoricProcessInstance> definitions) 
	{
		dataSource.removeAllItems();
		for( HistoricProcessInstance historicProcessInstance : definitions )
		{
			HistoricProcessInstanceTitle historicProcessInstanceTitle = new HistoricProcessInstanceTitle( historicProcessInstance, "requestTitle" );		
			dataSource.addItem( historicProcessInstanceTitle );
		}
		//dataSource.addAll(definitions);
	}
	
}
