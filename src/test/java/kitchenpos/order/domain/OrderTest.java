package kitchenpos.order.domain;

import kitchenpos.common.fixtrue.MenuFixture;
import kitchenpos.common.fixtrue.MenuGroupFixture;
import kitchenpos.common.fixtrue.MenuProductFixture;
import kitchenpos.common.fixtrue.OrderTableFixture;
import kitchenpos.common.fixtrue.ProductFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 테스트")
class OrderTest {

    Menu 후라이드_후라이드;
    OrderTable 주문_테이블;
    List<OrderLineItem> 주문_항목들;
    @BeforeEach
    void setUp() {
        Product 후라이드치킨 = ProductFixture.of("후라이드치킨", BigDecimal.valueOf(16000));
        MenuGroup 두마리치킨 = MenuGroupFixture.from("두마리치킨");
        MenuProduct 후라이드_메뉴_상품 = MenuProductFixture.of(후라이드치킨, 2);
        후라이드_후라이드 = MenuFixture.of(
                "후라이드+후라이드",
                BigDecimal.valueOf(31000),
                두마리치킨,
                MenuProducts.from(Collections.singletonList(후라이드_메뉴_상품)));
        주문_항목들 = Collections.singletonList(OrderLineItem.of(후라이드_후라이드, 1L));
        주문_테이블 = OrderTableFixture.of(4, false);
    }

    @Test
    void 주문_발생_시_주문상태는_조리상태이다() {
        // given
        Order actual = Order.of(주문_테이블, 주문_항목들);

        assertAll(() -> {
            assertThat(actual).isNotNull();
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        });
    }

    @Test
    void 주문_발생_시_주문_항목은_필수이다() {
        // given - when
        ThrowingCallable throwingCallable = () -> Order.of(주문_테이블, new ArrayList<>());

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable)
                .withMessage("주문 시 주문 항목은 필수 입니다.");
    }

    @Test
    void 주문_상태를_변경한다() {
        // given
        Order actual = Order.of(주문_테이블, 주문_항목들);

        // when
        actual.changeOrderStatus(OrderStatus.MEAL);

        // then
        Assertions.assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @Test
    void 주문_상태가_계산_완료_상태이면_변경할_수_없다() {
        // given
        Order actual = Order.of(주문_테이블, 주문_항목들);
        actual.changeOrderStatus(OrderStatus.COMPLETION);

        // when
        ThrowingCallable throwingCallable = () -> actual.changeOrderStatus(OrderStatus.MEAL);

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable)
                .withMessage("주문 상태가 계산 완료 상태 이면 주문상태를 변경할 수 없습니다.");
    }
}
