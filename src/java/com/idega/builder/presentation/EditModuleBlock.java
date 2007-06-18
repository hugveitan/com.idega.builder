package com.idega.builder.presentation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.idega.builder.business.BuilderConstants;
import com.idega.builder.business.BuilderLogic;
import com.idega.builder.business.ComponentPropertyComparator;
import com.idega.builder.business.IBPropertyHandler;
import com.idega.core.component.business.ComponentProperty;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Span;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.Heading3;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.ListItem;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Text;

public class EditModuleBlock extends Block {
	
	public EditModuleBlock() {
		setCacheable(getCacheKey(), 0);
	}
	
	public String getCacheKey() {
		return BuilderConstants.EDIT_MODULE_WINDOW_CACHE_KEY;
	}
	
	protected String getCacheState(IWContext iwc, String cacheStatePrefix) {
		String name = iwc.getParameter(BuilderConstants.MODULE_NAME);
		String instanceId = iwc.getParameter(BuilderConstants.IC_OBJECT_INSTANCE_ID_PARAMETER);
		String pageKey = iwc.getParameter(BuilderConstants.IB_PAGE_PARAMETER);

		return new StringBuffer(cacheStatePrefix).append(name).append(instanceId).append(pageKey).toString();
	}
	
	public void main(IWContext iwc) throws Exception {
		String name = iwc.getParameter(BuilderConstants.MODULE_NAME);
		String instanceId = iwc.getParameter(BuilderConstants.IC_OBJECT_INSTANCE_ID_PARAMETER);
		String pageKey = iwc.getParameter(BuilderConstants.IB_PAGE_PARAMETER);
		if (name == null || instanceId == null || pageKey == null) {
			return;
		}
		
		IWResourceBundle iwrb = BuilderLogic.getInstance().getBuilderBundle().getResourceBundle(iwc);
		
		List properties = getPropertyListOrdered(iwc, instanceId);
		
		// Header
		Layer header = new Layer();
		header.add(new Heading1(name));
		header.setId("editModuleHeader");
		this.add(header);
		
		// Menu
		Layer menu = new Layer();
		menu.setId("editModuleMenu");
		Lists navigation = new Lists();
		navigation.setId("editModuleMenuNavigation");
		
		ListItem settings = new ListItem();
		Link settingsLink = new Link(iwrb.getLocalizedString("settings", "Settings"), "#");
		settings.add(settingsLink);
		navigation.add(settings);
		
		/*ListItem settings2 = new ListItem();
		Link settings2Link = new Link(iwrb.getLocalizedString("settings2", "Settings2"), "#");
		settings2.add(settings2Link);
		navigation.add(settings2);*/
		menu.add(navigation);
		this.add(menu);
		
		this.add(new Break());
		
		Layer propertiesContainer = new Layer();
		addProperties(properties, propertiesContainer, iwrb, iwc, instanceId, pageKey);
		this.add(propertiesContainer);
	}
	
	@SuppressWarnings("unchecked")
	private List getPropertyListOrdered(IWContext iwc, String instanceId) throws Exception {
		List properties = IBPropertyHandler.getInstance().getComponentProperties(instanceId, iwc.getIWMainApplication(), iwc.getCurrentLocale(), true);
		java.util.Collections.sort(properties, ComponentPropertyComparator.getInstance());
		return properties;
	}
	
	private void addProperties(List properties, Layer container, IWResourceBundle iwrb, IWContext iwc, String instanceId, String pageKey) {
		if (properties == null) {
			return;
		}
		Object o = null;
		ComponentProperty property = null;
		List<ComponentProperty> simpleProperties = new ArrayList<ComponentProperty>();
		List<ComponentProperty> advancedProperties = new ArrayList<ComponentProperty>();
		for (int i = 0; i < properties.size(); i++) {
			o = properties.get(i);
			if (o instanceof ComponentProperty) {
				property = (ComponentProperty) o;
				if (property.isSimpleProperty()) {
					simpleProperties.add(property);
				}
				else {
					advancedProperties.add(property);
				}
			}
		}
		addPropertiesToContainer(simpleProperties, container, iwrb.getLocalizedString("simple_properties", "Simple Properties"), "simple_properties_box", null, false, instanceId, iwc, pageKey);		
		addPropertiesToContainer(advancedProperties, container, iwrb.getLocalizedString("advanced_properties", "Advanced Properties"), "advanced_properties_box", null, true, instanceId, iwc, pageKey);	
	}
	
	private void addPropertiesToContainer(List<ComponentProperty> properties, Layer parent, String name, String id, String className, boolean hidePropertiesList, String instanceId, IWContext iwc, String pageKey) {
		if (properties == null || parent == null || name == null) {
			return;
		}
		// Main container
		Layer container = new Layer();
		
		// Header
		Span header = new Span();
		header.add(new Heading3(name));
		container.add(header);
		container.setToolTip(name);
		
		if (id != null) {
			container.setId(id);
		}
		if (className != null) {
			container.setStyleClass(className);
		}
		
		//	Properties wrapper
		Layer propertiesWrapper = new Layer();
		
		// Properties container
		Layer propertiesContainer = new Layer();
		Random generator = new Random();
		int random = generator.nextInt(Integer.MAX_VALUE);
		String propertiesContainerId = new StringBuffer("propertiesContainerId").append(random).toString();
		propertiesContainer.setId(propertiesContainerId);
		header.setStyleClass("componentPropertiesListHeader");
		header.setOnClick(new StringBuffer("manageComponentPropertiesList('").append(propertiesContainerId).append("');").toString());
		if (hidePropertiesList) {
			propertiesContainer.setStyleAttribute("display: none;");
//			Script hide = new Script();
//			hide.addScriptLine(new StringBuffer("addPropertyIdAndClose('").append(propertiesContainerId).append("');").toString());
//			header.add(hide);
		}
		
		// Properties
		ComponentProperty property = null;
		Lists list = new Lists();
		list.setListOrdered(true);
		ListItem item = null;
		String itemStyle = "moduleProperty";
		String itemStyleSetProperty = "modulePropertyIsSet";
		String propertyId = null;
		Span propertyName = null;
		for (int i = 0; i < properties.size(); i++) {
			propertyId = new StringBuffer("property").append(generator.nextInt(Integer.MAX_VALUE)).toString();
			property = properties.get(i);
			item = new ListItem();
			item.setId(propertyId);
			if (BuilderLogic.getInstance().isPropertySet(pageKey, instanceId, property.getName(), iwc.getIWMainApplication())) {
				item.setStyleClass(itemStyleSetProperty);
			}
			else {
				item.setStyleClass(itemStyle);
			}
			propertyName = new Span(new Text(property.getDisplayName(iwc.getCurrentLocale())));
			propertyName.setOnClick(new StringBuffer("getPropertyBox('").append(propertyId).append("', '").append(property.getName()).append("', '").append(instanceId).append("');").toString());
			item.add(propertyName);
			list.add(item);
		}
		
		propertiesContainer.add(list);
		propertiesWrapper.add(propertiesContainer);
		propertiesWrapper.setId(new StringBuffer(propertiesContainer.getId()).append("Wrapper").toString());
		container.add(propertiesWrapper);
		parent.add(container);
	}

}