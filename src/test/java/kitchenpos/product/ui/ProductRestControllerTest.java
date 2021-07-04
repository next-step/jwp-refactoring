package kitchenpos.product.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.RestControllerTest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.ui.ProductRestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("상품 API")
@WebMvcTest(ProductRestController.class)
public class ProductRestControllerTest extends RestControllerTest<Product> {

    private static final String BASE_URL = "/api/products";
    private static final Product 햄버거 = new Product(1L, "햄버거", new BigDecimal(10000));

    @MockBean
    private ProductService productService;

    @DisplayName("상품을 등록한다.")
    @Test
    void create() throws Exception {
        // Given
        given(productService.create(any())).willReturn(햄버거);

        // When & Then
        String responseBody = objectMapper.writeValueAsString(햄버거);
        post(BASE_URL, 햄버거)
            .andExpect(content().string(responseBody));
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() throws Exception {
        // Given
        List<Product> products = Collections.singletonList(햄버거);
        given(productService.list()).willReturn(products);

        // When & Then
        String responseBody = objectMapper.writeValueAsString(products);
        get(BASE_URL)
            .andExpect(content().string(responseBody));
    }

}
