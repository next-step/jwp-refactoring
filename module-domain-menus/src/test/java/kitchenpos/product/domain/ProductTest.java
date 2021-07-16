package kitchenpos.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.utils.TestUtils.getRandomId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("상품")
public class ProductTest {

    @Test
    @DisplayName("상품을 생성한다.")
    public void initProduct() {
        // when
        Product product = 상품_생성("치킨", 10_000);

        // then
        assertThat(product).isNotNull();
    }

    @Test
    @DisplayName("가격은 필수값이고, 가격이 0원 이상이어야 한다.")
    public void exceptionProduct() {
        // then
        assertThatThrownBy(() -> 상품_생성("치킨", -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }

    public static Product 상품_생성(String name, int price) {
        return new Product(getRandomId(), name, BigDecimal.valueOf(price));
    }
}