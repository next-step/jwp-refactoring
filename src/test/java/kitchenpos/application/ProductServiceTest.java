package kitchenpos.application;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class ProductServiceTest {
    private final ProductService productService;

    @Autowired
    public ProductServiceTest(ProductService productService) {
        this.productService = productService;
    }

    @Test
    void create() {
        // given
        Product product = Product.of("마늘치킨", BigDecimal.valueOf(18000));

        // when
        Product saved = productService.create(product);

        // then
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void create_throwException_ifWrongPrice() {
        // when
        // then
        assertAll(
                () -> assertThatThrownBy(() -> productService.create(Product.of("마늘치킨", null))),
                () -> assertThatThrownBy(() -> productService.create(Product.of("마늘치킨", BigDecimal.valueOf(-18000))))
        );
    }
}