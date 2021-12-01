package com.library.events.producer.producer

import com.fasterxml.jackson.databind.ObjectMapper
import com.library.events.producer.domain.LibraryEvent
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import org.springframework.util.concurrent.ListenableFuture

@Component
@Slf4j
class LibraryEventProducer {

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<Int, String>

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    var log: Logger = LoggerFactory.getLogger("LibraryEventProducer")

    fun sendLibraryEvent(libraryEvent: LibraryEvent) {
        val key = libraryEvent.libraryEventId
        val value = this.objectMapper.writeValueAsString(libraryEvent.book)
        val listenableFuture: ListenableFuture<SendResult<Int,String>> = kafkaTemplate.sendDefault(key, value)
        listenableFuture.addCallback(
            { result:SendResult<Int,String>? -> handleSuccess(key, value, result) },
            {ex: Throwable -> handleFailure(key, value, ex)}
        )
    }

    private fun handleFailure(key: Int, value: String?, ex: Throwable) {
        log.error("Error sending the message for the key $key value $value and the exception is ${ex.message}")
        try{
            throw ex
        } catch (throwable:Throwable){
            log.error("Error in OnFailure: ${throwable.message}")
        }
    }


    private fun handleSuccess(key: Int, value: String, sendResult: SendResult<Int, String>?) {
        log.info("Message sent successfully for the key $key value $value, partition is ${sendResult?.recordMetadata?.partition()} ")
    }
}