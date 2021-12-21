package kitchenpos.order.domain;

import kitchenpos.common.exception.OrderLineItemEmptyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static kitchenpos.menu.domain.MenuTest.치킨세트;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderLineItemsTest {

    @Test
    @DisplayName("주문 항목 리스트 생성")
    public void create() {
        // given
        OrderLineItem 치킨 = new OrderLineItem(치킨세트, 1);
        // when
        OrderLineItems orderLineItems = new OrderLineItems(Collections.singletonList(치킨));
        // then
        assertThat(orderLineItems.getOrderLineItems()).hasSize(1);
        assertThat(orderLineItems.getOrderLineItems()).contains(치킨);
    }

    @Test
    @DisplayName("주문 항목이 없으면 주문을 등록 할 수 없습니다.")
    public void OrderLineItemEmptyExceptionTest() {
        // given
        // when
        // then
        assertThatThrownBy(() -> new OrderLineItems(Collections.emptyList()))
                .isInstanceOf(OrderLineItemEmptyException.class);
    }
}