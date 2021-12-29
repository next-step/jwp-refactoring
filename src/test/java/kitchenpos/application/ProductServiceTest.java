package kitchenpos.application;

import kitchenpos.dao.FakeProductRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        Product product = Product.of("소고기", -100);
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(product));
    }

    @DisplayName("상품 생성 성공")
    @Test
    void save() {
        Product product = Product.of("소고기", 30000);
        Product result = productService.create(product);
        assertAll(
                () -> assertThat(result.getName()).isEqualTo(product.getName()),
                () -> assertThat(result.getPrice()).isEqualTo(product.getPrice())
        );
    }

    @DisplayName("모든 상품 조회 성공")
    @Test
    void list() {
        Product product1 = Product.of("소고기", 30000);
        Product product2 = Product.of("쌈채소", 5000);

        productService.create(product1);
        productService.create(product2);

        List<Product> list = productService.list();
        assertThat(list.size()).isEqualTo(2);
    }

}
