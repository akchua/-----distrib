package com.chua.distributions.deserializer.json;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Apr 20, 2018
 */
public class MonthDeserializer extends StdDeserializer<Date> {

	private static final long serialVersionUID = -8220567522773187137L;

	public MonthDeserializer() {
		this(null);
	}
	
	public MonthDeserializer(Class<?> vc) {
		super(vc);
	}
	
	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		final Date date;
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		
		try {
			date = sdf.parse(node.asText());
			return date;
		} catch (NullPointerException e) {
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
