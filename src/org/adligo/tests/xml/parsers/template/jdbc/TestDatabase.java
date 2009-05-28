package org.adligo.tests.xml.parsers.template.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.adligo.i.log.client.Log;
import org.adligo.i.log.client.LogFactory;
import org.adligo.models.params.client.Param;
import org.adligo.xml.parsers.template.Template;
import org.adligo.xml.parsers.template.Templates;
import org.adligo.xml.parsers.template.jdbc.JdbcTemplateParserEngine;
import org.adligo.xml.parsers.template.jdbc.JdbcTemplateParserValues;

public class TestDatabase {
	private static final Log log = LogFactory.getLog(TestDatabase.class);
	private static boolean createdDb = false;
	
	public static Connection getMemConnection() throws SQLException {
		
		return DriverManager.getConnection("jdbc:hsqldb:mem:aname", "sa", "");
	}
	
	public static synchronized void createTestDb() throws SQLException {
		if (!createdDb) {
			try {
				Class.forName("org.hsqldb.jdbcDriver").newInstance();
			} catch (Exception x) {
				x.printStackTrace();
			}
			Connection con = getMemConnection();
			Templates templates = new Templates();
			templates.parseResource("/org/adligo/tests/xml/parsers/template/jdbc/CreateTestDb.xml");
			
			JdbcTemplateParserValues values = new JdbcTemplateParserValues();
			values.setParams(new Param());
			values.setConnection(con);
			
			Iterator<String> names = templates.getTemplateNames();
			while (names.hasNext()) {
				executeTemplate(templates, values, names.next());
			}
			createdDb = true;
		}
		
	}

	private static void executeTemplate(Templates templates,
			JdbcTemplateParserValues values, String templateName) throws SQLException {
		log.info("executing template " + templateName);
		
		Template temp = templates.getTemplate(templateName);
		values.setTemplate(temp);
		JdbcTemplateParserEngine.execute(values);
	}

	
}
