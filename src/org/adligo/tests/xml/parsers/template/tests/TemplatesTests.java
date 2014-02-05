package org.adligo.tests.xml.parsers.template.tests;

import org.adligo.tests.ATest;
import org.adligo.xml.parsers.template.Templates;

public class TemplatesTests extends ATest {

	public void testCarrots() {
		Templates templates = new Templates("/org/adligo/tests/xml/parsers/template/tests/Carrots.xml", true);
		
		IllegalArgumentException caught = null;
		try {
			templates.getTemplate("not_there");
		} catch (IllegalArgumentException x) {
			caught = x;
		}
		assertNotNull(caught);
		assertEquals("No template found with name 'not_there'", caught.getMessage());
	}
	
}
