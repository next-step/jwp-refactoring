package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("ProductRestController 테스트")
@WebMvcTest(ProductRestController.class)
public class ProductRestControllerTest {
    @Autowired
    protected MockMvc webMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;

    private Product 후라이드치킨;
    private Product 양념치킨;

    @BeforeEach
    public void setUp() {
        후라이드치킨 = Product.of(1L, "후라이드치킨", new BigDecimal(16_000));
        양념치킨 = Product.of(2L, "양념치킨", new BigDecimal(17_000));
    }

    @DisplayName("상품 등록에 실패한다.")
    @Test
    void 상품_등록에_실패한다() throws Exception {
        given(productService.create(any(Product.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(post("/api/products")
                        .content(objectMapper.writeValueAsString(후라이드치킨))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("상품 등록에 성공한다.")
    @Test
    void 상품_등록에_성공한다() throws Exception {
        given(productService.create(any(Product.class))).willReturn(양념치킨);

        webMvc.perform(post("/api/products")
                        .content(objectMapper.writeValueAsString(양념치킨))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(양념치킨.getId().intValue())))
                .andExpect(jsonPath("$.name", is(양념치킨.getName())))
                .andExpect(jsonPath("$.price", is(양념치킨.getPrice().intValue())));
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void 상품_목록을_조회한다() throws Exception {
        given(productService.list()).willReturn(Arrays.asList(후라이드치킨, 양념치킨));

        webMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
