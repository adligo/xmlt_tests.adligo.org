package org.adligo.tests.xml.parsers.template;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */
import junit.framework.*;
import org.apache.log4j.BasicConfigurator;
import java.lang.reflect.*;

public class Suite {

  public Suite() {
  }

  public static String getPackage() {
    return "org.adligo.tests.xml.parsers.template.";
  }

  public static Test suite() {
    //BasicConfigurator.configure();
    TestSuite suite= new TestSuite();
    try {
      for (int i = 0; i < 6; i++) {
        Class c = Class.forName(getPackage() + "Test" + i);
        Class [] args = new Class[] {String.class};
        Constructor ct = c.getConstructor(args);
        String [] sa = new String [] {"test" + i};
        Object o = ct.newInstance(sa);
        suite.addTest((TestCase) o);
      }
    } catch (Exception x) {x.printStackTrace(); }

    //suite.addTest(new Test0("test0"));
    return suite;
  }
}