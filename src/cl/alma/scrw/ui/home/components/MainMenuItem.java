package cl.alma.scrw.ui.home.components;

import com.github.peholmst.mvp4vaadin.navigation.ControllableView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * This class represents the menu items in Home.
 * 
 * a menu items contians a DisplayName, and a Description.
 * 
 */
public class MainMenuItem extends VerticalLayout {

	private static final long serialVersionUID = 8544361205036297095L;

	private final ControllableView view;

	public MainMenuItem(ControllableView view)
	{
		this.view = view;
		initComponents();
	}

	/**
	 * Adds a view with its description.
	 */
	private void initComponents() 
	{
		setMargin(true);
		setSizeUndefined();
		Label nameLabel = new Label( view.getDisplayName() );
		nameLabel.addStyleName( Reindeer.LABEL_H2 );
		addComponent( nameLabel );

		Label descrLabel = new Label(view.getDescription());
		addComponent(descrLabel);

		addStyleName("mainMenuItem");
	}
}
