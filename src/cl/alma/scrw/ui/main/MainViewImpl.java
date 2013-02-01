package cl.alma.scrw.ui.main;

import cl.alma.scrw.ui.home.HomeViewImpl;
import cl.alma.scrw.ui.main.components.WindowHeader;

import com.github.peholmst.mvp4vaadin.AbstractView;
import com.github.peholmst.mvp4vaadin.VaadinView;
import com.github.peholmst.mvp4vaadin.navigation.ViewProvider;
import com.github.peholmst.mvp4vaadin.navigation.ui.NavigationBar;
import com.github.peholmst.mvp4vaadin.navigation.ui.ViewContainerComponent;
import com.vaadin.Application;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.VerticalLayout;

/** 
 * This class shows the main view.
 * 
 * This class implements MainView, and the corresponding presenter (controller) is MainPresenter.
 *
 */
public class MainViewImpl extends AbstractView<MainView, MainPresenter>
		implements MainView, VaadinView 
		{

	private static final long serialVersionUID = -7679616805610225725L;

	private VerticalLayout viewLayout;

	private WindowHeader windowHeader;

	private NavigationBar navigationBar;

	private ViewContainerComponent viewContainer;

	private final Application application;

	//contains all the views (process view, taskview, unassigned task view, identity manager view, and user task form containter)
	private final ViewProvider viewProvider;
	
	

	public MainViewImpl(Application application, ViewProvider viewProvider) 
	{
		this.application = application;
		this.viewProvider = viewProvider;
		init();
	}

	@Override
	public String getDisplayName() 
	{
		return "SCRW";
	}

	@Override
	public String getDescription() 
	{
		return "";
	}

	@Override
	protected MainPresenter createPresenter() 
	{
		return new MainPresenter(this, application, viewProvider);
	}

	@Override
	/**
	 * initialices the view.
	 */
	protected void initView() 
	{
		viewLayout = new VerticalLayout();
		viewLayout.setSizeFull();

		windowHeader = createWindowHeader();
		viewLayout.addComponent(windowHeader);

		navigationBar = createNavigationBar();
		viewLayout.addComponent(navigationBar);

		viewContainer = createViewContainer();
		viewLayout.addComponent(viewContainer);
		viewLayout.setExpandRatio(viewContainer, 1.0F);

		createAndAddHomeView();
		
	}

	private WindowHeader createWindowHeader() 
	{
		return new WindowHeader( getPresenter() );
	}

	/**
	 * creates the navigation bar.
	 * This bar allows to go back steps in the application.
	 * @return the created navigation bar.
	 */
	private NavigationBar createNavigationBar() 
	{
		NavigationBar navigationBar = new NavigationBar();
		navigationBar.setViewController( getPresenter().getViewController() );
		navigationBar.addStyleName( "breadcrumbs" );
		navigationBar.setWidth( "100%" );
		return navigationBar;
	}

	/**
	 * creates the view container.
	 * @return the view container
	 */
	private ViewContainerComponent createViewContainer()
	{
		ViewContainerComponent viewContainer = new ViewContainerComponent();
		viewContainer.setViewController( getPresenter().getViewController() );
		viewContainer.setSizeFull();
		return viewContainer;
	}

	/**
	 * Adds the Homeview to the window.
	 */
	private void createAndAddHomeView()
	{
		HomeViewImpl homeView = new HomeViewImpl( viewProvider );
		getPresenter().getViewController().goToView( homeView );
	}

	@Override
	public ComponentContainer getViewComponent() 
	{
		return viewLayout;
	}

	@Override
	/**
	 * Sets the number of Unassigned Tasks that can be assigned by the current user.
	 * This are tasks assigned to a group.
	 * @param taskCount = number of unassigned tasks to set. 
	 */
	public void setNumberOfUnassignedTasks( long taskCount )
	{
		windowHeader.setNumberOfUnassignedTasks( taskCount );
	}

	@Override
	/**
	 * Sets the number of Tasks assigned to the current user.
	 * This are tasks assigned to the user.
	 * @param taskCount = number of tasks to set. 
	 */
	public void setNumberOfMyTasks( long taskCount ) 
	{
		windowHeader.setNumberOfMyTasks( taskCount );
	}

	@Override
	/**
	 * Sets the name of the current user.
	 * @param username = name of the current user. 
	 */
	public void setNameOfCurrentUser( String username ) 
	{
		windowHeader.setNameOfCurrentUser( username );
	}

}
