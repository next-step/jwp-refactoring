package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {
    @DisplayName("주문 테이블이 NULL일 수 없다.")
    @Test
    void createWithNullTable() {
        assertThatThrownBy(() -> new Order(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 필수 값 입니다.");
    }

    @DisplayName("주문 항목이 비어있을 경우 예외가 발생한다.")
    @Test
    void emptyOrderLineException() {
        Order order = new Order(1L);

        assertThatThrownBy(order::checkOrderLineItems)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목은 비어있을 수 없습니다.");
    }

    @DisplayName("주문에 할당된 메뉴 ID를 찾을 수 있다.")
    @Test
    void findMenus() {
        Order order = new Order(1L);
        order.addLineItem(new OrderLineItem(1L, 10));
        order.addLineItem(new OrderLineItem(2L, 10));

        List<Long> actual = order.findMenus();

        assertThat(actual).contains(1L, 2L);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeStatus() {
        Order order = new Order(1L);

        order.changeStatus(OrderStatus.MEAL);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("이미 완료된 주문의 상태를 변경할 수 없다.")
    @Test
    void changeStatusWithCompletion() {
        Order order = new Order(1L);
        order.changeStatus(OrderStatus.COMPLETION);

        assertThatThrownBy(() -> order.changeStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료된 주문의 상태를 변경할 수 없습니다.");
    }
}
