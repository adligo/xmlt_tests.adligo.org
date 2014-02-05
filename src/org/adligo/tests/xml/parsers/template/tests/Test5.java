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

public class Test5 extends TimedTest {
  private static final String sKey = new String("SELECT " + I_XMLBuilder.UNIX_LINE_FEED + 
		  "  " + I_XMLBuilder.UNIX_LINE_FEED + 
		  "  " + I_XMLBuilder.UNIX_LINE_FEED + 
  		"  fname, mname, lname, nickname, birthday, comment" + I_XMLBuilder.UNIX_LINE_FEED + 
  		"  " + I_XMLBuilder.UNIX_LINE_FEED + 
            "  FROM persons p" + I_XMLBuilder.UNIX_LINE_FEED + 
            "   WHERE" + I_XMLBuilder.UNIX_LINE_FEED + "    " + I_XMLBuilder.UNIX_LINE_FEED + 
            "    " + I_XMLBuilder.UNIX_LINE_FEED + "    " + I_XMLBuilder.UNIX_LINE_FEED + 
            "    " + I_XMLBuilder.UNIX_LINE_FEED + "    " + I_XMLBuilder.UNIX_LINE_FEED + 
            "    " + I_XMLBuilder.UNIX_LINE_FEED + "    " + I_XMLBuilder.UNIX_LINE_FEED + 
            "    " + I_XMLBuilder.UNIX_LINE_FEED + 
            "        NOT EXISTS (SELECT tid FROM o_e_addresses E WHERE O.tid = E.fk AND" + I_XMLBuilder.UNIX_LINE_FEED + 
            "        " + I_XMLBuilder.UNIX_LINE_FEED + "        " + I_XMLBuilder.UNIX_LINE_FEED + 
            "        " + I_XMLBuilder.UNIX_LINE_FEED + 
            "          E.type IN (1,2))" + I_XMLBuilder.UNIX_LINE_FEED + 
            "     AND " + I_XMLBuilder.UNIX_LINE_FEED + 
            "         EXISTS (SELECT tid FROM o_e_addresses E WHERE O.tid = E.fk AND" + I_XMLBuilder.UNIX_LINE_FEED + 
            "        " + I_XMLBuilder.UNIX_LINE_FEED + "         E.edited_by IN (3,4)" + I_XMLBuilder.UNIX_LINE_FEED + 
            "        " + I_XMLBuilder.UNIX_LINE_FEED + "        )");
  Templates templates = new Templates();

 public Test5(String s) {
  super(s);
 }

  public void setUp() throws IOException {
    templates.parseResource("/org/adligo/tests/xml/parsers/template/tests/PersonsSQL.xml");
  }

  public void test5() {
    Params params = new Params();
    params.addParam("default");
    Params whereArgs = new Params();
    Params addressArgs = new Params();
    Params addressArgs2 = new Params();
    Param typeParam = addressArgs.addParam("type_l",new String [] {"IN (", ")"});
    typeParam.addValue(1);
    typeParam.addValue(2);
    
    Param peParam = whereArgs.addParam("p_e_addresses", addressArgs);
    peParam.setOperator("NOT");
    Param editedByParam = addressArgs2.addParam("edited_by",new String [] {"IN (", ")"});
    editedByParam.addValue(3);
    editedByParam.addValue(4);
    whereArgs.addParam("p_e_addresses",addressArgs2);
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