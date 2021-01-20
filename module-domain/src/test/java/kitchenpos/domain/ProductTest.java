package kitchenpos.domain;

import kitchenpos.common.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("상품 도메인")
class ProductTest {

    @DisplayName("상품을 생성할수 있다.")
    @Test
    void create() {
        // given
        String name = "강정치킨";
        Money price = Money.valueOf(19000);

        // when
        Product product = new Product(name, price);

        // then
        assertThat(product).isNotNull();
    }

    @DisplayName("상품의 이름과 가격은 필수 정보이다.")
    @Test
    void required() {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> new Product("강정치킨", null));
        assertThrows(IllegalArgumentException.class, () -> new Product(null, Money.valueOf(19000)));
    }
}
