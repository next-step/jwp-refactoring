package kitchenpos.order.domain;

import kitchenpos.fixture.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

public class OrderLineItemsTest {

    @DisplayName("주문항목 일급 콜렉션 생성")
    @Test
    void create() {
        OrderLineItems orderLineItems = new OrderLineItems();

        assertThat(orderLineItems).isNotNull();
    }

    @DisplayName("주문항목 추가")
    @Test
    void add() {
        OrderLineItem 후라이드두마리세트_두개 = OrderLineItemFixture.샘플();
        OrderLineItems orderLineItems = new OrderLineItems();

        orderLineItems.add(후라이드두마리세트_두개);

        assertThat(orderLineItems.getOrderLineItems().contains(후라이드두마리세트_두개)).isTrue();
    }
}
