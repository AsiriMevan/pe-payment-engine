package lk.dialog.pe.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import lk.dialog.pe.common.dto.DCPEResponse;
import lk.dialog.pe.common.exception.DCPEException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import com.fasterxml.jackson.core.type.TypeReference;

@Service
public class RestExecutor {

	@Autowired
	@Qualifier("restExecutorTemplate")
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	private static final Logger LOGGER = LoggerFactory.getLogger(RestExecutor.class);

	public <T> T get(String uri, String traceId, final Class<T> responseType) throws DCPEException {

		String requestString = DCPEUtil.convertToString(responseType);
		LOGGER.info("getRequest : traceId={}|requestString={}", traceId, requestString);
		Instant start = Instant.now();
		uri = setTraceId(uri, traceId);
		T response = null;

		ResponseEntity<DCPEResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, generateHttpEntity(null), DCPEResponse.class);
		validateResponse(responseEntity, traceId);
			if (responseEntity.getBody() != null) {
				response = objectMapper.convertValue(responseEntity.getBody().getData(), responseType);
			}
		

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(response);
		LOGGER.info("get-Response : traceId={}|timeTaken={}|body={}", traceId, timeTaken, responseString);

		return response;
	}

	public <T> T post(String uri, String traceId, Object requestObject, final Class<T> responseType) throws DCPEException {

		String requestString = DCPEUtil.convertToString(requestObject);
		LOGGER.info("postRequest : traceId={}|requestString={}", traceId, requestString);
		Instant start = Instant.now();
		uri = setTraceId(uri, traceId);

		ResponseEntity<DCPEResponse> responseEntity = null;
		T response = null;
		try {
			responseEntity = restTemplate.exchange(uri, HttpMethod.POST, generateHttpEntity(requestObject), DCPEResponse.class);
			validateResponse(responseEntity, traceId);

			if (responseEntity.getBody() != null) {
				response = objectMapper.convertValue(responseEntity.getBody().getData(), responseType);
				Long timeTaken = DCPEUtil.getTimeTaken(start);
				String responseString = DCPEUtil.convertToString(response);
				LOGGER.info("getResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, responseString);
			}

			return response;

		} catch (DCPEException e) {
			throw e;
		} catch (Exception ex) {
			throw ex;

		}

	}

	public <T> T post(String uri, String traceId, Object requestObject, TypeReference<T> responseType) throws DCPEException {

		String requestString = DCPEUtil.convertToString(requestObject);

		Instant start = Instant.now();
		uri = setTraceId(uri, traceId);
		LOGGER.info("postRequest : traceId={} | URI={} |requestString={} ", traceId, uri, requestString);

		ResponseEntity<DCPEResponse> responseEntity = null;
		T response = null;
		try {
			responseEntity = restTemplate.exchange(uri, HttpMethod.POST, generateHttpEntity(requestObject), DCPEResponse.class);
			validateResponse(responseEntity, traceId);

			if (responseEntity.getBody() != null) {
				response = objectMapper.convertValue(responseEntity.getBody().getData(), responseType);
				Long timeTaken = DCPEUtil.getTimeTaken(start);
				String responseString = DCPEUtil.convertToString(response);
				LOGGER.info("getResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, responseString);
			}

			return response;

		} catch (DCPEException e) {
			throw e;
		} catch (Exception ex) {
			throw ex;

		}

	}

	public <T> T put(String uri, String traceId, Object requestObject, final Class<T> responseType) throws DCPEException {

		String requestString = DCPEUtil.convertToString(responseType);
		LOGGER.info("putRequest : traceId={}|requestString={}", traceId, requestString);
		Instant start = Instant.now();

		uri = setTraceId(uri, traceId);

		ResponseEntity<DCPEResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, generateHttpEntity(requestObject), DCPEResponse.class);

		validateResponse(responseEntity, traceId);

		T response = objectMapper.convertValue(responseEntity.getBody().getData(), responseType);
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(response);
		LOGGER.info("putResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, responseString);
		return response;
	}

	public <T> T delete(String uri, String traceId, Object requestObject, final Class<T> responseType) throws DCPEException {
		String requestString = DCPEUtil.convertToString(responseType);
		LOGGER.info("deleteRequest : traceId={}|requestString={}", traceId, requestString);
		Instant start = Instant.now();
		uri = setTraceId(uri, traceId);

		ResponseEntity<DCPEResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, generateHttpEntity(requestObject), DCPEResponse.class);

		validateResponse(responseEntity, traceId);

		T response = objectMapper.convertValue(responseEntity.getBody().getData(), responseType);
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(response);
		LOGGER.info("putResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, responseString);
		return response;
	}

	private boolean validateResponse(ResponseEntity<DCPEResponse> responseEntity, String traceId) throws DCPEException {

		String requestString = DCPEUtil.convertToString(responseEntity.getBody());

		LOGGER.info("validateRequest : traceId={}|responseEntity={}", traceId, requestString);
		Optional<DCPEResponse> response = Optional.of(responseEntity.getBody());

		if (response.get().getStatusCode() != DCPEErrorEnum.SUCCESS.status && response.get().getStatusCode() != HttpStatus.OK.value()) {
			LOGGER.error("validateResponse Fail : traceId={}", traceId);
			throw new DCPEException(response.get().getMessage(), response.get().getStatusCode());
		}

		LOGGER.info("validateResponse : traceId={}|messsage={}| isValid={}", traceId,response.get().getMessage(),true);
		return true;

	}

	private HttpEntity<?> generateHttpEntity(Object requestObject) {
		final HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType((MediaType.APPLICATION_JSON));
		return new HttpEntity<>(requestObject, headers);
	}

	private String setTraceId(String url, String traceId) {
		return url.replace("{traceId}", traceId);
	}

	public <T> T getRequest(String uri, String traceId, final Class<T> responseType) throws DCPEException {
		String requestString = DCPEUtil.convertToString(responseType);
		LOGGER.info("getRequest : traceId={}|requestString={}", traceId, requestString);
		Instant start = Instant.now();
		uri = this.setTraceId(uri, traceId);
		T response = null;
		ResponseEntity<Object> responseEntity = this.restTemplate.exchange(uri, HttpMethod.GET, this.generateHttpEntity((Object)null), Object.class, new Object[0]);
		LOGGER.info("get-Response main : traceId={}|body={}", new Object[]{traceId, responseEntity});
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(responseEntity);
		LOGGER.info("get-Response : traceId={}|timeTaken={}|body={}", new Object[]{traceId, timeTaken, responseString});
		return response;
	}


}
