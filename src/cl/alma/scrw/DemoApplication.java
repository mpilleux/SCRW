package cl.alma.scrw;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.activiti.engine.ProcessEngines;

import cl.alma.scrw.bpmn.forms.AcknowledgeCheckDoneForm;
import cl.alma.scrw.bpmn.forms.AcknowledgePageForm;
import cl.alma.scrw.bpmn.forms.AcknowledgeRestartDoneForm;
import cl.alma.scrw.bpmn.forms.ExecutionFinishedPageForm;
import cl.alma.scrw.bpmn.forms.NewRequestForm;
import cl.alma.scrw.bpmn.forms.ReviewPageForm;
import cl.alma.scrw.bpmn.forms.SetUpFinishedPageForm;
import cl.alma.scrw.bpmn.forms.SoftConfForm;
import cl.alma.scrw.bpmn.forms.informCancelForm;
import cl.alma.scrw.bpmn.session.UserData;
import cl.alma.scrw.bpmn.tasks.SystemCheckInconsistenciesServiceTask;
import cl.alma.scrw.cancel.CancelProcessView;
import cl.alma.scrw.cancel.CancelProcessViewImpl;
import cl.alma.scrw.diagram.DiagramView;
import cl.alma.scrw.diagram.DiagramViewImpl;
import cl.alma.scrw.history.HistoryDataView;
import cl.alma.scrw.history.HistoryDataViewImpl;
import cl.alma.scrw.history.HistoryView;
import cl.alma.scrw.history.HistoryViewImpl;
import cl.alma.scrw.instances.ActiveProcessInstanceView;
import cl.alma.scrw.instances.ActiveProcessInstanceViewImpl;
import cl.alma.scrw.instances.ProcessStatusView;
import cl.alma.scrw.instances.ProcessStatusViewImpl;
import cl.alma.scrw.reports.ReportView;
import cl.alma.scrw.reports.ReportViewImpl;
import cl.alma.scrw.ui.forms.UserFormView;
import cl.alma.scrw.ui.forms.UserFormViewImpl;
import cl.alma.scrw.ui.identity.IdentityManagementView;
import cl.alma.scrw.ui.identity.IdentityManagementViewImpl;
import cl.alma.scrw.ui.login.Authentication;
import cl.alma.scrw.ui.login.LoginViewImpl;
import cl.alma.scrw.ui.login.UserLoggedInEvent;
import cl.alma.scrw.ui.main.MainViewImpl;
import cl.alma.scrw.ui.main.UserLoggedOutEvent;
import cl.alma.scrw.ui.processes.ProcessView;
import cl.alma.scrw.ui.processes.ProcessViewImpl;
import cl.alma.scrw.ui.tasks.MyTasksView;
import cl.alma.scrw.ui.tasks.MyTasksViewImpl;
import cl.alma.scrw.ui.tasks.UnassignedTasksView;
import cl.alma.scrw.ui.tasks.UnassignedTasksViewImpl;
import cl.alma.scrw.ui.util.UserTaskFormContainer;

import com.github.peholmst.mvp4vaadin.ViewEvent;
import com.github.peholmst.mvp4vaadin.ViewListener;
import com.github.peholmst.mvp4vaadin.navigation.DefaultViewProvider;
import com.vaadin.Application;
import com.vaadin.ui.UriFragmentUtility;
import com.vaadin.ui.UriFragmentUtility.FragmentChangedEvent;
import com.vaadin.ui.UriFragmentUtility.FragmentChangedListener;
import com.vaadin.ui.Window;

/**
 * 
 * This class intends to create the basic objects necessary to run the application.
 * 
 * This includes the initial login window, the formContainer, and the view provider.
 * 
 * This class also handles the event of a user logged in, and creates the application with the logged in user.
 * 
 * This is the class where FORMS, and VIEWS are added MANUALLY. 
 *
 */
public class DemoApplication extends Application implements ViewListener {
	
	private static final long serialVersionUID = -9012126232401480280L;

	private LoginViewImpl loginView;

	private MainViewImpl mainView;

	private DefaultViewProvider viewProvider;

	private UserTaskFormContainer userTaskFormContainer;
	
	private final UriFragmentUtility urifu = new UriFragmentUtility();
	
	private final HashMap<String, String> URItoView = new HashMap<String, String>();
	
	@Override
	public void init() {
		setTheme("SCRW");
		createAndShowLoginWindow();
	}
	
	/**
	 * The fisrt view of the application.
	 * This view shows the login window by calling the loginViewImpl.
	 */
	private void createAndShowLoginWindow() {
		loginView = new LoginViewImpl();
		loginView.addListener( this );
		Window loginWindow = new Window( loginView.getDisplayName(),
				loginView.getViewComponent() );
		setMainWindow(loginWindow);
	}
	
	/**
	 * After the UseroggedInEvent occurs, the application removes the login window 
	 * and initiates the application with a logged in user.  
	 */
	private void createAndShowMainWindow() {
		createAndInitViewProvider();
		loginView.removeListener(this);//stop listening to the login view
		mainView = new MainViewImpl( this, viewProvider );
		mainView.addListener(this);
		// Remove old login window
		removeWindow(getMainWindow());
		// Set new main window
		final Window mainWindow = new Window( mainView.getDisplayName(),
				mainView.getViewComponent() );
		setMainWindow( mainWindow );
		
		mainWindow.addComponent( urifu );
				
		 String fragment =
                urifu.getFragment();
		if( fragment != null )
		{
			String ViewId = URItoView.get( fragment );
			if( ViewId != null && !ViewId.equals("") )
			{
				mainView.getPresenter().getViewController().goToView( ViewId );
				urifu.setFragment("");
			}
		}
			
		
		urifu.addListener(new FragmentChangedListener() {
		   private static final long serialVersionUID = 1L;

			public void fragmentChanged(FragmentChangedEvent source) {
				
		        String fragment =
		                  source.getUriFragmentUtility().getFragment();
		        
		        if (fragment != null)
		        {
		        	String ViewId = URItoView.get( fragment );
					if( ViewId != null && !ViewId.equals("") )
					{
						mainView.getPresenter().getViewController().goToView( ViewId );
						source.getUriFragmentUtility().setFragment("");
					}
		        }
		    }
		});
	}
	
