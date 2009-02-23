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
import org.adligo.xml.parsers.template.Templates;
import org.adligo.xml.parsers.template.TemplateParserEngine;
import junit.framework.TestCase;

public class Test6 extends TimedTest {
  private static final String sKey = new String("SELECT  TOP  \r\n  fname, mname, lname, nickname, birthday, comment\r\n" +
            "  FROM persons p\r\n   WHERE\r\n    \r\n    \r\n    \r\n    \r\n    \r\n    \r\n    \r\n    \r\n" +
            "        NOT EXISTS (SELECT tid FROM o_e_addresses E WHERE O.tid = E.fk AND\r\n" +
            "        \r\n        \r\n        \r\n" +
            "          E.type IN (1,2))\r\n" +
            "     AND \r\n" +
            "         EXISTS (SELECT tid FROM o_e_addresses E WHERE O.tid = E.fk AND\r\n" +
            "        \r\n         E.edited_by IN(1,2)\r\n        \r\n        )");
  Templates templates = new Templates();

 public Test6(String s) {
  super(s);
 }

  public void setUp() {
    templates.parseResource("/org/adligo/tests/xml/parsers/template/PersonsSQL.xml");
  }

  public void test6() {
    Params params = new Params();
    Params whereArgs = new Params();
    Params addressArgs = new Params();
    Params addressArgs2 = new Params();
    addressArgs.addParam("type_l",new String [] {"1", "2"}, null);
    whereArgs.addParam("p_e_addresses",null, addressArgs, new int [] {1});
    addressArgs2.addParam("edited_by",new String [] {"1", "2"}, null);
    whereArgs.addParam("p_e_addresses",null, addressArgs2, null);
    Param where = new Param("where", new String [] {}, whereArgs);
    params.addParam(where);
    /* Add this at the end to see if the maxrows param is added twice */
    Params.addParam(params, new Param("maxrows", null, null), false);
    /* add the foo param so we can move the currentParam pointer to the foo param 
     * which is after the maxrows param
     */
    params.addParam("foo", null, null);
    params.First();
    params.getNextParam("foo");
    /* try to add the maxrows param a second time since the current param is after 
     * the previous maxrows param
     */
    Params.addParam(params, new Param("maxrows", null, null), false);
    
    //inital parse
    templates.getTemplate("persons");
    long start = System.nanoTime();
    String sResult = TemplateParserEngine.parse(templates.getTemplate("persons"), params);
    long end = System.nanoTime();
    super.addTime(end - start);
    
    //System.out.println(sResult);

    assertTrue (sResult.indexOf(sKey) > -1);
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