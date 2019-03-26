package io.scadatom.nucleus.config;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

  public static final String TOPIC_SCADATOM = "topic_scadatom";
  public static final String ROUTING_TO_ELECTRON = "to_electron_";
  public static final String ROUTING_TO_NUCLEUS = "to_nucleus";

  @Bean
  public TopicExchange topicScadatom() {
    return new TopicExchange(TOPIC_SCADATOM);
  }

  @Bean
  public Queue queueInboundRequest() {
    return new AnonymousQueue();
  }

  @Bean
  public Binding bindingIncomingRequest(TopicExchange topicScadatom, Queue queueInboundRequest) {
    return BindingBuilder.bind(queueInboundRequest).to(topicScadatom).with(ROUTING_TO_NUCLEUS);
  }
}
