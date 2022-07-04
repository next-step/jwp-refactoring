package kitchenpos.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductTest {
    public static final Product 불고기버거 = new Product(1L, "불고기버거", BigDecimal.valueOf(1500));

    @Test
    @DisplayName("제품 생성 테스트")
    void create() {
        // then
        assertThat(불고기버거).isInstanceOf(Product.class);
    }
}
