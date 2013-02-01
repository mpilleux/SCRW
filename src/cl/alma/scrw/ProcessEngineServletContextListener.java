package cl.alma.scrw;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.activiti.engine.ProcessEngines;


@WebListener
/**
 * This class intends to create to context to the application.
 * This includes anything related to adding data to the database; 
 * such as adding users to groups, adding users to the activiti identity Service,
 * deploying processes, etc.
 *
 * PROCESSES MUST BE ADDED MANUALLY HERE.
 */
public class ProcessEngineServletContextListener implements
		ServletContextListener {

	private static final Logger log = Logger
			.getLogger(ProcessEngineServletContextListener.class.getName());

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		//log.info("Destroying process engines");
		ProcessEngines.destroy();
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		log.info("Initializing process engines");
		ProcessEngines.init();
		//createGroupsIfNotPresent();
		//createAdminUserIfNotPresent();
		
		//deployProcesses();
	}

	/*private void createAdminUserIfNotPresent() {
		if (!isAdminUserPresent()) {
			createAdminUser();
		}
	}

	private void createGroupsIfNotPresent() {
		//if (!isGroupPresent("managers")) {
			//createGroup("managers", "Managers");
		//}
		if (!isGroupPresent("software")) {
			createGroup("software", "Software");
		}
		//if (!isGroupPresent("reporters")) {
			//createGroup("reporters", "Reporters");
		//}
	}

	private boolean isAdminUserPresent() {
		UserQuery query = getIdentityService().createUserQuery();
		query.userId("admin");
		return query.count() > 0;
	}

	private void createAdminUser() {
		//log.info("Creating an administration user with the username 'admin' and password 'passwordb'");
		User adminUser = getIdentityService().newUser("admin");
		adminUser.setFirstName("Arnold");
		adminUser.setLastName("Administrator");
		adminUser.setPassword("123");
		getIdentityService().saveUser(adminUser);
		
		User user = getIdentityService().newUser("Mauro");
		user.setPassword("123");
		user.setFirstName("Mauricio");
		user.setLastName("Pilleux");
		getIdentityService().saveUser(user);
		
		
		assignAdminUserToGroups();
	}

	private void assignAdminUserToGroups() {
		//getIdentityService().createMembership("admin", "managers");
		//getIdentityService().createMembership("Mauro", "managers");
		getIdentityService().createMembership("Mauro", "software");
	}

	private boolean isGroupPresent(String groupId) {
		GroupQuery query = getIdentityService().createGroupQuery();
		query.groupId(groupId);
		return query.count() > 0;
	}

	private void createGroup(String groupId, String groupName) {
		//log.log(Level.INFO,
			//	"Creating a group with the id '{1}' and name '{2}'",
				//new Object[] { groupId, groupName });
		Group group = getIdentityService().newGroup(groupId);
		group.setName(groupName);
		getIdentityService().saveGroup(group);
	}

	private IdentityService getIdentityService() 
	{
		return ProcessEngines.getDefaultProcessEngine().getIdentityService();
	}*/

	/**
	 * This function deploys the necessary proceses used in the application.
	 * 
	 *  Processes must be added MANUALLY HERE.
	 */
	//private void deployProcesses() {
		/*log.info("Deploying processes");
		RepositoryService repositoryService = ProcessEngines
				.getDefaultProcessEngine().getRepositoryService();
		
		
		repositoryService
		.createDeployment()
		.addClasspathResource(
				"com/github/peholmst/vaadinactivitidemo/bpmn/ProcessSCRW.bpmn")
		.deploy();*/
		
		/*repositoryService
		.createDeployment()
		.addClasspathResource(
				"com/github/peholmst/vaadinactivitidemo/bpmn/mail.bpmn")
		.deploy();*/
		
		/*repositoryService
		.createDeployment()
		.addClasspathResource(
				"com/github/peholmst/vaadinactivitidemo/bpmn/webServiceInconsistencies.bpmn")
		.deploy();*/
		
		/*repositoryService
		.createDeployment()
		.addClasspathResource(
				"com/github/peholmst/vaadinactivitidemo/bpmn/emailAttach.bpmn")
		.deploy();*/
		
		
	//}

}
