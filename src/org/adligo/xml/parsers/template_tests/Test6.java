package org.adligo.xml.parsers.template_tests;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */
import java.io.IOException;

import org.adligo.jse.util.JSECommonInit;
import org.adligo.models.params.shared.I_XMLBuilder;
import org.adligo.models.params.shared.Param;
import org.adligo.models.params.shared.Params;
import org.adligo.xml.parsers.template.TemplateParserEngine;
import org.adligo.xml.parsers.template.Templates;

public class Test6 extends TimedTest {
  private static final String sKey = new String("SELECT  TOP  " + I_XMLBuilder.UNIX_LINE_FEED +
  		"   fname || mname || lname " + I_XMLBuilder.UNIX_LINE_FEED +
  		"  " + I_XMLBuilder.UNIX_LINE_FEED +
            "  FROM persons p" + I_XMLBuilder.UNIX_LINE_FEED + "   WHERE" + I_XMLBuilder.UNIX_LINE_FEED + 
            "    " + I_XMLBuilder.UNIX_LINE_FEED + "    " + I_XMLBuilder.UNIX_LINE_FEED + 
            "    " + I_XMLBuilder.UNIX_LINE_FEED + "    " + I_XMLBuilder.UNIX_LINE_FEED + 
            "    " + I_XMLBuilder.UNIX_LINE_FEED + "    " + I_XMLBuilder.UNIX_LINE_FEED + 
            "    " + I_XMLBuilder.UNIX_LINE_FEED + "    " + I_XMLBuilder.UNIX_LINE_FEED +
            "        NOT EXISTS (SELECT tid FROM o_e_addresses E WHERE O.tid = E.fk AND" + I_XMLBuilder.UNIX_LINE_FEED +
            "        " + I_XMLBuilder.UNIX_LINE_FEED + "        " + I_XMLBuilder.UNIX_LINE_FEED + 
            "        " + I_XMLBuilder.UNIX_LINE_FEED +
            "          E.type IN (1,2))" + I_XMLBuilder.UNIX_LINE_FEED +
            "     AND " + I_XMLBuilder.UNIX_LINE_FEED +
            "         EXISTS (SELECT tid FROM o_e_addresses E WHERE O.tid = E.fk AND" + I_XMLBuilder.UNIX_LINE_FEED +
            "        " + I_XMLBuilder.UNIX_LINE_FEED + "         E.edited_by IN (1,2)" + I_XMLBuilder.UNIX_LINE_FEED + "        " + I_XMLBuilder.UNIX_LINE_FEED + "        )");
  Templates templates = new Templates();

  static {
	  JSECommonInit.callLogDebug(Test6.class.getName());
  }
  
 public Test6(String s) {
  super(s);
 }

  public void setUp() throws IOException {
    templates.parseResource("/org/adligo/xml/parsers/template_tests/PersonsSQL.xml");
  }

  public void test6() {
	for (int i = 0; i < 1000; i++) {
		runMe();
	}  
  }
  
  public void runMe() {
    Params params = new Params();
    params.addParam("name");
    Params whereArgs = new Params();
    Params addressArgs = new Params();
    Params addressArgs2 = new Params();
    Param addParam = addressArgs.addParam("type_l", new String[] {"IN (", ")"},1);
    addParam.addValue(2);
    Param peParam = whereArgs.addParam("p_e_addresses",addressArgs);
    peParam.setOperator("NOT");
    Param editBy = addressArgs2.addParam("edited_by",new String [] {"IN (", ")"});
    editBy.addValue(1);
    editBy.addValue(2);
    
    whereArgs.addParam("p_e_addresses",addressArgs2);
    Param where = new Param("where", whereArgs);
    params.addParam(where);
    /* Add this at the end to see if the maxrows param is added twice */
    Params.addParam(params, new Param("maxrows"), false);
    /* add the foo param so we can move the currentParam pointer to the foo param 
     * which is after the maxrows param
     */
    params.addParam("foo");
    params.First();
    params.getNextParam("foo");
    /* try to add the maxrows param a second time since the current param is after 
     * the previous maxrows param
     */
    Params.addParam(params, new Param("maxrows"), false);
    
    //inital parse
    templates.getTemplate("persons");
    long start = System.nanoTime();
    String sResult = TemplateParserEngine.parse(templates.getTemplate("persons"), params);
    long end = System.nanoTime();
    super.addTime(end - start);
    
    //System.out.println(sResult);

    assertEquals(sKey, sResult);
  }
  
  public void tearDown() {
	  long totalTime = super.getTime();
	  int total = super.getNumber();
	  int avg = (int) totalTime/total;
	  long secondInNanos = 1000000000;
	  int Hz = (int) (secondInNanos/avg);
	  
	  System.out.println("parseing of compiled Template " +
	  		" objects and params took " + totalTime + " nano seconds " +
	  		" for " + total + " parsings having a average of " +
	  		avg + " nano seconds per parsing \n" +
	  		" There are 1,000,000 nano seconds in one millisecond " + 
	  		" and 1,000 milliseconds in one second " +
	  		" so frequency is " + Hz + " executions per second");
	  //2k-3k per second
  }

}