package kitchenpos.menu.application;

import kitchenpos.menu.domain.FakeProductRepository;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("상품 테스트")
class ProductServiceTest {
    private final ProductRepository productRepository = new FakeProductRepository();
    private final ProductService productService = new ProductService(productRepository);

    @DisplayName("상품가격이 0보다 작으면 예외발생")
    @Test
    void priceIsNegative() {
        ProductRequest product = ProductRequest.of("소고기", BigDecimal.valueOf(-100));
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(product));
    }

    @DisplayName("상품 생성 성공")
    @Test
    void save() {
        ProductRequest product = ProductRequest.of("소고기", BigDecimal.valueOf(30000));
        ProductResponse result = productService.create(product);
        assertAll(
                () -> assertThat(result.getName()).isEqualTo(product.getName()),
                () -> assertThat(result.getPrice()).isEqualTo(product.getPrice())
        );
    }

    @DisplayName("모든 상품 조회 성공")
    @Test
    void list() {
        ProductRequest product1 = ProductRequest.of("소고기", BigDecimal.valueOf(30000));
        ProductRequest product2 = ProductRequest.of("쌈채소", BigDecimal.valueOf(5000));

        productService.create(product1);
        productService.create(product2);

        List<ProductResponse> list = productService.list();
        assertThat(list.size()).isEqualTo(2);
    }

}
