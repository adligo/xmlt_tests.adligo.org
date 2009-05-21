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
import org.adligo.tests.InitJ2SE;
import org.adligo.xml.parsers.template.Templates;
import org.adligo.xml.parsers.template.TemplateParserEngine;
import junit.framework.TestCase;

public class Test6 extends TimedTest {
  private static final String sKey = new String("SELECT  TOP  \r\n" +
  		"   fname || mname || lname \r\n" +
  		"  \r\n" +
            "  FROM persons p\r\n   WHERE\r\n    \r\n    \r\n    \r\n    \r\n    \r\n    \r\n    \r\n    \r\n" +
            "        NOT EXISTS (SELECT tid FROM o_e_addresses E WHERE O.tid = E.fk AND\r\n" +
            "        \r\n        \r\n        \r\n" +
            "          E.type IN (1,2))\r\n" +
            "     AND \r\n" +
            "         EXISTS (SELECT tid FROM o_e_addresses E WHERE O.tid = E.fk AND\r\n" +
            "        \r\n         E.edited_by IN (1,2)\r\n        \r\n        )");
  Templates templates = new Templates();

  static {
	  InitJ2SE.init();
  }
  
 public Test6(String s) {
  super(s);
 }

  public void setUp() {
    templates.parseResource("/org/adligo/tests/xml/parsers/template/PersonsSQL.xml");
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