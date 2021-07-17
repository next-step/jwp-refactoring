package kitchenpos.product.ui;

import static kitchenpos.product.domain.ProductTest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.product.dto.ProductRequest;
import kitchenpos.utils.IntegrationTest;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("제품 통합 테스트")
class ProductRestControllerTest extends IntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("제품을 생성한다")
    void create() throws Exception {
        ProductRequest request = new ProductRequest("강정치킨", BigDecimal.valueOf(17000));
        mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value(request.getName()))
            .andExpect(jsonPath("$.price").value(request.getPrice().longValue()));
    }

    @Test
    @DisplayName("제품 생성이 실패한다 - 가격이 null 이거나 0보다 작을 경우")
    void create_failed() throws Exception {
        BigDecimal invalidPrice = BigDecimal.valueOf(-1);
        ProductRequest request = new ProductRequest("강정치킨", invalidPrice);
        mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("제품 목록을 가져온다")
    void list() throws Exception {
        mockMvc.perform(get("/api/products"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$[0].id").value(후라이드.getId()))
            .andExpect(jsonPath("$[0].name").value(후라이드.getName()));
    }
}
