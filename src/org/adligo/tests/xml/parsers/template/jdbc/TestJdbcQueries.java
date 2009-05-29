package org.adligo.tests.xml.parsers.template.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.adligo.i.log.client.Log;
import org.adligo.i.log.client.LogFactory;
import org.adligo.models.params.client.I_Operators;
import org.adligo.models.params.client.Operators;
import org.adligo.models.params.client.Params;
import org.adligo.tests.ATest;
import org.adligo.xml.parsers.template.Template;
import org.adligo.xml.parsers.template.TemplateParserEngine;
import org.adligo.xml.parsers.template.Templates;
import org.adligo.xml.parsers.template.jdbc.JdbcTemplateParserEngine;
import org.adligo.xml.parsers.template.jdbc.JdbcTemplateParserValues;
import org.adligo.xml.parsers.template.jdbc.PrettyParamDecorator;


public class TestJdbcQueries extends ATest {
	private static final Log log = LogFactory.getLog(TestJdbcQueries.class);
	private Set<I_Operators> operators = getAllOperators();
	Templates templates = new Templates();
	
	public Set<I_Operators> getAllOperators() {
		Set<I_Operators> toRet = new HashSet<I_Operators>();
		toRet.add(new Operators("="));
		
		return Collections.unmodifiableSet(toRet);
	}
	
	public void setUp() throws SQLException {
		TestDatabase.createTestDb();
		templates.parseResource("/org/adligo/tests/xml/parsers/template/jdbc/Persons2_0_SQL.xml");
	}
	
	public void testQuery() throws Exception {
		Params params = new Params();
		params.addParam("default");
		Params where_params = params.addWhereParams();
		where_params.addParam("fname","=", "john");
		
		JdbcTemplateParserValues jdbcValues = new JdbcTemplateParserValues();
		jdbcValues.setAllowedOperators(operators);
		jdbcValues.setParams(params);
		Template personsTemp = templates.getTemplate("persons");
		jdbcValues.setTemplate(personsTemp);
		jdbcValues.setConnection(TestDatabase.getMemConnection());
		ResultSet rs = JdbcTemplateParserEngine.query(jdbcValues);
		
		assertNotNull(rs);
		assertTrue(rs.next());
		
		assertEquals(1, rs.getInt(1));
		assertEquals(0, rs.getInt(2));
		assertEquals(1, rs.getInt(4));
		assertEquals("john", rs.getString(5));
		assertNull(rs.getString(6));
		assertEquals("doe",rs.getString(7));
		
		assertFalse(rs.next());
		
		String prettySql = TemplateParserEngine.parse(personsTemp, 
				new PrettyParamDecorator(params));
		log.info("some log message if your query blows up = " +
				prettySql);
	}
}
