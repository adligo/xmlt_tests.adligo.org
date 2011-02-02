package org.adligo.tests.xml.parsers.template;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */
import java.io.IOException;

import org.adligo.models.params.client.Param;
import org.adligo.models.params.client.Params;
import org.adligo.models.params.client.XMLBuilder;
import org.adligo.xml.parsers.template.TemplateParserEngine;
import org.adligo.xml.parsers.template.Templates;

public class Test3 extends TimedTest {
  Templates templates = new Templates();
  private static final String sKey = new String("SELECT " + XMLBuilder.UNIX_LINE_FEED +
		   "  " +XMLBuilder.UNIX_LINE_FEED +
		   "  " +XMLBuilder.UNIX_LINE_FEED +
		   "  fname, mname, lname, nickname, birthday, comment" + XMLBuilder.UNIX_LINE_FEED +
		   "  " + XMLBuilder.UNIX_LINE_FEED + 
            "  FROM persons p" + XMLBuilder.UNIX_LINE_FEED + "   WHERE" + XMLBuilder.UNIX_LINE_FEED + 
            "     oid NOT IN (1,2) " + XMLBuilder.UNIX_LINE_FEED + 
            "    " + XMLBuilder.UNIX_LINE_FEED + "     AND ( fname LIKE 'joe'  OR  fname LIKE 'bob' )");

 public Test3(String s) {
  super(s);
 }

  public void setUp() throws IOException {
    templates.parseResource("/org/adligo/tests/xml/parsers/template/PersonsSQL.xml");
  }

  public void test3() {
    Params params = new Params();
    params.addParam("default");
    Params whereArgs = new Params();
    Param oidParam = whereArgs.addParam("oid",new String [] {"NOT IN (", ")"}, 1);
    oidParam.addValue(2);
    whereArgs.addParam("fname","LIKE", "joe");
    whereArgs.addParam("fname","LIKE", "bob");
    Param where = new Param("where", whereArgs);
    params.addParam(where);
    
    //inital parse
    templates.getTemplate("persons");
    long start = System.nanoTime();
    String sResult = TemplateParserEngine.parse(
    		templates.getTemplate("persons"),
    		new MockParamDecorator(params));
    long end = System.nanoTime();
    super.addTime(end - start);
    
    assertEquals(sKey, sResult);
  }

}