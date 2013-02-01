package cl.alma.scrw.diagram;

import java.io.InputStream;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.explorer.Constants;
import org.activiti.explorer.ui.util.InputStreamStreamSource;

import cl.alma.scrw.ui.util.AbstractDemoView;

import com.vaadin.Application;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.StreamResource.StreamSource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/** 
 * This class shows the bpmn diagram of a processDefinition.
 * 
 * This class implements DiagramView, and the corresponding presenter (controller) is DiagramPresenter.
 * 
 * @author Mauricio Pilleux
 *
 */
public class DiagramViewImpl extends
		AbstractDemoView<DiagramView, DiagramPresenter> implements
		DiagramView {

	private static final long serialVersionUID = 8630129746309155685L;
	 
	private Application application;
	
	private VerticalLayout processImageContainer;

	private ProcessDefinition processDefinition;

	public DiagramViewImpl( Application app ) 
	{
		this.application = app;
		init();
	}

	@Override
	public String getDisplayName() 
	{
		return "Process Diagram";
	}

	@Override
	public String getDescription() 
	{
		return "";
	}

	@Override
	protected void initView() 
	{
		super.initView();

		processImageContainer = new VerticalLayout();
		getViewLayout().addComponent( processImageContainer );
		//getViewLayout().setExpandRatio(processImageContainer, 1.0F);
	
		updateControls();
	}
	
	/**
	 * Creates the current processDefinition diagram and add it to the image container.
	 * This method uses the getResourceAsStream method in the repository Service.
	 * this methods can be found at the activiti explorer source code.
	 */
	protected void initImage()
	{
		StreamResource imageResource = null;
	    if( processDefinition != null && processDefinition.getDiagramResourceName() != null) 
	    {
	    	
		      final InputStream definitionImageStream = getRepositoryService().getResourceAsStream(
		        processDefinition.getDeploymentId(), processDefinition.getDiagramResourceName());
		    
		    StreamSource streamSource = new InputStreamStreamSource( definitionImageStream );
		      
		     
		    String imageExtension = extractImageExtension(processDefinition.getDiagramResourceName());
		    String fileName = processDefinition.getId() + "." + imageExtension;
		      
		    imageResource = new StreamResource( streamSource, fileName, this.application );
	     
	    }
	    
		Embedded embedded = new Embedded(null, imageResource);
	    embedded.setType(Embedded.TYPE_IMAGE);
	    embedded.setSizeUndefined();
		
		
		Panel imagePanel = new Panel();
	    imagePanel.addStyleName(Reindeer.PANEL_LIGHT);
	    imagePanel.setWidth(100, Sizeable.UNITS_PERCENTAGE);
	    imagePanel.setHeight(400, Sizeable.UNITS_PIXELS);
	    HorizontalLayout panelLayout = new HorizontalLayout();
	    panelLayout.setSizeUndefined();
	    imagePanel.setContent(panelLayout);
	    imagePanel.addComponent(embedded);
	    
	    processImageContainer.addComponent(imagePanel);
	}
	
	/**
	 * gets the image extension of diagramResourseName
	 * @param diagramResourceName = diagramResourceName whose image extension will be obtained.
	 * @return the diagramResourceName image extension
	 */
	protected String extractImageExtension( String diagramResourceName ) 
	{
	    String[] parts = diagramResourceName.split(".");
	    if( parts.length > 1 ) 
	    {
	      return parts[parts.length - 1];
	    }
	    return Constants.DEFAULT_DIAGRAM_IMAGE_EXTENSION;
	}

	@Override
	/**
	 * clears all data from the window.
	 */
	public void hideData() 
	{
		this.processDefinition = null;
		updateControls();
	}

	@Override
	/**
	 * sets the processDefinition whose data will be shown.
	 * @param processDefinition = process definition whose data will be shown
	 */
	public void setProcessDefinition( ProcessDefinition processDefinition ) 
	{
		this.processDefinition = processDefinition;
		updateControls();
	}
	
	@Override
	protected DiagramPresenter createPresenter() 
	{
		return new DiagramPresenter(this);
	}
	
	/**
	 * Initializes the data and displays it if a processDefinition is defined.
	 */
	private void updateControls()
	{
		this.updateHeaderLabel();
		this.processImageContainer.removeAllComponents();
		if( this.processDefinition != null )
		{
			initImage();
		}
	}
	
	private RepositoryService getRepositoryService() 
	{
		return ProcessEngines.getDefaultProcessEngine().getRepositoryService();
	}
	
}
