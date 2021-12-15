package kitchenpos.domain.product;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.exception.EmptyNameException;
import kitchenpos.exception.NegativePriceException;

class ProductTest {

    @DisplayName("Product 는 Name 과 Price 로 생성된다.")
    @Test
    void create1() {
        // given
        String name = "돼지고기";
        BigDecimal price = BigDecimal.valueOf(1_000);

        // when
        Product product = Product.of(name, price);

        // then
        assertAll(
            () -> assertEquals(product.getName(), Name.from(name)),
            () -> assertEquals(product.getPrice(), Price.from(price))
        );
    }

    @DisplayName("Product 생성 시, Name 이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create2() {
        // given
        BigDecimal price = BigDecimal.valueOf(1_000);

        // when & then
        assertThrows(EmptyNameException.class, () -> Product.of(null, price));
    }

    @DisplayName("Product 생성 시, Price 가 존재하지 않으면 예외가 발생한다.")
    @Test
    void create3() {
        // given
        String name = "돼지고기";

        // when & then
        assertThrows(NegativePriceException.class, () -> Product.of(name, null));
    }
}