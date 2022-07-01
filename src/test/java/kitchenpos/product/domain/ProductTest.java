package kitchenpos.product.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ProductTest {

    @Test
    @DisplayName("상품 생성 성공")
    void instantiate_success() {
        // given
        Name name = Name.of("피자");
        Price price = Price.of(BigDecimal.valueOf(17_000));

        // when
        Product product = Product.of(name, price);

        // then
        assertAll(
                () -> assertThat(product).isNotNull(),
                () -> assertThat(product.getName()).isEqualTo("피자"),
                () -> assertThat(product.getPrice()).isEqualTo(BigDecimal.valueOf(17_000))
        );
    }

}
