package cl.alma.scrw.ui.main.components;

import cl.alma.scrw.ui.main.MainPresenter;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;

/**
 * This class intends to represent the header of the page.
 * 
 * This class contains the header title, and the navigation bar (tasks and logout).
 *
 */
public class WindowHeader extends HorizontalLayout implements RefreshListener
{

	private static final long serialVersionUID = 2604699348129000392L;
	private Label currentUserLabel;
	private Button logoutButton;
	private Button myTasksButton;
	private Button unassignedTasksButton;
	private Refresher refresher;
	private MainPresenter mainPresenter;
	private long numberOfUnassignedTasks = -1;
	private long numberOfMyTasks = -1;

	public WindowHeader(MainPresenter mainPresenter) 
	{
		this.mainPresenter = mainPresenter;

		setWidth("100%");
		setMargin(true);
		setSpacing(true);
		addStyleName(Reindeer.LAYOUT_BLACK);

		VerticalLayout title = createTitle();
		addComponent(title);
		setComponentAlignment(title, Alignment.MIDDLE_LEFT);
		setExpandRatio(title, 1.0F);

		myTasksButton = createMyTasksButton();
		addComponent(myTasksButton);
		setComponentAlignment(myTasksButton, Alignment.MIDDLE_RIGHT);

		unassignedTasksButton = createUnassignedTasksButton();
		addComponent(unassignedTasksButton);
		setComponentAlignment(unassignedTasksButton, Alignment.MIDDLE_RIGHT);

		logoutButton = createLogoutButton();
		addComponent(logoutButton);
		setComponentAlignment(logoutButton, Alignment.MIDDLE_RIGHT);

		refresher = createRefresher();
		addComponent(refresher);
	}

	/**
	 * Adds a header title.
	 * text add manually.
	 * @return the new header title.
	 */
	private VerticalLayout createTitle() 
	{
		VerticalLayout layout = new VerticalLayout();

		Label appTitle = new Label( "SCRW" );
		appTitle.addStyleName( Reindeer.LABEL_H1 );
		layout.addComponent( appTitle );

		currentUserLabel = new Label();
		layout.addComponent( currentUserLabel );

		return layout;
	}

	@SuppressWarnings("serial")
	/**
	 * Creates a new myTask button.
	 * This button displays the number of assigned tasks, which are tasks that 
	 * are assigned to the user.
	 * @return the new myTask button.
	 */
	private Button createMyTasksButton()
	{
		Button button = new Button();
		button.addListener(new Button.ClickListener() 
		{

			@Override
			public void buttonClick(ClickEvent event) 
			{
				mainPresenter.showMyTasks();
			}
		});
		button.addStyleName(Reindeer.BUTTON_SMALL);
		return button;
	}

	@SuppressWarnings("serial")
	/**
	 * Creates a new unaasignedTask button.
	 * This button displays the number of unassigned tasks, which are tasks that 
	 * are assigned to any of the user's groups.
	 * @return the new unaasignedTask button.
	 */
	private Button createUnassignedTasksButton() 
	{
		Button button = new Button();
		button.addListener(new Button.ClickListener() 
		{

			@Override
			public void buttonClick(ClickEvent event) 
			{
				mainPresenter.showUnassignedTasks();
			}
		});
		button.addStyleName(Reindeer.BUTTON_SMALL);
		return button;
	}

	@SuppressWarnings("serial")
	/**
	 * Creates a new logout button.
	 * the logout button calls the presenter to logout.
	 * @return the new logout button.
	 */
	private Button createLogoutButton() 
	{
		Button button = new Button("Logout");
		button.addListener(new Button.ClickListener() 
		{

			@Override
			public void buttonClick(ClickEvent event) 
			{
				mainPresenter.logout();
			}
		});
		button.addStyleName(Reindeer.BUTTON_SMALL);
		return button;
	}

	/**
	 * Creates a Refresher to the current window.
	 * @return the created refresher.
	 */
	private Refresher createRefresher() 
	{
		Refresher refresher = new Refresher();
		refresher.addListener(this);
		return refresher;
	}

	/**
	 * Sets the number of unassigned tasks to the current user to taskCount.
	 * An unassigned tasks is a task that
	 * if number of unassigned tasks to be set (taskCount) is greater than the previous number of 
	 * current user unassigned tasks, then send notification to the window.
	 * @param taskCount = number of tasks unassigned to the current user.
	 */
	public void setNumberOfUnassignedTasks( long taskCount ) 
	{
		unassignedTasksButton.setCaption(String.format("Unassigned tasks (%d)",
				taskCount));
		if (numberOfUnassignedTasks > -1 && numberOfUnassignedTasks < taskCount) {
			getWindow().showNotification("There are new unassigned tasks",
					Notification.TYPE_TRAY_NOTIFICATION);
		}
		numberOfUnassignedTasks = taskCount;
	}

	/**
	 * Sets the number of tasks assigned to the current user to taskCount
	 * if number of tasks to be set (taskCount) is greater than the previous number of 
	 * current user tasks, then send notification to the window.
	 * @param taskCount = number of tasks assigned to the current user.
	 */
	public void setNumberOfMyTasks( long taskCount )
	{
		myTasksButton.setCaption( String.format("My tasks (%d)", taskCount ) );
		if ( numberOfMyTasks > -1 && numberOfMyTasks < taskCount ) 
		{
			getWindow().showNotification("You have new tasks",
					Notification.TYPE_TRAY_NOTIFICATION);
		}
		numberOfMyTasks = taskCount;
	}

	/**
	 * Sets the name of the current user (username) in the header.
	 * @param username = user name to be set
	 */
	public void setNameOfCurrentUser(String username)
	{
		currentUserLabel.setContentMode( Label.CONTENT_XHTML );
		currentUserLabel.setValue(String.format(
				"Currently logged in as <b>%s</b>", username ) );
	}

	@Override
	public void refresh( Refresher source )
	{
		mainPresenter.refreshTaskCounters();
	}
}
