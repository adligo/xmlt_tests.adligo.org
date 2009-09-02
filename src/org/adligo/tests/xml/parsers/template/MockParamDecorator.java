package org.adligo.tests.xml.parsers.template;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.adligo.models.params.client.I_TemplateParams;
import org.adligo.models.params.client.ParamDecorator;

public class MockParamDecorator extends ParamDecorator {

	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	/**
	 * 
	 */
	private static final long serialVersionUID = -712990651675949276L;

	public MockParamDecorator(I_TemplateParams delegate) {
		super(delegate);
	}
	
	@Override
	public Object[] getValues() {
		Object [] values = super.getDelegate().getValues();
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				Object value = values[i];
				if (value instanceof String) {
					// this would also call the 
					// I_QueryLanguageDescriptor
					// to clean out the string,
					// but would only do this
					// for returning a representation
					// of the query to the client or to 
					// a log
					// but this is just for the vanilla tests
					values[i] = "'" + value + "'";
				} else if (value instanceof Date) {
					// this would actually get set by jdbc
					// but since the tests (0-6) were orignally written 
					// for just text replacement I am keeping 
					// them backwards comptable
					values[i] = "'" + sdf.format((Date) value) + "'";
				}
			}
		}
		return values;
	}

	@Override
	public I_TemplateParams getNestedParams() {
		// TODO Auto-generated method stub
		return new MockParamDecorator(super.getNestedParams());
	}

}
