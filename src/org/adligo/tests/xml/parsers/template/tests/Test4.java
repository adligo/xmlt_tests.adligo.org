package org.adligo.tests.xml.parsers.template.tests;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */
import java.io.IOException;

import org.adligo.models.params.client.I_XMLBuilder;
import org.adligo.models.params.client.Param;
import org.adligo.models.params.client.Params;
import org.adligo.xml.parsers.template.TemplateParserEngine;
import org.adligo.xml.parsers.template.Templates;

public class Test4 extends TimedTest {
  Templates templates = new Templates();
  private static final String sKey = new String("SELECT " + I_XMLBuilder.UNIX_LINE_FEED +
		  	"  " + I_XMLBuilder.UNIX_LINE_FEED +
		  	"  " + I_XMLBuilder.UNIX_LINE_FEED + 
		  	"  fname, mname, lname, nickname, birthday, comment" + I_XMLBuilder.UNIX_LINE_FEED +
		  	"  " + I_XMLBuilder.UNIX_LINE_FEED +
		  	"  FROM persons p" + I_XMLBuilder.UNIX_LINE_FEED + "   WHERE" + I_XMLBuilder.UNIX_LINE_FEED + 
		  	"    " + I_XMLBuilder.UNIX_LINE_FEED + "    " + I_XMLBuilder.UNIX_LINE_FEED + 
		  	"    " + I_XMLBuilder.UNIX_LINE_FEED + "    " + I_XMLBuilder.UNIX_LINE_FEED + 
		  	"    " + I_XMLBuilder.UNIX_LINE_FEED + "    " + I_XMLBuilder.UNIX_LINE_FEED + 
		  	"    " + I_XMLBuilder.UNIX_LINE_FEED + "    " + I_XMLBuilder.UNIX_LINE_FEED +
            "        NOT EXISTS (SELECT tid FROM o_e_addresses E WHERE O.tid = E.fk AND" + I_XMLBuilder.UNIX_LINE_FEED +
            "        " + I_XMLBuilder.UNIX_LINE_FEED + "        " + I_XMLBuilder.UNIX_LINE_FEED + 
            "        " + I_XMLBuilder.UNIX_LINE_FEED +
            "          E.type IN (1,2))" );

 public Test4(String s) {
  super(s);
 }
  public void setUp() throws IOException {
    templates.parseResource("/org/adligo/tests/xml/parsers/template/tests/PersonsSQL.xml");
  }

  public void test4() {
    Params params = new Params();
    params.addParam("default");
    Params whereArgs = new Params();
    Params addressArgs = new Params();
    Param addParam = addressArgs.addParam("type_l",new String [] {"IN (",")"},1);
    addParam.addValue(2);
    Param peParam = whereArgs.addParam("p_e_addresses");
    peParam.setOperator("NOT");
    peParam.setParams(addressArgs);
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