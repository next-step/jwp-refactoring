package kitchenpos.order.unit;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static kitchenpos.table.unit.OrderTableTest.테이블_생성되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @DisplayName("주문 생성에 성공한다.")
    @Test
    void 생성() {
        // given
        OrderTable 테이블 = 테이블_생성되어_있음(0, false);
        OrderLineItem 주문_항목1 = 주문_항목_생성되어_있음();
        OrderLineItem 주문_항목2 = 주문_항목_생성되어_있음();

        // when
        Order order = Order.createOrder(테이블.getId(), Arrays.asList(주문_항목1, 주문_항목2));

        // then
        assertThat(order).isNotNull();
    }

    @DisplayName("주문 항목이 1개 미만이면 주문 생성에 실패한다.")
    @Test
    void 생성_예외_주문_항목_1개_미만() {
        // given
        OrderTable 테이블 = 테이블_생성되어_있음(0, false);
        OrderLineItem 주문_항목 = 주문_항목_생성되어_있음();

        // when, then
        assertThatThrownBy(() -> Order.createOrder(테이블.getId(), Arrays.asList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목은 하나 이상이어야 합니다.");

    }

    @DisplayName("주문의 상태를 변경한다.")
    @Test
    void 상태_변경() {
        // given
        Order 주문 = 주문_생성되어_있음();

        // when
        주문.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("계산 완료 상태이면 상태 변경에 실패한다.")
    @Test
    void 상태_변경_예외_계산_완료() {
        // given
        Order 주문 = 주문_생성되어_있음();
        주문.changeOrderStatus(OrderStatus.COMPLETION);
        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);

        // when, then
        assertThatThrownBy(() -> 주문.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalStateException.class);
    }

    OrderLineItem 주문_항목_생성되어_있음() {
        Menu menu = Menu.createMenu(
                "메뉴",
                BigDecimal.valueOf(400),
                new MenuGroup("메뉴 그룹"),
                Arrays.asList(
                        new MenuProduct(1L, 1),
                        new MenuProduct(2L, 1),
                        new MenuProduct(3L, 2)),
                Menu.DEFAULT_VERSION);
        return new OrderLineItem(menu.getId(), 1);
    }

    Order 주문_생성되어_있음() {
        OrderTable 테이블 = 테이블_생성되어_있음(0, false);
        OrderLineItem 주문_항목1 = 주문_항목_생성되어_있음();
        OrderLineItem 주문_항목2 = 주문_항목_생성되어_있음();
        return Order.createOrder(테이블.getId(), Arrays.asList(주문_항목1, 주문_항목2));
    }
}
