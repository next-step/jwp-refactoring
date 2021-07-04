package kitchenpos.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.utils.DataInitializer;

@SpringBootTest
@AutoConfigureMockMvc
class TableRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        DataInitializer.reset();
    }

    @Test
    void create() {
    }

    @Test
    void list() {
    }

    @Test
    void changeEmpty() {
    }

    @Test
    void changeNumberOfGuests() {
    }
}
