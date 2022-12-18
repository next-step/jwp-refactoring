package kitchenpos.product;

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
import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

class ProductRestControllerTest extends IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;

    @DisplayName("상품을 등록한다")
    @Test
    void test1() throws Exception {
        Long id = 상품_등록();
        assertThat(productRepository.findById(id)).isNotEmpty();
    }

    @DisplayName("전체 상품을 조회한다")
    @Test
    void products2() throws Exception {
        상품_등록();

        mockMvc.perform(get("/api/products"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$..id").exists())
            .andExpect(jsonPath("$..name").exists())
            .andExpect(jsonPath("$..price").exists());
    }

    private Long 상품_등록() throws Exception {
        ProductRequest request = new ProductRequest(new Name("강정치킨"), new Price(BigDecimal.valueOf(17_000)));

        MvcResult result = mockMvc.perform(post("/api/products")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value(request.getName().value()))
            .andExpect(jsonPath("$.price").value(request.getPrice().value()))
            .andReturn();

        return getId(result);
    }

    private Long getId(MvcResult result) throws
        com.fasterxml.jackson.core.JsonProcessingException,
        UnsupportedEncodingException {
        String response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, ProductResponse.class).getId();
    }
}
