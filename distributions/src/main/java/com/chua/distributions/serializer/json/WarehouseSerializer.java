package com.chua.distributions.serializer.json;

import java.io.IOException;

import com.chua.distributions.database.entity.Warehouse;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   31 Aug 2017
 */
public class WarehouseSerializer extends JsonSerializer<Warehouse> {

	@Override
	public void serialize(Warehouse warehouse, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException, JsonProcessingException {
		jsonGenerator.writeObject(warehouse);
	}
}
