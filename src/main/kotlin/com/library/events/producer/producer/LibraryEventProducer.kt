package com.library.events.producer.producer

import com.fasterxml.jackson.databind.ObjectMapper
import com.library.events.producer.domain.LibraryEvent
import lombok.extern.slf4j.Slf4j
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.Header
import org.apache.kafka.common.header.internals.RecordHeader
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import org.springframework.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

@Component
@Slf4j
class LibraryEventProducer {

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<Int, String>

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    var log: Logger = LoggerFactory.getLogger("LibraryEventProducer")

//    Default message sending (async)
    fun sendLibraryEventAsync(libraryEvent: LibraryEvent) {
        val key = libraryEvent.libraryEventId
        val value = this.objectMapper.writeValueAsString(libraryEvent.book)

        val listenableFuture: ListenableFuture<SendResult<Int,String>> = kafkaTemplate.sendDefault(key, value)
            listenableFuture.addCallback(
                { result:SendResult<Int,String>? -> handleSuccess(key, value, result) },
                {ex: Throwable -> handleFailure(key, value, ex)}
            )
    }

//    By using .get() function, we make sure the send method is synchronous
    fun sendLibraryEvent(libraryEvent: LibraryEvent):SendResult<Int,String>{
        val key = libraryEvent.libraryEventId
        val value = this.objectMapper.writeValueAsString(libraryEvent.book)
        var sendResult: SendResult<Int, String>? = null
        try {
            sendResult = kafkaTemplate.sendDefault(key,value).get(1, TimeUnit.SECONDS)
        } catch (e: Throwable ) {
            when(e) {
                is ExecutionException, is InterruptedException ->{
                    log.error("ExecutionException/InterruptedException Sending the message and the exception is ${e.message}")
                    throw e
                }
                is Exception -> {
                    log.error("Exception Sending the Message and the exception is ${e.message}")
                    throw e
                }
            }
        }
        return sendResult
    }

//    Specifies sender configurations when sending message
    fun sendLibraryEventAsyncConfiguration(libraryEvent: LibraryEvent){
        val key = libraryEvent.libraryEventId
        val value = this.objectMapper.writeValueAsString(libraryEvent.book)

        val producerRecord: ProducerRecord<Int,String> = buildProducerRecord(key,value, "library-events")
        val listenableFuture: ListenableFuture<SendResult<Int,String>> = kafkaTemplate.send(producerRecord)
        listenableFuture.addCallback(
            { result:SendResult<Int,String>? -> handleSuccess(key, value, result) },
            {ex: Throwable -> handleFailure(key, value, ex)}
        )
     }

//    Method that builds a ProducerRecord in order to further customize out message sending
//    Maybe you might want a default header or some default behaviour to some producer calls. This is the way to go
    private fun buildProducerRecord(key: Int, value: String?, topic: String):ProducerRecord<Int,String> {
        val recordHeaders:List<Header> = listOf(RecordHeader("event-source","scanner".toByteArray()))
        return ProducerRecord(topic,null,key,value,recordHeaders)

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