package org.adligo.tests.xml.parsers.template;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */
import org.adligo.xml.parsers.template.*;
import junit.framework.TestCase;

public class Test1 extends TestCase {
  Templates templates = new Templates();
  private static final String sKey = new String("SELECT \r\n  fname, mname, lname, nickname, birthday, comment\r\n" +
            "  FROM persons p\r\n   WHERE\r\n" +
            "    \r\n     parent IN (7)");

 public Test1(String s) {
  super(s);
 }

  public void setUp() {
    templates.parseResource("/org/adligo/tests/xml/parsers/template/PersonsSQL.xml");
  }

  public void test1() {
    Params params = new Params();
    Params whereArgs = new Params();
    whereArgs.addParam("parent",new String [] {"7"}, null);
    Param where = new Param("where", new String [] {}, whereArgs);
    params.addParam(where);
    String sResult = TemplateParserEngine.parse(templates.getTemplate("persons"), params);

    assertTrue(sResult.indexOf(sKey) > -1);
  }
}