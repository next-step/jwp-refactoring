package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {

    @Test
    @DisplayName("메뉴가 없으면 주문 항목을 생성할 수 없다.")
    void noMenu() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> OrderLineItem.of(null, BigDecimal.ONE, "이름", 1L)
        );
    }

    @Test
    @DisplayName("주문이 변경 된다.")
    void changeOrder() {
        //given
        Order order = new Order(1L, 1L, OrderStatus.COOKING, LocalDateTime.now(),OrderLineItems.from(
                Collections.singletonList(OrderLineItem.of(2L, BigDecimal.ONE, "이름", 1L))));

        OrderLineItem orderLineItem = OrderLineItem.of(1L, BigDecimal.ONE, "이름", 1L);

        //when
        orderLineItem.changeOrder(order);

        //then
        assertThat(order.getId()).isEqualTo(1L);
    }


}
