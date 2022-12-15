package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.IntegrationTest;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;

class ProductRestControllerTest extends IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;

    @DisplayName("상품을 등록한다")
    @Test
    void products1() throws Exception {
        Product product = new Product("강정치킨", BigDecimal.valueOf(17_000));

        MvcResult result = mockMvc.perform(post("/api/products")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(product)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value(product.getName()))
            .andExpect(jsonPath("$.price").value(product.getPrice().intValue()))
            .andReturn();

        assertThat(productRepository.findById(getId(result))).isNotEmpty();
    }

    @DisplayName("전체 상품을 조회한다")
    @Test
    void products2() throws Exception {
        mockMvc.perform(get("/api/products"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$..id").exists())
            .andExpect(jsonPath("$..name").exists())
            .andExpect(jsonPath("$..price").exists())
        ;
    }

    private Long getId(MvcResult result) throws
        com.fasterxml.jackson.core.JsonProcessingException,
        UnsupportedEncodingException {
        String response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, Product.class).getId();
    }
}
