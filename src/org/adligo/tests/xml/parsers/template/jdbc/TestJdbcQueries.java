package org.adligo.tests.xml.parsers.template.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.adligo.i.log.client.Log;
import org.adligo.i.log.client.LogFactory;
import org.adligo.models.params.client.I_Operators;
import org.adligo.models.params.client.Operators;
import org.adligo.models.params.client.Param;
import org.adligo.models.params.client.Params;
import org.adligo.models.params.client.SqlOperators;
import org.adligo.tests.ATest;
import org.adligo.xml.parsers.template.Template;
import org.adligo.xml.parsers.template.TemplateParserEngine;
import org.adligo.xml.parsers.template.Templates;
import org.adligo.xml.parsers.template.jdbc.BaseSqlOperators;
import org.adligo.xml.parsers.template.jdbc.JdbcTemplateParserEngine;
import org.adligo.xml.parsers.template.jdbc.JdbcEngineInput;
import org.adligo.xml.parsers.template.jdbc.PrettyParamDecorator;


public class TestJdbcQueries extends ATest {
	private static final Log log = LogFactory.getLog(TestJdbcQueries.class);
	Templates templates = new Templates();
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	
	public void setUp() throws SQLException {
		TestDatabase.createTestDb();
		templates.parseResource("/org/adligo/tests/xml/parsers/template/jdbc/Persons2_0_SQL.xml");
	}
	
	
	private ResultSet getResultSet(Params params, Template template)
			throws SQLException {
		JdbcEngineInput jdbcValues = new JdbcEngineInput();
		jdbcValues.setAllowedOperators(BaseSqlOperators.OPERATORS);
		jdbcValues.setParams(params);
		
		jdbcValues.setTemplate(template);
		jdbcValues.setConnection(TestDatabase.getMemConnection());
		ResultSet rs = JdbcTemplateParserEngine.query(jdbcValues);
		return rs;
	}
	
	private void prettySql(Params params, Template personsTemp) {
		String prettySql = TemplateParserEngine.parse(personsTemp, 
				new PrettyParamDecorator(params));
		log.info("pretty sql for a log message if your query blows up;\n\n" +
				prettySql + "\n\n");
	}
	
	private void assertJohnDoe(Params params, Template template) throws SQLException {
		ResultSet rs = getResultSet(params, template);
		
		assertNotNull(rs);
		assertTrue(rs.next());
		
		assertEquals(1, rs.getInt(1));
		assertEquals(0, rs.getInt(2));
		assertEquals(1, rs.getInt(4));
		assertEquals("john", rs.getString(5));
		assertNull(rs.getString(6));
		assertEquals("doe",rs.getString(7));
		
		assertFalse(rs.next());
	}
	
	private void assertLisa(Params params, Template template) throws SQLException {
		ResultSet rs = getResultSet(params, template);
		
		assertNotNull(rs);
		assertTrue(rs.next());
		
		assertEquals(2, rs.getInt(1));
		assertEquals(0, rs.getInt(2));
		assertEquals(1, rs.getInt(4));
		assertEquals("lisa", rs.getString(5));
		assertNull(rs.getString(6));
		assertEquals("Smith",rs.getString(7));
		
		assertFalse(rs.next());
	}
	
	public void testStringQuery() throws Exception {
		Params params = new Params();
		params.addParam("default");
		Params where_params = params.addWhereParams();
		where_params.addParam("fname",SqlOperators.EQUALS, "john");
		
		Template personsTemp = templates.getTemplate("persons");
		prettySql(params, personsTemp);
		assertJohnDoe(params, personsTemp);
	}
	
	public void testShortQuery() throws Exception {
		Params params = new Params();
		params.addParam("default");
		Params where_params = params.addWhereParams();
		where_params.addParam("id",SqlOperators.EQUALS, (short) 1);
		
		Template personsTemp = templates.getTemplate("persons");
		prettySql(params, personsTemp);
		assertJohnDoe(params, personsTemp);
	}
	
	public void testIntQuery() throws Exception {
		Params params = new Params();
		params.addParam("default");
		Params where_params = params.addWhereParams();
		where_params.addParam("id",SqlOperators.EQUALS,  1);
		
		Template personsTemp = templates.getTemplate("persons");
		prettySql(params, personsTemp);
		assertJohnDoe(params, personsTemp);
	}
	
