package kitchenpos.domain;

import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 항목들 테스트")
class OrderLineItemsTest {

    Menu 후라이드_후라이드;

    @BeforeEach
    void setUp() {
        Product 후라이드치킨 = ProductFixture.of("후라이드치킨", BigDecimal.valueOf(16000));
        MenuGroup 두마리치킨 = MenuGroupFixture.from("두마리치킨");
        MenuProduct 후라이드_메뉴_상품 = MenuProductFixture.of(후라이드치킨.getId(), 2);
        후라이드_후라이드 = MenuFixture.of(
                "후라이드+후라이드",
                BigDecimal.valueOf(31000),
                두마리치킨,
                MenuProducts.from(Collections.singletonList(후라이드_메뉴_상품)));
    }

    @Test
    void 주문_항목들_생성() {
        OrderLineItems actual = OrderLineItems.from(Collections.singletonList(OrderLineItem.of(후라이드_후라이드, 1L)));

        assertThat(actual).isNotNull();
    }


    @ParameterizedTest
    @NullAndEmptySource
    void 주문_발생_시_주문_항목은_필수이다(List<OrderLineItem> orderLineItems) {
        // given - when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> OrderLineItems.from(orderLineItems);

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable)
                .withMessage("주문 시 주문 항목은 필수 입니다.");
    }

}
