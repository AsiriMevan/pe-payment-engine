package lk.dialog.pe.retrieval.cancelation;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication(scanBasePackages = {"lk.dialog.pe.credit.cam","lk.dialog.pe","lk.dialog.pe.common"})
@PropertySource("classpath:/cam-application.properties")
@PropertySource("classpath:/cam-api-urls.properties")
public class PeRetrievalCancelationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeRetrievalCancelationServiceApplication.class, args);
	}

	@Bean
	public Docket swaggerConfiguration() {
		// Prepare and return a Docket instance
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("lk.dialog.pe.retrieval.cancelation.api")).build().apiInfo(getApiDetails());
	}

	private ApiInfo getApiDetails() {
		return new ApiInfo("Payment Engine API", "Payment Engine API documentation", "1.0", "", new Contact("Nadisha Bandara", "https://digiratina.com/", "nadisha.bandara@digiratina.com"), "API License",
				"https://digiratina.com/", Collections.emptyList());

	}
}
