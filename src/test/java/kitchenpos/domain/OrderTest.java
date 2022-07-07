package kitchenpos.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTest {

    private List<OrderLineItem> orderLineItems;

    @BeforeEach
    void setUp() {
        orderLineItems = Arrays.asList(new OrderLineItem(1L, 1), new OrderLineItem(2L, 1));
    }

    @Test
    void 주문_생성() {
        Order order = new Order(1L);

        assertAll(
                () -> assertThat(order.getOrderedTime()).isNotNull(),
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING)
        );
    }

    @Test
    void 주문내역_추가() {
        Order order = new Order(1L);

        order.addOrderLineItems(orderLineItems);

        assertAll(
                () -> assertThat(order.getOrderLineItems()).hasSize(2),
                () -> assertThat(order.getOrderLineItems()).element(0)
                        .satisfies(it -> {
                            assertThat(it.getOrder()).isNotNull();
                        })
        );
    }

    @Test
    void 테이블이_없는_경우() {
        assertThatThrownBy(() -> new Order(null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 빈_주문_내역을_추가하는_경우() {
        Order order = new Order(1L);
        orderLineItems = Collections.emptyList();

        assertThatThrownBy(() -> order.addOrderLineItems(orderLineItems))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문상태_변경() {
        Order order = new Order(1L);

        order.changeOrderStatus(OrderStatus.MEAL);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @Test
    void 완료된_주문의_상태를_변경하는_경우() {
        Order order = new Order(1L);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class);
    }
}