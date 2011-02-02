package org.adligo.tests.xml.parsers.template;

import org.adligo.tests.ATest;
import org.adligo.xml.parsers.template.Templates;

public class TestLoadExceptions extends ATest {

	public void testLoadResourceAndFile() {
		Exception caught = null;
		try {
			Templates templates = new Templates("none_found");
		} catch (Exception x) {
			caught = x;
		}
		assertNotNull(caught);
		assertTrue(caught instanceof IllegalArgumentException);
		assertEquals(Templates.CANNOT_ACCESS_FILE + "none_found", caught.getMessage());
		
		caught = null;
		try {
			Templates templates = new Templates("none_found", true);
		} catch (Exception x) {
			caught = x;
		}
		assertNotNull(caught);
		assertTrue(caught instanceof IllegalArgumentException);
		assertEquals(Templates.THERE_WAS_A_PROBLEM_PARSING_OR_COULD_NOT_FIND_RESOURCE + "none_found", caught.getMessage());
		
		caught = null;
		try {
			Templates templates = new Templates("none_found", false);
		} catch (Exception x) {
			caught = x;
		}
		assertNotNull(caught);
		assertTrue(caught instanceof IllegalArgumentException);
		assertEquals(Templates.CANNOT_ACCESS_FILE + "none_found", caught.getMessage());
		
		Templates templates = new Templates();
		caught = null;
		try {
			templates.parseFile("none_found");
		} catch (Exception x) {
			caught = x;
		}
		assertNotNull(caught);
		assertTrue(caught instanceof IllegalArgumentException);
		assertEquals(Templates.CANNOT_ACCESS_FILE + "none_found", caught.getMessage());
		
		caught = null;
		try {
			templates.parseResource("none_found");
		} catch (Exception x) {
			caught = x;
		}
		assertNotNull(caught);
		assertTrue(caught instanceof IllegalArgumentException);
		assertEquals(Templates.THERE_WAS_A_PROBLEM_PARSING_OR_COULD_NOT_FIND_RESOURCE + "none_found",
				caught.getMessage());
		
		caught = null;
		try {
			templates.parseResource("none_found", "\n\r");
		} catch (Exception x) {
			caught = x;
		}
		assertNotNull(caught);
		assertTrue(caught instanceof IllegalArgumentException);
		assertEquals(Templates.THERE_WAS_A_PROBLEM_PARSING_OR_COULD_NOT_FIND_RESOURCE + "none_found",
				caught.getMessage());
		
	}
}
