package org.adligo.xml.parsers.template_tests;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */
import java.io.IOException;

import org.adligo.models.params.shared.I_XMLBuilder;
import org.adligo.models.params.shared.Param;
import org.adligo.models.params.shared.Params;
import org.adligo.xml.parsers.template.Templates;
import org.adligo.xml.parsers.template.TemplateParserEngine;

public class Test1 extends TimedTest {
  Templates templates = new Templates();
  private static final String sKey = new String("SELECT " + I_XMLBuilder.UNIX_LINE_FEED + 
		  	"  " + I_XMLBuilder.UNIX_LINE_FEED + 
		  	"  " + I_XMLBuilder.UNIX_LINE_FEED + 
		  	"  fname, mname, lname, nickname, birthday, comment" + I_XMLBuilder.UNIX_LINE_FEED + 
		  	"  " + I_XMLBuilder.UNIX_LINE_FEED + 
		  	"  FROM persons p" + I_XMLBuilder.UNIX_LINE_FEED + 
            "   WHERE" + I_XMLBuilder.UNIX_LINE_FEED + 
            "    " + I_XMLBuilder.UNIX_LINE_FEED + 
            "     parent IN (7)");

 public Test1(String s) {
  super(s);
 }

  public void setUp() throws IOException {
    templates.parseResource("/org/adligo/xml/parsers/template_tests/PersonsSQL.xml");
  }

  public void test1() {
    Params params = new Params();
    params.addParam("default");
    Params whereArgs = new Params();
    whereArgs.addParam("parent",new String[] {"IN (", ")"},7);
    Param where = new Param("where", whereArgs);
    params.addParam(where);
    
    //inital parse
    templates.getTemplate("persons");
    long start = System.nanoTime();
    String sResult = TemplateParserEngine.parse(templates.getTemplate("persons"), params);
    long end = System.nanoTime();
    super.addTime(end - start);
    
    assertEquals(sKey, sResult);
  }
}