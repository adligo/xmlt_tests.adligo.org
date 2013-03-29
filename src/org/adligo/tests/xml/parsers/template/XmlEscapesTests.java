package org.adligo.tests.xml.parsers.template;

import org.adligo.models.params.client.Params;
import org.adligo.tests.ATest;
import org.adligo.xml.parsers.template.Template;
import org.adligo.xml.parsers.template.TemplateParserEngine;
import org.adligo.xml.parsers.template.Templates;

public class XmlEscapesTests extends ATest {

	public void testSimpleXmlEscapes() {
		Templates templates = new Templates("/org/adligo/tests/xml/parsers/template/Escapes.xml", true);
		Template temp = templates.getTemplate("simple");
		String result = TemplateParserEngine.parse(temp, new Params());
		assertEquals("><&", result);
		
		temp = templates.getTemplate("simple_foo");
		result = TemplateParserEngine.parse(temp, new Params());
		assertEquals(">>&", result);
		
		temp = templates.getTemplate("simple_bar");
		Params params = new Params();
		params.addParam("bar");
		result = TemplateParserEngine.parse(temp, params);
		assertEquals("&>>", result);
		
	}
	
	public void testCarrots() {
		Templates templates = new Templates("/org/adligo/tests/xml/parsers/template/Carrots.xml", true);
		Template temp = templates.getTemplate("simple_<>");
		String result = TemplateParserEngine.parse(temp, new Params());
		assertEquals("<>", result);
		
		temp = templates.getTemplate("simple_<");
		result = TemplateParserEngine.parse(temp, new Params());
		assertEquals("<", result);
		
		temp = templates.getTemplate("simple_>");
		Params params = new Params();
		params.addParam("bar");
		result = TemplateParserEngine.parse(temp, params);
		assertEquals(">", result);
		
		temp = templates.getTemplate("simple_><");
		result = TemplateParserEngine.parse(temp, new Params());
		assertEquals("><", result);
		
		temp = templates.getTemplate("ab");
		result = TemplateParserEngine.parse(temp, new Params());
		assertEquals("a <> b", result);
	}
	
}
