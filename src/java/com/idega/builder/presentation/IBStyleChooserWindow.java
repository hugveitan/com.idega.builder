package com.idega.builder.presentation;

import com.idega.presentation.text.Paragraph;
import com.idega.block.text.business.TextFormatter;
import com.idega.builder.business.BuilderLogic;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWConstants;
import com.idega.presentation.ui.AbstractChooserWindow;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Iterator;
import com.idega.util.text.StyleConstants;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class IBStyleChooserWindow extends AbstractChooserWindow {

private HashMap _styleMap;
private String _styleString;
private String[] _styles = StyleConstants.ALL_STYLES;

  public IBStyleChooserWindow() {
    setDefaultValues();
    setScrollbar(true);
    setWidth(370);
    setHeight(350);
  }

  public void displaySelection(IWContext iwc) {
    addTitle("Style chooser",IWConstants.BUILDER_FONT_STYLE_TITLE);

    if ( iwc.getParameter("style") != null ) {
       _styleString = iwc.getParameter("style");
       setMapStyles();
    }

    collectStyles(iwc);

    if ( iwc.isParameterSet("submit") ) {
      getParentPage().setOnLoad(SELECT_FUNCTION_NAME+"('"+_styleString+"','"+_styleString+"')");
    }
    else {
      drawForm(iwc);
    }
  }

  private void drawForm(IWContext iwc) {
    IWResourceBundle iwrb = iwc.getApplication().getBundle(BuilderLogic.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);

    Form form = new Form();
    Table formTable = new Table(1,3);
      formTable.setAlignment(1,3,Table.HORIZONTAL_ALIGN_RIGHT);
      formTable.setCellpadding(5);
      formTable.setCellspacing(0);
      formTable.setWidth(Table.HUNDRED_PERCENT);

    Table leftTable = new Table(5,7);
      leftTable.mergeCells(2,1,5,1);
      leftTable.setWidth(3,Table.HUNDRED_PERCENT);
      leftTable.setColumnAlignment(1,Table.HORIZONTAL_ALIGN_RIGHT);
      leftTable.setColumnAlignment(4,Table.HORIZONTAL_ALIGN_RIGHT);
      leftTable.setWidth(3,"16");
    formTable.add(leftTable,1,1);

    int row = 1;

    DropdownMenu fontFamily = new DropdownMenu(StyleConstants.ATTRIBUTE_FONT_FAMILY);
      fontFamily.addMenuElementFirst("","");
      fontFamily.addMenuElement(StyleConstants.FONT_FAMILY_ARIAL,StyleConstants.FONT_FAMILY_ARIAL);
      fontFamily.addMenuElement(StyleConstants.FONT_FAMILY_COURIER,StyleConstants.FONT_FAMILY_COURIER);
      fontFamily.addMenuElement(StyleConstants.FONT_FAMILY_GENEVA,StyleConstants.FONT_FAMILY_GENEVA);
      fontFamily.addMenuElement(StyleConstants.FONT_FAMILY_GEORGIA,StyleConstants.FONT_FAMILY_GEORGIA);
      fontFamily.addMenuElement(StyleConstants.FONT_FAMILY_TIMES,StyleConstants.FONT_FAMILY_TIMES);
      fontFamily.addMenuElement(StyleConstants.FONT_FAMILY_VERDANA,StyleConstants.FONT_FAMILY_VERDANA);
      fontFamily.setSelectedElement(getStyleValue(StyleConstants.ATTRIBUTE_FONT_FAMILY));
      fontFamily.setToSubmit();
      fontFamily.setStyleAttribute(IWConstants.BUILDER_FONT_STYLE_INTERFACE+"width:100%;");
    Text fontFamilyText = new Text("Font family:");
      fontFamilyText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
    leftTable.add(fontFamilyText,1,row);
    leftTable.add(fontFamily,2,row);
    row++;

    DropdownMenu fontSize = new DropdownMenu(StyleConstants.ATTRIBUTE_FONT_SIZE);
      fontSize.addMenuElementFirst("","");
      for ( int a = 1; a <= 72; a++ ) {
	fontSize.addMenuElement(Integer.toString(a)+"px",Integer.toString(a)+"px");
      }
      fontSize.setSelectedElement(getStyleValue(StyleConstants.ATTRIBUTE_FONT_SIZE));
      fontSize.setToSubmit();
      fontSize.setStyleAttribute(IWConstants.BUILDER_FONT_STYLE_INTERFACE);
    Text fontSizeText = new Text("Size:");
      fontSizeText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
    leftTable.add(fontSizeText,1,row);
    leftTable.add(fontSize,2,row);
    row++;

    DropdownMenu fontStyle = new DropdownMenu(StyleConstants.ATTRIBUTE_FONT_STYLE);
      fontStyle.addMenuElementFirst("","");
      fontStyle.addMenuElement(StyleConstants.FONT_STYLE_NORMAL,StyleConstants.FONT_STYLE_NORMAL);
      fontStyle.addMenuElement(StyleConstants.FONT_STYLE_ITALIC,StyleConstants.FONT_STYLE_ITALIC);
      fontStyle.addMenuElement(StyleConstants.FONT_STYLE_OBLIQUE,StyleConstants.FONT_STYLE_OBLIQUE);
      fontStyle.setSelectedElement(getStyleValue(StyleConstants.ATTRIBUTE_FONT_STYLE));
      fontStyle.setToSubmit();
      fontStyle.setStyleAttribute(IWConstants.BUILDER_FONT_STYLE_INTERFACE);
    Text fontStyleText = new Text("Style:");
      fontStyleText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
    leftTable.add(fontStyleText,1,row);
    leftTable.add(fontStyle,2,row);
    row++;

    DropdownMenu fontWeight = new DropdownMenu(StyleConstants.ATTRIBUTE_FONT_WEIGHT);
      fontWeight.addMenuElementFirst("","");
      fontWeight.addMenuElement(StyleConstants.FONT_WEIGHT_NORMAL,StyleConstants.FONT_WEIGHT_NORMAL);
      fontWeight.addMenuElement(StyleConstants.FONT_WEIGHT_BOLD,StyleConstants.FONT_WEIGHT_BOLD);
      fontWeight.addMenuElement(StyleConstants.FONT_WEIGHT_BOLDER,StyleConstants.FONT_WEIGHT_BOLDER);
      fontWeight.addMenuElement(StyleConstants.FONT_WEIGHT_LIGHT,StyleConstants.FONT_WEIGHT_LIGHT);
      fontWeight.setSelectedElement(getStyleValue(StyleConstants.ATTRIBUTE_FONT_WEIGHT));
      fontWeight.setToSubmit();
      fontWeight.setStyleAttribute(IWConstants.BUILDER_FONT_STYLE_INTERFACE);
    Text fontWeightText = new Text("Weight:");
      fontWeightText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
    leftTable.add(fontWeightText,1,row);
    leftTable.add(fontWeight,2,row);
    row++;

    DropdownMenu fontVariant = new DropdownMenu(StyleConstants.ATTRIBUTE_FONT_VARIANT);
      fontVariant.addMenuElementFirst("","");
      fontVariant.addMenuElement(StyleConstants.FONT_VARIANT_NORMAL,StyleConstants.FONT_VARIANT_NORMAL);
      fontVariant.addMenuElement(StyleConstants.FONT_VARIANT_SMALLCAPS,StyleConstants.FONT_VARIANT_SMALLCAPS);
      fontVariant.setSelectedElement(getStyleValue(StyleConstants.ATTRIBUTE_FONT_VARIANT));
      fontVariant.setToSubmit();
      fontVariant.setStyleAttribute(IWConstants.BUILDER_FONT_STYLE_INTERFACE);
    Text fontVariantText = new Text("Variant:");
      fontVariantText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
    leftTable.add(fontVariantText,1,row);
    leftTable.add(fontVariant,2,row);
    row++;

    DropdownMenu textTransform = new DropdownMenu(StyleConstants.ATTRIBUTE_TEXT_TRANSFORM);
      textTransform.addMenuElementFirst("","");
      textTransform.addMenuElement(StyleConstants.TEXT_TRANSFORM_CAPITALIZE,StyleConstants.TEXT_TRANSFORM_CAPITALIZE);
      textTransform.addMenuElement(StyleConstants.TEXT_TRANSFORM_UPPERCASE,StyleConstants.TEXT_TRANSFORM_UPPERCASE);
      textTransform.addMenuElement(StyleConstants.TEXT_TRANSFORM_LOWERCASE,StyleConstants.TEXT_TRANSFORM_LOWERCASE);
      textTransform.addMenuElement(StyleConstants.TEXT_TRANSFORM_NONE,StyleConstants.TEXT_TRANSFORM_NONE);
      textTransform.setSelectedElement(getStyleValue(StyleConstants.ATTRIBUTE_TEXT_TRANSFORM));
      textTransform.setToSubmit();
      textTransform.setStyleAttribute(IWConstants.BUILDER_FONT_STYLE_INTERFACE);
    Text textTransformText = new Text("Transform:");
      textTransformText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
    leftTable.add(textTransformText,1,row);
    leftTable.add(textTransform,2,row);
    row++;

    DropdownMenu textAlign = new DropdownMenu(StyleConstants.ATTRIBUTE_TEXT_ALIGN);
      textAlign.addMenuElementFirst("","");
      textAlign.addMenuElement(StyleConstants.TEXT_ALIGN_LEFT,StyleConstants.TEXT_ALIGN_LEFT);
      textAlign.addMenuElement(StyleConstants.TEXT_ALIGN_CENTER,StyleConstants.TEXT_ALIGN_CENTER);
      textAlign.addMenuElement(StyleConstants.TEXT_ALIGN_RIGHT,StyleConstants.TEXT_ALIGN_RIGHT);
      textAlign.addMenuElement(StyleConstants.TEXT_ALIGN_JUSTIFY,StyleConstants.TEXT_ALIGN_JUSTIFY);
      textAlign.setSelectedElement(getStyleValue(StyleConstants.ATTRIBUTE_TEXT_ALIGN));
      textAlign.setToSubmit();
      textAlign.setStyleAttribute(IWConstants.BUILDER_FONT_STYLE_INTERFACE);
    Text textAlignText = new Text("Alignment:");
      textAlignText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
    leftTable.add(textAlignText,1,row);
    leftTable.add(textAlign,2,row);
    row = 2;

    DropdownMenu textDecoration = new DropdownMenu(StyleConstants.ATTRIBUTE_TEXT_DECORATION);
      textDecoration.addMenuElementFirst("","");
      textDecoration.addMenuElement(StyleConstants.TEXT_DECORATION_NONE,StyleConstants.TEXT_DECORATION_NONE);
      textDecoration.addMenuElement(StyleConstants.TEXT_DECORATION_UNDERLINE,StyleConstants.TEXT_DECORATION_UNDERLINE);
      textDecoration.addMenuElement(StyleConstants.TEXT_DECORATION_OVERLINE,StyleConstants.TEXT_DECORATION_OVERLINE);
      textDecoration.addMenuElement(StyleConstants.TEXT_DECORATION_LINETHROUGH,StyleConstants.TEXT_DECORATION_LINETHROUGH);
      textDecoration.addMenuElement(StyleConstants.TEXT_DECORATION_BLINK,StyleConstants.TEXT_DECORATION_BLINK);
      textDecoration.setSelectedElement(getStyleValue(StyleConstants.ATTRIBUTE_TEXT_DECORATION));
      textDecoration.setToSubmit();
      textDecoration.setStyleAttribute(IWConstants.BUILDER_FONT_STYLE_INTERFACE);
    Text textDecorationText = new Text("Decoration:");
      textDecorationText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
    leftTable.add(textDecorationText,4,row);
    leftTable.add(textDecoration,5,row);
    row++;

    TextInput color = new TextInput(StyleConstants.ATTRIBUTE_COLOR);
      color.setLength(7);
      color.setMaxlength(7);
      color.setContent(getStyleValue(StyleConstants.ATTRIBUTE_COLOR));
      color.setStyleAttribute(IWConstants.BUILDER_FONT_STYLE_INTERFACE);
    Text colorText = new Text("Color:");
      colorText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
    leftTable.add(colorText,4,row);
    leftTable.add(color,5,row);
    row++;

    TextInput letterSpacing = new TextInput(StyleConstants.ATTRIBUTE_LETTER_SPACING);
      letterSpacing.setLength(4);
      letterSpacing.setContent(getStyleValue(StyleConstants.ATTRIBUTE_LETTER_SPACING));
      letterSpacing.setStyleAttribute(IWConstants.BUILDER_FONT_STYLE_INTERFACE);
    Text letterSpacingText = new Text("Letter spacing:");
      letterSpacingText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
    leftTable.add(letterSpacingText,4,row);
    leftTable.add(letterSpacing,5,row);
    row++;

    TextInput wordSpacing = new TextInput(StyleConstants.ATTRIBUTE_WORD_SPACING);
      wordSpacing.setLength(4);
      wordSpacing.setContent(getStyleValue(StyleConstants.ATTRIBUTE_WORD_SPACING));
      wordSpacing.setStyleAttribute(IWConstants.BUILDER_FONT_STYLE_INTERFACE);
    Text wordSpacingText = new Text("Word spacing:");
      wordSpacingText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
    leftTable.add(wordSpacingText,4,row);
    leftTable.add(wordSpacing,5,row);
    row++;

    TextInput lineHeight = new TextInput(StyleConstants.ATTRIBUTE_LINE_HEIGHT);
      lineHeight.setLength(4);
      lineHeight.setContent(getStyleValue(StyleConstants.ATTRIBUTE_LINE_HEIGHT));
      lineHeight.setStyleAttribute(IWConstants.BUILDER_FONT_STYLE_INTERFACE);
    Text lineHeightText = new Text("Line height:");
      lineHeightText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
    leftTable.add(lineHeightText,4,row);
    leftTable.add(lineHeight,5,row);
    row++;

    Text previewText = new Text("Preview:");
      previewText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
    Table previewTable = new Table(3,3);
      previewTable.setCellspacing(0);
      previewTable.setCellpadding(0);
      previewTable.setColor("#000000");
    Table innerTable = new Table(1,1);
      innerTable.setColor("#FFFFFF");
      innerTable.setCellpadding(4);
    previewTable.add(innerTable,2,2);
    formTable.add(previewText,1,2);
    formTable.add(Text.getBreak(),1,2);
    formTable.add(previewTable,1,2);

    Paragraph paragraph = new Paragraph();
    Text text = new Text(TextFormatter.getLoremIpsumString(iwc,256));
    if ( _styleString != null && _styleString.length() > 0 )
      paragraph.setStyleAttribute(_styleString);
    paragraph.add(text);
    innerTable.add(paragraph,1,1);
    row++;

    form.maintainParameter(SCRIPT_PREFIX_PARAMETER);
    form.maintainParameter(SCRIPT_SUFFIX_PARAMETER);
    form.maintainParameter(DISPLAYSTRING_PARAMETER_NAME);
    form.maintainParameter(VALUE_PARAMETER_NAME);

    formTable.add(new SubmitButton(iwrb.getLocalizedImageButton("preview","Preview")),1,3);
    formTable.add(Text.getNonBrakingSpace(),1,3);
    formTable.add(new SubmitButton(iwrb.getLocalizedImageButton("submit","Submit"),"submit"),1,3);

    form.add(formTable);
    add(form);
  }

  private void collectStyles(IWContext iwc) {
    if ( _styles != null ) {
      for ( int a = 0; a < _styles.length; a++ ) {
	getParameter(_styles[a],iwc);
      }
    }
    getMapStyleString();
  }

  private void getParameter(String attribute,IWContext iwc) {
    String value = iwc.getParameter(attribute);
    if ( value != null && value.length() > 0 ) {
      if ( attribute.equalsIgnoreCase(StyleConstants.ATTRIBUTE_LINE_HEIGHT) && value.indexOf("px") == -1 )
	value += "px";
      if ( attribute.equalsIgnoreCase(StyleConstants.ATTRIBUTE_LETTER_SPACING) && value.indexOf("px") == -1 )
	value += "px";
      if ( attribute.equalsIgnoreCase(StyleConstants.ATTRIBUTE_WORD_SPACING) && value.indexOf("px") == -1 )
	value += "px";
      if ( attribute.equalsIgnoreCase(StyleConstants.ATTRIBUTE_LINE_HEIGHT) && value.indexOf("px") == -1 )
	value += "px";
      setStyleValue(attribute,value);
    }
  }

  private void getMapStyleString() {
    Iterator iter = _styleMap.keySet().iterator();
    String attribute;
    String value;
    _styleString = "";
    while (iter.hasNext()) {
      attribute = (String) iter.next();
      value = (String) _styleMap.get(attribute);
      if ( value != null ) {
	_styleString += attribute + StyleConstants.DELIMITER_COLON + value + StyleConstants.DELIMITER_SEMICOLON;
      }
    }
  }

  private void setMapStyles() {
    if ( _styleString != null ) {
      StringTokenizer tokens = new StringTokenizer(_styleString,";");
      int a = -1;
      String attribute;
      String value;

      while (tokens.hasMoreTokens()) {
	StringTokenizer tokens2 = new StringTokenizer(tokens.nextToken(),":");

	a = 1;
	attribute = null;
	value = null;

	while (tokens2.hasMoreTokens()) {
	  if ( a == 1 ) {
	    attribute = tokens2.nextToken();
	    a++;
	  }
	  else if ( a == 2 )
	    value = tokens2.nextToken();
	}
	_styleMap.put(attribute,value);
      }
    }
  }

  private void setDefaultValues() {
    if ( _styleMap == null )
      _styleMap = new HashMap();

    if ( _styles != null ) {
      for ( int a = 0; a < _styles.length; a++ ) {
	_styleMap.put(_styles[a],null);
      }
    }
  }

  private void setStyleValue(String attribute,String value) {
    _styleMap.put(attribute,value);
  }

  private String getStyleValue(String attribute) {
    String value = (String) _styleMap.get(attribute);
    if ( value != null ) {
      if ( attribute != StyleConstants.ATTRIBUTE_FONT_SIZE && value.indexOf("px") != -1 )
	value = value.substring(0,value.indexOf("px"));
      return value;
    }
    return "";
  }
}