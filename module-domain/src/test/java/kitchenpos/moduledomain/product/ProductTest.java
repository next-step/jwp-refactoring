package kitchenpos.moduledomain.product;

import static kitchenpos.moduledomain.common.ProductFixture.가격이없는_반반치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.moduledomain.common.exception.DomainMessage;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    void 상품을_생성할수_있다() {
        // when
        Product 양념치킨 = Product.of("양념치킨", 3000L);

        // then
        assertThat(양념치킨).isNotNull();
    }

    @Test
    void 가격이_없는경우_생성할수_없다() {

        // then
        assertThatThrownBy(() -> {
            Product 가격이없는_반반치킨 = 가격이없는_반반치킨();
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(DomainMessage.AMOUNT_IS_NOT_LESS_THAN_ZERO.getMessage());
    }
}
