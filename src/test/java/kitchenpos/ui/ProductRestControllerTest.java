package kitchenpos.ui;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductRestControllerTest extends ControllerTest {

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
    }

    @Test
    void create() throws Exception {
        Product 뿌링클 = new Product("뿌링클", BigDecimal.valueOf(18000));
        String body = objectMapper.writeValueAsString(뿌링클);

        mockMvc.perform(post(PRODUCT_URI)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void search() throws Exception {
        mockMvc.perform(get(PRODUCT_URI))
                .andDo(print())
                .andExpect(status().isOk());
    }
}