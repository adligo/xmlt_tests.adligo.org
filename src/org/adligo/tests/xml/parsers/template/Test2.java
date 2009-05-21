package org.adligo.tests.xml.parsers.template;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */
import java.text.SimpleDateFormat;

import org.adligo.models.params.client.Params;
import org.adligo.xml.parsers.template.Templates;
import org.adligo.xml.parsers.template.TemplateParserEngine;

public class Test2 extends TimedTest {
  private static final String sKey = new String("SELECT \r\n" +
		  		"  \r\n" +
		  		"  \r\n" +
  				"  fname, mname, lname, nickname, birthday, comment\r\n" +
  				"  \r\n" +
  				"  FROM persons p\r\n" +
                "   WHERE\r\n    \r\n    \r\n    \r\n    \r\n    \r\n" +
                "    \r\n    \r\n      (\r\n        ( birthday >= '01/01/2001' \r\n" +
                "           AND  birthday <= '02/01/2001' \r\n        )\r\n         OR \r\n" +
                "        ( birthday >= '01/01/2002' \r\n" +
                "           AND  birthday <= '02/01/2002' \r\n        )\r\n" +
                "        \r\n      )" ) ;
  Templates templates = new Templates();
  private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
  
 public Test2(String s) {
  super(s);
 }

  public void setUp() {
    templates.parseResource("/org/adligo/tests/xml/parsers/template/PersonsSQL.xml");
  }

  public void test2() throws Exception {
    Params params = new Params();
    params.addParam("default");
    Params whereParams = new Params();
    Params birthdayParams = new Params();
    Params birthdayRangeParams1 = new Params();
    Params birthdayRangeParams2 = new Params();

    birthdayRangeParams1.addParam("start_range", ">=",  sdf.parse("1/1/2001"));
    birthdayRangeParams1.addParam("end_range", "<=", sdf.parse("2/1/2001"));
    birthdayParams.addParam("range", birthdayRangeParams1);

    birthdayRangeParams2.addParam("start_range", ">=", sdf.parse("1/1/2002"));
    birthdayRangeParams2.addParam("end_range", "<=", sdf.parse("2/1/2002"));
    birthdayParams.addParam("range", birthdayRangeParams2);

    whereParams.addParam("birthday",birthdayParams);
    params.addParam("where", whereParams);

    //inital parse
    templates.getTemplate("persons");
    long start = System.nanoTime();
    String sResult = TemplateParserEngine.parse(templates.getTemplate("persons"), 
    		new TestParamDecorator(params));
    long end = System.nanoTime();
    super.addTime(end - start);
    
    assertEquals(sKey,sResult);
  }
}