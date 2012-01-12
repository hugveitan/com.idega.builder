package com.idega.builder;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.ejb.FinderException;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.builder.app.IBApplication;
import com.idega.builder.business.BuilderSlideListenerBean;
import com.idega.builder.business.ComponentPropertyHandler;
import com.idega.builder.business.IBMainServiceBean;
import com.idega.builder.business.IBPropertyHandler;
import com.idega.builder.business.PageUrl;
import com.idega.builder.business.UserLoggedInListener;
import com.idega.builder.dynamicpagetrigger.data.DynamicPageTrigger;
import com.idega.builder.presentation.InvisibleInBuilder;
import com.idega.builder.view.BuilderApplicationViewNode;
import com.idega.builder.view.BuilderRootViewNode;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.accesscontrol.business.AuthenticationBusiness;
import com.idega.core.accesscontrol.business.StandardRoles;
import com.idega.core.builder.business.BuilderPageWriterService;
import com.idega.core.builder.business.BuilderService;
import com.idega.core.builder.data.ICDynamicPageTrigger;
import com.idega.core.builder.data.ICPage;
import com.idega.core.builder.data.ICPageHome;
import com.idega.core.view.ComponentClassViewNode;
import com.idega.core.view.DefaultViewNode;
import com.idega.core.view.FramedApplicationViewNode;
import com.idega.core.view.KeyboardShortcut;
import com.idega.core.view.ViewManager;
import com.idega.core.view.ViewNode;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.Applet;
import com.idega.presentation.GenericPlugin;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.repository.RepositoryService;
import com.idega.repository.data.ImplementorRepository;
import com.idega.repository.data.SingletonRepository;
import com.idega.util.CoreConstants;
import com.idega.util.expression.ELUtil;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jun 10, 2004
 */
public class IWBundleStarter implements IWBundleStartable {

	public static final String BUILDER_ROOT_VIEW_NODE_NAME = "pages";

	static Logger log = Logger.getLogger(IWBundleStarter.class.getName());

	@Autowired
	private RepositoryService repositoryService;

	@Override
	public void start(IWBundle starterBundle) {

		// implementors
		ImplementorRepository repository = ImplementorRepository.getInstance();

		repository.addImplementor(InvisibleInBuilder.class, Applet.class);
		repository.addImplementor(InvisibleInBuilder.class, GenericPlugin.class);
		repository.addImplementor(InvisibleInBuilder.class, DropdownMenu.class);

		repository.addImplementor(ICDynamicPageTrigger.class, DynamicPageTrigger.class);

		// services registration
		//IBOLookup.registerImplementationForBean(ICDomain.class, IBDomainBMPBean.class);
		//IBOLookup.registerImplementationForBean(ICPage.class, IBPageBMPBean.class);
		IBOLookup.registerImplementationForBean(BuilderService.class, IBMainServiceBean.class);
		IBOLookup.registerImplementationForBean(BuilderPageWriterService.class, IBMainServiceBean.class);

		//Registering the views:
		//This is the way it should be but doesn't work because of the startTemporaryBundleStarers() method in IWMainApplicationStarter
		/*if(starterBundle!=null){
			IWMainApplication iwma = starterBundle.getApplication();
			//IWMainApplication iwma = IWMainApplication.getDefaultIWMainApplication();
			ViewManager viewManager = ViewManager.getInstance(iwma);
			BuilderRootViewNode pagesViewNode = new BuilderRootViewNode(BUILDER_ROOT_VIEW_NODE_NAME,viewManager.getApplicationRoot());
			pagesViewNode.setKeyboardShortcut(new KeyboardShortcut("p"));
		}*/

		try {
			AuthenticationBusiness authentication = (AuthenticationBusiness) IBOLookup.getServiceInstance(starterBundle.getApplication().getIWApplicationContext(), AuthenticationBusiness.class);
			authentication.addAuthenticationListener(new UserLoggedInListener());
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}

		addViewNodes(starterBundle);

		updateBuilderPageUris();

		registerSlideListener(starterBundle);
	}

