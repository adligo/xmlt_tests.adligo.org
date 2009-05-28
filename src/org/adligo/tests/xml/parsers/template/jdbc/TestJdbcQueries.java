package org.adligo.tests.xml.parsers.template.jdbc;

import java.sql.SQLException;

import org.adligo.tests.ATest;

import junit.framework.TestCase;

public class TestJdbcQueries extends ATest {

	
	public void setUp() throws SQLException {
		TestDatabase.createTestDb();
	}
	
	public void testQuery() {
		
	}
}
