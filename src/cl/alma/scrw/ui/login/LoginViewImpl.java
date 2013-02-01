package cl.alma.scrw.ui.login;

import com.github.peholmst.mvp4vaadin.AbstractView;
import com.github.peholmst.mvp4vaadin.VaadinView;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;

/**
 * This view intends to create a login GUI.
 * 
 * The login form consists of a username and a password.
 * 
 * The presenter associated with this class is LoginPresenter.
 * 
 */
public class LoginViewImpl extends AbstractView<LoginView, LoginPresenter>
		implements VaadinView, LoginView 
		{

	private static final long serialVersionUID = -4792685981025713834L;

	private HorizontalLayout viewLayout;
	private TextField username;
	private PasswordField password;
	private Button loginButton;
	private Button restartButton;

	public LoginViewImpl() 
	{
		super(true);
	}

	@Override
	public String getDisplayName() 
	{
		return "SCRW Login";
	}

	@Override
	public String getDescription() 
	{
		return "";
	}

	@Override
	protected LoginPresenter createPresenter() 
	{
		return new LoginPresenter(this);
	}

	@Override
	public ComponentContainer getViewComponent() 
	{
		return viewLayout;
	}

	@Override
	/**
	 * initializes the login form.
	 */
	protected void initView() 
	{
		VerticalLayout loginPanel = new VerticalLayout();
		loginPanel.setSpacing(true);
		loginPanel.setWidth("300px");

		Label header = new Label("Please login");
		header.addStyleName(Reindeer.LABEL_H1);
		loginPanel.addComponent(header);

		username = new TextField("Username");
		username.setWidth("100%");
		loginPanel.addComponent(username);

		password = new PasswordField("Password");
		password.setWidth("100%");
		loginPanel.addComponent(password);

		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(true);
		loginPanel.addComponent(buttons);
		loginPanel.setComponentAlignment(buttons, Alignment.MIDDLE_RIGHT);

		loginButton = new Button("Login");
		loginButton.setClickShortcut(KeyCode.ENTER);
		loginButton.addStyleName(Reindeer.BUTTON_DEFAULT);
		loginButton.addListener(createLoginButtonListener());
		buttons.addComponent(loginButton);

		restartButton = new Button("Restart");
		restartButton
				.setDescription("This button is only here for debugging purposes");
		restartButton.addListener(createRestartButtonListener());
		buttons.addComponent(restartButton);

		viewLayout = new HorizontalLayout();
		viewLayout.addComponent(loginPanel);
		viewLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
		viewLayout.setSizeFull();
		viewLayout.addStyleName(Reindeer.LAYOUT_BLACK);

		username.focus();
	}

	@SuppressWarnings("serial")
	/**
	 * creates a login button which will attempt to login to the application.
	 * @return the login button.
	 */
	private Button.ClickListener createLoginButtonListener() 
	{
		return new Button.ClickListener() 
		{
			@Override
			public void buttonClick(ClickEvent event) {
				getPresenter().attemptLogin((String) username.getValue(),
						(String) password.getValue());
			}
		};
	}

	@SuppressWarnings("serial")
	/**
	 * Creates a restart button which calls the clear form.
	 * @return the restart button
	 */
	private Button.ClickListener createRestartButtonListener() 
	{
		return new Button.ClickListener() 
		{
			@Override
			public void buttonClick(ClickEvent event) 
			{
				viewLayout.getApplication().close();
			}
		};
	}

	@Override
	/**
	 * Displays a login failed message in the current window.
	 */
	public void showLoginFailed() {
		viewLayout.getWindow().showNotification(
				"Login failed. Please try again.",
				Notification.TYPE_HUMANIZED_MESSAGE);
	}

	@Override
	/**
	 * Removes all user data (username, password) from the login form.
	 */
	public void clearForm() {
		username.setValue("");
		password.setValue("");
		username.focus();
	}

}
