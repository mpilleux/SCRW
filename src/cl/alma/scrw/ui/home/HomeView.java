package cl.alma.scrw.ui.home;

import com.github.peholmst.mvp4vaadin.navigation.ControllableView;

/**
 * This interface shows the necessary methods that HomeViewImpl will implement, and HomePresenter will call. 
 * 
 *  in this case, only the view_id is needed to identify this form in the form container.
 *
 */
public interface HomeView extends ControllableView {

	String VIEW_ID = "home";
}
