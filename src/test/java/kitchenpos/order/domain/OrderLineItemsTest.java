package kitchenpos.order.domain;

import kitchenpos.order.domain.fixture.OrderLineItemFixture;
import kitchenpos.order.domain.fixture.OrderLineItemsFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static kitchenpos.order.application.OrderService.ORDER_LINE_ITEMS_EMPTY_EXCEPTION_MESSAGE;
import static kitchenpos.order.domain.fixture.OrderLineItemsFixture.orderLineItemsA;
import static kitchenpos.order.domain.fixture.OrderFixture.orderA;
import static org.assertj.core.api.Assertions.*;

@DisplayName("주문 항목 일급 콜렉션")
class OrderLineItemsTest {

    @DisplayName("주문 항목 일급 콜렉션을 생성한다.")
    @Test
    void create() {
        assertThatNoException().isThrownBy(OrderLineItemsFixture::orderLineItemsA);
    }

    @DisplayName("주문항목이 empty 일수 없다.")
    @Test
    void create_empty() {
        assertThatThrownBy(() -> new OrderLineItems(Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_LINE_ITEMS_EMPTY_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문 항목의 갯수를 구한다.")
    @Test
    void size() {
        OrderLineItems orderLineItems = new OrderLineItems(Collections.singletonList(OrderLineItemFixture.OrderLineItem()));
        assertThat(orderLineItems.getOrderLineItems()).hasSize(1);
    }

    @DisplayName("주문 항목의 메뉴 아이디를 조회한다.")
    @Test
    void menuIds() {
        OrderLineItems orderLineItems = new OrderLineItems(Collections.singletonList(OrderLineItemFixture.OrderLineItem()));
        assertThat(orderLineItems.getMenuIds()).hasSize(1);
    }

    @DisplayName("주문 항목을 주문과 매핑한다.")
    @Test
    void mapOrder() {
        Order order = orderA(1L);
        OrderLineItems orderLineItems = new OrderLineItems(Collections.singletonList(OrderLineItemFixture.OrderLineItem()));
        orderLineItems.mapOrder(order);
        for (OrderLineItem orderLineItem : orderLineItems.getOrderLineItems()) {
            assertThat(orderLineItem.getOrder()).isEqualTo(order);
        }
    }

    @DisplayName("주문 항목의 empty 여부를 반환한다. / true")
    @Test
    void isEmpty_true() {
        assertThat(orderLineItemsA()).isEmpty();
    }

    @DisplayName("주문 항목의 empty 여부를 반환한다. / false")
    @Test
    void isEmpty_false() {
        assertThat(new OrderLineItems().isEmpty()).isTrue();
    }
}
