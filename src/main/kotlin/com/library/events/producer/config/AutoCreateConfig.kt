package com.library.events.producer.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.config.TopicBuilder

@Configuration
// Define que código deve ser executado apenas localmente
@Profile("local")
class AutoCreateConfig {

    // Cria tópico automaticamente
    @Bean
    fun libraryEvents() =
        TopicBuilder.name("library-events")
                    .partitions(3)
                    .replicas(3)
                    .build()
}