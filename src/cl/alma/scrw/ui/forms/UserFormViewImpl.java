package cl.alma.scrw.ui.forms;


import cl.alma.scrw.ui.util.AbstractDemoView;
import cl.alma.scrw.ui.util.UserTaskForm;
import cl.alma.scrw.ui.util.UserTaskFormContainer;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.Panel;

/**
 * This view intends to generate activiti form views.
 * 
 * The form contains all necessary data to implement a specific userTask.
 * 
 * This class implements UserFormView, and the corresponding presenter (controller) is UserFormPresenter.
 *
 */
public class UserFormViewImpl extends
		AbstractDemoView<UserFormView, UserFormPresenter> implements
		UserFormView 
		{

	private static final long serialVersionUID = 3741153612537481922L;

	private final UserTaskFormContainer userTaskFormContainer;

	private Button submitButton;
	
	private TextArea description;

	private Panel formContainerLayout;

	private UserTaskForm currentForm;

	public UserFormViewImpl(UserTaskFormContainer userTaskFormContainer) 
	{
		this.userTaskFormContainer = userTaskFormContainer;
		init();
	}

	@SuppressWarnings("serial")
	@Override
	/**
	 * initial view.
	 */
	protected void initView() 
	{
		super.initView();

		this.description = new TextArea();
		getViewComponent().addComponent( description );
		this.description.setRows( 2 );
		this.description.setColumns( 50 );
		this.description.setReadOnly( true );
		
		formContainerLayout = new Panel();
		formContainerLayout.setSizeFull();
		formContainerLayout.addStyleName(Reindeer.PANEL_LIGHT);
		getViewLayout().addComponent(formContainerLayout);
		getViewLayout().setExpandRatio(formContainerLayout, 1.0F);

		submitButton = new Button("Submit Form");
		submitButton.addListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (currentForm != null) {
					getPresenter().submitForm(currentForm);
				}
			}
		});
		getViewComponent().addComponent(submitButton);
		submitButton.setVisible(false);
	}

	@Override
	public String getDisplayName() 
	{
		return currentForm != null ? currentForm.getDisplayName()
				: "No form available";
	}

	@Override
	public String getDescription() 
	{
		return currentForm != null ? currentForm.getDescription()
				: "There is no form to show";
	}

	@Override
	protected UserFormPresenter createPresenter() 
	{
		return new UserFormPresenter(this, userTaskFormContainer);
	}

	@Override
	/**
	 * sets the form whose data will be shown. 
	 * @param form = form whose data will be shown.
	 */
	public void setForm(UserTaskForm form) {
		currentForm = form;
		updateControls();
	}

	@Override
	/**
	 * clears all data from the window (removes current form).
	 */
	public void hideForm() {
		currentForm = null;
		updateControls();
	}
	
	/**
	 * refreshes the window.
	 */
	private void updateControls() 
	{
		updateHeaderLabel();
		submitButton.setVisible(currentForm != null);
		formContainerLayout.removeAllComponents();
		if (currentForm != null) 
		{
			formContainerLayout.addComponent(currentForm.getFormComponent());
			
			if( currentForm.getDescription() != null)
			{
				this.description.setVisible( true );
				this.description.setReadOnly( false );
				this.description.setValue( currentForm.getDescription() );
				this.description.setReadOnly( true );
			}
			else
				this.description.setVisible( false );
		}
	}
}
