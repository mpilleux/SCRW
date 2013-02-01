package cl.alma.scrw.cancel;

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
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
/**
 * This class represents the view implementation of a cancel ProcessInstanceView.
 * 
 * This class shows a list of active process instances (which can be cancelled) and allows the ability to cancel them.
 * 
 * This class implements CancelProcessView, and the corresponding presenter (controller) is CancelProcessPresenter.
 * 
 * @author Mauricio Pilleux
 *
 */
public class CancelProcessViewImpl extends
		AbstractDemoView<CancelProcessView, CancelProcessPresenter> implements
		CancelProcessView {

	
	private static final long serialVersionUID = 8460252522943724956L;

	private Table processTable;

	private BeanItemContainer<HistoricProcessInstanceTitle> dataSource;

	public CancelProcessViewImpl() {
		super(true);
	}

	@Override
	public String getDisplayName() {
		return "Cancel Process Instances";
	}

	@Override
	public String getDescription() {
		return "Cancel processes that are currently active";
	}

	@Override
	protected CancelProcessPresenter createPresenter() {
		return new CancelProcessPresenter(this);
	}

	@Override
	/**
	 * Shows the process list table.
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
		processTable.addGeneratedColumn("id", createNameColumnGenerator());
		getViewLayout().addComponent( refresh );
		getViewLayout().addComponent(processTable);
		getViewLayout().setExpandRatio(processTable, 1.0F);
	}

	@SuppressWarnings("serial")
	/**
	 * Creates a table column with the createCancelProcessInstancePopup for each element in the column.
	 * @return the table column with the cancelProcessInstance popup.
	 */
	private ColumnGenerator createNameColumnGenerator() {
		return new ColumnGenerator() {

			@Override
			public Component generateCell(Table source, Object itemId,
					Object columnId) {
				HistoricProcessInstance historicProcessInstance = (HistoricProcessInstance) itemId;
				PopupView popupView = createCancelProcessInstancePopup(historicProcessInstance);
				return popupView;
			}
		};
	}

	@SuppressWarnings("serial")
	/**
	 * Creates a popup that allows to cancel historicProcessInstance.
	 * To cancel the process, the popup calls cancelProcess method in the presenter.
	 * @param historicProcessInstance = the process instance that will have the popup
	 * @return a PopupView that allows historicProcessInstance to be cancelled.
	 */
	private PopupView createCancelProcessInstancePopup(
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

		Button startNewInstanceButton = new Button("Cancel Process");
		startNewInstanceButton.addStyleName(Reindeer.BUTTON_SMALL);
		startNewInstanceButton.addListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				getPresenter().cancelProcess( historicProcessInstance );
				popup.setPopupVisible(false);
			}
		});
		layout.addComponent(startNewInstanceButton);

		return popup;
	}

	@Override
	/**
	 * Sets a list of active process instances that can be cancelled.
	 * @param list = active process instance list to be set.
	 */
	public void setProcessInstances(List<HistoricProcessInstance> definitions) {
		dataSource.removeAllItems();
		for( HistoricProcessInstance historicProcessInstance : definitions )
		{
			HistoricProcessInstanceTitle historicProcessInstanceTitle = new HistoricProcessInstanceTitle( historicProcessInstance, "requestTitle" );		
			dataSource.addItem( historicProcessInstanceTitle );
		}
		//dataSource.addAll(definitions);
	}

	@Override
	/**
	 * Message to be show in a window when a process is cancelled successfully.
	 * @param historicProcessInstance = cancelled process.
	 */
	public void showProcessCanceled(
			HistoricProcessInstance historicProcessInstance) {
		getViewLayout().getWindow().showNotification(
				String.format("%s cancelled successfully", historicProcessInstance.getId()
						),
				Notification.TYPE_HUMANIZED_MESSAGE);
		
	}
	
}
