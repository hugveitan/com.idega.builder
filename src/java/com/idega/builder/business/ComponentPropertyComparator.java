package com.idega.builder.business;

import java.util.Comparator;

import com.idega.core.component.business.ComponentProperty;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class ComponentPropertyComparator implements Comparator<ComponentProperty>{

    public int compare(ComponentProperty p1, ComponentProperty p2){
      String s1 = p1.getDisplayName();
      String s2 = p2.getDisplayName();
      return s1.compareTo(s2);
    }


    public static ComponentPropertyComparator getInstance(){
      return new ComponentPropertyComparator();
    }

}
