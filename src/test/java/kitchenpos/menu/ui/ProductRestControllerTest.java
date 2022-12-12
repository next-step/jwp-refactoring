package kitchenpos.menu.ui;

import kitchenpos.ControllerTest;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.ui.ProductRestController;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("ProductRestController ui 테스트")
@WebMvcTest(ProductRestController.class)
public class ProductRestControllerTest extends ControllerTest {
    @MockBean
    private ProductService productService;

    private Product 후라이드;
    private Product 양념;

    @BeforeEach
    protected void setUp() {
        super.setUp();

        후라이드 = new Product(1L, "후라이드", new BigDecimal(18_000));
        양념 = new Product(2L, "양념", new BigDecimal(20_000));
    }

    @DisplayName("상품 생성 api 테스트")
    @Test
    void createProduct() throws Exception {
        given(productService.create(any(Product.class))).willReturn(후라이드);

        mockMvc.perform(post("/api/products")
                        .content(mapper.writeValueAsString(후라이드))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @DisplayName("상품 조회 api 테스트")
    @Test
    void listProducts() throws Exception {
        given(productService.list()).willReturn(Lists.newArrayList(후라이드, 양념));

        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

}
