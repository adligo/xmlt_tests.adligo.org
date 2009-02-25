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
import junit.framework.TestCase;

public class TimedTest extends TestCase {
    private static long time = 0;
    private static int number = 0;
    
 	public TimedTest(String p) {
 		super(p);
 	}
 	
 	public static synchronized void addTime(long p) {
 		time = time + p;
 		number++;
 	}
 	
	public static long getTime() {
		return time;
	}

	public static int getNumber() {
		return number;
	}
 
}