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

public class Test4 extends TimedTest {
  Templates templates = new Templates();
  private static final String sKey = new String("SELECT \r\n  fname, mname, lname, nickname, birthday, comment\r\n" +
            "  FROM persons p\r\n   WHERE\r\n    \r\n    \r\n    \r\n    \r\n    \r\n    \r\n    \r\n    \r\n" +
            "        NOT EXISTS (SELECT tid FROM o_e_addresses E WHERE O.tid = E.fk AND\r\n" +
            "        \r\n        \r\n        \r\n" +
            "          E.type IN (1,2))" );

 public Test4(String s) {
  super(s);
 }
  public void setUp() {
    templates.parseResource("/org/adligo/tests/xml/parsers/template/PersonsSQL.xml");
  }

  public void test4() {
    Params params = new Params();
    Params whereArgs = new Params();
    Params addressArgs = new Params();
    addressArgs.addParam("type_l",new String [] {"1", "2"}, null);
    whereArgs.addParam("p_e_addresses",null, addressArgs, new int [] {1});
    Param where = new Param("where", new String [] {}, whereArgs);
    params.addParam(where);
    
    //inital parse
    templates.getTemplate("persons");
    long start = System.nanoTime();
    String sResult = TemplateParserEngine.parse(templates.getTemplate("persons"), params);
    long end = System.nanoTime();
    super.addTime(end - start);
    
    assertTrue(sResult.indexOf(sKey) > -1);
  }

}