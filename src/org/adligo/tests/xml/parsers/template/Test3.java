package org.adligo.tests.xml.parsers.template;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */
import org.adligo.xml.parsers.template.Templates;
import org.adligo.xml.parsers.template.TemplateParserEngine;
import org.adligo.xml.params.*;
import junit.framework.TestCase;

public class Test3 extends TestCase {
  Templates templates = new Templates();
  private static final String sKey = new String("SELECT \r\n  fname, mname, lname, nickname, birthday, comment\r\n" +
            "  FROM persons p\r\n   WHERE\r\n" +
            "     oid NOT IN (1,2) \r\n    \r\n     AND ( fname LIKE 'joe'  OR  fname LIKE 'bob' )");

 public Test3(String s) {
  super(s);
 }

  public void setUp() {
    templates.parseResource("/org/adligo/tests/xml/parsers/template/PersonsSQL.xml");
  }

  public void test3() {
    Params params = new Params();
    Params whereArgs = new Params();
    whereArgs.addParam("oid",new String [] {"1","2"}, null, new int [] {1});
    whereArgs.addParam("fname",new String [] {"'joe'"}, null);
    whereArgs.addParam("fname",new String [] {"'bob'"}, null);
    Param where = new Param("where", new String [] {}, whereArgs);
    params.addParam(where);
    String sResult = TemplateParserEngine.parse(templates.getTemplate("persons"), params);

    assertTrue(sResult.indexOf(sKey) > -1);
  }

}