package com.chua.distributions.deserializer.json;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.chua.distributions.beans.SalesReportQueryBean;
import com.chua.distributions.enums.Warehouse;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	9 Feb 2017
 */
public class SalesReportQueryDeserializer extends StdDeserializer<SalesReportQueryBean> {

	private static final long serialVersionUID = 1430257213484900758L;

	public SalesReportQueryDeserializer() {
		this(null);
	}
	
	public SalesReportQueryDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public SalesReportQueryBean deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		final SalesReportQueryBean srq = new SalesReportQueryBean();
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			srq.setFrom(sdf.parse(node.get("from").asText()));
			srq.setTo(sdf.parse(node.get("to").asText()));
		} catch (NullPointerException e) {
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		JsonNode warehouseNode = node.get("warehouse");
		srq.setWarehouse(warehouseNode != null ? Warehouse.valueOf(warehouseNode.asText()) : null);
		JsonNode clientIdNode = node.get("clientId");
		srq.setClientId(clientIdNode != null ? clientIdNode.asLong() : null);
		
		JsonNode includePaidNode = node.get("includePaid");
		srq.setIncludePaid(includePaidNode != null ? includePaidNode.asBoolean() : null);
		JsonNode includeDeliveredNode = node.get("includeDelivered");
		srq.setIncludeDelivered(includeDeliveredNode != null ? includeDeliveredNode.asBoolean() : null);
		JsonNode includeDispatchedNode = node.get("includeDispatched");
		srq.setIncludeDispatched(includeDispatchedNode != null ? includeDispatchedNode.asBoolean() : null);
		JsonNode includeToFollowNode = node.get("includeToFollow");
		srq.setIncludeToFollow(includeToFollowNode != null ? includeToFollowNode.asBoolean() : null);
		JsonNode includeAcceptedNode = node.get("includeAccepted");
		srq.setIncludeAccepted(includeAcceptedNode != null ? includeAcceptedNode.asBoolean() : null);
		JsonNode includeSubmittedNode = node.get("includeSubmitted");
		srq.setIncludeSubmitted(includeSubmittedNode != null ? includeSubmittedNode.asBoolean() : null);
		JsonNode includeCreatingNode = node.get("includeCreating");
		srq.setIncludeCreating(includeCreatingNode != null ? includeCreatingNode.asBoolean() : null);
		JsonNode showNetTrailNode = node.get("showNetTrail");
		srq.setShowNetTrail(showNetTrailNode != null ? showNetTrailNode.asBoolean() : null);
		JsonNode sendMailNode = node.get("sendMail");
		srq.setSendMail(sendMailNode != null ? sendMailNode.asBoolean() : null);
		JsonNode downloadFileNode = node.get("downloadFile");
		srq.setDownloadFile(downloadFileNode != null ? downloadFileNode.asBoolean() : null);
		
		return srq;
	}
}
