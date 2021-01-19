package kitchenpos.application;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static kitchenpos.utils.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void createProduct() {
        Product product = product_생성(7L, "스노윙치킨", BigDecimal.valueOf(18000));

        Product result = productService.create(product);

        assertThat(result).isEqualTo(product);
    }

    @DisplayName("상품의 가격이 설정되어 있지 않을 경우 상품을 등록할 수 없다.")
    @Test
    void createProductException1() {
        assertThatThrownBy(() -> productService.create(product_price_변경(후라이드, null)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 0원보다 작을 경우 상품을 등록할 수 없다.")
    @Test
    void createProductException2() {
        assertThatThrownBy(() -> productService.create(product_price_변경(후라이드, BigDecimal.valueOf(-1))))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
