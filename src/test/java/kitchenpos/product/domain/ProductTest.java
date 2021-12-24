package kitchenpos.product.domain;

import static common.ProductFixture.후라이드;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import kitchenpos.common.exception.Message;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void 상품등록() {
        Product 후라이드 = 후라이드();
        assertThat(후라이드).isEqualTo(후라이드);
    }

    @Test
    void 상품등록시_이름이_없다면_예외() {
        assertThatThrownBy(() -> {
            Product.of(null, 1000L);
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(Message.PRODUCT_NAME_IS_NOT_EMPTY.getMessage());
    }

    @Test
    void 상품등록시_가격이_없다면_예외() {
        assertThatThrownBy(() -> {
            Product.of("후라이드", null);
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(Message.AMOUNT_PRICE_IS_NOT_EMPTY.getMessage());
    }
}