	/**
	 * <p>
	 * TODO tryggvil describe method addViewNodes
	 * </p>
	 */
	private void addViewNodes(IWBundle starterBundle) {
		//Registering the views:
		//FIXME: This is the way it should be but doesn't work because of the startTemporaryBundleStarers() method in IWMainApplicationStarter
		if(starterBundle!=null){
			IWMainApplication iwma = starterBundle.getApplication();
			ViewManager viewManager = ViewManager.getInstance(iwma);
			BuilderRootViewNode pagesViewNode = new BuilderRootViewNode(BUILDER_ROOT_VIEW_NODE_NAME, viewManager.getApplicationRoot());
			pagesViewNode.setKeyboardShortcut(new KeyboardShortcut("p"));

			ViewNode workspaceNode = viewManager.getWorkspaceRoot();

			Collection<String> roles = new ArrayList<String>();
			roles.add(StandardRoles.ROLE_KEY_BUILDER);

			DefaultViewNode builderAppNode = new DefaultViewNode(CoreConstants.BUILDER_APPLICATION, workspaceNode);
			builderAppNode.setName("#{localizedStrings['com.idega.builder']['builder']}");
			builderAppNode.setAuthorizedRoles(roles);
			builderAppNode.setVisibleInMenus(false);

			FramedApplicationViewNode builderNode = new FramedApplicationViewNode("builder", workspaceNode);
			builderNode.setAuthorizedRoles(roles);
			builderNode.setFaceletUri(starterBundle.getFaceletURI("builderapp.xhtml"));
			builderNode.setKeyboardShortcut(new KeyboardShortcut("2"));
			builderNode.setVisibleInMenus(false);

			DefaultViewNode setupNode = new DefaultViewNode("initialsetup",builderNode);
			setupNode.setFaceletUri(starterBundle.getFaceletURI("initialSetup.xhtml"));
			setupNode.setName("#{localizedStrings['com.idega.builder']['initialsetup']}");
			setupNode.setVisibleInMenus(false);

			DefaultViewNode applicationNode = new BuilderApplicationViewNode("application");
			applicationNode.setParent(builderNode);
			applicationNode.setVisibleInMenus(false);
			builderNode.setFrameUrl(applicationNode.getURI());

			ComponentClassViewNode afterinitialsetupNode = new ComponentClassViewNode("afterinitialsetup");
			afterinitialsetupNode.setParent(builderNode);
			afterinitialsetupNode.setComponentClass(IBApplication.class);
			afterinitialsetupNode.setVisibleInMenus(false);

		}
	}

	/**
	 * This method updates the ib_page table with creating a generated URI for all pages that had it set null.
	 */
	private void updateBuilderPageUris() {

		try {
			ICPageHome pHome = (ICPageHome)IDOLookup.getHome(ICPage.class);
			Collection<ICPage> pages = pHome.findAllPagesWithoutUri();
			int domainId=-1;
			for (Iterator<ICPage> iter = pages.iterator(); iter.hasNext();) {
				ICPage page = iter.next();
				//TODO: implemennt support for domainId:
				try{
					PageUrl newUrl = new PageUrl(page,domainId);
					String newGeneratedUri = newUrl.getGeneratedUrlFromName();
					page.setDefaultPageURI(newGeneratedUri);
					log.info("Updating Builder Page with uri="+newGeneratedUri);
					page.store();
				}
				catch(Exception e){
					log.throwing("IWBundleStarter","updateBuilderPageUris",e);
				}
			}

		}
		catch (IDOLookupException e) {
		}
		catch (FinderException e) {
			log.throwing("IWBundleStarter","updateBuilderPageUris",e);
		}

	}

	@Override
	public void stop(IWBundle starterBundle) {
		SingletonRepository repository = SingletonRepository.getRepository();
		repository.unloadInstance(ComponentPropertyHandler.class);
		repository.unloadInstance(IBPropertyHandler.class);
	}

	public void registerSlideListener(IWBundle bundle){
		// FIXME: This is the way it should be but doesn't work because of the startTemporaryBundleStarers() method in IWMainApplicationStarter
		if (bundle != null) {
			//add it as a repository change listener for caching purposes
			getRepositoryService().addRepositoryChangeListeners(new BuilderSlideListenerBean());
		}
	}

	private RepositoryService getRepositoryService() {
		if (repositoryService == null)
			ELUtil.getInstance().autowire(this);
		return repositoryService;
	}
}
