package kitchenpos.order.domain;

import kitchenpos.common.fixtrue.MenuFixture;
import kitchenpos.common.fixtrue.MenuGroupFixture;
import kitchenpos.common.fixtrue.MenuProductFixture;
import kitchenpos.common.fixtrue.OrderFixture;
import kitchenpos.common.fixtrue.OrderLineItemFixture;
import kitchenpos.common.fixtrue.OrderTableFixture;
import kitchenpos.common.fixtrue.ProductFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 테스트")
class OrderTest {

    OrderTable 주문_테이블;
    OrderLineItem 주문_상품;

    @BeforeEach
    void setUp() {
        Product 후라이드치킨 = ProductFixture.of("후라이드치킨", BigDecimal.valueOf(16000));
        MenuGroup 두마리치킨 = MenuGroupFixture.of(1L, "두마리치킨");
        Menu 후라이드_후라이드 = MenuFixture.of(
                "후라이드+후라이드",
                BigDecimal.valueOf(31000),
                두마리치킨.getId());

        후라이드_후라이드.addMenuProduct(MenuProductFixture.of(후라이드치킨.getId(), 2));

        주문_테이블 = OrderTableFixture.of(1L, 4, false);
        주문_상품 = OrderLineItemFixture.of(1L, 1L, 후라이드_후라이드.getId(), 1L);

    }

    @Test
    void 주문_발생_시_주문상태는_조리상태이다() {
        Order actual = OrderFixture.of(
                1L,
                주문_테이블.getId(),
                Collections.singletonList(주문_상품));

        assertAll(() -> {
            assertThat(actual).isNotNull();
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        });
    }

    @Test
    void 주문_발생_시_주문_항목은_필수이다() {
        ThrowingCallable throwingCallable = () -> OrderFixture.of(
                1L,
                주문_테이블.getId(),
                new ArrayList<>());

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable)
                .withMessage("주문 시 주문 항목은 필수 입니다.");
    }

    @Test
    void 주문_상태를_변경한다() {
        // given
        Order actual = OrderFixture.of(
                1L,
                주문_테이블.getId(),
                Collections.singletonList(주문_상품));

        // when
        actual.changeOrderStatus(OrderStatus.MEAL.name());

        // then
        Assertions.assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    void 주문_상태가_계산_완료_상태이면_변경할_수_없다() {
        // given
        Order actual = OrderFixture.of(
                1L,
                주문_테이블.getId(),
                Collections.singletonList(주문_상품));
        actual.changeOrderStatus(OrderStatus.COMPLETION.name());

        // when
        ThrowingCallable throwingCallable = () -> actual.changeOrderStatus(OrderStatus.MEAL.name());

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable)
                .withMessage("주문 상태가 계산 완료 상태 이면 주문상태를 변경할 수 없습니다.");
    }
}
