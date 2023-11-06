package lk.dialog.pe.common.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import lk.dialog.pe.common.exception.DCPEException;

/**
 * 
 * @author Nishedha
 *
 */
@Component
public class JSONSchemaValidator {
	private static final Logger LOGGER = LoggerFactory.getLogger(JSONSchemaValidator.class);
	private Map<String, JsonSchema> jsonMap = new HashMap<>();

	private void readJSONSchemaFile(String schemaName) throws IOException, ProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		java.io.InputStream inputStream = JSONSchemaValidator.class.getResourceAsStream("/jsonschemas/" +schemaName+ ".json");
		JsonNode schemaContentNode = mapper.readTree(inputStream);
		JsonSchema jsonSchema = JsonSchemaFactory.byDefault().getJsonSchema(schemaContentNode);
		LOGGER.info("json file loaded : schemaName={}", schemaName);
		jsonMap.put(schemaName, jsonSchema);
	}
	
	
	private JsonSchema getJSONSchemaFile(String schemaName) throws ProcessingException, IOException {
		JsonSchema jsonSchema = null;
		if (jsonMap != null) {
			jsonSchema = jsonMap.get(schemaName);
			if (jsonSchema == null) {
				readJSONSchemaFile(schemaName);
				jsonSchema = jsonMap.get(schemaName);
			}
		}
		return jsonSchema;
	}
	
	public String validateJSON(long timeTaken, Object obj, String schemaName) throws DCPEException, IOException, ProcessingException {
		LOGGER.info("Timetaken={}", timeTaken);
		ObjectMapper mapper = new ObjectMapper();
		StringBuilder stringValidtionError = new StringBuilder();
		try {
			String jsonInString = mapper.writeValueAsString(obj);
			LOGGER.info("validateJSONRequest : request={}", jsonInString);
			JsonNode inJsonRequest = mapper.readTree(jsonInString);

			JsonSchema jsonSchema = getJSONSchemaFile(schemaName);
			if (jsonSchema == null) {
				throw new DCPEException(Constants.MSG_INVALID_SCHEMA);
			}
			ProcessingReport errors = jsonSchema.validate(inJsonRequest);
			Iterator<ProcessingMessage> errorList = errors.iterator();
			while (errorList.hasNext()) {
				ProcessingMessage msg = errorList.next();
				stringValidtionError.append(";" + msg.getMessage() + "," + msg.asJson().get("instance").get("pointer"));
				LOGGER.error("validateJSON : ErrorMsg={}", msg.getMessage());
			}

		} catch (ProcessingException | IOException ex) {
			LOGGER.error("validateJSONResponse : response={}", ex.getMessage());
			throw ex;
		}
		if (stringValidtionError.indexOf(";") == 0) {
			stringValidtionError.deleteCharAt(0);
		}
		return stringValidtionError.toString().trim();

	}

}