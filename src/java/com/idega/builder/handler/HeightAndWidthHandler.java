/*
 * $Id: HeightAndWidthHandler.java,v 1.8 2007/05/24 11:31:11 valdas Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.builder.handler;

import java.util.List;

import com.idega.core.builder.presentation.ICPropertyHandler;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.TextInput;

/**
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class HeightAndWidthHandler implements ICPropertyHandler {
  /**
   *
   */
  public HeightAndWidthHandler() {
  }

  /**
   *
   */
  public List getDefaultHandlerTypes() {
    return(null);
  }

  /**
   *
   */
  public PresentationObject getHandlerObject(String name, String value, IWContext iwc, boolean oldGenerationHandler, String instanceId, String method) {
    Table table = new Table();
    TextInput input = new TextInput(name);
    if (value != null) {
      input.setValue(value);
    }
    input.setMaxlength(4);
    input.setLength(4);
    table.add(input,1,1);

    return(table);
  }

  /**
   *
   */
  public void onUpdate(String values[], IWContext iwc) {
  }
}
