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
  private static final String sKey = new String("SELECT \r\n" +
		  "  \r\n" +
		  "  \r\n" +
		  "  fname, mname, lname, nickname, birthday, comment\r\n" +
		  "  \r\n" +
		  "  FROM persons p\r\n" +
		  "   WHERE\r\n" +
		  "     oid IN (1,2) \r\n" +
		  "    \r\n" +
  		  "     AND ( fname LIKE 'joe'  OR  fname LIKE 'bob' )");

	 public Test0(String s) {
	  super(s);
	 }

  public void setUp() {
    templates.parseResource("/org/adligo/tests/xml/parsers/template/PersonsSQL.xml");
  }

  public void test0() {
    Params params = new Params();
    params.addParam("default");
    Params whereArgs = new Params();
    
    //this will allow operators to be checked for validity
    // aginst a set of allowed Strings
    //
    // the values can get set via jdbc prepaired statements
    // (assuming your using a database,
    // and not a ldap, xml, or other storage solution)
    // to prevent sql injection
    Param oidParam = new Param("oid", new String[] {"IN (", ")"});
    oidParam.addValue(1);
    oidParam.addValue(2);
    whereArgs.addParam(oidParam);
    whereArgs.addParam("fname","LIKE", "joe");
    whereArgs.addParam("fname","LIKE", "bob");
    Param where = new Param("where", whereArgs);
    params.addParam(where);
    
    //inital parse
    templates.getTemplate("persons");
    long start = System.nanoTime();
    String sResult = TemplateParserEngine.parse(templates.getTemplate("persons"),
    			new TestParamDecorator(params));
    long end = System.nanoTime();
    super.addTime(end - start);
    
    assertEquals(sKey, sResult);
  }
}