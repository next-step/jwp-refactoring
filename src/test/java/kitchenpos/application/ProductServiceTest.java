package kitchenpos.application;

import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
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

    @DisplayName("`상품`을 생성한다.")
    @Test
    void createProduct() {
        // Given
        String name = "짬뽕";
        BigDecimal price = BigDecimal.valueOf(8_000);
        ProductRequest request = new ProductRequest(name, price);

        // When
        ProductResponse actual = productService.create(request);

        // Then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(name),
                () -> assertThat(actual.getPrice().intValue()).isEqualTo(price.intValue())
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
        // Given
        ProductResponse savedProduct = productService.create(new ProductRequest("짬뽕", BigDecimal.valueOf(8_000)));

        // When
        List<ProductResponse> actual = productService.list();

        // Then
        assertAll(
                () -> assertThat(actual).extracting(ProductResponse::getId)
                        .containsAnyElementsOf(Collections.singletonList(savedProduct.getId())),
                () -> assertThat(actual).extracting(ProductResponse::getName)
                        .containsAnyElementsOf(Collections.singletonList(savedProduct.getName())),
                () -> assertThat(actual).extracting(ProductResponse::getPrice)
                        .containsAnyElementsOf(Collections.singletonList(savedProduct.getPrice()))
        );
    }
}
