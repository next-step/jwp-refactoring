package kitchenpos.product.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.product.dto.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("상품 컨트롤러 테스트")
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/db/test_data.sql")
class ProductRestControllerTest {
    public static final String DEFAULT_PRODUCTS_URI = "/api/products/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        productRequest = new ProductRequest("후라이드", 19_000);
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void 상품_생성() throws Exception {
        final String jsonTypeProduct = objectMapper.writeValueAsString(productRequest);

        mockMvc.perform(post(DEFAULT_PRODUCTS_URI)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonTypeProduct))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value(productRequest.getName()))
            .andExpect(jsonPath("$.price").value(productRequest.getPrice()));
    }

    @DisplayName("상품을 조회한다.")
    @Test
    void 상품_조회() throws Exception {
        mockMvc.perform(get(DEFAULT_PRODUCTS_URI)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(greaterThan(5))));
    }
}
