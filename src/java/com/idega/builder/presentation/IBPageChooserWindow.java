package com.idega.builder.presentation;

import com.idega.jmodule.object.interfaceobject.AbstractChooserWindow;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.interfaceobject.TreeViewer;
import com.idega.jmodule.object.textObject.Link;
import com.idega.builder.data.IBDomain;


/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @modified by <a href=teiki@idega.is">Eirikur Hrafnsson</a>
 * @version 1.0
 */

public class IBPageChooserWindow extends AbstractChooserWindow {

  public IBPageChooserWindow(){
    this.setName("Page Chooser");
    this.setWidth(300);
    this.setHeight(500);
    add("Select a page");
  }
/**
 * @todo get a treeviewer with the top page selected by default
 * with better implementation ibdomain...
 */
  public void displaySelection(ModuleInfo modinfo){
    try{
     // TreeViewer viewer = TreeViewer.getTreeViewerInstance(new com.idega.projects.golf.entity.Union(3),modinfo);
     //IBDomain domain = IBDomain.getDomain(1);
    //int i_page_id = domain.getStartPageID();
    int i_page_id = 1;

      TreeViewer viewer = TreeViewer.getTreeViewerInstance(new com.idega.builder.data.IBPage(i_page_id),modinfo);

      add(viewer);
      viewer.setToMaintainParameter(SCRIPT_PREFIX_PARAMETER,modinfo);
      viewer.setToMaintainParameter(SCRIPT_SUFFIX_PARAMETER,modinfo);
      viewer.setToMaintainParameter(DISPLAYSTRING_PARAMETER_NAME,modinfo);
      viewer.setToMaintainParameter(VALUE_PARAMETER_NAME,modinfo);

      Link prototype = new Link();
      viewer.setToUseOnClick();
      //sets the hidden input and textinput of the choosing page
      viewer.setOnClick(SELECT_FUNCTION_NAME+"("+viewer.ONCLICK_DEFAULT_NODE_NAME_PARAMETER_NAME+","+viewer.ONCLICK_DEFAULT_NODE_ID_PARAMETER_NAME+")");
    }
    catch(Exception e){
      e.printStackTrace();
    }

    /*Link link = new Link("tester");
    link.setURL("#");
    link.setOnClick(SELECT_FUNCTION_NAME+"('tester','tester')");
    add(link);*/
  }

}