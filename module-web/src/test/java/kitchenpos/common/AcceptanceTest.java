package kitchenpos.common;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.web.server.*;

import io.restassured.*;
import io.restassured.parsing.*;
import org.springframework.context.annotation.Bean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.defaultParser = Parser.JSON;
    }

    @Bean
    Hibernate5Module hibernate5Module(){
        return new Hibernate5Module();
    }
}
