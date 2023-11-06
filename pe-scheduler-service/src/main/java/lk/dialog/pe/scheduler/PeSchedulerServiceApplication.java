package lk.dialog.pe.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.dialog.pe.scheduler.service.JobConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.util.Collections;

@SpringBootApplication(
		scanBasePackages = {"lk.dialog.pe.credit.cam","lk.dialog.pe.scheduler","lk.dialog.pe.common"},
		exclude = {
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class
}
		)
@PropertySource({"classpath:/cam-api-urls.properties","classpath:/cam-application.properties","classpath:/cam-application.properties","classpath:/scheduler-config.properties","classpath:ccbs-sms.properties"})
@EnableSwagger2
@EnableCaching
public class PeSchedulerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeSchedulerServiceApplication.class, args);
	}

	@Bean
	public ObjectMapper mapper() {
	  return new ObjectMapper();
	}

	@Autowired
	JobConfigService jobConfigService;

	@Bean
	public Docket swaggerConfiguration() {
		// Prepare and return a Docket instance
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("lk.dialog.pe.billing.api")).build().apiInfo(getApiDetails());
	}

	private ApiInfo getApiDetails() {
		return new ApiInfo("Payment Engine API", "Payment Engine API documentation", "1.0", "", new Contact("Nadisha Bandara", "https://digiratina.com/", "nadisha.bandara@digiratina.com"), "API License",
				"https://digiratina.com/", Collections.emptyList());

	}

	@PostConstruct
	private void postConstruct() {
		jobConfigService.initializeJobConfigData();
	}

}
