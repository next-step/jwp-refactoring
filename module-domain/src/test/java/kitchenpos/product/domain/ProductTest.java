package kitchenpos.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {

    @DisplayName("상품을 생성할수 있다.")
    @Test
    void createProduct() {
        // given
        String name = "강정치킨";
        BigDecimal price = BigDecimal.valueOf(17000);

        // when
        Product product = new Product(name, price);

        // then
        assertThat(product).isNotNull();
    }

    @DisplayName("상품의 이름과 가격은 필수 정보이다.")
    @Test
    void requireNameAndPrice() {
        // when & then
        assertThrows(IllegalArgumentException.class, () -> new Product("강정치킨", null));
        assertThrows(IllegalArgumentException.class, () -> new Product(null, BigDecimal.valueOf(19000)));
    }
}
