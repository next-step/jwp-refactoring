package kitchenpos.application;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        Product product = new Product("페페로니", BigDecimal.valueOf(1000));

        Product response = productService.create(product);

        assertThat(response.getName()).isEqualTo(product.getName());
    }

    @DisplayName("상품 가격이 null이거나 0보다 작을 수 없다.")
    @Test
    void createProductPriceNullOrZeroException() {
        Product product1 = new Product("페페로니", BigDecimal.valueOf(-1));
        Product product2 = new Product("페페로니", null);

        assertAll(
                () -> assertThatThrownBy(() -> productService.create(product1)),
                () -> assertThatThrownBy(() -> productService.create(product2))
        );
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        Product product = new Product("페페로니", BigDecimal.valueOf(1000));
        productService.create(product);

        List<Product> list = productService.list();

        List<String> names = list.stream()
                .map(Product::getName)
                .collect(Collectors.toList());
        assertThat(names).contains(product.getName());
    }
}
