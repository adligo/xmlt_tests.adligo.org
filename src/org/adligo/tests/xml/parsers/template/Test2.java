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

public class Test2 extends TimedTest {
  private static final String sKey = new String("SELECT \r\n  fname, mname, lname, nickname, birthday, comment\r\n" +
                "  FROM persons p\r\n   WHERE\r\n    \r\n    \r\n    \r\n    \r\n    \r\n" +
                "    \r\n    \r\n      (\r\n        ( birthday  >= '1/1/2001' \r\n" +
                "           AND  birthday  <= '2/1/2001' \r\n        )\r\n         OR \r\n" +
                "        ( birthday  >= '1/1/2002' \r\n" +
                "           AND  birthday  <= '2/1/2002' \r\n        )\r\n" +
                "        \r\n      )" ) ;
  Templates templates = new Templates();

 public Test2(String s) {
  super(s);
 }

  public void setUp() {
    templates.parseResource("/org/adligo/tests/xml/parsers/template/PersonsSQL.xml");
  }

  public void test2() {
    Params params = new Params();
    Params whereParams = new Params();
    Params birthdayParams = new Params();
    Params birthdayRangeParams1 = new Params();
    Params birthdayRangeParams2 = new Params();

    birthdayRangeParams1.addParam("start_range", new String[] {" >= '1/1/2001' "}, null);
    birthdayRangeParams1.addParam("end_range", new String[] {" <= '2/1/2001' "}, null);
    birthdayParams.addParam("range", null, birthdayRangeParams1);

    birthdayRangeParams2.addParam("start_range", new String[] {" >= '1/1/2002' "}, null);
    birthdayRangeParams2.addParam("end_range", new String[] {" <= '2/1/2002' "}, null);
    birthdayParams.addParam("range", null, birthdayRangeParams2);

    whereParams.addParam("birthday",null, birthdayParams);
    params.addParam("where", new String [] {}, whereParams);

    //inital parse
    templates.getTemplate("persons");
    long start = System.nanoTime();
    String sResult = TemplateParserEngine.parse(templates.getTemplate("persons"), params);
    long end = System.nanoTime();
    super.addTime(end - start);
    
    assertTrue(sResult.indexOf(sKey) > -1);
  }
}