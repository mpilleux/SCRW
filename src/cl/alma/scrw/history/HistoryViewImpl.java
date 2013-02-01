package cl.alma.scrw.history;

import java.util.List;

import org.activiti.engine.history.HistoricProcessInstance;

import cl.alma.scrw.bpmn.session.HistoricProcessInstanceTitle;
import cl.alma.scrw.format.TimeColumnGenerator;
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
 * This class represents the view implementation of a historic view.
 * 
 * This class shows a list of finished process instances and allows the ability to see the process history and its final report.
 * 
 * This class implements HistoryView, and the corresponding presenter (controller) is HistoryPresenter.
 * 
 * @author Mauricio Pilleux
 *
 */
public class HistoryViewImpl extends
		AbstractDemoView<HistoryView, HistoryPresenter> implements
		HistoryView {

	private static final long serialVersionUID = 8609961578309155685L;

	private Table processTable;

	private BeanItemContainer<HistoricProcessInstanceTitle> dataSource;

	public HistoryViewImpl() {
		super(true);
	}

	@Override
	public String getDisplayName() {
		return "History Browser";
	}

	@Override
	public String getDescription() {
		return "Browse processes that have already finished";
	}

	@Override
	protected HistoryPresenter createPresenter() {
		return new HistoryPresenter(this);
	}

	@Override
	/**
	 * displays a list of historicProcessInstances 
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
		processTable.setVisibleColumns(new String[] { "id", "processDefinitionId", "title", "startUserId", "startTime", "endTime", "durationInMillis"});
		processTable.setSizeFull();
		processTable.addGeneratedColumn("id", createNameColumnGenerator());
		processTable.addGeneratedColumn("durationInMillis", new TimeColumnGenerator() );
		getViewLayout().addComponent( refresh );
		getViewLayout().addComponent(processTable);
		getViewLayout().setExpandRatio(processTable, 1.0F);
	}

	@SuppressWarnings("serial")
	/**
	 * creates a historicProcessInstance popup for each element in the column.
	 * @return the column with historicProcessInstance popup in each element
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
	 * Creates a popup that allows to see the process history 
	 * and the final report of historicProcessInstance 
	 * @param historicProcessInstance = historicProcessInstance to be seen
	 * @return the popup of historicProcessInstance
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

		Button porcessHistory = new Button("See process history");
		porcessHistory.addStyleName(Reindeer.BUTTON_SMALL);
		porcessHistory.addListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				getPresenter().setHistBrowser(historicProcessInstance);
				popup.setPopupVisible(false);
			}
		});
		layout.addComponent(porcessHistory);
		
		Button finalReport = new Button("see final report");
		finalReport.addStyleName(Reindeer.BUTTON_SMALL);
		finalReport.addListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				getPresenter().setReportBrowser(historicProcessInstance);
				popup.setPopupVisible(false);
			}
		});
		layout.addComponent(finalReport);

		return popup;
	}

	@Override
	/**
	 * Sets a list of finished process instances to the view.
	 * @param list = finished process instance list to be set.
	 */
	public void setProcessInstances(List<HistoricProcessInstance> definitions) {
		dataSource.removeAllItems();
		
		for( HistoricProcessInstance historicProcessInstance : definitions )
		{
			HistoricProcessInstanceTitle historicProcessInstanceTitle = new HistoricProcessInstanceTitle( historicProcessInstance, "requestTitle" );		
			dataSource.addItem( historicProcessInstanceTitle );
		}
		
		//dataSource.addAll( definitions );
	}
	
	
	
}
