package kitchenpos.product.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.ui.ProductRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("상품 컨트롤러 테스트")
@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {
    public static final String DEFAULT_PRODUCTS_URI = "/api/products/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("후라이드");
        product.setPrice(new BigDecimal(19000));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void 상품_생성() throws Exception {
        given(productService.create(any())).willReturn(product);

        final String jsonTypeProduct = objectMapper.writeValueAsString(product);

        mockMvc.perform(post(DEFAULT_PRODUCTS_URI)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonTypeProduct))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").value(product.getId()))
            .andExpect(jsonPath("name").value(product.getName()))
            .andExpect(jsonPath("price").value(product.getPrice().longValue()));
    }

    @DisplayName("상품을 조회한다.")
    @Test
    void 상품_조회() throws Exception {
        given(productService.list()).willReturn(Collections.singletonList(product));

        mockMvc.perform(get(DEFAULT_PRODUCTS_URI)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(product.getId()))
            .andExpect(jsonPath("$[0].name").value(product.getName()))
            .andExpect(jsonPath("$[0].price").value(product.getPrice().longValue()));
    }
}
