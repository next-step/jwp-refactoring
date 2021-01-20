package kitchenpos.application;

import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.application.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("상품 서비스에 관련한 기능")
@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    private ProductRequest request;
    private ProductResponse response;

    @BeforeEach
    void beforeEach() {
        request = new ProductRequest("짬뽕", BigDecimal.valueOf(8_000));
        response = productService.create(request);
    }

    @DisplayName("`상품`을 생성한다.")
    @Test
    void createProduct() {
        // Then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getName()).isEqualTo(request.getName()),
                () -> assertThat(response.getPrice().intValue()).isEqualTo(request.getPrice().intValue())
        );
    }

    @DisplayName("가격은 필수이고, 0원 이상이 아니면 `상품`을 생성할 수 없다.")
    @Test
    void exceptionToCreateProduct() {
        // Given
        ProductRequest invalidRequest1 = new ProductRequest("짬뽕", null);

        // When & Then
        assertThatThrownBy(() -> productService.create(invalidRequest1)).isInstanceOf(IllegalArgumentException.class);

        // Given
        ProductRequest invalidRequest2 = new ProductRequest("짬뽕", BigDecimal.valueOf(-1));

        // When & Then
        assertThatThrownBy(() -> productService.create(invalidRequest2)).isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("모든 `상품` 목록을 조회한다.")
    @Test
    void findAllProducts() {
        // When
        List<ProductResponse> actual = productService.list();

        // Then
        assertThat(actual).containsAnyElementsOf(Collections.singletonList(response));
    }
}
