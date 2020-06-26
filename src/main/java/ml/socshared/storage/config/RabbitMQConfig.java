package ml.socshared.storage.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ml.socshared.storage.service.sentry.SentryService;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.listener.FatalExceptionStrategy;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    public final static String QUEUE_PUBLICATION_NAME = "socshared-publications-queue";
    public final static String EXCHANGE_NAME = "socshared-publication";
    public final static String ROUTING_KEY = "12345";

    private final SentryService sentryService;

    @Bean
    public TopicExchange appExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue appQueuePublication() {
        return new Queue(QUEUE_PUBLICATION_NAME);
    }

    @Bean
    public Binding declareBindingPublication() {
        return BindingBuilder.bind(appQueuePublication()).to(appExchange()).with(ROUTING_KEY);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
            SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setErrorHandler(errorHandler());
        return factory;
    }

    @Bean
    public ErrorHandler errorHandler() {
        return new ConditionalRejectingErrorHandler(customExceptionStrategy());
    }

    @Bean
    FatalExceptionStrategy customExceptionStrategy() {
        return new CustomFatalExceptionStrategy(sentryService);
    }

    @Slf4j
    @RequiredArgsConstructor
    public static class CustomFatalExceptionStrategy
            extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {

        private final SentryService sentryService;

        @Override
        public boolean isFatal(Throwable t) {
            log.error(t.getMessage());
            if (t instanceof IOException) {
                return true;
            }
            sentryService.logException(t);
            return super.isFatal(t);
        }
    }
}
