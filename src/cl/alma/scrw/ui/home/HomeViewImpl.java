package cl.alma.scrw.ui.home;

import cl.alma.scrw.cancel.CancelProcessView;
import cl.alma.scrw.history.HistoryView;
import cl.alma.scrw.instances.ActiveProcessInstanceView;
import cl.alma.scrw.ui.home.components.MainMenuItem;
import cl.alma.scrw.ui.processes.ProcessView;

import com.github.peholmst.mvp4vaadin.VaadinView;
import com.github.peholmst.mvp4vaadin.navigation.AbstractControllableView;
import com.github.peholmst.mvp4vaadin.navigation.ControllableView;
import com.github.peholmst.mvp4vaadin.navigation.ViewProvider;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * This class is the view implementation of HomeView.
 * 
 * this class intends to show the links to the main functionalities of the application.
 * 
 *  Every main functionality of the application MUST BE ADDED HERE in initView().
 *  
 *  The presenter associated to this class is HomeViewPresenter.
 *
 */
public class HomeViewImpl extends
		AbstractControllableView<HomeView, HomePresenter> implements HomeView,
		VaadinView 
		{

	private static final long serialVersionUID = -6441832004361841454L;

	private VerticalLayout viewLayout;

	private ViewProvider viewProvider;

	public HomeViewImpl(ViewProvider viewProvider) 
	{
		this.viewProvider = viewProvider;
		init();
	}

	@Override
	public String getDisplayName() 
	{
		return "Home";
	}

	@Override
	public String getDescription() 
	{
		return "The starting point of the SCRW";
	}

	@Override
	protected HomePresenter createPresenter() 
	{
		return new HomePresenter(this);
	}

	@Override
	public ComponentContainer getViewComponent() 
	{
		return viewLayout;
	}

	@Override
	/**
	 * Every item accessible through the home view must be added MANUALLY HERE.
	 * 
	 * shows the Home view.
	 */
	protected void initView() 
	{
		viewLayout = new VerticalLayout();
		viewLayout.setMargin(true);
		viewLayout.setSpacing(true);

		Label header = new Label("Welcome to SCRW!");
		header.addStyleName(Reindeer.LABEL_H1);
		viewLayout.addComponent(header);

		Label info = new Label(
				"This screen is the main menu for SCRW. What would you like to do?" );
		info.addStyleName( Reindeer.LABEL_SMALL );
		viewLayout.addComponent( info );

		addItemForView( viewProvider.getView( ProcessView.VIEW_ID ) );
		//addItemForView(viewProvider.getView(IdentityManagementView.VIEW_ID));
		addItemForView( viewProvider.getView( HistoryView.VIEW_ID ) );
		addItemForView( viewProvider.getView( ActiveProcessInstanceView.VIEW_ID ) );
		addItemForView( viewProvider.getView( CancelProcessView.VIEW_ID ) );
	}

	@SuppressWarnings("serial")
	/**
	 * Adds all the links to the view providers in the HomeView.
	 * 
	 * MainMenuItem is a VerticalLayout.
	 * @param view = current view.
	 */
	private void addItemForView( final ControllableView view ) 
	{
		MainMenuItem item = new MainMenuItem( view );
		item.addListener( new LayoutClickListener() 
		{
			@Override
			public void layoutClick(LayoutClickEvent event) 
			{
				getViewController().goToView( view );
			}
		});
		viewLayout.addComponent( item );
	}
}
