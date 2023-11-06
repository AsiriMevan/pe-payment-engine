package lk.dialog.pe.persistence;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = { "lk.dialog.pe.common", "lk.dialog.pe.persistence" })
@EnableSwagger2
public class PePersistenceServiceApplication {

	@Bean
	public Docket swaggerConfiguration() {
		// Prepare and return a Docket instance
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("lk.dialog.pe.persistence.api")).build()
				.apiInfo(getApiDetails());
	}

	private ApiInfo getApiDetails() {
		return new ApiInfo("Payment Engine API", "Payment Engine API documentation", "1.0", "",
				new Contact("Nadisha Bandara", "https://digiratina.com/", "nadisha.bandara@digiratina.com"),
				"API License", "https://digiratina.com/", Collections.emptyList());
	}

	public static void main(String[] args) {
		SpringApplication.run(PePersistenceServiceApplication.class, args);
	}

}
