package kitchenpos.order.domain;

import kitchenpos.order.exception.AlreadyCompletionException;
import kitchenpos.order.exception.NotOrderLineItemsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    private OrderStatus 주문상태 = OrderStatus.COOKING;
    private OrderLineItem 주문수량메뉴1 = new OrderLineItem(1L, 1L, 1L, 1L);
    private OrderLineItem 주문수량메뉴2 = new OrderLineItem(2L, 1L, 1L, 1L);
    private List<OrderLineItem> 주문한내역들 = Arrays.asList(주문수량메뉴1, 주문수량메뉴2);

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        Order order = new Order(1L, 주문상태, 주문한내역들);

        assertThat(order.getOrderStatus()).isEqualTo(주문상태);
        assertThat(order.getOrderLineItems().get(0)).isEqualTo(주문수량메뉴1);
        assertThat(order.getOrderLineItems().get(1)).isEqualTo(주문수량메뉴2);
    }

    @DisplayName("주문 생성에 실패한다. - 주문 내역이 없으면 주문 등록에 실패한다.")
    @Test
    void fail_create() {
        assertThatThrownBy(() -> new Order(1L, 주문상태,  Collections.emptyList()))
                .isInstanceOf(NotOrderLineItemsException.class);
    }

    @DisplayName("주문 상태가 변경된다.")
    @Test
    void changeStatus() {
        OrderStatus 계산완료 = OrderStatus.COOKING;
        OrderStatus 변경할상태 = OrderStatus.MEAL;
        Order order = new Order(1L, 계산완료, 주문한내역들);

        order.changeOrderStatus(변경할상태);

        assertThat(order.getOrderStatus()).isEqualTo(변경할상태);
    }

    @DisplayName("주문 상태 변경에 실패한다. - 주문 상태가 기존에 계산 완료(COMPLETION) 상태일 경우")
    @Test
    void fail_changeStatus() {
        OrderStatus 계산완료 = OrderStatus.COMPLETION;
        OrderStatus 변경할상태 = OrderStatus.COOKING;
        Order order = new Order(1L, 계산완료, 주문한내역들);
        assertThatThrownBy(() -> order.changeOrderStatus(변경할상태))
                .isInstanceOf(AlreadyCompletionException.class);
    }
}
