package kitchenpos.product.ui;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.RestControllerTest;
import kitchenpos.common.domain.Price;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("상품 API")
@WebMvcTest(ProductRestController.class)
public class ProductRestControllerTest extends RestControllerTest<ProductRequest> {

    public static final String BASE_URL = "/api/products";
    private static final Product 햄버거 = new Product(1L, "햄버거", Price.wonOf(10000));
    private static final ProductResponse 햄버거_응답 = ProductResponse.of(햄버거);

    @MockBean
    private ProductService productService;

    @DisplayName("상품을 등록한다.")
    @Test
    void create() throws Exception {
        // Given
        given(productService.create(any())).willReturn(햄버거_응답);

        // When & Then
        post(BASE_URL, ProductRequest.of(햄버거))
            .andExpect(jsonPath("$.name").value(햄버거.getName()));
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() throws Exception {
        // Given
        List<ProductResponse> products = new ArrayList<>(Collections.singletonList(햄버거_응답));
        given(productService.list()).willReturn(products);

        // When & Then
        get(BASE_URL)
            .andExpect(jsonPath("$.*", hasSize(products.size())));
    }

}
