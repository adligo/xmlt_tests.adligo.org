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
import junit.framework.TestCase;

public class Test0 extends TimedTest {
  Templates templates = new Templates();
  private static final String sKey = new String("SELECT \r\n  fname, mname, lname, nickname, birthday, comment\r\n" +
            "  FROM persons p\r\n   WHERE\r\n" +
            "     oid  IN (1,2) \r\n    \r\n     AND ( fname LIKE 'joe'  OR  fname LIKE 'bob' )");

 public Test0(String s) {
  super(s);
 }

  public void setUp() {
    templates.parseResource("/org/adligo/tests/xml/parsers/template/PersonsSQL.xml");
  }

  public void test0() {
    Params params = new Params();
    Params whereArgs = new Params();
    whereArgs.addParam("oid",new String [] {"1","2"}, null);
    whereArgs.addParam("fname",new String [] {"'joe'"}, null);
    whereArgs.addParam("fname",new String [] {"'bob'"}, null);
    Param where = new Param("where", new String [] {}, whereArgs);
    params.addParam(where);
    
    //inital parse
    templates.getTemplate("persons");
    long start = System.nanoTime();
    String sResult = TemplateParserEngine.parse(templates.getTemplate("persons"), params);
    long end = System.nanoTime();
    super.addTime(end - start);
    
    assertTrue( "Test returned '" + sResult + 
    		"' \n\n and should have returned '" + sKey + "' "
    		+ " index is " + sResult.indexOf(sKey),
			sResult.indexOf(sKey) > -1);
  }
}