	public void testLongQuery() throws Exception {
		Params params = new Params();
		params.addParam("default");
		Params where_params = params.addWhereParams();
		where_params.addParam("id",SqlOperators.LESS_THAN_EQUALS, (long) 1);
		
		Template personsTemp = templates.getTemplate("persons");
		prettySql(params, personsTemp);
		assertJohnDoe(params, personsTemp);
		
	}

	public void testLongQuery2() throws Exception {
		Params params = new Params();
		params.addParam("default");
		Params where_params = params.addWhereParams();
		where_params.addParam("id",SqlOperators.LESS_THAN_EQUALS, (long) 100000000 * 1000);
		
		Template personsTemp = templates.getTemplate("persons");
		prettySql(params, personsTemp);
		ResultSet rs = getResultSet(params, personsTemp);
		assertTrue(rs.next());
		assertTrue(rs.next());
		assertTrue(rs.next());
		assertFalse(rs.next());
	}
	
	public void testDateQuery() throws Exception {
		Params params = new Params();
		params.addParam("default");
		Params where_params = params.addWhereParams();
		where_params.addParam("edited",SqlOperators.EQUALS, sdf.parse("12/14/2007"));
		
		Template personsTemp = templates.getTemplate("persons");
		prettySql(params, personsTemp);
		assertLisa(params, personsTemp);
		
		
	}

	public void testBooleanQuery() throws Exception {
		Params params = new Params();
		params.addParam("default");
		Params where_params = params.addWhereParams();
		where_params.addParam("is_alive",SqlOperators.EQUALS, true);
		
		Template personsTemp = templates.getTemplate("persons");
		prettySql(params, personsTemp);
		//should have two people alive
		ResultSet rs = getResultSet(params, personsTemp);
		assertTrue(rs.next());
		assertTrue(rs.next());
		assertFalse(rs.next());
		
		
	}
	
	public void testFloatQuery() throws Exception {
		Params params = new Params();
		params.addParam("default");
		Params where_params = params.addWhereParams();
		//note there actually seems to be a issue in the hsqldb
		// with floating point precision here, when I use the equlas operator
		// this returns false
		where_params.addParam("height",SqlOperators.LESS_THAN_EQUALS, (float) 2.23);
		
		Template personsTemp = templates.getTemplate("persons");
		prettySql(params, personsTemp);
		//should have two people alive
		assertLisa(params, personsTemp);
	}
	
	public void testDoubleQuery() throws Exception {
		Params params = new Params();
		params.addParam("default");
		Params where_params = params.addWhereParams();
		where_params.addParam("height",SqlOperators.EQUALS, (double) 2.23);
		
		Template personsTemp = templates.getTemplate("persons");
		prettySql(params, personsTemp);
		//should have two people alive
		assertLisa(params, personsTemp);
	}
	
	public void testTwoStringQuery() throws Exception {
		Params params = new Params();
		params.addParam("default");
		Params where_params = params.addWhereParams();
		where_params.addParam("fname",SqlOperators.EQUALS, "john");
		where_params.addParam("fname",SqlOperators.EQUALS, "lisa");
		
		Template personsTemp = templates.getTemplate("persons");
		prettySql(params, personsTemp);
		ResultSet rs = getResultSet(params, personsTemp);
		//shold have two rows
		assertTrue(rs.next());
		assertTrue(rs.next());
		assertFalse(rs.next());
		
	}
	
	public void testInQuery() throws Exception {
		Params params = new Params();
		params.addParam("default");
		Params where_params = params.addWhereParams();
		Param idParam = where_params.addParam("id",SqlOperators.IN);
		idParam.addValue(1);
		idParam.addValue(2);
		idParam.addValue(3);
		
		Template personsTemp = templates.getTemplate("persons");
		prettySql(params, personsTemp);
		ResultSet rs = getResultSet(params, personsTemp);
		//shold have three rows
		assertTrue(rs.next());
		assertTrue(rs.next());
		assertTrue(rs.next());
		assertFalse(rs.next());
	}
}