	/**
	 * Every view must be added here.
	 * views added here are available throughout the entire application
	 * 
	 * views follow the MVP patter (you must add an interface, a view implementation and a view presenter)
	 */
	private void createAndInitViewProvider() {
		createAndInitUserTaskFormContainer();
		viewProvider = new DefaultViewProvider();
		viewProvider.addPreinitializedView(new MyTasksViewImpl(this),
				MyTasksView.VIEW_ID);
		URItoView.put("myTasks", MyTasksView.VIEW_ID);
		
		viewProvider.addPreinitializedView(new UnassignedTasksViewImpl(this),
				UnassignedTasksView.VIEW_ID);
		URItoView.put("unassignedTasks", UnassignedTasksView.VIEW_ID );
		
		viewProvider.addPreinitializedView(new ProcessViewImpl(),
				ProcessView.VIEW_ID);
		URItoView.put("processes", ProcessView.VIEW_ID );
		
		viewProvider.addPreinitializedView(new HistoryViewImpl(),
				HistoryView.VIEW_ID);
		URItoView.put("history", HistoryView.VIEW_ID );
		
		//not used in the application due to LDAP replacement
		viewProvider.addPreinitializedView(new IdentityManagementViewImpl(),
				IdentityManagementView.VIEW_ID);
		
		viewProvider.addPreinitializedView(new UserFormViewImpl(
				userTaskFormContainer), UserFormView.VIEW_ID);
		
		viewProvider.addPreinitializedView(new HistoryDataViewImpl(),
				HistoryDataView.VIEW_ID);
		
		viewProvider.addPreinitializedView(new ActiveProcessInstanceViewImpl(),
				ActiveProcessInstanceView.VIEW_ID);
		URItoView.put("activeProcesses", ActiveProcessInstanceView.VIEW_ID );
		
		viewProvider.addPreinitializedView(new ProcessStatusViewImpl(this),
				ProcessStatusView.VIEW_ID);
		
		viewProvider.addPreinitializedView(new DiagramViewImpl(this),
				DiagramView.VIEW_ID);
		
		viewProvider.addPreinitializedView(new ReportViewImpl(),
				ReportView.VIEW_ID);
		URItoView.put("reports", ReportView.VIEW_ID );
		
		viewProvider.addPreinitializedView(new CancelProcessViewImpl(),
				CancelProcessView.VIEW_ID);
		URItoView.put("cancel", CancelProcessView.VIEW_ID );
		
	}
	
	/**
	 * here we add MANUALLY all of the proceses forms.
	 * The form can be found at package com.github.peholmst.vaadinactivitidemo.bpmn.forms
	 * 
	 * The forms are available throughout the entire application.
	 */
	private void createAndInitUserTaskFormContainer() {
		userTaskFormContainer = new UserTaskFormContainer();
		userTaskFormContainer.registerForm(NewRequestForm.FORM_KEY, NewRequestForm.class);
		userTaskFormContainer.registerForm(AcknowledgePageForm.FORM_KEY, AcknowledgePageForm.class);
		userTaskFormContainer.registerForm(SetUpFinishedPageForm.FORM_KEY, SetUpFinishedPageForm.class);
		userTaskFormContainer.registerForm(ReviewPageForm.FORM_KEY, ReviewPageForm.class);
		userTaskFormContainer.registerForm(ExecutionFinishedPageForm.FORM_KEY, ExecutionFinishedPageForm.class);
		userTaskFormContainer.registerForm(SoftConfForm.FORM_KEY, SoftConfForm.class);
		userTaskFormContainer.registerForm(AcknowledgeRestartDoneForm.FORM_KEY, AcknowledgeRestartDoneForm.class);
		userTaskFormContainer.registerForm(AcknowledgeCheckDoneForm.FORM_KEY, AcknowledgeCheckDoneForm.class);
		userTaskFormContainer.registerForm(informCancelForm.FORM_KEY, informCancelForm.class);
	}

	@Override
	public void close() {
		ProcessEngines.getDefaultProcessEngine().getIdentityService()
				.setAuthenticatedUserId(null);
		super.close();
	}

	@Override
	public void handleViewEvent(ViewEvent event) {
		Logger log = Logger.getLogger(SystemCheckInconsistenciesServiceTask.class
				.getName());
		log.log(Level.INFO, "handleViewEvent: ");
		
		if (event instanceof UserLoggedInEvent) {
			String username = ((UserLoggedInEvent) event).getUsername();
			UserData user = new UserData();
			user.setUsername( username );
			
			if( username.equals("mpilleux"))//for testing purposes
				user.addGroup("software");
			else if( Authentication.groupContainsUser( "software", username ) )//adds user to software.
				user.addGroup("software");
			setUser( user );
			createAndShowMainWindow();
		} else if (event instanceof UserLoggedOutEvent) {
			close();
		}
	}

}
