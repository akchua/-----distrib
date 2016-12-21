package com.chua.distributions.serializer.json;

import java.io.IOException;

import com.chua.distributions.database.entity.PurchaseOrder;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 21, 2016
 */
public class PurchaseOrderSerializer extends JsonSerializer<PurchaseOrder> {

	@Override
	public void serialize(PurchaseOrder purchaseOrder, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException, JsonProcessingException {
		jsonGenerator.writeObject(purchaseOrder);
	}
}
