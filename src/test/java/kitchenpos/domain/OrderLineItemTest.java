package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {

    @Test
    @DisplayName("메뉴가 없으면 주문 항목을 생성할 수 없다.")
    void noMenu() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> OrderLineItem.from(null, 1)
        );
    }

    @Test
    @DisplayName("순서가 변경 된다.")
    void changeSeq() {
        //given
        OrderLineItem orderLineItem = OrderLineItem.from(1L, 2);

        //when
        orderLineItem.changeSeq(3);

        //then
        assertThat(orderLineItem.getSeq()).isEqualTo(3L);
    }

    @Test
    @DisplayName("주문이 변경 된다.")
    void changeOrder() {
        //given
        Order order = new Order(1L, 1L, OrderStatus.COOKING, LocalDateTime.now(),OrderLineItems.from(
                Collections.singletonList(OrderLineItem.from(1L, 2))));

        OrderLineItem orderLineItem = OrderLineItem.from(1L, 2);

        //when
        orderLineItem.changeOrder(order);

        //then
        assertThat(order.getId()).isEqualTo(1L);
    }


}
