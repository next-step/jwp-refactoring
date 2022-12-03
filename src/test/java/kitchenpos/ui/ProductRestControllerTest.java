package kitchenpos.ui;

import kitchenpos.ControllerTest;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

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
class ProductRestControllerTest extends ControllerTest {

    @MockBean
    private ProductService productService;

    private Product 스테이크;
    private Product 스파게티;

    @BeforeEach
    public void setUp() {
        super.setUp();

        스테이크 = new Product(1L, "스테이크", new BigDecimal(25000));
        스파게티 = new Product(2L, "스파게티", new BigDecimal(18000));
    }

    @Test
    void 상품_등록() throws Exception {
        given(productService.create(any(Product.class))).willReturn(스테이크);

        webMvc.perform(post("/api/products")
                        .content(mapper.writeValueAsString(스테이크))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(스테이크.getId().intValue())))
                .andExpect(jsonPath("$.name", is(스테이크.getName())))
                .andExpect(jsonPath("$.price", is(스테이크.getPrice().intValue())));
    }

    @Test
    void 상품_등록_실패() throws Exception {
        given(productService.create(any(Product.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(post("/api/products")
                    .content(mapper.writeValueAsString(스테이크))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 상품_목록_조회() throws Exception {
        given(productService.list()).willReturn(Arrays.asList(스테이크, 스파게티));

        webMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
