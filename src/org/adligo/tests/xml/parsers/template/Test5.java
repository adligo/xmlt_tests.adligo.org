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
import org.adligo.models.params.client.XMLBuilder;
import org.adligo.xml.parsers.template.Templates;
import org.adligo.xml.parsers.template.TemplateParserEngine;
import junit.framework.TestCase;

public class Test5 extends TimedTest {
  private static final String sKey = new String("SELECT " + XMLBuilder.UNIX_LINE_FEED + 
		  "  " + XMLBuilder.UNIX_LINE_FEED + 
		  "  " + XMLBuilder.UNIX_LINE_FEED + 
  		"  fname, mname, lname, nickname, birthday, comment" + XMLBuilder.UNIX_LINE_FEED + 
  		"  " + XMLBuilder.UNIX_LINE_FEED + 
            "  FROM persons p" + XMLBuilder.UNIX_LINE_FEED + 
            "   WHERE" + XMLBuilder.UNIX_LINE_FEED + "    " + XMLBuilder.UNIX_LINE_FEED + 
            "    " + XMLBuilder.UNIX_LINE_FEED + "    " + XMLBuilder.UNIX_LINE_FEED + 
            "    " + XMLBuilder.UNIX_LINE_FEED + "    " + XMLBuilder.UNIX_LINE_FEED + 
            "    " + XMLBuilder.UNIX_LINE_FEED + "    " + XMLBuilder.UNIX_LINE_FEED + 
            "    " + XMLBuilder.UNIX_LINE_FEED + 
            "        NOT EXISTS (SELECT tid FROM o_e_addresses E WHERE O.tid = E.fk AND" + XMLBuilder.UNIX_LINE_FEED + 
            "        " + XMLBuilder.UNIX_LINE_FEED + "        " + XMLBuilder.UNIX_LINE_FEED + 
            "        " + XMLBuilder.UNIX_LINE_FEED + 
            "          E.type IN (1,2))" + XMLBuilder.UNIX_LINE_FEED + 
            "     AND " + XMLBuilder.UNIX_LINE_FEED + 
            "         EXISTS (SELECT tid FROM o_e_addresses E WHERE O.tid = E.fk AND" + XMLBuilder.UNIX_LINE_FEED + 
            "        " + XMLBuilder.UNIX_LINE_FEED + "         E.edited_by IN (3,4)" + XMLBuilder.UNIX_LINE_FEED + 
            "        " + XMLBuilder.UNIX_LINE_FEED + "        )");
  Templates templates = new Templates();

 public Test5(String s) {
  super(s);
 }

  public void setUp() {
    templates.parseResource("/org/adligo/tests/xml/parsers/template/PersonsSQL.xml");
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