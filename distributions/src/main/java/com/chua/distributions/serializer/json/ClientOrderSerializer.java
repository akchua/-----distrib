package com.chua.distributions.serializer.json;

import java.io.IOException;

import com.chua.distributions.database.entity.ClientOrder;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 14, 2016
 */
public class ClientOrderSerializer extends JsonSerializer<ClientOrder> {

	@Override
	public void serialize(ClientOrder clientOrder, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException, JsonProcessingException {
		jsonGenerator.writeObject(clientOrder);
	}
}
