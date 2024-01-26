package com.wenge.kafka;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HAO灏 2022/11/15 22:27
 */
@Getter
@Setter
@EnableKafka
@Component
@ConfigurationProperties(prefix = "spring.kafka.one")
public class KafkaConfigOne {
	private List<String> bootstrapServers;

	@Bean
	public KafkaTemplate<String, String> kafkaOneTemplate() {
		return new KafkaTemplate<>(this.producerFactory());
	}

	@Bean
	KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, String>> kafkaOneContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(this.consumerFactory());
		return factory;
	}

	private ProducerFactory<String, String> producerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		//props.put(ProducerConfig.LINGER_MS_CONFIG, this.producer.getLingerMs());
		//props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, this.producer.getMaxRequestSize());
		//props.put(ProducerConfig.BATCH_SIZE_CONFIG, this.producer.getBatchSize());
		//props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, this.producer.getBufferMemory());
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return new DefaultKafkaProducerFactory<>(props);
	}

	private ConsumerFactory<Integer, String> consumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		//props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, this.consumer.getEnableAutoCommit());
		//props.put(ConsumerConfig.GROUP_ID_CONFIG, this.consumer.getGroupId());
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(props);
	}

}
