package org.adligo.tests.xml.parsers.template;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */
import org.adligo.models.params.client.Param;
import org.adligo.models.params.client.Params;
import org.adligo.xml.parsers.template.Templates;
import org.adligo.xml.parsers.template.TemplateParserEngine;

public class Test1 extends TimedTest {
  Templates templates = new Templates();
  private static final String sKey = new String("SELECT \r\n" +
		  	"  \r\n" +
		  	"  \r\n" +
		  	"  fname, mname, lname, nickname, birthday, comment\r\n" +
		  	"  \r\n" +
		  	"  FROM persons p\r\n" +
            "   WHERE\r\n" +
            "    \r\n" +
            "     parent IN (7)");

 public Test1(String s) {
  super(s);
 }

  public void setUp() {
    templates.parseResource("/org/adligo/tests/xml/parsers/template/PersonsSQL.xml");
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