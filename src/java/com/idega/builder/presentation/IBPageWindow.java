/*
 * $Id: IBCreatePageWindow.java,v 1.34 2002/05/10 15:55:26 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.builder.presentation;

import com.idega.builder.business.BuilderLogic;
import com.idega.builder.business.IBPageHelper;
import com.idega.builder.business.IBPropertyHandler;
import com.idega.builder.data.IBDomain;
import com.idega.builder.data.IBPage;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;

/**
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
*/
public class IBPageWindow extends IWAdminWindow {
	
	
  protected static final String PAGE_NAME_PARAMETER   = "ib_page_name";
  protected static final String PAGE_CHOOSER_NAME     = IBPropertyHandler.PAGE_CHOOSER_NAME;
  protected static final String TEMPLATE_CHOOSER_NAME = IBPropertyHandler.TEMPLATE_CHOOSER_NAME;
  protected static final String PAGE_TYPE             = "ib_page_type";
  protected static final String IW_BUNDLE_IDENTIFIER  = "com.idega.builder";

  private static final String TOP_LEVEL = "top_level";

  public IBPageWindow() {
    setWidth(280);
    setHeight(180);
    setScrollbar(false);
  }


  /*
   *
   */
  protected IBPageChooser getPageChooser(String name, IWContext iwc) {
    IBPageChooser chooser = new IBPageChooser(name);
    chooser.setInputStyle(IWConstants.BUILDER_FONT_STYLE_INTERFACE);

    try {
      IBPage current = BuilderLogic.getInstance().getCurrentIBPageEntity(iwc);
      if (current.getType().equals(com.idega.builder.data.IBPageBMPBean.PAGE))
      	chooser.setSelectedPage(current.getID(),current.getName());
      else {
      	IBDomain domain = com.idega.builder.data.IBDomainBMPBean.getDomain(1);
      	IBPage top = domain.getStartPage();
      	if (top != null)
      	  chooser.setSelectedPage(top.getID(),top.getName());
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }

    return(chooser);
  }

  /**
   *
   */
  protected IBTemplateChooser getTemplateChooser(String name, IWContext iwc, String type){
    IBTemplateChooser chooser = new IBTemplateChooser(name);
    chooser.setInputStyle(IWConstants.BUILDER_FONT_STYLE_INTERFACE);

    try {
      String templateId = iwc.getParameter(TEMPLATE_CHOOSER_NAME);
      if (templateId == null || templateId.equals("")) {
      	IBPage current = BuilderLogic.getInstance().getCurrentIBPageEntity(iwc);
      	if (current.getType().equals(com.idega.builder.data.IBPageBMPBean.TEMPLATE))
      	  chooser.setSelectedPage(current);
      	else {
      	  if (type.equals(IBPageHelper.TEMPLATE)) {
	          IBDomain domain = com.idega.builder.data.IBDomainBMPBean.getDomain(1);
    	      IBPage top = domain.getStartTemplate();
    	      if (top != null)
	            chooser.setSelectedPage(top);
      	  }
      	}
      }
      else {
      	IBPage top = ((com.idega.builder.data.IBPageHome)com.idega.data.IDOLookup.getHomeLegacy(IBPage.class)).findByPrimaryKeyLegacy(Integer.parseInt(templateId));
      	if (top != null)
      	  chooser.setSelectedPage(top);
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    return chooser;
  }

  /**
   *
   */
  public String getBundleIdentifier() {
    return IW_BUNDLE_IDENTIFIER;
  }
}