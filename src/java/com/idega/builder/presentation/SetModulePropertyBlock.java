package com.idega.builder.presentation;

import java.lang.reflect.Method;
import java.util.List;

import com.idega.builder.bean.PropertyHandlerBean;
import com.idega.builder.business.BuilderConstants;
import com.idega.builder.business.BuilderLogic;
import com.idega.builder.business.IBPropertyHandler;
import com.idega.core.builder.presentation.ICPropertyHandler;
import com.idega.core.component.business.ComponentProperty;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Text;
import com.idega.util.CoreConstants;
import com.idega.util.reflect.MethodFinder;

public class SetModulePropertyBlock extends Block {
	
	private boolean isMultiValue = false;
	
	public SetModulePropertyBlock() {
		setCacheable(getCacheKey());
	}
	
	public String getCacheKey() {
		return BuilderConstants.SET_MODULE_PROPERTY_CACHE_KEY;
	}
	
	protected String getCacheState(IWContext iwc, String cacheStatePrefix) {
		String pageKey = iwc.getApplicationAttribute(BuilderConstants.IB_PAGE_PARAMETER).toString();
		String propertyName = iwc.getApplicationAttribute(BuilderConstants.METHOD_ID_PARAMETER).toString();
		String instanceId = iwc.getApplicationAttribute(BuilderConstants.IC_OBJECT_INSTANCE_ID_PARAMETER).toString();

		return new StringBuffer(cacheStatePrefix).append(pageKey).append(propertyName).append(instanceId).toString();
	}
	
	public void main(IWContext iwc) throws Exception {
		String pageKey = iwc.getApplicationAttribute(BuilderConstants.IB_PAGE_PARAMETER).toString();
		String propertyName = iwc.getApplicationAttribute(BuilderConstants.METHOD_ID_PARAMETER).toString();
		String instanceId = iwc.getApplicationAttribute(BuilderConstants.IC_OBJECT_INSTANCE_ID_PARAMETER).toString();
		if (pageKey == null || propertyName == null || instanceId == null) {
			return;
		}
		
		Layer container = new Layer();
		
		IBPropertiesWindowSetter setter = new IBPropertiesWindowSetter();
		
		Class<?> presObjClass = null;
		int icObjectInstanceIDint = BuilderLogic.getInstance().getIBXMLReader().getICObjectInstanceIdFromComponentId(instanceId, null, pageKey);
		if (icObjectInstanceIDint == -1) {
			presObjClass = com.idega.presentation.Page.class;
		}
		else {
			presObjClass = BuilderLogic.getInstance().getObjectClass(icObjectInstanceIDint);
		}
		String namePrefix = "ib_property_";
	
		MethodFinder methodFinder = MethodFinder.getInstance();
		Class<?> parameters[] = null;
		boolean isMethodIdentifier = false;
		boolean needsReload = doesPropertyNeedReload(instanceId, propertyName, iwc);
		if (methodFinder.isMethodIdentifier(propertyName)) {
			Method method = MethodFinder.getInstance().getMethod(propertyName, presObjClass);
			parameters = method.getParameterTypes();
			isMethodIdentifier = true;
		}
		else{
			parameters = new Class[]{String.class};
		}
		isMultiValue = parameters.length == 1 ? false : true;
		boolean isChangingProperty = setter.isChangingProperty(iwc);
		String selectedValues[] = setter.parseValues(iwc);
		String paramDescriptions[] = IBPropertyHandler.getInstance().getPropertyDescriptions(iwc, instanceId, propertyName);
		String realValues[] = BuilderLogic.getInstance().getPropertyValues(iwc.getIWMainApplication(), pageKey, instanceId, propertyName, selectedValues, !isChangingProperty);
		String value = BuilderConstants.EMPTY;
		String name = null;
		String paramDescription = null;
		String handlerClass = null;
		Class<?> parameterClass = null;
		Text description = null;
		for (int i = 0; i < parameters.length; i++) {
			parameterClass = parameters[i];
			value = BuilderConstants.EMPTY;
			try {
				value = realValues[i];
			}
			catch (ArrayIndexOutOfBoundsException e) {
			}
			catch (NullPointerException npe) {
			}
			
			name = new StringBuffer(namePrefix).append(i).toString();
			handlerClass = null;
			if (isMethodIdentifier) {
				handlerClass = IBPropertyHandler.getInstance().getMethodParameterProperty(iwc, instanceId, propertyName, i,
						IBPropertyHandler.METHOD_PARAMETER_PROPERTY_HANDLER_CLASS);
			}
			
			if (isMultiValue) {
				paramDescription = paramDescriptions[i];
				if (paramDescription != null) {
					description = setter.formatDescription(new StringBuffer(paramDescription).append(":").toString());
					description.setFontStyle("font-family:Arial,Helvetica,sans-serif;font-size:8pt;");
					container.add(description);
				}
			}
			
			PresentationObject handlerBox = IBPropertyHandler.getInstance().getPropertySetterComponent(iwc,	new PropertyHandlerBean(instanceId,
					propertyName, name, value, CoreConstants.BUILDER_PORPERTY_SETTER_STYLE_CLASS, parameterClass, i, needsReload, isMultiValue, parameters.length));
			
			handlerBox.setMarkupAttribute("jsfcomponent", IBPropertyHandler.getInstance().isJsfComponent(iwc, presObjClass.getName()));
			container.add(handlerBox);
			
			ICPropertyHandler handler = null;
			if (handlerClass != null && !handlerClass.equals(BuilderConstants.EMPTY)) {
				handler = IBPropertyHandler.getInstance().getPropertyHandler(handlerClass);
				if (handler != null) {
					iwc.setSessionAttribute(CoreConstants.HANDLER_PARAMETER, handler);
				}
			}
			if (handler == null) {
				iwc.removeSessionAttribute(CoreConstants.HANDLER_PARAMETER);
			}
		}
		
		this.add(container);
	}
	
	public boolean isMultiValue() {
		return isMultiValue;
	}
	
	public String getBundleIdentifier() {
		return BuilderConstants.IW_BUNDLE_IDENTIFIER;
	}
	
	private boolean doesPropertyNeedReload(String instanceId, String propertyName, IWContext iwc) {
		if (instanceId == null || propertyName == null || iwc == null) {
			return false;
		}
		
		List<ComponentProperty> properties = IBPropertyHandler.getInstance().getComponentProperties(instanceId, iwc.getIWMainApplication(), iwc.getCurrentLocale());
		if (properties == null) {
			return false;
		}
		
		ComponentProperty property = null;
		for (int i = 0; i < properties.size(); i++) {
			property = properties.get(i);
			if (propertyName.equals(property.getName())) {
				return property.isNeedsReload();
			}
		}
		
		return false;
	}

}