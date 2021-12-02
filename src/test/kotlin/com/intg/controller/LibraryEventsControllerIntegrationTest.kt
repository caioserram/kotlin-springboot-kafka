package controller

import com.library.events.producer.ProducerApplication
import com.library.events.producer.ProducerApplicationTests
import com.library.events.producer.config.AutoCreateConfig
import com.library.events.producer.controller.LibraryEventController
import com.library.events.producer.domain.Book
import com.library.events.producer.domain.LibraryEvent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.test.context.ContextConfiguration
import java.net.URI

//Avoids conflict with 8080 port when testing
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
/* fixme o caminho pode ser configurado de forma que não seja necessária a declaração da classe de configuração.
    Mas caso esteja ocorrendo algum erro, basta incluir a anotação abaixo */
@ContextConfiguration(classes= [ProducerApplication::class])
class LibraryEventsControllerIntegrationTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun postLibraryEvent(){
        //given
        val book = Book(123, "Caio", "Kafka + SpringBoot")
        val libraryEvent:LibraryEvent = LibraryEvent(123, book, null)

        val headers = HttpHeaders();
        headers.set("content-type",MediaType.APPLICATION_JSON.toString())

        val request: HttpEntity<LibraryEvent> = HttpEntity(libraryEvent,headers)
        //when
        val responseEntity:ResponseEntity<LibraryEvent> =  restTemplate.exchange("/v1/libraryevent",HttpMethod.POST,request, LibraryEvent::class.java)
        //then
        Assertions.assertEquals(HttpStatus.CREATED,responseEntity.statusCode)
    }
}