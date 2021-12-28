package kitchenpos.product.domain;

import kitchenpos.common.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("상품 테스트")
class ProductTest {

    @DisplayName("상품 생성 성공 테스트")
    @Test
    void instantiate_success() {
        // given
        String name = "강정치킨";
        BigDecimal price = BigDecimal.valueOf(17_000);

        // when
        Product product = Product.of(name, price);

        // then
        assertAll(
                () -> assertThat(product).isNotNull()
                , () -> assertThat(product.getName()).isEqualTo(name)
                , () -> assertThat(product.getPrice()).isEqualTo(Price.of(price))
        );
    }
}
