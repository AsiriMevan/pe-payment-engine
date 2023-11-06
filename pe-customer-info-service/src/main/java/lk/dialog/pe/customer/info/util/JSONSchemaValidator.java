package lk.dialog.pe.customer.info.util;

import java.util.Optional;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONSchemaValidator {

	private JSONSchemaValidator() {}
	private static final Logger LOGGER = LoggerFactory.getLogger(JSONSchemaValidator.class);

	public static Optional<String> validateJSON(Object obj, PaymentDelegationEnum schemaName, String traceId) {
		
		LOGGER.info("validateJSONRequest : traceId={}", traceId);

		JSONObject jsonSchema = new JSONObject(new JSONTokener(JSONSchemaValidator.class.getResourceAsStream("/jsonschemas/" + schemaName.getName() + ".json")));
        JSONObject jsonSubject = new JSONObject(obj); 
        StringBuilder errorMessages = new StringBuilder();
        
        //validate schema
        try {
        Schema schema = SchemaLoader.load(jsonSchema);
        schema.validate(jsonSubject);
        } catch (ValidationException e) {
			LOGGER.error("validateJSONResponse : traceId={}", traceId, e);
			e.getCausingExceptions().stream().forEach(ex -> 
				errorMessages.append(e.getCausingExceptions().indexOf(ex) != 0 ? "; " : "").append(ex.getMessage())
			);
			errorMessages.append(e.getMessage());
			return Optional.of(errorMessages.toString());
        }
		return Optional.empty();
	}

}