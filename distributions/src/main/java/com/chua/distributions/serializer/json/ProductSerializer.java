package com.chua.distributions.serializer.json;

import java.io.IOException;

import com.chua.distributions.database.entity.Product;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 19, 2016
 */
public class ProductSerializer extends JsonSerializer<Product> {

	@Override
	public void serialize(Product product, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException, JsonProcessingException {
		jsonGenerator.writeObject(product);
	}
}
