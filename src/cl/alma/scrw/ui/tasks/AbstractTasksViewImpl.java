package cl.alma.scrw.ui.tasks;

import java.util.List;

import org.activiti.engine.task.Task;

import cl.alma.scrw.bpmn.session.TaskTitle;
import cl.alma.scrw.ui.util.AbstractDemoView;

import com.vaadin.Application;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.themes.Reindeer;

/**
 * This class intends to implement the common features of task.
 * 
 * Task can be user tasks, or unassigned tasks.
 * 
 * Everytask has its own behavior, but all of the have one, which is activated by a popup.
 *
 * @param <V> view interface
 * @param <P> presenter
 */
public abstract class AbstractTasksViewImpl<V extends TasksView, P extends TasksPresenter<V>>
		extends AbstractDemoView<V, P> implements TasksView 
		{

	private static final long serialVersionUID = 2607026506045321251L;

	private Table taskTable;

	private BeanItemContainer<TaskTitle> dataSource;

	private final Application application;

	public AbstractTasksViewImpl(Application application) 
	{
		this.application = application;
		init();
	}

	@Override
	protected void initView() 
	{
		super.initView();
		taskTable = new Table();
		dataSource = new BeanItemContainer<TaskTitle>(TaskTitle.class);
		taskTable.setContainerDataSource( dataSource );
		taskTable.setVisibleColumns(getVisibleColumns());
		taskTable.setSizeFull();
		taskTable.addGeneratedColumn("name", createNameColumnGenerator() );
		getViewLayout().addComponent(taskTable);
		getViewLayout().setExpandRatio(taskTable, 1.0F);
	}

	@SuppressWarnings("serial")
	/**
	 * Creates a new popup for every name in the task table
	 * @return the column with the associated popup
	 */
	private ColumnGenerator createNameColumnGenerator() 
	{
		return new ColumnGenerator() 
		{
			@Override
			public Component generateCell(Table source, Object itemId,
					Object columnId)
			{
				Task task = (Task) itemId;
				PopupView popupView = createTaskPopup(task);
				return popupView;
			}
		};
	}
	
	//@SuppressWarnings("serial")
	/**
	 * Creates a new popup for every name in the task table
	 * @return the column with the associated popup
	 */
	/*private ColumnGenerator createTitleColumnGenerator() 
	{
		return new ColumnGenerator() 
		{
			@Override
			public Component generateCell(Table source, Object itemId,
					Object columnId)
			{
				 Property prop =
				            source.getItem( itemId ).getItemProperty( "id" );
				 TextArea title = new TextArea();
				 if ( prop !=null && prop.getType().equals( String.class ) ) 
				 {
					 String procInstanceId = getTaskQuery()
							 .taskId( (String)prop.getValue() )
							 .singleResult()
							 .getProcessInstanceId();
					 HistoricVariableInstance historicVariableInstance = getHistoricVariableInstanceQuery()
							 .processInstanceId( procInstanceId )
							 .variableName( "requestTitle" )
							 .singleResult();
					 
					 String procTitle = "";
					 if( historicVariableInstance != null )
						 procTitle = (String) historicVariableInstance.getValue();
					 
					 title.setValue( procTitle );
					 
				 }
				
				return title;
			}
		};
	}*/
	
	

	@Override
	public void setTasks(List<Task> tasks) 
	{
		dataSource.removeAllItems();
		
		for( Task task : tasks )
		{
			 TaskTitle taskTitle = new TaskTitle( task, "requestTitle" );			 
			 dataSource.addItem( taskTitle );
		}
		//dataSource.addAll(tasks);
	}

	@SuppressWarnings("serial")
	@Override
	protected void addAdditionalControlsToHeader( HorizontalLayout headerLayout )
	{
		Button refreshButton = new Button("Refresh");
		refreshButton.addStyleName(Reindeer.BUTTON_SMALL);
		refreshButton.addListener(new Button.ClickListener() 
		{
			@Override
			public void buttonClick(ClickEvent event) 
			{
				getPresenter().refreshTasks();
			}
		});
		headerLayout.addComponent(refreshButton);
		headerLayout.setComponentAlignment(refreshButton,
				Alignment.MIDDLE_RIGHT);
	}

	protected abstract PopupView createTaskPopup( final Task task );

	protected abstract String[] getVisibleColumns();

	protected final Application getApplication() 
	{
		return application;
	}
}
