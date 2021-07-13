package kitchenpos.product.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import kitchenpos.product.domain.entity.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootTest
@AutoConfigureMockMvc
class ProductRestControllerTest {

    MockMvc mockMvc;

    @Autowired
    ProductRestController productRestController;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProductService productService;

    private Product 상품;
    private ProductRequest 상품_리퀘스트;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productRestController)
            .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
            .alwaysDo(print())
            .build();

        상품 = new Product(1L, "강정치킨", BigDecimal.valueOf(17000));
        상품_리퀘스트 = new ProductRequest("강정치킨", BigDecimal.valueOf(17000));
    }

    @Test
    @DisplayName("상품을 생성한다.")
    void create() throws Exception {
        //given
        String requestBody = objectMapper.writeValueAsString(상품_리퀘스트);

        //when && then
        mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(content().string(containsString("강정치킨")));
    }

    @Test
    @DisplayName("상품가격이 0원 미만일 경우 상품 생성을 실패한다.")
    void create_with_exception_when_price_smaller_than_zero() throws Exception {
        //given
        상품_리퀘스트 = new ProductRequest("강정치킨", BigDecimal.valueOf(-1));
        String requestBody = objectMapper.writeValueAsString(상품_리퀘스트);

        //when && then
        try {
            mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("전체 상품을 조회한다.")
    void list() throws Exception {
        //when && then
        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("양념치킨")));
    }
}