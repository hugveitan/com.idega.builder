/*
 * $Id: IBTemplateChooser.java,v 1.1 2001/09/13 17:38:17 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.builder.presentation;

import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.Image;

/**
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.3
 */
public class IBTemplateChooser extends AbstractChooser {
  /**
   *
   */
  public IBTemplateChooser(String name) {
    addForm(false);
    setChooseButtonImage(new Image("/common/pics/arachnea/toolbar_open_1.gif","Choose"));
    setChooserParameter(name);
  }

  /**
   *
   */
  public Class getChooserWindowClass() {
    return(IBTemplateChooserWindow.class);
  }
}