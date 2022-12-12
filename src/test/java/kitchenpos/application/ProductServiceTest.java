package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Product;

class ProductServiceTest {

    @DisplayName("상품 등록 시 가격이 없거나 0 미만인 경우 예외발생한다")
    @Test
    void product1() {
        ProductService productService = new ProductService(null);

        assertThatThrownBy(() -> productService.create(new Product()))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> productService.create(new Product("test", BigDecimal.valueOf(-10))))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
