package lk.dialog.pe.scheduler.config;

import lk.dialog.pe.scheduler.util.SchUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@Configuration
public class KafkaPublishConfiguration {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Value("${spring.kafka.bootstrap-servers}")
    String boosterServersConfig;
    @Value("${spring.kafka.producer.retries}")
    int retriesConfig;
    @Value("${spring.kafka.producer.batch-size}")
    int batchSizeConfig;
    @Value("${spring.kafka.producer.linger-ms}")
    int lingerMsConfig;
    @Value("${spring.kafka.producer.buffer-memory}")
    int bufferMemoryConfig;

    @Value("${spring.kafka.security.protocol}")
    String securityProtocol;
    @Value("${spring.kafka.sasl.kerberos.service.name}")
    String kafkaServiceName;

    @Bean
    public ProducerFactory<Integer, String> producerFactory() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties());

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,boosterServersConfig);
        props.put(ProducerConfig.RETRIES_CONFIG,retriesConfig);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG,batchSizeConfig);
        props.put(ProducerConfig.LINGER_MS_CONFIG,lingerMsConfig);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG,bufferMemoryConfig);

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        props.put("security.protocol",securityProtocol);
        props.put("sasl.kerberos.service.name",kafkaServiceName);

        String kafkaConfigsStr =SchUtil.convertToString(props);
        log.info("Kafka Producer configurations loaded:{}",kafkaConfigsStr);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<Integer, String> kafkaTemplate() {
        return new KafkaTemplate<Integer, String>(producerFactory());
    }}
