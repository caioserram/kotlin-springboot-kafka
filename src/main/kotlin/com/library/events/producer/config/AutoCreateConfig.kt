package com.library.events.producer.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.kafka.config.TopicBuilder

@Configuration
class AutoCreateConfig {

    @Bean
    fun libraryEvents() =
        TopicBuilder.name("library-events")
                    .partitions(3)
                    .replicas(3)
                    .build()
}